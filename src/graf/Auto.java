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
	protected LinkedList<Integer> cesta;
	
	//seznam zpracovavanych objednavek
	public	LinkedList<Objednavka> objednavky;
	
	/**
	 * True pokud auto jeden, nebo muze v dalsim kroku jet.
	 */
	public boolean jede;
	
	//ID domovskeho uzlu vozu
	protected final int DOMOV;
	
	/**
	 * Typ auta - auto, nakladak, cisterna, kamion. Pouze pro ulehceni metody, ktera tvori statistiku.
	 */
	protected String typ;
	
	/**
	 * Pole seznamu cest. Na kazdy den pripada jeden seznam cest (=pole ma velikost 7).
	 * Kazda polozka seznamu obsahuje pole statistickych zaznamu. 
	 * Jeden tento statisticky zaznam indikuje uzel
	 * kterym auto projelo. 
	 */
	protected LinkedList<statZaznam[]>[] statistika;
	
	/**
	 * Pocet zpracovanych objednavek.
	 */
	protected int zpracovaneObjednavky;
	
	/**
	 * Jedna statisticka cesta. Prekladiste (pivovar) tento seznam vzdy pri vyslani auta 
	 * znovuvytvori. Po skonceni okruzni cesty, se udaje z tohoto seznamu prevedou na pole
	 * a ulozi do statistika.
	 */
	protected LinkedList<statZaznam> statCesta;
	
	public Auto(int domov)
	{
		this.id = Auto.DEF_ID;
		Auto.DEF_ID++;
		this.DOMOV = domov;
		this.zpracovaneObjednavky = 0;
		this.cesta = new LinkedList<Integer>();
		this.statistika = new LinkedList[7];
		this.typ = "Obecne auto";
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
    		    //auto vyklada
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
    				//prijeti objednavky uzlem
    				Simulator.objekty[o.id].prijmiNaklad(o,this.id);
    				potrebnyCas = o.objem * this.dobaNakladaniJednotky;
    			    vyrad = o;
    			    
    			    //pridani statistickeho znaznmu
    			    if(statCesta != null)
    			    {   
    			    	//kontrola jestli posledni zadana cesta nema stejn id hospody
    			    	//jinak dochazi k vice stejnym zaznamum
    			    	if(statCesta.isEmpty())
    			    	{    			    		
    			    		statCesta.add(new statZaznam(o.id, o.objem, 0));
    			    	}
    			    	else if(statCesta.getLast().idUzlu != o.id)
    			    	{
    			    		statCesta.add(new statZaznam(o.id, o.objem, 0));
    			    	}
    			    }
    			    break;
    		   }
    		else
    			{
    				//auto pouze projizdi
    			 if(statCesta != null)
 			    {   
    				//kontrola jestli posledni zadana cesta nema stejn id hospody
 			    	//jinak dochazi k vice stejnym zaznamum
 			    	if(statCesta.isEmpty())
			    	{    			    		
 			    		statCesta.add(new statZaznam(cesta.getFirst(), -1, 0));
			    	}
			    	else if(statCesta.getLast().idUzlu != cesta.getFirst())
			    	{
			    		statCesta.add(new statZaznam(cesta.getFirst(), -1, 0));
			    	}
 			    }
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
	
	/**
	 * Metoda smaze ulozenou cestu.
	 */
	public void resetCesta()
	{
		this.cesta = new LinkedList<Integer>();
	}
	
	/**
	 * Metoda vytvori promennou statCesta.
	 */
	public void novaStatCesta()
	{
		this.statCesta = new LinkedList<statZaznam>();
		this.statCesta.add(new statZaznam(this.DOMOV, -1, 0)); //pocatek cesty je v prekladisti
	}
	
	/**
	 * Metoda prida novou cestu do seznamu statistik.
	 * Statistickou cestu si auto vytvari pri okruzni jizde.
	 * Tato metoda ji prevede ze seznamu na pole a ulozi do nove
	 * vytvorene polozky ve statistika.
	 * 
	 * @param Jedna okruzni cesta auta.
	 */
	public void vlozStatCestu(LinkedList<statZaznam> stCesta)
	{
		if (stCesta == null)
		{
			return;
		}
		int d = Simulator.getCas().den;
		if(statistika[d] == null)
		{
			statistika[d] = new LinkedList<statZaznam[]>();
		}
		statistika[d].add(stCesta.toArray(new statZaznam[0]));
	}
	
	/**
	 * Kompletni statistika 1 auta za 7 dni.
	 * @param odsazeni Pocet tabulatoru jako odsazeni.
	 * @return	Statistika v XML
	 */
	public String statistikaXML(int odsazeni)
	{
		//odsazeni
		StringBuffer ods = new StringBuffer();
		StringBuffer vysledek = new StringBuffer();
		for (int i = 0; i < odsazeni; i++) {
			ods.append("\t");
		}
		
		String nl = "\r\n";
		vysledek.append(ods+"<auto id=\""+this.id+"\"  typ=\""+this.typ+"\">"+nl);
		for (int i = 0; i < 7; i++) {			
			vysledek.append(ods+"\t<den cislo=\""+i+"\">"+nl);
			vysledek.append(statistikaXMLDen(i,odsazeni+2));
			vysledek.append(ods+"\t</den>"+nl);
		}
		vysledek.append(ods+"</auto>"+nl);
		
		return vysledek.toString();
	}
	
	/**
	 * Metoda vrati statistiku pro 1 den v XML formatu.
	 * @param odsazeni	Pocet tabulatoru jako odsazeni.
	 * @return	Statistika v XML.
	 */
	public String statistikaXMLDen(int den, int odsazeni)
	{
		int d =  den;
		if (statistika[d] == null || den < 0 || den > 0 )
		{
			return "";
		}
		if(statistika[d].size() == 0)
		{
			return "";
		}
		
		/* struktura
		 * <cesta c=1>
		 * 		<stat zaznam 1 />
		 * 		<stat zaznam 2 />
		 * 		...
		 * </cesta>
		 * <cesta c=2>
		 * 		...
		 * </cesta>
		 */
		
		//odsazeni
		StringBuffer ods = new StringBuffer();
		StringBuffer vysledek = new StringBuffer();
		for (int i = 0; i < odsazeni; i++) {
			ods.append("\t");
		}
		
		int pocC = 0; //cislo cesty od 0
		String nl = "\r\n";
		LinkedList<statZaznam[]> st = statistika[d];
		for(statZaznam[] sz : st)
		{
			vysledek.append(ods+"<cesta id=\""+pocC+"\" >"+nl);
			for (int i = 0; i < sz.length; i++) {
				vysledek.append(sz[i].toXMLTags(odsazeni+1));
			}
			vysledek.append(ods+"</cesta>"+nl);
			pocC++;
		}
		
		return vysledek.toString();
	}
	
	/**
	 * Prepravka pro vytvoreni vysledne statistiky.
	 * 
	 * @author Zdenek Vales
	 *
	 */
	protected class statZaznam
	{
		/**
		 * ID uzlu kterym auto projizdelo, pripadne v nem vykladalo/nakladalo.
		 */
		public int idUzlu;
		
		/**
		 * Naklad vylozeny v uzlu. Pokud je -1 auto pouze projizdelo.
		 */
		public int naklad;
		
		/**
		 * Sudy vyzvednute v hospode.
		 */
		public int vyzvednuteSudy;

		
		public statZaznam(int idHospody, int naklad, int vyzvednuteSudy) {
			super();
			this.idUzlu = idHospody;
			this.naklad = naklad;
			this.vyzvednuteSudy = vyzvednuteSudy;
		}
		
		/**
		 * Metoda prevede statisticky zaznam na 1 XML element. 
		 * @param odsazeni Pocet tabulatoru vlozeny pred element.
		 * @return XML element.
		 */
		public String toXMLTags(int odsazeni)
		{
			//vyrobeni odsazeni
			StringBuffer ods = new StringBuffer();
			for (int i = 0; i < odsazeni; i++) {
				ods.append("\t");
			}
			
			//zadne vykladani ani nakladani, vratit pouze id uzlu
			if(naklad < 0)
			{
				return ods.toString()+"<uzel id=\""+this.idUzlu+"\" />\r\n";
			}
			else
			{
				return ods.toString()+"<uzel id=\""+this.idUzlu+"\" "
								+ "		vylozeno=\""+this.naklad+"\" "
								+ "		nalozenoSudu=\""+this.vyzvednuteSudy+"\" />";
			}
		}
	}
}
