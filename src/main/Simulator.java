package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.Timer;

import graf.HospodaSud;
import graf.HospodaTank;
import graf.Pivovar;
import graf.Prekladiste;
import graf.Uzel;


public class Simulator extends Observable{

	/**
	 * Pole uzlu, ktere budou na mape. Index uzlu v poli odpovida jeho id.
	 */
	public static Uzel[] objekty;
	
	/**
	 * Nas oblibeny pivovar Chmelokvas
	 */
	public static Pivovar pivovar;

	/**
	 * mapa prekladist zasobujicich nase hospodySud, key odpovida id kazdeho prekladiste
	 */
	public static Map<Integer, Prekladiste> prekladiste = new TreeMap<Integer, Prekladiste>();
	
	/**
	 * mapa hospod zavislych na prisunu sudu z prekladist, key odpovida id kazde hospody
	 */
	public static Map<Integer, HospodaSud> hospodySud = new TreeMap<Integer, HospodaSud>();
	
	/**
	 * mapa hospod zasobovanych primo z Chmelokvasu, key odpovida id kazde hospody
	 */
	public static Map<Integer, HospodaTank> hospodyTank = new TreeMap<Integer, HospodaTank>();
	
	/**
	 * matice vzdalenosti sousedu - delky cest
	 */
	public Float[][] delkyCest; 
	
	/**
	 * udavaji hodinu, od ktere zacina kazdy pracovni den a hodinu konce smeny
	 */
	public final int ZACATEK_DNE = 8;
	public final int KONEC_DNE = 16;
	
	
	private static Cas soucCas;
	
	
	/**
	 * Timer ridici simulaci casu.
	 */
	private Timer timerCas;
	
	//konstruktor vola zacatek simulace
	public Simulator(){
		
		soucCas = new Cas();
		timerCas = new Timer(10, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(soucCas.den == 1)
				{
					timerCas.stop();
					return;
				}
				
				/*
				 * Zacatek noveho dne. 
				 */
				if (soucCas.hodina == 0 && soucCas.minuta == 0)
				{
					simulujDen();
				}
				
				/*
				 * Zacatek nove hodiny.
				 */
				if(soucCas.minuta == 0)
				{
					simulujHodinu(soucCas.hodina);
				}
					
				/*
				 * Konec dne.
				 */
				if(soucCas.hodina == 23 && soucCas.minuta == 59)
				{
					statistikaDne();
				}
				
				noteCas();
				notePrekresli();
				soucCas.incCas();
			}
		});
	}
	
	
	
	/**
	 * Metoda provadejici simulaci. Simulace zacina v den 0 a hodinu 0;
	 */
	public void simuluj(){
		
		pivovar.hospodyTank.putAll(hospodyTank);
		//spoctiVzdalenosti();
		rozdelHospody();
		startSimulace();
	}

	//pro potreby GUI - start, restart, pauza..
	public void startSimulace()
	{
		soucCas = new Cas();
		timerCas.start();
		
		noteCas();
	}
	
	
	
	/**
	 * Metoda provadejici simulaci jednoho dne.
	 */
	public void simulujDen(){
		
		addLog("Simuluji den: "+soucCas.den);
		generujObjednavky();
		
		/*
		 * Presunuto do metody statistikaDne()
		 * 
		if(getCas().den != 0){
			
			addLog("Pivovar: ma "+ pivovar.vozy.size() +" vozu");
			MainApp.zapisDoSouboru("Pivovar: ma "+ pivovar.vozy.size() +" vozu");
			addLog("         zbylo v nem "+ pivovar.objednavky.size() +" objednavek");
			MainApp.zapisDoSouboru("         zbylo v nem "+ pivovar.objednavky.size() +" objednavek");
			addLog("         objednavka s nejvyssi prioritou je z " + pivovar.objednavky.peek().getDen() + ". dne " + pivovar.objednavky.peek().getCas() + ". hodiny");
			MainApp.zapisDoSouboru("         objednavka s nejvyssi prioritou je z " + pivovar.objednavky.peek().getDen() + ". dne " + pivovar.objednavky.peek().getCas() + ". hodiny");
			
			for(Prekladiste p : prekladiste.values()){
				addLog("Prekladiste "+ (p.id-4001) +": ma "+ p.vozy.size() +" vozu");
				MainApp.zapisDoSouboru("Prekladiste "+ (p.id-4001) +": ma "+ p.vozy.size() +" vozu");
				addLog("               zbylo v nem "+ p.objednavky.size() +" objednavek");
				MainApp.zapisDoSouboru("               zbylo v nem "+ p.objednavky.size() +" objednavek");
				addLog("               objednavka s nejvyssi prioritou je z " + p.objednavky.peek().getDen() + ". dne " + p.objednavky.peek().getCas() + ". hodiny");
				MainApp.zapisDoSouboru("               objednavka s nejvyssi prioritou je z " + p.objednavky.peek().getDen() + ". dne " + p.objednavky.peek().getCas() + ". hodiny");
			}
			
		}*/
		
		//pivovar.testAuta();

		/*
		 * test pro zjisteni poctu prijatych objednavek u jednoho prekladiste
		 * jestli sedi k poctu registrovanych hospod k danemu prekladisti
		 */
		/*
		int n = 0;
		while(prekladiste.get(4000 + 1).objednavky.isEmpty()==false){

			n++;
			System.out.println(n + ":  " + prekladiste.get(4000 + 1).objednavky.poll().toString());
			
		}
		*/
			
	}
	
	
	/**
	 * Metoda provadejici simulaci jedne hodiny
	 * @param hodina - aktualni hodina
	 */
	public void simulujHodinu(int hodina){
		
		/*
		 * Projde hospody a zjisti, ktera si objednava tuto hodinu
		 * Objednavky zaradi do fronty prislusneho prekladiste
		 */
		addLog("Simuluji hodinu "+hodina);
		prijmiObjednavky(hodina);
		
		while((pivovar.objednavky.size() >= 1) && ((hodina <= 14) && (hodina >= 8))){
			pivovar.zpracujObjednavky();
		}
		
		for(Prekladiste p : prekladiste.values()){
		
			while((p.objednavky.size() >= 1) && ((hodina <= 14) && (hodina >= 8))){
				
				p.zpracujObjednavky();
				
			}
			
		}
		
		
	}
	
	/**
	 * Metoda zjisti statistiku dne:
	 * 		- Pocet zbylych sudu u prekladist
	 * 		- Pocet vozu kazdeho prekladiste
	 * 		- Pocet prijatych objednavek
	 * 		- Pocet zpracovanych objednavek
	 */
	private void statistikaDne()
	{
		MainApp.zapisDoSouboru("==================");
		MainApp.zapisDoSouboru("= STATISTIKA DNE =");
		MainApp.zapisDoSouboru("==================");
		MainApp.zapisDoSouboru("Pivovar: ");
		addLog("Pivovar: ma "+ pivovar.vozy.size() +" vozu");
		MainApp.zapisDoSouboru(pivovar.vozy.size() +" vozu");
		addLog("         zbylo v nem "+ pivovar.objednavky.size() +" objednavek");
		MainApp.zapisDoSouboru("zbylo v nem "+ pivovar.objednavky.size() +" objednavek");
		if (pivovar.objednavky.size() > 1)
		{
			addLog("         objednavka s nejvyssi prioritou je z " + pivovar.objednavky.peek().getDen() + ". dne " + pivovar.objednavky.peek().getCas() + ". hodiny");
			MainApp.zapisDoSouboru("objednavka s nejvyssi prioritou je z " + pivovar.objednavky.peek().getDen() + ". dne " + pivovar.objednavky.peek().getCas() + ". hodiny");
		}
		MainApp.zapisDoSouboru("");
		
		for(Prekladiste p : prekladiste.values()){
			MainApp.zapisDoSouboru("Prekladiste "+p.id);
			MainApp.zapisDoSouboru("Sudy: "+p.sklad);
			addLog("Prekladiste "+ (p.id-4001) +": ma "+ p.vozy.size() +" vozu");
			MainApp.zapisDoSouboru(p.vozy.size() +" vozu");
			addLog("               zbylo v nem "+ p.objednavky.size() +" objednavek");
			MainApp.zapisDoSouboru("zbylo v nem "+ p.objednavky.size() +" objednavek");
			if (p.objednavky.size() > 1)
			{
				addLog("               objednavka s nejvyssi prioritou je z " + p.objednavky.peek().getDen() + ". dne " + p.objednavky.peek().getCas() + ". hodiny");
				MainApp.zapisDoSouboru("objednavka s nejvyssi prioritou je z " + p.objednavky.peek().getDen() + ". dne " + p.objednavky.peek().getCas() + ". hodiny");
			}
			MainApp.zapisDoSouboru("");
		}
		MainApp.zapisDoSouboru("");
	}
	
	/**
	 * Metoda rozdelujici hospodySud do kompetence nejblizsiho prepraviste
	 * Hospody tank zaviseji vsechny na pivovaru Chmelokvas
	 */
	public void rozdelHospody(){
		
		/*
		 * vzdalenost hospody od prekladiste
		 */
		int vzdalenost;
		/*
		 * aktualne minimalni nalezena vzdalenost od hospody k prekladisti
		 */
		int minVzdalenost;
		int skladID;
		
		/*
		 * pocitadlo hospod prirazenych k jednotlivym prekladistim
		 */
		int[] test = {0,0,0,0,0,0,0,0};
		
		
		/*
		 * Projde po jedne vsechny hospodySud a najde jim nejblizsi prekladiste
		 */
		for(HospodaSud hospoda : hospodySud.values()){
	
			/*
			 * Nastavim minimalni vzdalenost na hodnotu vyrazne vyssi nez ocekavanou
			 */
			minVzdalenost = 1000000;
			
			skladID = 0;
			
			/*
			 * Projdu vsechny prekladiste a pro kazde urcim vzdalenost k hospode, nejblizsi vyhrava
			 * Pokud je hospoda velmi blizko prekladisti (40km), nehleda dalsi
			 * Pri vypoctu vzdalenosti hodnotu vzdalenosti neodmocnuji(zbytecny vypocet) a porovnavam
			 * primo kvadraty jejich vzdalenosti(proto 1600 - 40 km)
			 */
			for(Prekladiste sklad : prekladiste.values()){
		
				/*
				 * Vypocet kvadratu vzdalenosti hospody od tohoto prekladiste
				 */
				vzdalenost = (sklad.poloha[0] - hospoda.poloha[0])*(sklad.poloha[0] - hospoda.poloha[0]) + 
						(sklad.poloha[1] - hospoda.poloha[1])*(sklad.poloha[1] - hospoda.poloha[1]);
				
				
				/*
				 * Zjisteni, zda neni hospoda velmi blizko prekladisti, pokud ano - break
				 */
				if(vzdalenost<=1600){
					minVzdalenost = vzdalenost;
					skladID = sklad.id;
					break;
				}
				
				/*
				 * Zjistovani, zda je toto prekladiste prozatim nejblize hospode
				 */
				if(vzdalenost<minVzdalenost){
					minVzdalenost = vzdalenost;
					skladID = sklad.id;
				}
				
			}
			
			/*
			 * pripocte hospodu k danemu prekladisti
			 * prekladiste maji ID 4001-4008
			 */
			test[skladID-4001]++;
			
			/*
			 * pridani hospody do spravy nejblizsiho prekladiste
			 */
			prekladiste.get(skladID).prihlasHospodu(hospoda);
			hospoda.setIdPrekladiste(skladID);
			
		}
		
		/*
		 * testovaci vypis poctu hospod prihlasenych k jednotlivym prekladistim
		 */
		for(int i = 0; i < test.length; i++){
			
			System.out.println("Prekladiste "+ (i+1) + ": " + test[i] + " hospod");
			MainApp.zapisDoSouboru("Prekladiste "+ (i+1) + ": " + test[i] + " hospod");
			
		}
		
		/*
		 * testovaci soucet hospod registrovanych u prekladist + hospod u pivovaru
		 */
		int soucet = 0;
		for(int i = 0; i < test.length; i++){
			
			soucet += test[i];
			
		}
		System.out.println("Prekladiste soucet: " + soucet);
		MainApp.zapisDoSouboru("Prekladiste soucet: " + soucet);
		
		System.out.println("Pivovar: " + pivovar.hospodyTank.size());
		MainApp.zapisDoSouboru("Pivovar: " + pivovar.hospodyTank.size());
		
	}

	
	/**
	 * spocte delky vsech cest a ulozi je do matice
	 * 
	 */
	public void spoctiVzdalenosti()
	{
		System.out.print("Pocitam vzdalenosti.. ");

		delkyCest = new Float[objekty.length][objekty.length];
		
		for(int i = 0; i < objekty.length; i++){
			
			for(int j = 0; j < objekty.length; j++){
				
				delkyCest[i][j] = (float)0;
				
			}
			
		}
		
		
		for(int i = 0; i < objekty.length; i++){
		
			for(Integer sousedID : objekty[i].sousedi){
				
				delkyCest[i][sousedID] = (float) Math.sqrt(((objekty[sousedID].poloha[0] - objekty[i].poloha[0]) * (objekty[sousedID].poloha[0] - objekty[i].poloha[0]) +
						(objekty[sousedID].poloha[1] - objekty[i].poloha[1]) * (objekty[sousedID].poloha[1] - objekty[i].poloha[1])));
				
			}
			
			
		}
		
		System.out.println("Hotovo");
		
	}
	
	
	
	
	/**
	 * Metoda projde objednavky hospod a aktualni zaradi do fronty prekladiste.
	 * @param hodina - aktualni hodina
	 */
	public void prijmiObjednavky(int hodina){
		
		int pocObjednavek = 0;
		for(HospodaSud hospoda : hospodySud.values()){
			
			if(hospoda.nova.getObjem() < 1)
			{
				continue;
			}
			
			if(hospoda.nova.getCas()==hodina){
				
				prekladiste.get(hospoda.idPrekladiste).prijmiObjednavku(hospoda.nova);
				pocObjednavek++;
				
			}
		}
		
		for(HospodaTank hospoda : hospodyTank.values()){
			
			if(hospoda.nova.getObjem() < 1)
			{
				continue;
			}
			
			if(hospoda.nova.getCas()==hodina){
				
				pivovar.prijmiObjednavku(hospoda.nova);
				pocObjednavek++;
				
			}
		}
		
		if(pocObjednavek > 0)
		{
			addLog("Prijato objednavek "+pocObjednavek);			
		}
	
	}
	
	
	
	/**
	 * Metoda necha vygenerovat objednavky pro vsechny hospody(sud i tank) a preda jim je
	 */
	public void generujObjednavky(){
		 
		Random r = new Random();
		float rObjem;
		
		/*
		 * Projde po jedne vsechny hospodySud a nahodne jim zvoli objednavku
		 */
		for(HospodaSud hospoda : hospodySud.values()){
			rObjem = r.nextFloat();

			hospoda.zadejObjednavku(zvolCasObjednavky(), zvolObjemObjednavky(rObjem), getCas().den);
			
		}
		
		
		/*
		 * Projde po jedne vsechny hospodyTank a nahodne jim zvoli objednavku
		 */
		for(HospodaTank hospoda : hospodyTank.values()){
			
			rObjem = r.nextFloat();
			
			hospoda.zadejObjednavku(zvolCasObjednavky(), zvolObjemObjednavky(rObjem), getCas().den);
			
		}
		
		
	}
	
	public void zadejObjednavkuManualne(int id, int objem)
	{
		if(objekty[id] instanceof HospodaSud)
		{
			((HospodaSud)objekty[id]).zmenaObjednavky(10, objem);
		}
		else if (objekty[id] instanceof HospodaTank)
		{
			((HospodaTank)objekty[id]).zmenaObjednavky(10, objem);
		}
	}
	
	/**
	 * Metoda urci objem objednavky na zaklade nahodneho cisla
	 * 
	 * @param rObjem - nahodne cislo v rozsahu (0,1) pro urceni objemu objednavky
	 * @return nahodne urceny objem objednavky
	 */
	public int zvolObjemObjednavky(float rObjem){
		
		if(rObjem < 0.25){ return 1; }
		else if(rObjem < 0.5){ return 2; }
		else if(rObjem < 0.7){ return 3; }
		else if(rObjem < 0.85){ return 4; }
		else if(rObjem < 0.95){ return 5; }
		else{ return 6; }
		
	}
	
	/**
	 * Metoda urci cas objednavky na zaklade nahodneho cisla
	 * 
	 * @param rCas - nahodne cislo v rozsahu (0,1) pro urceni casu objednavky
	 * @return nahodne urceny cas objednavky
	 */
	public int zvolCasObjednavky(){
		
		/*
		 * Cas objednavky je urcen gaussovym rozdelenim
		 * Nejvice objednavek chodi kolem 10
		 * Posledni objednavka muze prijit max 16:00
		 * Prvni objednavka muze prijit nejdrive v 8:00
		 * Funkce pro normalni rozlozeni:
		 * 	f(x) = 1/(o*sqrt(2*PI)) * exp(-(x-mid)*(x-mid) / (2*o*o))
		 * kde mid=10 a o=2.5
		 * 
		 * tato funkce lze v jave nahradit: r.nextGaussian()*2.5 + 10
		 */
		/*if(rCas < 0.05){ return 8; }
		else if(rCas < 0.15){ return 9; }
		else if(rCas < 0.50){ return 10; }
		else if(rCas < 0.65){ return 11; }
		else if(rCas < 0.75){ return 12; }
		else if(rCas < 0.85){ return 13; }
		else if(rCas < 0.90){ return 14; }
		else if(rCas < 0.95){ return 15; }
		else{ return 16; }*/
		
		/*
		 * funkce vraci hodnoty v prilis velkem rozmezi, proto je omezime jen na hodnoty od 8 do 16 
		 * vsechny hodnoty, ktere budou mimo tento interval budou vraceny jako hodnota 10
		 */
		double o = 2.5, mid=10;
		Random r = new Random();
		int cislo = (int)Math.round(r.nextGaussian()*o+mid);
		cislo = (cislo >=8) && (cislo<=16) ? cislo : (int)mid;
		return cislo;      
		
	}
	
	/**
	 * Metoda posle spravu Observerovi(Okno) aby vypsal zpravu.
	 * 
	 * @param log Zprava.
	 */
	private void addLog(String log)
	{
		setChanged();
		notifyObservers(log);
	}
	
	/**
	 * Metoda informuje vsechny obderatele o zmene casu.
	 */
	private void noteCas()
	{
		setChanged();
		//notifyObservers(new int[] {soucDen,soucHodina});
		notifyObservers(soucCas.clone());
	}
	
	/**
	 * Metoda posla vsem predplatitelum zpravu 0. Trida okno tuto zpravu pochopi a zavola rpekresleni mapy.
	 * Ostatni tuto zpravu ignoruji.
	 */
	private void notePrekresli()
	{
		setChanged();
		notifyObservers(new Integer(0));
	}
	
	/**
	 * Metoda vraci soucasny cas.
	 * @return Cas ve formatu {den,hodina}
	 */
	public static Cas getCas()
	{
		return soucCas.clone();
	}
	
	/**
	 * Metoda vraci polohu uzlu s danym id.
	 * @param id ID uzlu.
	 * @return  POloha [x,y]
	 */
	public static int[] getPoloha(int id)
	{
		if(id < 0 || id > Simulator.objekty.length)
		{
			return null;
		}
		else
		{
			return Simulator.objekty[id].poloha;
		}
	}
	
	
	public void resetSimulace()
	{
		timerCas.stop();
		startSimulace();
	}
	
	public void pauzaSimulace()
	{
		timerCas.stop();
	}
	
	public void pokracovaniSimulace()
	{
		timerCas.start();
	}
}
