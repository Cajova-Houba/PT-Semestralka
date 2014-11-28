package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import generator.Generator;
import graf.Auto;
import graf.Hospoda;
import graf.HospodaSud;
import graf.HospodaTank;
import graf.Pivovar;
import graf.Prekladiste;
import graf.Uzel;
import graf.UzelTyp;
import gui.Okno;

public class MainApp {

	//trida ridici simulaci
	private static Simulator sim = new Simulator();
	
	/**
	 * Metoda nacte ze souboru vygenerovanou mapu objeku.
	 * @param jmeno	Jmeno souboru.
	 */
	private static void nactiZeSouboru(String jmeno)
	{
		//nacteni dat ze souboru
		try(BufferedReader br = new BufferedReader(new FileReader(jmeno)))
		{
			String s;
			int id=0;
			UzelTyp typ = UzelTyp.UZEL;
			
			//prvni radek indikuje pocet uzlu
			s = br.readLine();
			int pocet = Integer.parseInt(s);
			Simulator.objekty = new Uzel[pocet];
			
			//nacitani objektu
			String[] tmp;
			while(!(s = br.readLine()).equals("cesty"))
			{
				tmp = s.split(",");
				
				//radek musi obsahovat x,y,id
				if(tmp.length == 3)
				{
					//zapsani do pole uzlu
					id = Integer.parseInt(tmp[2]);
					switch(typ)
					{
					case PIVOVAR: 
						Simulator.objekty[id] = new Pivovar(id, typ, Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]),sim);
						Simulator.pivovar = (Pivovar)Simulator.objekty[id];
						break;
					case HOSPODA_SUD:
						Simulator.objekty[id] = new HospodaSud(id, typ, Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]),sim);
						Simulator.hospodySud.put(id, (HospodaSud)Simulator.objekty[id]);
						break;
					case HOSPODA_TANK:
						Simulator.objekty[id] = new HospodaTank(id, typ, Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]),sim);
						Simulator.hospodyTank.put(id, (HospodaTank)Simulator.objekty[id]);
						break;
					case PREKLADISTE:
						Prekladiste pr = new Prekladiste(id, typ, Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]),sim);
						Simulator.objekty[id] = pr;
						Simulator.prekladiste.put(id, pr);
						
						break;
					default:
						Simulator.objekty[id] = new Uzel(id, typ, Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]),sim);
						break;
					}
				}
				else if (s.equalsIgnoreCase("pivovar")) {typ = UzelTyp.PIVOVAR;}
				else if (s.equalsIgnoreCase("hospoda")) {typ = UzelTyp.HOSPODA_SUD;}
				else if (s.equalsIgnoreCase("hospodat")) {typ = UzelTyp.HOSPODA_TANK;}
				else if (s.equalsIgnoreCase("prekladiste")) {typ = UzelTyp.PREKLADISTE;}
			}
			
			boolean zapsat = true;
			//nacitani cest
			while((s = br.readLine()) != null)
			{
				tmp = s.split(",");
				zapsat = true;
				
				if(tmp.length == 2)
				{
					int idZ = Integer.parseInt(tmp[0]);
					int idDo = Integer.parseInt(tmp[1]);
					
					//zapsani souseda
					
					Simulator.objekty[idZ].sousedi.addLast(idDo);
					Simulator.objekty[idDo].sousedi.addLast(idZ);
			
				}
			}
			
		}
		catch (Exception e)
		{
			System.err.println("Chyba pri cteni souboru "+jmeno);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Metoda zapise retezec do souboru. Mela by byt pouzivana primarne jako log.
	 * @param retezec Retezec k zapsani.
	 */
	public static void zapisDoSouboru(String retezec)
	{
		try
		{
			File file = new File("vystup.txt");
			FileWriter fw = new FileWriter(file,true);
			BufferedWriter bw = new BufferedWriter(fw);
					
			fw.append(retezec);
			fw.append("\r\n");
			bw.close();
			
		}
		catch(Exception e)
		{
			System.err.println("Chyba pri zapisovani do souboru vystup.txt");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Metoda vytvori statistiky hospod a aut a zapise je do souboru.
	 */
	public static void vytvorStatistikuHospod(String fname)
	{
		//celkova spotreba piva
		int celkemSudu = 0;
		int celkemHl = 0;
		
		String nl = "\r\n"; //nova radka
		
		//zapis do souboru hospody-statistika
		try {
			File file = new File(fname);
			FileWriter fw = new FileWriter(file,false);
			BufferedWriter bw = new BufferedWriter(fw);
			
			fw.append("<?xml version=\"1.0\" ?>"+nl); //jedna se o xml soubor
			fw.append("<statistika>"+nl);
			
			fw.append("\t<hospody typ=\"sud\">"+nl); //element klasickych hospod
			for(Hospoda hospoda : Simulator.hospodySud.values()) //vsechny hospody
			{
				fw.append("\t\t<hospoda id=\""+hospoda.id+"\">"+nl);
				//pro kazdou hospodu 7 dni
				for (int i = 0; i < 7; i++) {
					fw.append("\t\t\t"); //odsazeni
					fw.append("<den cislo=\""+i+"\">"+nl);
					fw.append(hospoda.statistikaXML(i, 4));
					fw.append("\t\t\t</den>"+nl);
				}
				celkemSudu += hospoda.celkemPiva;
				fw.append("\t\t</hospoda>"+nl);
			}
			fw.append("\t</hospody>"+nl);
			
			fw.append("\t<hospody typ=\"tank\">"+nl); //element tankovych hospod
			for(Hospoda hospoda : Simulator.hospodyTank.values())
			{
				fw.append("\t\t<hospoda id=\""+hospoda.id+"\">"+nl);
				//pro kazdou hospodu 7 dni
				for (int i = 0; i < 7; i++) {
					fw.append("\t\t\t"); //odsazeni
					fw.append("<den cislo=\""+i+"\">"+nl);
					fw.append(hospoda.statistikaXML(i, 4));
					fw.append("</den>"+nl);
					celkemHl += hospoda.celkemPiva;
				}
				fw.append("\t\t</hospoda>");
			}			
			fw.append("\t</hospody>"+nl); //konec klasickych hospod
			
			//souhrnne informace za celou dobu simulace
			fw.append("\t<souhrn>"+nl);
			fw.append("\t\t<celkemDodano sudu=\""+celkemSudu+"\" hl=\""+celkemHl+"\" />"+nl);
			fw.append("\t\t<celkemVytvoreno nakl=\"\" cist=\"\" kam=\"\" />"+nl);
			fw.append("\t</souhrn>"+nl);
			
			fw.append("</statistika>"+nl); //konec xml 
			
			//uzavreni zapisoveho proudu
			bw.close();
		} catch (Exception e) {
			System.out.println("Chyba pri zapisu statistiky do xml souboru.");
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public static void vytvorStatistikuAut(String fname)
	{
		String nl = "\r\n"; //nova radka
		
		//zapis do souboru hospody-statistika
		try {
			File file = new File(fname);
			FileWriter fw = new FileWriter(file,false);
			BufferedWriter bw = new BufferedWriter(fw);
			
			fw.append("<?xml version=\"1.0\" ?>"+nl); //jedna se o xml soubor
			fw.append("<statistika>"+nl);
			
			fw.append("\t<auta>"+nl);
			for(Auto a : sim.auta) //vsechny hospody
			{
				fw.append(a.statistikaXML(2));
			
			}
			fw.append("\t</auta>"+nl);
			
			//souhrnne informace za celou dobu simulace
			fw.append("\t<souhrn>"+nl);
			fw.append("\t</souhrn>"+nl);
			
			fw.append("</statistika>"+nl); //konec xml 
			
			//uzavreni zapisoveho proudu
			bw.close();
		} catch (Exception e) {
			System.out.println("Chyba pri zapisu statistiky do xml souboru.");
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	private static void smazSouborVypis()
	{
		try
		{
			File file = new File("vystup.txt");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
					
			bw.write("");
			bw.close();
			
		}
		catch(Exception e)
		{
			System.err.println("Chyba pri zapisovani do souboru vystup.txt");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		//vygenerovani
		System.out.print("Generuji mapu.. ");
		//Generator.generuj();
		System.out.print("Hotovo\n");
		
		//nacteni ze souboru
		System.out.print("Nacitam vygenerovanou mapu.. ");
		nactiZeSouboru("gen.txt");
		System.out.print("Hotovo\n");
		
		//vymazani souboru pro vypis
		smazSouborVypis();
		
		//zobrazeni
		Okno okno = new Okno(sim);
		sim.addObserver(okno);
		
		//start simulace
		sim.simuluj();
	}

}
