package graf;

import java.awt.Color;

/**
 * Výčtový typ sloužící k rozlišení typu uzlu.
 * 
 * @author Zděnek Valeš
 *
 */
public enum UzelTyp {
	
	UZEL(Color.black,"Uzel"),
	PIVOVAR(Color.green,"Pivovar"),
	PREKLADISTE(Color.red,"Prekladiste"),
	HOSPODA_SUD(Color.blue,"Hospoda-sud"),
	HOSPODA_TANK(Color.cyan,"Hospoda-tank");
	
	private Color barva;
	private String dsc;
	
	private UzelTyp(Color barva, String dsc)
	{
		this.barva = barva;
		this.dsc = dsc;
	}
	
	/**
	 * Varti barvu pro kazdy typ uzlu.
	 * @return Barva uzlu.
	 */
	public Color getBarva()
	{
		return this.barva;
	}

	public String toString()
	{
		return this.dsc;
	}
}
