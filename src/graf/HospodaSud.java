package graf;

import main.Simulator;

public class HospodaSud extends Uzel{

	/**
	 * udaje o dnesni objednavce
	 */
	public Objednavka nova;
	
	/**
	 * udaje o vcerejsi objednavce
	 */
	public Objednavka stara;
	
	/**
	 * pocet prazdnych sudu cekajicich na vraceni
	 */
	public int prazdneSudy;
	
	/**
	 * id prekladiste, od ktereho hospoda odebira
	 */
	public int idPrekladiste;
	
	
	


	/**
	 * Vytvori hospodu danych parametru. Vynuluje objednavky a pocet prazdnych sudu.
	 * 
	 * @param id Identifikator uzlu.
	 * @param typ Typ uzlu.
	 * @param x X-ova poloha uzlu.
	 * @param y Y-ova poloha uzlu.
	 */
	public HospodaSud(int id, UzelTyp typ, int x, int y, Simulator sim)
	{
		super(id, typ, x, y, sim);
		
		nova = new Objednavka(id,0,0,0);
		stara = new Objednavka(id,0,0,0);
		
		prazdneSudy = 0;
	}

	
	/**
	 * Metoda pro zadani objednavky, metodu vola Simulator
	 * 
	 * @param cas - cas, kdy hospoda poda objednavku
	 * @param pocetSudu - pocet sudu, ktere si hospoda objedna
	 */
	public void zadejObjednavku(int cas, int pocetSudu, int den)
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
		nova.setObjem(pocetSudu);
		nova.setDen(den);
		nova.soucCas = this.soucCas.clone();
		
		/*
		 * testovaci vypis
		 */
		//System.out.println(nova.den+", "+nova.cas+", "+stara.den+", "+stara.cas);
		
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
	
	
	public void setIdPrekladiste(int idPrekladiste) {
		this.idPrekladiste = idPrekladiste;
	}
	
}
