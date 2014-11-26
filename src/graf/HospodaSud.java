package graf;

import main.Simulator;

public class HospodaSud extends Hospoda{
	
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
	
	public void setIdPrekladiste(int idPrekladiste) {
		this.idPrekladiste = idPrekladiste;
	}
	
}
