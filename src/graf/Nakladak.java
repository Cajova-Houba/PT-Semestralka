package graf;

import java.util.LinkedList;
import java.util.Observable;

import main.Cas;
import main.Simulator;

public class Nakladak extends Auto{

	final int MAX_OBJEM = 30;
	final int RYCHLOST = 70;
	//seznam zpracovavanych objednavek
	LinkedList<Objednavka> objednavky;
	
	
	public Nakladak(Cas cas, int domov){
		
		super(domov);
		
		this.kDispozici = true;
		this.objem = 0;
		this.objednavky = new LinkedList<Objednavka>();
		this.dobaNakladani = 0;
		this.soucCas = cas;
		this.nalozenoCas = cas;
		this.cesta = new LinkedList<Integer>();
		this.poloha = new float[2];
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
			this.objednavky.add(obj);
			//pridani casu 5*pocet_sudu
			nalozenoCas.incCas(5*obj.objem);
			
			return true;
		}
		else
		{
			return false;
		}
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
	 * Metoda nalozi na nakladak pozadovane mnozstvi piva. Vraci false pokud se nepodarilo nalozit
	 * (nakladak byl plny), true pokud se naklad podaril.
	 * 
	 * @param mnozstvi Mnozstvi piva (sudy).
	 * @return True pokud bylo nalozeno, false pokud ne.
	 */
	private boolean naloz(int mnozstvi)
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
	 * @return
	 */
    private void vyloz()
    {
    	float chyba = 0.01f;
    	Objednavka vyrad = null;
    	for(Objednavka o : this.objednavky)
    	{
    		int[] tmp = Simulator.getPoloha(o.id);
    		if(Math.abs(tmp[0] - poloha[0]) < chyba &&
    		   Math.abs(tmp[0] - poloha[0]) < chyba)
    		   {
    				System.out.println("Vykladam objednavku: "+o.toString());
    				objem -= o.objem;
    				Simulator.objekty[o.id].sklad += o.objem;
    			    vyrad = o;
    			    break;
    		   }
    	}
    	if(vyrad != null)
    	{
    		this.objednavky.remove(vyrad);
    	}
    }
	
	/**
	 * Metoda aktualizuje soucasny cas a zaroven popojede s autem, pokud ma auto jet.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		super.update(arg0, arg1);
		
		if(this.muzeVyjet()==true){
			this.jede = true;
		}
		
		//auto jede a Simulator vysila cas
		if(this.jede && arg1 instanceof Cas)
		{
			//pokud neni cesta, neni kam jet
			if (this.cesta.isEmpty())
			{
				return;
			}
			
			//vypocet rozdilu mezi body   
			int[] cilPoz = Simulator.objekty[cesta.getFirst()].poloha;
			float dX = cilPoz[0] - poloha[0];
			float dY = cilPoz[1] - poloha[1];
			
			//normalizace dx,dy
			double prep = Math.sqrt(dX*dX + dY*dY);
			dX = (float) (dX / prep);
			dY = (float) (dY / prep);
			
			//pohyb vozidla - rychlosti km/m

			this.poloha[0] += dX*RYCHLOST/60;
			this.poloha[1] += dY*RYCHLOST/60;
			
			//test jestli uz neni v cili
			int chyba = 5;
			if((Math.abs(this.poloha[0] - cilPoz[0]) < chyba) && 
					(Math.abs(this.poloha[1] - cilPoz[1]) < chyba))
			{
				System.out.println("Auto "+this.id+" dojelo do cile: "+this.cesta.getFirst() + " v case: " + super.soucCas);
				this.poloha[0] = cilPoz[0];
				this.poloha[1] = cilPoz[1];
				
				//pokud auto dojelo do sveho domovskeho prekladiste a v ceste je pouze jedinny uzel
				//vratilo se auto z rozvazeni a muze byt znovu pouzito
				if((this.DOMOV == this.cesta.getFirst()) &&
					(this.cesta.size() == 1))
				{
					System.out.println("Auto "+this.id+" se vratilo z okruzni jizdy.");
					this.jede = false;
					this.kDispozici = true;
				}
				
				this.cesta.removeFirst();
				//nakladak zkusi vylozit pokud je v cili
				this.vyloz();
				
				
			}
		}
	}
}
