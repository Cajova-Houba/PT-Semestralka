package graf;


import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import main.Simulator;

public class Pivovar extends Skladiste{

	/**
	 * pocet hektolitru piva k dispozici k vydeji
	 */
	int pivo;
	
	/**
	 * pocet hektolitru piva kazdodenni produkce, zaroven defaultni pocet hl piva
	 */
	private final int PRODUKCE = 700000;  
	
	/**
	 * mapa hospod zavislych na prisunu piva primo z pivovaru
	 */
	public Map<Integer, HospodaTank> hospodyTank = new TreeMap<Integer, HospodaTank>();
	
	
	
	/**
	 * Vytvori pivovar danych parametru.
	 * 
	 * @param id Identifikator uzlu.
	 * @param typ Typ uzlu.
	 * @param x X-ova poloha uzlu.
	 * @param y Y-ova poloha uzlu.
	 */
	public Pivovar(int id, UzelTyp typ, int x, int y, Simulator sim)
	{
		super(id, typ, x, y,sim);
		
		/**
		 * nastaveni defaultni hodnoty hektolitru piva v pivovaru
		 */
		pivo = PRODUKCE;
		this.vozy = new LinkedList<Auto>();
	}
	
	
	/**
	 * pripocte ke zbylemu pivu produkci noveho dne
	 */
	public void pridejNovePivo ()
	{
	
		pivo = pivo + PRODUKCE;
		
	}
	
	
	/**
	 * Metoda zpracuje co nejvic prijatych objednavek.
	 */
	public void zpracujObjednavky()
	{
		int idtmp = 0;
		float delkaCesty = 0;
		
		if (this.objednavky.isEmpty())
		{
			return ;
		}
		
		Cisterna cist = (Cisterna) getVolneAuto();
		
		cist.poloha[0] = this.poloha[0];
		cist.poloha[1] = this.poloha[1];
		Objednavka ob = this.objednavky.remove();

		delkaCesty += vyberCestu(this.id, ob.id, cist);
		
		
		while(cist.pridejObjednavku(ob))
		{
			idtmp = ob.id;
			
			ob = vyberObjednavku(ob.id);
			if (ob == null)
			{
				ob=cist.objednavky.getLast();
				break;
			}
			objednavky.remove(ob);
			
			delkaCesty += vyberCestu(idtmp, ob.id, cist);
			
			
			if((cist.objem > 44)||(13-Simulator.getCas().hodina)*(cist.RYCHLOST-10) + 120 < delkaCesty){
				//System.out.println("cistadak ma "+nakl.objem);
				cist.kDispozici = false;
				//System.out.println("Auto jede " + delkaCesty + "km");
				break;		
			}
			/*
			if((Simulator.getCas().hodina > 12) && (delkaCesty > 80) ){
				cist.kDispozici = false;
				//System.out.println("Auto jede " + delkaCesty + "km");
				break;		
			}
			
			if((Simulator.getCas().hodina > 10) && (delkaCesty > 130) ){
				cist.kDispozici = false;
				//System.out.println("Auto jede " + delkaCesty + "km");
				break;		
			}
			*/
		}
		
		
		//cesta zpatky
		vyberCestu(ob.id, this.id, cist);
		if (cist.objem >= 1)
		{
			cist.kDispozici = false;
			cist.jede = true;
			cist.novaStatCesta();
		}
		else
		{
			cist.resetCesta();
		}

	}
	
	
	/**
	 * Vybere vhodnou cestu mezi dvema uzly a preda uzly, pres ktere tato cesta vede autu
	 * 
	 * @param idStart - id startovniho uzlu
	 * @param idCil - id ciloveho uzlu
	 * @param cist - pouzivana cisterna pro tuto cestu
	 */
	public float vyberCestu(int idStart ,int idCil, Auto auto)
	{
		//System.out.println("Vybiram cestu");
		int id = 0;
		float nejblizsi = 2000;
		float aktualni = 2000;
		float delkaCesty = 0;
	
		Uzel uzel = Simulator.objekty[idStart];
		Uzel cil = Simulator.objekty[idCil];
	
		while(!((Simulator.objekty[uzel.id].poloha[0] == Simulator.objekty[cil.id].poloha[0])&&
				(Simulator.objekty[uzel.id].poloha[1] == Simulator.objekty[cil.id].poloha[1]))){
			
			//nastaveni hodnoty nejblizsi na vyrazne vyssi, nez je ocekavana
			nejblizsi = 2000;
			
			for(Integer idtmp : uzel.sousedi){
				
				aktualni = cil.spoctiVzdalenost(Simulator.objekty[idtmp]);
				//System.out.println(aktualni);
				
				if(aktualni < nejblizsi){
					
					nejblizsi = aktualni;
					id = idtmp;
					
				}
				
			}
			
			//System.out.println("vybrano: "+nejblizsi);
			delkaCesty += uzel.spoctiVzdalenost(Simulator.objekty[id]);
			
			uzel = Simulator.objekty[id];
			
			auto.pridejUzel(uzel);
			//System.out.print(uzel.id+", ");
				
		}
		//System.out.println("Heureka");
		return delkaCesty;
		
	}
	
	
	/**
	 * Metoda projde seznam vozu pridelenych prekladisti a vybere prvni volne auto (=to, ktere
	 * neni na vyjezdu, nebo neni plne).
	 * 
	 * @return Prvni auto ktere je k dispozici.
	 */
	public Auto getVolneAuto()
	{
		Cisterna cist;
		//seznam aut je prazdny, vytvori se nove auto
		if(this.vozy.isEmpty())
		{
			cist = new Cisterna(Simulator.getCas(),this.id);
			sim.auta.add(cist);
			sim.addObserver(cist);
			this.vozy.add(cist);
			return this.vozy.get(0);
		}
		else
		{
			//prochazeni seznamu aut
			for(Auto a : this.vozy)
			{
				if (a.kDispozici)
				{
					return a;
				}
			}
			
			//pokud neni zadne auto volne, vrat null
			//return null;
			//prozatim pokud neni volne auto, vytvori nove a vrati ho
			cist = new Cisterna(Simulator.getCas(),this.id);
			sim.addObserver(cist);
			this.vozy.addLast(cist);
			return this.vozy.getLast();
		}
	}

}
