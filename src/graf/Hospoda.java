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
	 * Vysledna statistika.
	 */
	public LinkedList<statZaznam> statistika;
	
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
		statistika.add(new statZaznam(new Cas(ob.den, ob.cas, 0), soucCas, ob.objem, autoID));
	}
	
	/**
	 * Metoda reprezentuje jeden zaznam urceny pro vyslednou statitiku.
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
		
	}
}
