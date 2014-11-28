package graf;

import graf.Auto.statZaznam;

import java.util.LinkedList;
import java.util.Observable;

import main.Cas;
import main.Simulator;

public class Cisterna extends Auto{

	final int RYCHLOST = 60;
	
	//minuta posledniho updatu nakladaku
	int minuta;
	
	public Cisterna(Cas cas, int domov){
		
		super(domov);
		
		this.MAX_OBJEM = 50;
		this.kDispozici = true;
		this.objem = 0;
		this.objednavky = new LinkedList<Objednavka>();
		this.dobaVykladani = 0;
		this.dobaNakladaniJednotky = 2;
		this.soucCas = cas;
		this.nalozenoCas = cas;
		this.cesta = new LinkedList<Integer>();
		this.poloha = new float[2];
		this.typ = "Cisterna";
	}
	
	
	/**
	 * Metoda aktualizuje soucasny cas a zaroven popojede s autem, pokud ma auto jet.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		super.update(arg0, arg1);
		
		if(minuta != super.soucCas.minuta){
			minuta = super.soucCas.minuta;
			this.dobaVykladani--;
		}
		
		if(this.muzeVyjet() == true){
			this.jede = true;
		}
		else{
			this.jede = false;
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
			int chyba = 2;
			if((Math.abs(this.poloha[0] - cilPoz[0]) < chyba) && 
					(Math.abs(this.poloha[1] - cilPoz[1]) < chyba))
			{
				//System.out.println("Auto "+this.id+" dojelo do cile: "+this.cesta.getFirst() + " v case: " + super.soucCas);
				this.poloha[0] = cilPoz[0];
				this.poloha[1] = cilPoz[1];
				
				//pokud auto dojelo do sveho domovskeho prekladiste a v ceste je pouze jedinny uzel
				//vratilo se auto z rozvazeni a muze byt znovu pouzito
				if((this.DOMOV == this.cesta.getFirst()) &&
					(this.cesta.size() == 1))
				{
					System.out.println("Auto "+this.id+" (cisterna) se vratilo z okruzni jizdy v case: " + Simulator.getCas().toString());
					this.jede = false;
					this.kDispozici = true;
					//zapsani konecneho bodu - prekladiste
					if(statCesta != null)
					{
						statCesta.add(new statZaznam(this.DOMOV, -1, 0));
					}
					
					//zapsani statisticke cesty do statistiky
					super.vlozStatCestu(statCesta);
				}
				
				
				//nakladak zkusi vylozit pokud je v cili
				this.dobaVykladani = super.vyloz();
				this.cesta.removeFirst();
				
				
			}
		}
	}
	
}
