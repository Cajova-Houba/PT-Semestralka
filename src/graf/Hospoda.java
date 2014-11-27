package graf;

import java.util.LinkedList;

import main.Cas;
import main.Simulator;

public class Hospoda extends Uzel{

	/**
	 * udaje o dnesni objednavce
	 */
	public Objednavka nova;
	
	/**
	 * udaje o vcerejsi objednavce
	 */
	public Objednavka stara;
	
	/**
	 * Vysledna statistika. Pole ma 7 prvku, pro kazdy den 1
	 */
	public statZaznam[] statistika;
	
	/**
	 * Vytvori hospodu danych parametru.
	 * 
	 * @param id Identifikator uzlu.
	 * @param typ Typ uzlu.
	 * @param x X-ova poloha uzlu.
	 * @param y Y-ova poloha uzlu.
	 */
	public Hospoda(int id, UzelTyp typ, int x, int y, Simulator sim) {
		super(id, typ, x, y, sim);
		// TODO Auto-generated constructor stub
		nova = new Objednavka(id,0,0,0);
		stara = new Objednavka(id,0,0,0);
		
		statistika = new statZaznam[7];
	}
	
	
	/**
	 * Metoda pro rucni zadani objednavky, metodu bude volat prvek gui
	 * Na zacatku dne uz byla vygenerovana objednavka pro tuto hospodu, proto nahradi novou objednavku
	 * 
	 * @param cas - cas, kdy hospoda poda objednavku
	 * @param pocetSudu - pocet sudu, ktere si hospoda objedna
	 */
	public void zmenaObjednavky(int cas, int pocetSudu)
	{
		
		/*
		 * ulozeni udaju o nove objednavce
		 */
		nova.setCas(cas);
		nova.setObjem(pocetSudu);
		
		/*
		 * testovaci vypis
		 */
		//System.out.println(nova.cas+", "+nova.objem+", "+stara.cas+", "+stara.objem);
		
	}
	
	/**
	 * Metoda pro zadani objednavky, metodu vola Simulator
	 * 
	 * @param cas - cas, kdy hospoda poda objednavku
	 * @param pocetHLitru - pocet hektolitru, ktere si hospoda objedna
	 */
	public void zadejObjednavku(int cas, int mnozstvi, int den)
	{
		/*
		 * presunuti nove objednavky do stare
		 */
		stara.setCas(nova.getCas());
		stara.setObjem(nova.getObjem());
		stara.setDen(nova.getDen());
		
		/*
		 * ulozeni udaju o nove objednavce
		 */
		nova.setCas(cas);
		nova.setObjem(mnozstvi);
		nova.setDen(den);
		nova.soucCas = this.soucCas.clone();
		/*
		 * testovaci vypis
		 */
		//System.out.println(nova.cas+", "+nova.objem+", "+stara.cas+", "+stara.objem);
		
	}

	/**
	 * Metoda prida statisticky zaznam o prave dodane objednavce.
	 * @param autoID	ID auta, ktere objednavku dovezlo.
	 * @param ob		Odkaz na dovezenou objednavku.
	 */
	public void pridejStatZaznam(int autoID, Objednavka ob)
	{

		statistika[soucCas.den] = new statZaznam(new Cas(ob.den, ob.cas, 0), soucCas, ob.objem, autoID);
	}
	
	/**
	 * Metoda vrati statistiku pro jeden den v podobne xml tagu.
	 * @param den	Den.
	 * @param odsazeni	Pocet tabulatoru pro odsazeni kazdeho tagu.
	 * @return	Statistika pro jeden den v XML.
	 */
	public String statistikaXML(int den, int odsazeni)
	{
		StringBuffer vysledek = new StringBuffer();
		if(den < 0 || den > 6 || statistika[den] == null)
		{
			return "";
		}
		
		return statistika[den].toXMLtags(odsazeni);
	}
	
	/**
	 * Prepravka pro uchovavani statistickych zaznamu.
	 * @author Zdenek Vales
	 *
	 */
	private class statZaznam
	{
		public Cas casPodani;
		public Cas casDoruceni;
		public int mnozstvi;
		public int autoID;
		
		
		public statZaznam(Cas casPodani, Cas casDoruceni, int mnozstvi, int autoID) {
			super();
			this.casPodani = casPodani;
			this.casDoruceni = casDoruceni;
			this.mnozstvi = mnozstvi;
			this.autoID = autoID;
		}
		
		public String toCSVString()
		{
			return casPodani.toString()+","+casDoruceni.toString()+","+mnozstvi+","+autoID;
		}
		
		/**
		 * Metoda prevede zaznam do xml tagu a pred kazdy tag prida zadany pocet tabulatoru.
		 * @param odsazeni Pocet tabulatoru.
		 * @return	Zaznam jako XML tag.
		 */
		public String toXMLtags(int odsazeni)
		{
			//vyrobeni odsazeni
			StringBuffer ods = new StringBuffer();
			for (int i = 0; i < odsazeni; i++) {
				ods.append("\t");
			}
			
			return ods+"<casPodani value=\""+casPodani+"\" />\r\n"+
				   ods+"<casDoruceni value=\""+casDoruceni+"\" />\r\n"+
				   ods+"<mnozstvi value=\""+mnozstvi+"\" />\r\n"+
				   ods+"<autoID value=\""+autoID+"\" />\r\n";
			
			/*lepsi pokud by bylo vice objednavek za den
			 * return ods+"<zaznam casPodani=\""+casPodani+"\" "
					+ "casDoruceni=\""+casDoruceni+"\" "
					+ "mnozstvi=\""+mnozstvi+"\" "
					+ "autoID=\""+autoID+"\" />\r\n";*/
		}
		
	}
}
