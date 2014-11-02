package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import generator.Generator;
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
						sim.addObserver(pr);
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
		Generator.generuj();
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
