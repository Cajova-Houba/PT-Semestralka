package graf;


import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import main.Simulator;


public class Prekladiste extends Skladiste{

	/**
	 * pocet sudu k dispozici k vydeji
	 */
	public int pocetSudu;
	
	/**
	 * pocatecni a zaroven maximalni pocet sudu na prekladisti
	 */
	private final int PRIPRAVENE_SUDY = 2000;
	
	/**
	 * mapa hospod zavislych na prisunu sudu z tohoto prekladiste
	 */
	public Map<Integer, HospodaSud> hospodySud = new TreeMap<Integer, HospodaSud>();
	
	
	
	/**
	 * Vytvori prekladiste danych parametru. Nastavi pocet sudu na defaultni hodnotu.
	 * 
	 * @param id Identifikator uzlu.
	 * @param typ Typ uzlu.
	 * @param x X-ova poloha uzlu.
	 * @param y Y-ova poloha uzlu.
	 */
	public Prekladiste(int id, UzelTyp typ, int x, int y, Simulator sim)
	{
		super(id, typ, x, y, sim);
		
		sklad = PRIPRAVENE_SUDY;
		this.vozy = new LinkedList<Auto>();
	}
	
	
	/**
	 * Prihlasi hospodu mezi odberatele od tohoto prekladiste
	 * 
	 * @param HospodaSud hospoda, pro kterou je nejblizsi prave tohle prekladiste
	 */
	public void prihlasHospodu(HospodaSud hospoda)
	{
		
		hospodySud.put(hospoda.id, hospoda);
		
		//kontrolni vypis
		/*
		System.out.println(this.id);
		*/
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
		
		Nakladak nakl = (Nakladak) getVolneAuto();
		
		nakl.poloha[0] = this.poloha[0];
		nakl.poloha[1] = this.poloha[1];
		Objednavka ob = this.objednavky.remove();
		/*System.out.println();
		System.out.println("Objednavka hospody:" + ob.id + " se zpracuje pres trasu: ");
		*/
		delkaCesty += vyberCestu(this.id, ob.id, nakl);
		//if(){
			
		//}
		
		while(nakl.pridejObjednavku(ob))
		{
			idtmp = ob.id;
			
			if(objednavky.size() < 1){
				break;
			}
			
			ob = vyberObjednavku(ob.id);	
			objednavky.remove(ob);

			delkaCesty += vyberCestu(idtmp, ob.id, nakl);
			
			if((nakl.objem > 24)||(13-Simulator.getCas().hodina)*(nakl.RYCHLOST) + 100 < delkaCesty){
				//System.out.println("Nakladak ma "+nakl.objem);
				nakl.kDispozici = false;
				//System.out.println("Auto jede " + delkaCesty + "km");
				break;		
			}
			/*
			if((Simulator.getCas().hodina > 12) && (delkaCesty > 80) ){
				//System.out.println("Nakladak ma "+nakl.objem);
				nakl.kDispozici = false;
				//System.out.println("Auto jede " + delkaCesty + "km");
				break;		
			}
			
			if((Simulator.getCas().hodina > 9) && (delkaCesty > 130) ){
				//System.out.println("Nakladak ma "+nakl.objem);
				nakl.kDispozici = false;
				//System.out.println("Auto jede " + delkaCesty + "km");
				break;		
			}*/
			
		}
		
		
		//cesta zpatky
		vyberCestu(ob.id, this.id, nakl);
		nakl.kDispozici = false;
		nakl.jede = true;

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
				if(idtmp == 0){
					continue;
				}
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
		Nakladak nakl;
		//seznam aut je prazdny, vytvori se nove auto
		if(this.vozy.isEmpty())
		{
			nakl = new Nakladak(Simulator.getCas(),this.id);
			sim.addObserver(nakl);
			this.vozy.add(nakl);
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
			nakl = new Nakladak(Simulator.getCas(),this.id);
			sim.addObserver(nakl);
			this.vozy.addLast(nakl);
			return this.vozy.getLast();
		}
	}

}
