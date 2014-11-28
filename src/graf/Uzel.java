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
public class Uzel{
	
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
	 * Odkaz na tridu simulator kvuli prihlasovani novych aut. (navrhovy vzor vydavatel-predplatitel)
	 */
	public Simulator sim;
	
	/**
	 * Indikace vybrani uzlu ke zvyrazneni na mape.
	 */
	public boolean jeVybrany;
	
	/**
	 * Kolik bylo celkem dovezeno piva. Promenna se zvysi vzdy pri vylozeni piva.
	 */
	public int celkemPiva;
	
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
		this.sim = sim;
		this.jeVybrany = false;
		celkemPiva = 0;
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
		
		if(this.jeVybrany)
		{
			g2.setColor(Color.ORANGE);
		}
		else
		{			
			g2.setColor(this.typ.getBarva());
		}
		
		
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
		g2.setColor(puvodniC);
	}
	
	public void vykresliCesty(Graphics2D g2, float Xmeritko, float Ymeritko)
	{
		Color puvodniC = g2.getColor();
		g2.setColor(Color.black);	
		
		float w = 3*Xmeritko;
		int x1 = (int)(this.poloha[0] * Xmeritko - (int)(w/2));
		int y1 = (int)(this.poloha[1] * Ymeritko - (int)(w/2));
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
	
	/**
	 * Pokud stred uzlu lezi na zadanych souradnichc, vrati true. Metoda vraci true pokud stred uzlu lezi take v uzkem okoli kliknuti.
	 * @param x X.
	 * @param y Y.
	 * @return
	 */
	public boolean lezisTady(int x, int y)
	{
		int pres = 2;
		int xs = this.poloha[0], ys = this.poloha[1];
		
		if ((x >= xs-pres) && (x <= xs+pres) && (y >= ys-pres) && (y <= ys+pres))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Uzel prijme dane mnozstvi piva. Protoze hospody a prekladiste maji jiny zpusob prijeti
	 * (u hospody se sklad prepisuje, u prekladiste se sklad zvysi), je nutne metodu v techto tridach
	 * upravit. ID auta se predava kvuli statistickemu zaznamu.
	 * @param ob Prijata objednavka.
	 * @param idAuta ID auta, ktere naklad dovezlo.
	 */
	public void prijmiNaklad(Objednavka ob, int idAuta)
	{
		this.celkemPiva += ob.objem;
	}
	
}
