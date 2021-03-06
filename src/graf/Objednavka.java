package graf;

import main.Cas;


public class Objednavka implements Comparable<Objednavka>{


	/**
	 * Mnozstvi objednaneho piva (1,6)
	 */
	int objem = 0;
	
	/**
	 * Cas zadani objednavky (8-16)
	 */
	int cas = 0;
	
	/**
	 * Den podani objednavky (0-6)
	 */
	int den = 0;
	
	/**
	 * id hospody, ktere patri tato objednavka
	 */
	int id = 0;
	
	Cas soucCas;
	
	public Objednavka(int id, int cas, int objem, int den){
		
		this.id = id;
		this.objem = objem;
		this.cas = cas;
		this.den = den;
		this.soucCas = new Cas();
	}


	public void setObjem(int objem) {
		this.objem = objem;
	}


	public void setCas(int cas) {
		this.cas = cas;
	}


	public int getObjem() {
		return objem;
	}


	public int getCas() {
		return cas;
	}


	@Override
	public String toString() {
		return "Objednavka: [den=" + den + ", cas=" + cas + ", objem=" + objem +  ", id=" + id
				+ "]";
	}


	@Override
	public int compareTo(Objednavka arg0) {
		
		/*
		 * nejprve se radi podle dne
		 */
		if(arg0.den != this.den){
			return  this.den  - arg0.den;
		}
		
		
		/*
		 * pak se radi podle casu
		 */
		if(arg0.cas != this.cas){
			return  this.cas - arg0.cas;
		}
		
		/*
		 * kdyz je cas stejny tak se radi podle objemu
		 */
		if(arg0.objem != this.objem){
			return  arg0.objem - this.objem;
		}
		
		/*
		 * jinak maji stejnou prioritu
		 */
		return 0;
	}


	public int getDen() {
		return den;
	}
	

	public void setDen(int den) {
		this.den = den;
	}



}
