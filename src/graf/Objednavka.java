package graf;


public class Objednavka implements Comparable<Objednavka>{


	/**
	 * udaj o objemu objednavky (1,6)
	 */
	int objem = 0;
	
	/**
	 * udaj o case objednavky (8-16)
	 */
	int cas = 0;
	
	/**
	 * id hospody, ktere patri tato objednavka
	 */
	int id = 0;
	
	
	
	public Objednavka(int id, int cas, int objem){
		
		this.id = id;
		this.objem = objem;
		this.cas = cas;
		
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
		return "Objednavka: [cas=" + cas + ", objem= " + objem +  ", id=" + id
				+ "]";
	}


	@Override
	public int compareTo(Objednavka arg0) {
		/*
		 * nejprve se radi podle casu
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



}
