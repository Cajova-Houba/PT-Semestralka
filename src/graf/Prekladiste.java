package graf;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import main.Simulator;


public class Prekladiste extends Uzel{

	/**
	 * pocet sudu k dispozici k vydeji
	 */
	int pocetSudu;
	
	/**
	 * pocatecni a zaroven maximalni pocet sudu na prekladisti
	 */
	private final int PRIPRAVENE_SUDY = 2000;
	
	/**
	 * mapa hospod zavislych na prisunu sudu z tohoto prekladiste
	 */
	public Map<Integer, HospodaSud> hospodySud = new TreeMap<Integer, HospodaSud>();
	
	/**
	 * fronta prijatych objednavek
	 */
	public PriorityQueue<Objednavka> objednavky = new PriorityQueue<Objednavka>(); 
	
	/**
	 * Seznam aut prirazenych prekladisti.
	 */
	public LinkedList<Auto> vozy; 
	
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
		
		pocetSudu = PRIPRAVENE_SUDY;
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
		//int delkaCesty = 0;
		
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
		vyberCestu(this.id, ob.id, nakl);
		
		
		while(nakl.pridejObjednavku(ob))
		{
			idtmp = ob.id;
			
			if(objednavky.size() < 1){
				break;
			}
			
			ob = vyberObjednavku(ob.id);	
			objednavky.remove(ob);

			vyberCestu(idtmp, ob.id, nakl);
			
			if(nakl.objem > 24){
				//System.out.println("Nakladak ma "+nakl.objem);
				nakl.kDispozici = false;
				break;		
			}
			
		}
		
		//cesta zpatky
		vyberCestu(ob.id, this.id, nakl);
		nakl.kDispozici = false;
		nakl.jede = true;

	}

	
	
	/**
	 * Metoda vybere objednavku ktera je nejblize zadanemu uzlu.
	 * 
	 * @param ID uzlu   
	 */
	public Objednavka vyberObjednavku(int iduzlu)
	{
		//System.out.println("Vybiram objednavku");
		Uzel uzel = Simulator.objekty[iduzlu];
		float aktualni = 2000;
		float nejblizsi = 2000;
		Objednavka objednavka = null;
		
		for(Objednavka ob : objednavky){
				
			aktualni = uzel.spoctiVzdalenost(Simulator.objekty[ob.id]);
			//System.out.println(aktualni);
			
			if(aktualni < nejblizsi){
					
				nejblizsi = aktualni;
				objednavka = ob;
				
			}
			
			
		}
		//System.out.println("nejblizsi objednavka: " + nejblizsi);
		/*System.out.println();
		System.out.print("+objednavka: " + objednavka.id + " pres:");*/
		return objednavka;
		
	}

	
	
	/**
	 * 
	 * 
	 * @param 
	 */
	public void vyberCestu(int idStart ,int idCil, Nakladak nakl)
	{
		//System.out.println("Vybiram cestu");
		int delkaCesty = 0;
		int id = 0;
		float nejblizsi = 2000;
		float aktualni = 2000;
	
		Uzel uzel = Simulator.objekty[idStart];
		Uzel cil = Simulator.objekty[idCil];
		
		while(uzel != cil){
			
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
			delkaCesty += nejblizsi;
			uzel = Simulator.objekty[id];
			
			nakl.pridejUzel(uzel);
			//System.out.print(uzel.id+", ");
				
		}
		//System.out.println("Heureka");
		//return delkaCesty;
	}
	
	
	/**
	 * Prida objednavku do fronty
	 * 
	 * @param HospodaSud hospoda, pro kterou je nejblizsi prave tohle prekladiste
	 */
	public void prijmiObjednavku(Objednavka objednavka)
	{
		
		objednavky.add(objednavka);	
	}
	
	
	/**
	 * Metoda projde seznam vozu pridelenych prekladisti a vybere prvni volne auto (=to, ktere
	 * neni na vyjezdu, nebo neni plne).
	 * 
	 * @return Prvni auto ktere je k dispozici.
	 */
	private Auto getVolneAuto()
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
	
	
	
	/**
	 * Metoda vykresli vozy prislusici danemu prekladisty. Vykresluji se pouze vozy ktere jedou a neparkuji v 
	 * v prekladisti.
	 * 
	 * @param g2 Dodany graficky kontext.
	 * @param Xmeritko	Meritko X-osy.
	 * @param Ymeritko  Meritko Y-osy.
	 */
	public void vykresliVozy(Graphics2D g2, float Xmeritko, float Ymeritko)
	{
		for(Auto a : this.vozy)
		{
			a.vykresliSe(g2, Xmeritko, Ymeritko);
		}
	}
}
