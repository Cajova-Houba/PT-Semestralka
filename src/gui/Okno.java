package gui;

import graf.HospodaSud;
import graf.UzelTyp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.Cas;
import main.Simulator;

public class Okno extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Buffer pro ukladani zaznamu logu.
	 */
	private static StringBuffer log;
	
	/**
	 * Vykreslvoani panel
	 */
	Zobrazovac zob;
	
	/**
	 * Odkaz na instanci simulatoru.
	 */
	Simulator sim;
	
	//labely pro zobrazeni hodiny a casu
	private JLabel denLab;
	private JLabel casLab;
	private final String denLabS = "Den: ";
	private final String casLabS = "Čas:  ";
	
	//textarea pro vypisovani logu
	private JTextArea logArea;
	
	//buttony pro ovladani simulace
	private JButton bResetSim;
	private JButton bStopSim;
	private JButton bStartSim;
	
	
	//komponenty k vybiracimu panelu
	private JLabel vybrUzelLab;
	private final String vybrMsg = "Vybran uzel :";
	private final String nevybrMsg = "Nevybran zadny uzel.";
	
	private JLabel typUzluLab;
	private final String typMsg = "Typ uzlu: ";
	
	private JLabel polohaLab;
	private final String polMsg = "Poloha: ";
	
	private JLabel skladLab;
	private final String skladMsg = "Stav skladiste: ";
	
	private JLabel prazdneSudyLab;
	private final String prazdneSudyMsg = "Pocet prazdnych sudu: ";
	
	private JLabel zadejObjLab;
	private final String zadejObjMsg = "Zadání objednávky";
	private JButton zadejObjBtn;
	private JLabel idLab;
	private JLabel objemLab;
	private JLabel hodinaLab;
	private JTextField idTF;
	private JTextField objemTF;
	private JTextField hodinaTF;
	
	
	public Okno(Simulator sim)
	{
		this.log = new StringBuffer();
		this.sim = sim;
		
		this.setTitle("PT - simulace");
		this.setSize(new Dimension(1000, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.add(inicPanel(), BorderLayout.CENTER);
		this.add(inicInfoPanel(), BorderLayout.NORTH);
		this.add(inicLogPanel(), BorderLayout.WEST);
		this.add(inicOvlPanel(), BorderLayout.SOUTH);
		this.add(inicVyberPanel(), BorderLayout.EAST);
		
		this.setVisible(true);
	}
	
	/**
	 * Metoda inicializuje hlavni panel pro vykresleni mapy hospod.
	 * 
	 * @return ScrollPane s panelem.
	 */
	private Container inicPanel()
	{
		zob = new Zobrazovac(this);
		JScrollPane sp = new JScrollPane(zob);
		
		sp.setPreferredSize(new Dimension(500,500));
		
		return sp;
	}
	
	/**
	 * Metoda inicializuje info panel.
	 * 
	 * @return Panel s labelama.
	 */
	private Container inicInfoPanel()
	{
		JPanel pan = new JPanel();
		pan.setPreferredSize(new Dimension(0,25));
		pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
		JScrollPane sp = new JScrollPane(pan);
		
		denLab = new JLabel(denLabS);
		casLab = new JLabel(casLabS);
		pan.add(denLab);
		pan.add(Box.createRigidArea(new Dimension(30, 25)));
		pan.add(casLab);
		
		//return sp;
		return pan;
	}
	
	/**
	 * Metoda inicializuje panel ve kterem bude vypisovan log.
	 * 
	 * @return Scrollpane s panelem.
	 */
	private Container inicLogPanel()
	{
		JPanel pan = new JPanel();
		pan.setPreferredSize(new Dimension(200,0));
		pan.setLayout(new BorderLayout());
		
		logArea = new JTextArea();
		logArea.append("Log:\n");
		logArea.setEditable(false);
		
		JScrollPane sp = new JScrollPane(logArea);
		pan.add(sp,BorderLayout.CENTER);
		
		return pan;
	}
	
    /**
     * Metoda inicializuje panel pro ovladani simulace.
     * @return ScrollPane s ovladacim panelem.
     */
	private Container inicOvlPanel()
	{
		JPanel pan = new JPanel();
		JScrollPane sp = new JScrollPane(pan);
		pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
		bResetSim = new JButton(new aResetSimulace());
		bResetSim.setEnabled(false);
		bStopSim = new JButton(new aStopSimulace());
		bStartSim = new JButton(new aStartSimulace());
		
		pan.add(Box.createHorizontalGlue());
		pan.add(bResetSim);
		pan.add(Box.createRigidArea(new Dimension(15, 0)));
		pan.add(bStopSim);
		pan.add(Box.createRigidArea(new Dimension(15, 0)));
		pan.add(bStartSim);
		pan.add(Box.createHorizontalGlue());
		
		return sp;
	}
	
	
	/**
	 * Metoda inicializuje panel pro vyber uzlu.
	 * @return ScrollPane s panelem vyberu.
	 */
	private Container inicVyberPanel()
	{
		JPanel pan = new JPanel();
		JScrollPane sp = new JScrollPane(pan);
		
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		vybrUzelLab = new JLabel();
		vybrUzelLab.setFont(new Font("SansSerif", Font.BOLD, 18));
		vybrUzelLab.setAlignmentX(CENTER_ALIGNMENT);
		typUzluLab = new JLabel();
		typUzluLab.setAlignmentX(CENTER_ALIGNMENT);
		polohaLab = new JLabel();
		polohaLab.setAlignmentX(CENTER_ALIGNMENT);
		skladLab = new JLabel();
		skladLab.setAlignmentX(CENTER_ALIGNMENT);
		prazdneSudyLab = new JLabel();
		prazdneSudyLab.setAlignmentX(CENTER_ALIGNMENT);
		
		//vytvoreni komponent pro zadani objednavky
		JPanel panPom = new JPanel(); //panel na idLab,idTF,objemLab,objemTF
		zadejObjLab = new JLabel(zadejObjMsg);
		zadejObjLab.setFont(new Font("SansSerif", Font.BOLD, 18));
		idLab = new JLabel("ID H/P:");
		objemLab = new JLabel("Objem:");
		hodinaLab = new JLabel("Hodina: ");
		idTF = new JTextField(10);
		idTF.setMaximumSize(idTF.getPreferredSize());
		objemTF = new JTextField(10);
		objemTF.setMaximumSize(objemTF.getPreferredSize());
		hodinaTF = new JTextField(10);
		hodinaTF.setMaximumSize(hodinaTF.getPreferredSize());
		zadejObjBtn = new JButton(new aZadejObjednavkuManualne());
		zadejObjBtn.setAlignmentX(CENTER_ALIGNMENT);
		panPom.setLayout(new BoxLayout(panPom,BoxLayout.X_AXIS));
		panPom.add(Box.createHorizontalGlue());
		panPom.add(idLab);
		panPom.add(Box.createRigidArea(new Dimension(5,10)));
		panPom.add(idTF);
		panPom.add(Box.createRigidArea(new Dimension(10,10)));
		panPom.add(objemLab);
		panPom.add(Box.createRigidArea(new Dimension(5,10)));
		panPom.add(objemTF);
		panPom.add(Box.createHorizontalGlue());
		
		JPanel panPom2 = new JPanel();
		panPom2.setLayout(new BoxLayout(panPom2, BoxLayout.X_AXIS));
		//panPom2.add(Box.createHorizontalGlue());
		panPom2.add(hodinaLab);
		panPom2.add(Box.createRigidArea(new Dimension(5, 10)));
		panPom2.add(hodinaTF);
		//panPom2.add(Box.createHorizontalGlue());
		panPom2.setAlignmentX(CENTER_ALIGNMENT);
		
		//inicializace komponent
		int id = this.zob.vybranyUzel;
		setVyberPanel(id);
		
		//odsazeni ze stran
		int gapx = 10;
		//odsazeni ze spodu a z vrchu
		int gapy = 10;
		
		pan.setBorder(BorderFactory.createEmptyBorder(gapy, gapx, gapy, gapx));
		pan.add(vybrUzelLab);
		pan.add(typUzluLab);
		pan.add(polohaLab);
		pan.add(skladLab);
		pan.add(prazdneSudyLab);
		pan.add(Box.createRigidArea(new Dimension(1, 30))); //odsazeni mezi vyberem a zadanim objednavky
		pan.add(panPom);
		pan.add(panPom2);
		pan.add(Box.createRigidArea(new Dimension(1,10)));
		pan.add(zadejObjBtn);
		
		
		return sp;
	}
	
	
	/**
	 * Metoda aktualizuje komponentu skladLab na spravnou informaci o objemu piva.
	 * @param ID uzlu z intervalu <0;4008>.
	 */
	public void updateVyberPanel(int id)
	{
		if((id < 0) || (id > 4008))
		{
			return ;
		}
		else
		{
			skladLab.setText(skladMsg+Simulator.objekty[id].sklad);
			if (Simulator.objekty[id] instanceof HospodaSud)
			{
				prazdneSudyLab.setVisible(true);
				prazdneSudyLab.setText(prazdneSudyMsg+((HospodaSud)Simulator.objekty[id]).prazdneSudy);
			}
			else
			{
				prazdneSudyLab.setVisible(false);
			}
		}
	}
	
	/**
	 * Metodu vola instance tridy {@link Zobrazovac}. Metoda nastavi informace v panelu vyberu podle zadaneho id uzlu.
	 * Pokud id nelezi v itnervalu <0;4008>, nevybere se zadny uzel.
	 * 
	 * @param id ID uzlu z intervalu <0;4008>.
	 */
	public void setVyberPanel(int id)
	{
		if((id < 0) || (id > 4008))
		{
			vybrUzelLab.setText(nevybrMsg);
			typUzluLab.setText(typMsg+nevybrMsg);
			polohaLab.setText(polMsg+nevybrMsg);
			skladLab.setText(skladMsg+nevybrMsg);
			prazdneSudyLab.setVisible(false);
			idTF.setText("");
		}
		else
		{		
			vybrUzelLab.setText(vybrMsg+id);
			typUzluLab.setText(typMsg+Simulator.objekty[id].typ);
			int xu = (int)(Simulator.objekty[id].poloha[0] * zob.Xmeritko);
			int yu = (int)(Simulator.objekty[id].poloha[1] * zob.Ymeritko);
			polohaLab.setText(polMsg+xu+","+yu);
			skladLab.setText(skladMsg+Simulator.objekty[id].sklad);
			if (Simulator.objekty[id] instanceof HospodaSud)
			{
				prazdneSudyLab.setVisible(true);
				prazdneSudyLab.setText(prazdneSudyMsg+((HospodaSud)Simulator.objekty[id]).prazdneSudy);
			}
			else
			{
				prazdneSudyLab.setVisible(false);
			}
			idTF.setText(Integer.toString(id));
		}
	}

	/**
	 * Metoda vezme udaje z komponent idTF a objemTF a zadá Simulátoru novou objednávku. 
	 * Pokud jsou údaje nekorektně zadané, vyhodí chybovou hlášku.
	 */
	public void manualniObjednavka()
	{
		int id,objem,hodina;
		
		try
		{
			id = Integer.parseInt(idTF.getText());
			objem = Integer.parseInt(objemTF.getText());
			hodina = Integer.parseInt(hodinaTF.getText());
		}
		catch(Exception e)
		{
			zobrazChybu("Chyba: Špatný formát zadaných údaju.");
			return ;
		}
		
		//Kontrola zadanych udaju
		if(id == 0)
		{
			zobrazChybu("Chyba: Pivovar nemůže objednávat.");
			return ;
		}
		else if ((id < 1) || (id > 4008))
		{
			zobrazChybu("Chyba: ID musí být v intervalu <1;4008>.");
			return ;
		}
		else if(((id >=1) && (id <= 4000) ) &&
				((objem < 1) || (objem > 6)))
		{
			zobrazChybu("Chyba: Objednávka pro hospodu musí být minimálně 1 a maximálně 6 sudů/hl piva.");
			return ;
		}
		else if((objem < 1) || (objem > 1000))
		{
			zobrazChybu("Chyba: Objednávka pro prekladiste muze byt maximalne 1000 sudu.");
			return ;
		}
		else if((hodina < 10) || (hodina > 16))
		{
			zobrazChybu("Chyba: Objednávku lze zadat pouze v rozsahu hodin 10-16.");
			return;
		}
		
		//zadani objednavky
		sim.zadejObjednavkuManualne(id, objem, hodina);
	}
	
	private void zobrazChybu(String err)
	{
		JOptionPane.showMessageDialog(this, 
				err,
				"Chyba",
				JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		//0=den 1=hodina
		if (arg1 instanceof Cas)
		{
			Cas resp = (Cas) arg1;
			
			denLab.setText(denLabS+resp.den);
			casLab.setText(casLabS+resp.hodina+":"+resp.minuta);	
		}
		//log
		else if(arg1 instanceof String)
		{
			log.append(arg1+"\n\r");
			logArea.append((String)arg1+"\n");
		}
		else if(arg1 instanceof Integer)
		{
			if(((Integer)arg1).intValue() == 0)
			{
				//prekresleni mapy
				zob.repaint();				
			}
		}
	}
	
	
	/*
	 * ========== AKCE ===========
	 */
	
	private class aResetSimulace extends AbstractAction
	{

	    public aResetSimulace() {
			// TODO Auto-generated constructor stub
			putValue(NAME, "Reset simulace");
			putValue(SHORT_DESCRIPTION, "Resetuje simulaci");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			sim.resetSimulace();
		}
	}
	
	private class aStartSimulace extends AbstractAction
	{

	    public aStartSimulace() {
			// TODO Auto-generated constructor stub
			putValue(NAME, "Start simulace");
			putValue(SHORT_DESCRIPTION, "Spusti simulaci");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			sim.pokracovaniSimulace();
		}
	}
	
	private class aStopSimulace extends AbstractAction
	{

	    public aStopSimulace() {
			// TODO Auto-generated constructor stub
			putValue(NAME, "Stop simulace");
			putValue(SHORT_DESCRIPTION, "Pozastavi simulaci");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			sim.pauzaSimulace();
		}
	}
	
	private class aZadejObjednavkuManualne extends AbstractAction
	{

		public aZadejObjednavkuManualne() {
			// TODO Auto-generated constructor stub
			putValue(NAME, "Zadej");
			putValue(SHORT_DESCRIPTION, "Zada objednavku");
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			manualniObjednavka();
		}
	}
}
