package graf;

import main.Simulator;

public class HospodaTank extends Uzel{

	/**
	 * udaje o dnesni objednavce
	 */
	public Objednavka nova;
	
	/**
	 * udaje o vcerejsi objednavce
	 */
	public Objednavka stara;
	
	

	/**
	 * Vytvori hospodu danych parametru. Vynuluje objednavky.
	 * 
	 * @param id Identifikator uzlu.
	 * @param typ Typ uzlu.
	 * @param x X-ova poloha uzlu.
	 * @param y Y-ova poloha uzlu.
	 */
	public HospodaTank(int id, UzelTyp typ, int x, int y, Simulator sim)
	{
		super(id, typ, x, y, sim);
		
		nova = new Objednavka(id,0,0,sim.getCas().den);
		stara = new Objednavka(id,0,0,sim.getCas().den);

	}
	
	
	/**
	 * Metoda pro zadani objednavky, metodu vola Simulator
	 * 
	 * @param cas - cas, kdy hospoda poda objednavku
	 * @param pocetHLitru - pocet hektolitru, ktere si hospoda objedna
	 */
	public void zadejObjednavku(int cas, int pocetHLitru, int den)
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
		nova.setObjem(pocetHLitru);
		nova.setDen(den);
		nova.soucCas = this.soucCas.clone();
		/*
		 * testovaci vypis
		 */
		//System.out.println(nova.cas+", "+nova.objem+", "+stara.cas+", "+stara.objem);
		
	}
	
	/**
	 * Metoda pro rucni zadani objednavky, metodu bude volat prvek gui
	 * Na zacatku dne uz byla vygenerovana objednavka pro tuto hospodu, proto nahradi novou objednavku
	 * 
	 * @param cas - cas, kdy hospoda poda objednavku
	 * @param pocetHLitru - pocet hektolitru piva, ktere si hospoda objedna
	 */
	public void zmenaObjednavky(int cas, int pocetHLitru)
	{
		
		/*
		 * ulozeni udaju o nove objednavce
		 */
		nova.setCas(cas);
		nova.setObjem(pocetHLitru);
		
		/*
		 * testovaci vypis
		 */
		//System.out.println(nova.cas+", "+nova.objem+", "+stara.cas+", "+stara.objem);
		
	}
	
	
}
