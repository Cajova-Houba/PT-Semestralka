package graf;

import main.Simulator;

public class HospodaTank extends Hospoda{
	
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
		
		nova = new Objednavka(id,0,0,Simulator.getCas().den);
		stara = new Objednavka(id,0,0,Simulator.getCas().den);

	}
	
}
