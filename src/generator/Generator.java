package generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Random;

/**
 * Trida generuje pozice hospod, prekladist, pivovaru a cesty mezi nimi. 
 * Pozice jsou generovány do mapy o rozměrech 500km x 500km. 
 * Vygenerovane pozice jsou ulozeny do textoveho souboru. 
 * 
 * @author Zděnek Valeš
 *
 */
public class Generator {
	
	/**
	 * Pocet generovanych hospod.
	 */
	private static final int POCET_HOSPOD = 4000;
	
	/**
	 * Procento z celkoveho poctu hospod ktere bude cepovat z tanku.
	 */
	private static final float PROCENT_HOSPOD_TANK = 0.05f;
	
	/**
	 * Pocet generovanych prekladist.
	 */
	private static final int POCET_PREKLADIST = 8;
	
	/**
	 * Pocet generovanych pivovaru.
	 */
	private static final int POCET_PIVOVARU = 1;
	
	/**
	 * Sirka mapy v kilometrech.
	 */
	private static final int SIRKA_MAPY = 500;
	
	/**
	 * Vyska mapy v kilometrech.
	 */
	private static final int VYSKA_MAPY = 500;
	
	/**
	 * ID pro vsechny vygenerovane hospody. S kazdym vygenerovanym objektem se zvysi o 1.
	 * Pivovar ma ID 0.
	 */
	private static int id;
	
	/**
	 * Vygenerovana pozice pivovaru. Pole je ve formatu [0]=x [1]=y [2]=id
	 */
	private static int [] pivovar = new int[3];	

	/**
	 * Vygenerovane pozice prakladist. Pole je ve formatu [0]=x [1]=y [2]=id. 
	 */
	private static int [][] prekladiste = new int [POCET_PREKLADIST][0];

	/**
	 * Vygenerovane pozice hospod v zonach 0..8. Pole je ve formatu [0]=x [1]=y [2]=id.
	 */
	private static LinkedList<int[]>[] hospody = new LinkedList[9];
	
	/**
	 * Seznam cest. Cesty jsou ve tvaru id1,id2. Cesty jsou obousmerne.
	 */
	private static LinkedList<int[]> cesty = new LinkedList<int []>();
	
	
	//METODY
	
	/**
	 * Metoda inicializuje mapu pozic. Mapa pozic je dvourozmerne pole hodnot true a false, ktere indikuje obsazeni pozice.
	 * Pokud je na pozici true, pozice je volna, pokud false, pozice je obsazena.
	 * 
	 * @return Mapa pozic se vsemi pozicemi volnymi.
	 */
	private static boolean[][] inicializujMapu()
	{
		boolean[][] pozice = new boolean[SIRKA_MAPY][VYSKA_MAPY];
		
		for(int i=0; i<SIRKA_MAPY;i++)
		{
			for(int j=0;j<VYSKA_MAPY;j++)
				pozice[i][j] = true;
		}
		
		return pozice;
	}
	
	
	/**
	 * Metoda vygeneruje pozici pivovaru, obsadi ji v dodane mape pozic a tuto upravenou mapu pozic vrati.
	 * 
	 * @param pozice Mapa pozic bez pivovaru.
	 * @return Mapa pozic s pivovarem.
	 */
	private static boolean[][] generujPivovar(boolean[][] pozice)
	{
		if(pozice == null) {pozice = inicializujMapu();}
		
		Random r = new Random();
		//vygenerovani pivovaru
		//pivovar ma lezet v centralni oblasti = > ve ctverci 50*50 se stredem v 249,249
		int x = r.nextInt(51) + 224;
		int y = r.nextInt(51) + 224;
		pivovar = new int[] {x,y,id++};
		pozice[x][y] = false;
		
		return pozice;
	}
	
	
	/**
	 * Metoda vygeneruje pozice hospod a zaradi je do seznamu pro jednotlive oblasti. Vraci upravenou mapu pozic predanou v parametru.
	 * 
	 * @param pozice Mapa pozic bez hospod.
	 * @return	Mapa pozic s hospodami.
	 */
	private static boolean[][] generujHospody(boolean[][] pozice)
	{
		if(pozice == null) {pozice = inicializujMapu();}
		
		Random r = new Random();
		int x=0, y=0;	//pozice generovanych hospod
		boolean spravne = false;	//spravnost vygenerovane pozice hospody 
		
		//generovani hospod
		for(int i=0;i<POCET_HOSPOD;i++)
		{
			while (!spravne)
			{
				spravne = true;
				x = r.nextInt(SIRKA_MAPY);
				y = r.nextInt(VYSKA_MAPY);
				if (!pozice[x][y])  //hospoda nesmi lezet na miste kde uz neco je
				{continue;}
				
				/* ===================================
				 * STARY, NEPOUZIVAT, JENOM DO 4 SMERU
				 *            FUNGUJE
				 * ===================================
				//kontrola jestli hospoda neni prilis blizko jine
				//doprava
				if((x+2) < SIRKA_MAPY)     //podminka proti prekroceni rozmeru pole
				{
					spravne = spravne & pozice[x+1][y] & pozice[x+2][y];    //pokud v poradku
				}															  //spravne=true & true & true
				else if((x+1) < VYSKA_MAPY)
				{
					spravne = spravne & pozice[x+1][y];
				}
				
				//doleva
				if((x-2) > 0)
				{
					spravne = spravne & pozice[x-1][y] & pozice[x-2][y];
				}
				else if((x-1) > 0)
				{
					spravne = spravne & pozice[x-1][y];
				}
				
				//nahoru
				if((y-2) > 0 )
				{
					spravne = spravne & pozice[x][y-1] & pozice[x][y-2];
				}
				else if((y-1) > 0)
				{
					spravne = spravne & pozice[x][y-1];
				}
				
				//dolu
				if((y+2) < 500)
				{
					spravne = spravne & pozice[x][y+1] & pozice[x][y+2];
				}
				else if((y+1) < 500)
				{
					spravne = spravne & pozice[x][y+1];
				}*/
				
				//nahoru doleva
				//kontrola spravnosti, nutno projit pole 4*4 se stredem v x,y
				//pokud bude nektera z techto pozic obsazena, hospoda se musi generovat jinde
				for (int k=-3;k<3;k++)
				{
					for (int j=-3;j<3;j++)
					{
						if(((x+k) > 499) || ((x+k) < 0)) {continue;}
						else if (((y+j) > 499) || (y+j) < 0) {continue;}
						else if (k == 0 || j == 0) {continue;}
						
						spravne = spravne & pozice[x+k][y+j];
					}
				}
			}
			pozice[x][y] = false; //objekt se vygeneroval v poradku, pozice se oznaci jako obsazena
			//zarazeni hospody do spravne oblasti:
			int index = getOblast(x,y);
			if(hospody[index] == null) //pripadna inicializace spojoveho seznamu
			{
				hospody[index] = new LinkedList<int []>();
			}
			hospody[index].add(new int[] {x,y,id});
			id++;
			
			spravne = false;
		}
		return pozice;
	}
	
	
	/**
	 * Metoda vygeneruje prekladiste a zaradi je do mapy pozic predane v parametru. Kazde prekladiste bude lezet v pribliznem
	 * stredu hospod pro danou oblast. To znamena, ze jeho pozice je prumer pozic hospod v dane oblasti. 
	 * Metoda vraci upravenou mapu pozic.
	 * @param pozice	Mapa pozic bez prekladist.
	 * @return	Mapa pozic s prekladisti.
	 */
	private static boolean[][] generujPrekladiste(boolean[][] pozice)
	{
		if(pozice == null) {pozice = inicializujMapu();}
		
		Random r = new Random();
		
		//generovani prekladist
		//prekladiste bude priblizne v prostredni casti mezi hospodami v dane oblasti
		//pozice prekladiste je vypocitana jako prumer pozic hospod
		//pokud se na teto pozici neco nachazi, bude prekladiste nahodne umisteno do oblasti 20*20 kolem toho bodu
		//pokud je prekladiste pirlis blizko jinemu, bude posunuto
		int index = 0; //index v poli prekladist
		for (int i = 0; i<hospody.length;i++)
		{
			if (i == 4) {continue;} //v teto oblasti bude pouze pivovar
			
			//vypocet prumeru
			int xPrumer = 0;
			int yPrumer = 0;
			for (int[] p : hospody[i])
			{
				xPrumer += p[0];
				yPrumer += p[1];
			}
			xPrumer /= hospody[i].size();
			yPrumer /= hospody[i].size();
			
			//kontrola pozice
			if(!pozice[xPrumer][yPrumer])
			{
				boolean spravne = false; //spravnost pozice prekladiste
				
				while(!spravne)
				{
					xPrumer = r.nextInt(41) + xPrumer - 20;
					yPrumer = r.nextInt(41) + yPrumer - 20;
					
					if((xPrumer < 0) || (xPrumer > 499)) {continue;}
					if((yPrumer < 0) || (yPrumer > 499)) {continue;}
					spravne = pozice[xPrumer][yPrumer];
				}
			}
			
			//kontrola jestli neni moc blizko jineho prekladiste
			/*for(int j = 0; j< POCET_PREKLADIST; j++)
			{
				if()
			}*/
			
			prekladiste[index++] = new int[] {xPrumer,yPrumer,id++};
			pozice[xPrumer][yPrumer] = false;
		}
		
		return pozice;
	}
	
	
	private static void generujCesty() 
	{	
		//cesty mezi prekladisti a pivovarem
		for(int [] pr : prekladiste)
		{
			cesty.add(new int[] {0,pr[2]});
		}
		
		//cesty mezi prekladisti
		for(int i=0;i<prekladiste.length-1;i++)
		{
			for(int j=i+1;j<prekladiste.length;j++)
			{
				cesty.add(new int[] {prekladiste[i][2],prekladiste[j][2]});
			}
		}
		
		//cesty mezi prekladisti a hospodami
		//z kazdeho prekladiste ma vest prima cesta do 50 hospod
		//techto 50 hospod bude lezet ve stejne oblasti jako prekladiste
		//docasny seznam hospod pro oblast
		//Pokud bude hospoda vybrana, bude ze seznamu vyrazena => zadne duplicity
		LinkedList<int[]> tmpList;   
		Random r = new Random();
		int index;
		
		//cesta z pivovaru do hospod
		//pivovar je v oblasti 4
		//docasny seznam
		tmpList = (LinkedList<int[]>)hospody[4].clone();
		for(int i=0;i<50;i++)
		{
			index = r.nextInt(tmpList.size());
			cesty.add(new int[] {0,tmpList.get(index)[2]});
			tmpList.remove(index);
		}
		
		//cesty z prekladist
		for(int i=0;i<POCET_PREKLADIST;i++)
		{
			if(i<4)
			{
				tmpList = (LinkedList<int[]>)hospody[i].clone();				
			}
			else
			{
				tmpList = (LinkedList<int[]>)hospody[i+1].clone();
			}
			
			for(int j=0;j<50;j++)
			{
				index = r.nextInt(tmpList.size());
				cesty.add(new int[] {prekladiste[i][2],tmpList.get(index)[2]});
				tmpList.remove(index);
			}
		}
		
		//kazda hospoda bude sousedit s 15 nejblizsimi
		//hospody sou ulozene v seznamench po oblastech		
		//docasny seznam pro hospody
		LinkedList<int []> tmpHospody = new LinkedList<int []>();
		for(int i=0;i<POCET_PREKLADIST+1;i++)
		{
			tmpHospody.addAll(hospody[i]);
		}
		
		//prochazeni kazde hospody s kazdou
		for(int[] hosp : tmpHospody)
		{
			int x1=hosp[0];  //poloha hospody se kterou bude porovnavano
			int y1=hosp[1]; 
			
			LinkedList<Integer> sousedi = new LinkedList<Integer>();  //nalezene nejblizsi hospody 
			for(int i=0;i<15;i++)  //15 nejblizsich sousedu
			{
				float minVzd = Float.MAX_VALUE;   //slouzi k hledani minima
				int minIndex = hosp[2]; 
				
				for(int[] sous : tmpHospody)
				{
					//porovnani hospody samo se sebou
					if(hosp[2] == sous[2]) {continue;}
				
					//porovnani s jiz existujicimi sousedy
					boolean existuje = false; //false pokud spolu hospody jeste nesousedi
					for(Integer h : sousedi) 
					{
						if(h == sous[2])
						{ 
							existuje = true;
							break;
						}
					}
					if(existuje) {continue;}
						
					float vzdalenost = getVzdalenost(x1, y1, sous[0], sous[1]);
					if(vzdalenost < minVzd)  
					{
						minIndex = sous[2];
						minVzd = vzdalenost;
					}
				}
				cesty.add(new int[] {hosp[2],minIndex}); //pridani zaznamu o ceste
				sousedi.add(minIndex);
			}
			
		}
		
	}
	
	/**
	 * Zapise vygenerovane pozice objektu do textoveho souboru. 
	 * Format:
	 * <nazev objektu>
	 * <seznam pozic, na kazdem radku jedna pozice a id uzlu ve tvaru x,y,id>
	 * 
	 * @param jmeno Jmeno txt souboru.
	 */
	private static void zapisDoSouboru(String jmeno)
	{
		//zapis do souboru
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(jmeno)))
		{
			//prvni radek indikuje celkovy pocet uzlu
			bw.write(POCET_PIVOVARU+POCET_PREKLADIST+POCET_HOSPOD+"\r\n");
			
			//zapis pivovar
			bw.write("pivovar\r\n");
			bw.write(pivovar[0]+","+pivovar[1]+","+pivovar[2]+"\r\n");
			int pocetHospodTank = (int)((POCET_HOSPOD / 9) * PROCENT_HOSPOD_TANK);  //pocet hospod s tankem v jedne olasti
			
			//zapis hospod
			for(int i=0; i< hospody.length; i++)
			{
				int mez = hospody[i].size() - pocetHospodTank;
				bw.write("hospoda\r\n");  //obycejne hospody v jedne oblasti
				for(int j=0; j< mez; j++)
				{
					bw.write(hospody[i].get(j)[0]+","+hospody[i].get(j)[1]+","+hospody[i].get(j)[2]+"\r\n");
				}
				
				bw.write("hospodat\r\n");  //hospody s tanekm
				for(int j=mez; j< hospody[i].size(); j++)
				{
					bw.write(hospody[i].get(j)[0]+","+hospody[i].get(j)[1]+","+hospody[i].get(j)[2]+"\r\n");
				}
			}
			
			//zapis prekladist
			bw.write("prekladiste\r\n");
			for(int i=0; i<POCET_PREKLADIST;i++)
			{
				bw.write(prekladiste[i][0]+","+prekladiste[i][1]+","+prekladiste[i][2]+"\r\n");
			}
			
			//zapis cest
			bw.write("cesty\r\n");
			for(int[] cesta : cesty)
			{
				bw.write(cesta[0]+","+cesta[1]+"\r\n");
			}
			
			bw.flush();
					
		}
		catch(Exception e)
		{
			System.err.println("Chyba pri zapisovani do souboru gen.txt");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Metoda vygeneruje pozice hospod, prekladist, pivovaru a cest mezi nimi. Pozice a cesty pak ulozi do
	 * textoveho souboru.
	 */
	public static void generuj()
	{
		//inicializace mapy pozic
		boolean[][] pozice = inicializujMapu();
		
		//inicializace id
		id = 0;
		
		//generovani pivovar
		pozice = generujPivovar(pozice);
		
		//generovani hospod
		pozice = generujHospody(pozice);
		
		//generovani prekladist
		pozice = generujPrekladiste(pozice);
		
		//generovani cest
		generujCesty();
		
		//zapis do soubrou
		zapisDoSouboru("gen.txt");
	}
	
	
	/**
	 * Metoda vrati cislo oblasti od 0 do 9 podle zadane pozice.
	 * 
	 * @param x	X-ova pozice.
	 * @param y	Y-ova pozice.
	 * @return	Cislo oblasti.
	 */
	private static int getOblast(int x, int y)
	{
		//0: (0,0,167,167)
		//1: (168,0,335,167)
		//2: (336,0,499,167)
		//3: (0,168,167,335)
		//4: (168,168,335,335) ..v teto oblasti bude pivovar
		//5: (336,168,499,335)
		//6: (0,336,167,499)
		//7: (168,336,335,499)
		//8: (336,336,499,499)
		if((x >= 0) && (x <= 167) && (y >= 0) && (y <= 167)) {return 0;}
		if((x >= 168) && (y >= 0) && (x <= 335) & (y <= 167)) {return 1;}
		if((x >= 336) && (y >= 0) && (x <= 499) & (y <= 167)) {return 2;}
		if((x >= 0) && (y >= 168) && (x <= 167) & (y <= 335)) {return 3;}
		if((x >= 168) && (y >= 168) && (x <= 335) & (y <= 335)) {return 4;}
		if((x >= 336) && (y >= 168) && (x <= 499) & (y <= 335)) {return 5;}
		if((x >= 0) && (y >= 336) && (x <= 167) & (y <= 499)) {return 6;}
		if((x >= 336) && (y >= 336) && (x <= 499) & (y <= 499)) {return 7;}
		else {return 8;}
	}

	/**
	 * Metoda vrati vzdalenost mezi dvema uzly. Uzly jsou dany polohou.
	 * 
	 * @param x1 X-ova slozka prvniho uzlu.
	 * @param y1 Y-ova slozka prvniho uzlu.
	 * @param x2 X-ova slozka druheho uzlu.
	 * @param y2 Y-ova slozka druheho uzlu.
	 * @return Vzdalenost uzlu.
	 */
	private static float getVzdalenost(int x1, int y1, int x2, int y2)
	{
		int dx = Math.abs(x1-x2);
		int dy = Math.abs(y1-y2);
		return (float)Math.sqrt(dx*dx+dy*dy);
	}
}
