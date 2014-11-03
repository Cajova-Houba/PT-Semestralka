package graf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import main.Cas;
import main.Simulator;


/**
 * Trida predstavujici uzel v grafu (tedy pivovar, prekladiste, nebo hospodu).
 * 
 * @author Zděnek Valeš
 *
 */
public class Uzel implements Observer{
	
	/**
	 * Identifikator uzlu.
	 */
	public final int id;
	
	/**
	 * Poloha uzlu v mape 500km x 500km.
	 */
	public final int[] poloha = new int[2];
	
	/**
	 * Typ uzlu. Viz {@link UzelTyp}.
	 */
	public final UzelTyp typ;
	
	/**
	 * Pole sousedu uzlu. Pole obsahuje pouze ID sousedu.
	 */
	public LinkedList<Integer> sousedi = new LinkedList<Integer>();
	
	/**
	 * Stav skladu uzlu.
	 */
	public int sklad;
	
	/**
	 * Soucasny cas. Aktualizovano z tridy Simulator pres navrhovy vzor Vydavatel-Predplatitel.
	 */
	private Cas soucCas;
	
	/**
	 * Odkaz na tridu simulator kvuli prihlasovani novych aut. (navrhovy vzor vydavatel-predplatitel)
	 */
	public Simulator sim;
	
	/**
	 * Vytvori uzel danych parametru.
	 * 
	 * @param id Identifikator uzlu.
	 * @param typ Typ uzlu.
	 * @param x X-ova poloha uzlu.
	 * @param y Y-ova poloha uzlu.
	 */
	public Uzel(int id, UzelTyp typ, int x, int y, Simulator sim)
	{
		this.poloha[0] = x;
		this.poloha[1] = y;
		this.id = id;
		this.typ = typ;
		this.soucCas = new Cas();
		this.sim = sim;
	}
	
	/**
	 * Metoda zajisti vykresleni uzlu na dany graficky kontext.
	 * @param g2 Garficky kontext.
	 */
	public void vykresliSe(Graphics2D g2, float Xmeritko, float Ymeritko)
	{
		float w = 3*Xmeritko;
		float h = 3*Ymeritko;
		Color puvodniC = g2.getColor();
		
		//vykresleni sebe sama
		int x1 = (int)(this.poloha[0] * Xmeritko - (int)(w/2));
		int y1 = (int)(this.poloha[1] * Ymeritko - (int)(w/2));
		
		g2.setColor(this.typ.getBarva());
		
		
		/**
		 * test vyberu vhodneho prekladiste(1-8) pro kazdou hospodu
		 * pro prehlednost hospody pro vybrane prekladiste nekresli
		 * pro fungovani nutno zakomentovat radek oznaceny radkovym komentarem
		 */
		/*
		boolean kresli = true;
		for(Uzel uzel : Simulator.prekladiste.get(4000+2).hospodySud.values()){
			if(this==uzel){ kresli = false; break;}
		}
		if(kresli == true){
			g2.fillRect(x1, y1, (int)w, (int)h);
		}
		*/
		
		// tady
		g2.fillRect(x1, y1, (int)w, (int)h);
		
		
		//vykresleni cest k sousedum
		
		g2.setColor(Color.black);	
		
		int[] sousedPoloha = new int[2];
		int x2,y2;
		
		for(Integer i : this.sousedi)
			
		{ 
			sousedPoloha = Simulator.objekty[i.intValue()].poloha;
			x2 = (int)(sousedPoloha[0] * Xmeritko);
			y2 = (int)(sousedPoloha[1] * Ymeritko);
			g2.drawLine(x1, y1, x2, y2);
			
		}
		
		g2.setColor(puvodniC);
	}
	
	/**
	 * Spocte vzdalenost mezi soucasnym uzlem a dodanym uzlem.
	 * 
	 * @return Vzdalenost mezi uzly.
	 */
	public float spoctiVzdalenost(Uzel uzel)
	{
		
		return (float) Math.sqrt((uzel.poloha[0] - this.poloha[0]) * (uzel.poloha[0] - this.poloha[0]) +
				(uzel.poloha[1] - this.poloha[1]) * (uzel.poloha[1] - this.poloha[1]));
		
		
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		//predani casu od tridy simulator
		if(arg1 instanceof Cas)
		{
			this.soucCas = (Cas)arg1;
		}
	}
}
