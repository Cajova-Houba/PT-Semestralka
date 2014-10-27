package graf;

import java.awt.Color;

/**
 * Výčtový typ sloužící k rozlišení typu uzlu.
 * 
 * @author Zděnek Valeš
 *
 */
public enum UzelTyp {
	
	UZEL(Color.black),
	PIVOVAR(Color.green),
	PREKLADISTE(Color.red),
	HOSPODA_SUD(Color.blue),
	HOSPODA_TANK(Color.cyan);
	
	private Color barva;
	
	private UzelTyp(Color barva)
	{
		this.barva = barva;
	}
	
	/**
	 * Varti barvu pro kazdy typ uzlu.
	 * @return Barva uzlu.
	 */
	public Color getBarva()
	{
		return this.barva;
	}

}
