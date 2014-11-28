package graf;

import java.util.LinkedList;

import main.Cas;

public class Kamion extends Auto {

	final int MAX_OBJEM = 100;
	final int RYCHLOST = 90;
	
	
public Kamion(Cas cas, int domov){
		
		super(domov);
		
		this.kDispozici = true;
		this.objem = 0;
		this.dobaVykladani = 0;
		this.dobaNakladaniJednotky = 2;
		this.soucCas = cas;
		this.nalozenoCas = cas;
		this.cesta = new LinkedList<Integer>();
		this.poloha = new float[2];
		this.typ = "Kamion";
	}
	
}
