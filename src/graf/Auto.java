package graf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import main.Cas;
import main.MainApp;
import main.Simulator;


public class Auto implements Observer{

	protected static int DEF_ID = 0;
	
	public int id;
	
	public int objem;
	
	public int MAX_OBJEM;
	
	/**
	 * Auto je k dispozici a lze na nej nakladat.
	 */
	public boolean kDispozici;
	public float[] poloha;
	
	//soucasny cas
	public Cas soucCas;
	
	//doba nakladani v minutach
	//1 sud = 5 min
	//1hl pro cisternu = 2min
	public int dobaNakladaniJednotky;
	
	//celkova doba vykladani jedne objednavky
	public int dobaVykladani;
	
	//cas kdy bude nakladak nalozen
	public Cas nalozenoCas;
	
	/**
	 * Cesta po tkere auto pojede, obsahuje ID uzlu, bez pocatecniho.
	 */
	public LinkedList<Integer> cesta;
	
	//seznam zpracovavanych objednavek
	public	LinkedList<Objednavka> objednavky;
	
	/**
	 * True pokud auto jeden, nebo muze v dalsim kroku jet.
	 */
	public boolean jede;
	
	//ID domovskeho uzlu vozu
	protected final int DOMOV;
	
	/**
	 * Pocet zpracovanych objednavek.
	 */
	protected int zpracovaneObjednavky;
	
	public Auto(int domov)
	{
		this.id = Auto.DEF_ID;
		Auto.DEF_ID++;
		this.DOMOV = domov;
		this.zpracovaneObjednavky = 0;
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
		
		
		if(this instanceof Cisterna){
			g2.setColor(Color.red);
		}
		else{
			g2.setColor(Color.yellow);
		}
		
		g2.fillRect(x1, y1, (int)w, (int)h);
		
		g2.setColor(puvodniC);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		//predani casu od tridy simulator
		if(arg1 instanceof Cas)
		{
			this.soucCas = (Cas)arg1;
			
			//porovnani soucasneho casu s casem nalozeni
			if(soucCas.equals(nalozenoCas))
			{
				this.dobaVykladani = 0;
			}
		}
	}
	
	
	/**
	 * Metoda nalozi priradi nakladaku objednavku. Vrati false pokud se objednavku nepodari pridelit (nakladak je plny).
	 * Pokud se objednavku podari priradit, vrati true.
	 * 
	 * @param Objednavka.
	 * @return True pokud byla objednavka prijata, false pokud ne.
	 */
	public boolean pridejObjednavku(Objednavka obj)
	{
		if (naloz(obj.objem))
		{
			//this.objednavky.add(new Objednavka(obj.id, obj.cas, obj.objem, obj.den));
			this.objednavky.add(obj);
			//pridani casu 5*pocet_sudu
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Metoda nalozi na nakladak pozadovane mnozstvi piva. Vraci false pokud se nepodarilo nalozit
	 * (nakladak byl plny), true pokud se naklad podaril.
	 * 
	 * @param mnozstvi Mnozstvi piva (sudy).
	 * @return True pokud bylo nalozeno, false pokud ne.
	 */
	public boolean naloz(int mnozstvi)
	{
		if(this.objem + mnozstvi > MAX_OBJEM)
		{
			return false;
		}
		else
		{
			this.objem += mnozstvi;
			return true;
		}
	}
	
	

	/**
	 * Metoda porovna soucasnou polohu se vsemi polohami v zadanych objednavka. Pokud se nejaka shoduje, 
	 * nakladak vylozi danny pocet sudu a objednavku vyradi.
	 * @return doba vykladani
	 */
    public int vyloz()
    {
    	String retezec = null;
    	int potrebnyCas = 0;
    	float chyba = 0.01f;
    	Objednavka vyrad = null;
    	for(Objednavka o : this.objednavky)
    	{
    		int[] tmp = Simulator.getPoloha(o.id);
    		if(Math.abs(tmp[0] - poloha[0]) < chyba &&
    		   Math.abs(tmp[0] - poloha[0]) < chyba)
    		   {
    				if(this.DOMOV == 0){
    					retezec = "Auto " + this.id + " patrici pivovaru vyklada v case: " + Simulator.getCas().toString() + " "+o.toString();
    				}
    				else{
    					retezec = "Auto " + this.id + " patrici prekladisti " + (this.DOMOV - 4001) + " vyklada v case: " + Simulator.getCas().toString() + " "+o.toString();
    				}
    				
    				MainApp.zapisDoSouboru(retezec);
    				System.out.println(retezec);
    				
    				objem -= o.objem;
    				Simulator.objekty[o.id].sklad += o.objem;
    				potrebnyCas = o.objem * this.dobaNakladaniJednotky;
    			    vyrad = o;
    			    break;
    		   }
    	}
    	if(vyrad != null)
    	{
    		this.objednavky.remove(vyrad);
    		zpracovaneObjednavky++;
    	}
    	
    	return potrebnyCas;
    }
    
    

	
	/**
	 * Prida uzel cesty
	 * 
	 * @param uzel
	 */
	public void pridejUzel(Uzel uzel)
	{
		cesta.add(uzel.id);
		
	}
	
	
	/**
	 * Pokud je auto pripravene k vyjezdu = parkuje a je nalozene (pripadne vylozene), metoda vrati true.
	 * 
	 * @return True pokud je auto pripraveno k vyjezdu, false pokud ne.
	 */
	public boolean muzeVyjet()
	{
		if (!this.kDispozici)
		{
			
			if(this.dobaVykladani <= 0){
				return true;
			}
			else{
				//System.out.println("Auto " + this.id + "ceka jeste " + this.dobaNakladani + " minut v case " + Simulator.getCas().minuta);
				return false;
			}
			
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Metoda vrati pocet zpracovanych (=dorucenych) objednavek.
	 * @return Pocet dorucenych objednavek.
	 */
	public int getPocZpracObj()
	{
		return this.zpracovaneObjednavky;
	}
}
