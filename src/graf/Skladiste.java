package graf;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.PriorityQueue;

import main.Simulator;

public class Skladiste extends Uzel{

	/**
	 * Seznam aut prirazenych prekladisti.
	 */
	public LinkedList<Auto> vozy; 
	
	/**
	 * fronta prijatych objednavek
	 */
	public PriorityQueue<Objednavka> objednavky = new PriorityQueue<Objednavka>(); 
	
	
	public Skladiste(int id, UzelTyp typ, int x, int y, Simulator sim)
	{
		super(id, typ, x, y, sim);
		
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
	 * Prida objednavku do fronty
	 * 
	 * @param HospodaSud hospoda, pro kterou je nejblizsi prave tohle prekladiste
	 */
	public void prijmiObjednavku(Objednavka obj)
	{
		
		objednavky.add(new Objednavka(obj.id, obj.cas, obj.objem, obj.den));
		
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
