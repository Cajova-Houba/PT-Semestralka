package main;

/**
 * Prepravka ktera symbolizuje realny cas v simulatoru.
 * 
 * @author Zděnek Valeš
 *
 */
public class Cas {

	public int den;
	public int hodina;
	public int minuta;
	
	/**
	 * Bezparametrický konstruktor. Nastaví čas na 0 0:0
	 */
	public Cas()
	{
		this(0,0,0);
	}
	
	/**
	 * Parametricky konstruktor. Nastavi pozadovany cas. Pokud bude zadana hodina(minuta) >23 (>59), automaticky se
	 * ulozi 23 (59). Ze zadaneho casu se bere absolutni hodnota.
	 * 
	 * @param d  Den.
	 * @param h	 Hodina.
	 * @param m  Minuta.
	 */
	public Cas(int d, int h, int m)
	{
		this.den = d;
		this.hodina = Math.min(Math.abs(h), 23);
		this.minuta = Math.min(Math.abs(m), 59);
	}
	
	/**
	 * Posune cas o dany pocet minut.
	 * @param min
	 */
	public void incCas(int min)
	{
		for(int i=0; i<min; i++)
		{
			incCas();
		}
	}
	
	/**
	 * Metoda posune cas o 1 minutu.
	 */
	public void incCas()
	{
		this.minuta++;
		if(this.minuta > 59)
		{
			this.minuta = 0;
			this.hodina++;
			if(this.hodina > 23)
			{
				this.hodina = 0;
				this.den++;
			}
		}
	}
	
	/**
	 * Metoda vrati kopii objektu se stejnymi hodnotami.
	 */
	public Cas clone()
	{
		return new Cas(this.den,this.hodina,this.minuta);
	}
	
	@Override
	/**
	 * Metoda zjisti zda jsou dv casy rovne.
	 * 
	 * @param obj Cas.
	 */
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (!(obj instanceof Cas))
		{
			return false;
		}
		Cas objC = (Cas)obj;
		
		if(this.den == objC.den &&
		   this.hodina == objC.hodina &&
		   this.minuta == objC.minuta)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "den "+den+". "+hodina+":"+minuta;
	}
}
