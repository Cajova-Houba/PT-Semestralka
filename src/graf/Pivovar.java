package graf;


import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import main.Simulator;

public class Pivovar extends Uzel{

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
	 * fronta prijatych objednavek
	 */
	public PriorityQueue<Objednavka> objednavky = new PriorityQueue<Objednavka>(); 
	
	/**
	 * Seznam aut prirazenych prekladisti.
	 */
	public LinkedList<Auto> vozy; 
	
	
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
	 * Prida objednavku do fronty
	 * 
	 * @param HospodaSud hospoda, pro kterou je nejblizsi prave tohle prekladiste
	 */
	public void prijmiObjednavku(Objednavka objednavka)
	{
		
		objednavky.add(objednavka);
		
	}
	
	/**
	 * Metoda vytvori testovaci nakladak a necha ho objet prekladiste. Pak se nakladak vrati do pivovaru.
	 */
	public void testAuta()
	{
		Nakladak nakl = new Nakladak(Simulator.getCas());
		nakl.poloha[0] = this.poloha[0];
		nakl.poloha[1] = this.poloha[1];
		//cesta po prekladistich
		for(int i=4001;i<4009;i++)
		{
			nakl.cesta.add(i);
		}
		nakl.kDispozici = false;
		nakl.jede = true;
		sim.addObserver(nakl);
		
		this.vozy.add(nakl);
		
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
