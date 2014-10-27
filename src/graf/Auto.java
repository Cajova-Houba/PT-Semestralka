package graf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


public class Auto implements Observer{

	protected static int DEF_ID = 0;
	
	public int id;
	
	public int objem;
	
	/**
	 * Auto je k dispozici a lze na nej nakladat.
	 */
	public boolean kDispozici;
	public float[] poloha;
	
	//soucasny cas
	public int[] soucCas;
	
	//doba nakladani v minutach
	//1 sud = 5 min
	//1hl pro cisternu = 2min
	public int dobaNakladani;
	
	//cas kdy bude nakladak nalozen
	public int[] nalozenoCas;
	
	/**
	 * Cesta po tkere auto pojede, obsahuje ID uzlu, bez pocatecniho.
	 */
	public LinkedList<Integer> cesta;
	
	/**
	 * True pokud auto jeden, nebo muze v dalsim kroku jet.
	 */
	public boolean jede;
	
	
	public Auto()
	{
		this.id = Auto.DEF_ID;
		Auto.DEF_ID++;
	}
	
	/**
	 * Metoda zajisti vykresleni uzlu na dany graficky kontext.
	 * @param g2 Garficky kontext.
	 */
	public void vykresliSe(Graphics2D g2, float Xmeritko, float Ymeritko)
	{
		//pokud auto pouze parkuje, nebudeme jej zobrazovat
		if(kDispozici)
		{
			return;
		}
		
		float w = 2*Xmeritko;
		float h = 2*Ymeritko;
		Color puvodniC = g2.getColor();
		
		//vykresleni sebe sama
		int x1 = (int)(this.poloha[0] * Xmeritko - (w/2));
		int y1 = (int)(this.poloha[1] * Ymeritko - (w/2));
		
		g2.setColor(Color.yellow);
		g2.fillRect(x1, y1, (int)w, (int)h);
		
		g2.setColor(puvodniC);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		//predani casu od tridy simulator
		if(arg1 instanceof int[])
		{
			this.soucCas = (int[])arg1;
			
			//porovnani soucasneho casu s casem nalozeni
			if((nalozenoCas[0] == soucCas[0]) && (nalozenoCas[1] == soucCas[1]))
			{
				this.dobaNakladani = 0;
			}
		}
	}
	
	/**
	 * Pokud je auto pripravene k vyjezdu = parkuje a je nalozene (pripadne vylozene), metoda vrati true.
	 * 
	 * @return True pokud je auto pripraveno k vyjezdu, false pokud ne.
	 */
	public boolean muzeVyjet()
	{
		if (!this.kDispozici && (this.dobaNakladani == 0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
