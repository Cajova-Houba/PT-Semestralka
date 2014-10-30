package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
	private final String casLabS = "Cas:  ";
	
	//textarea pro vypisovani logu
	private JTextArea logArea;
	
	//buttony pro ovladani simulace
	private JButton bResetSim;
	private JButton bStopSim;
	private JButton bStartSim;
	
	public Okno(Simulator sim)
	{
		this.log = new StringBuffer();
		this.sim = sim;
		
		this.setTitle("PT - simulace");
		this.setSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.add(inicPanel(), BorderLayout.CENTER);
		this.add(inicInfoPanel(), BorderLayout.NORTH);
		this.add(inicLogPanel(), BorderLayout.WEST);
		this.add(inicOvlPanel(), BorderLayout.SOUTH);
		
		this.setVisible(true);
		
	}
	
	/**
	 * Metoda inicializuje hlavni panel pro vykresleni mapy hospod.
	 * 
	 * @return ScrollPane s panelem.
	 */
	private Container inicPanel()
	{
		zob = new Zobrazovac();
		JScrollPane sp = new JScrollPane(zob);
		
		sp.setPreferredSize(new Dimension(500,500));
		
		return sp;
	}
	
	/**
	 * Metoda inicializuje info panel.V hlavni aplikaci by mel byt zobrazovan na strane.
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
		pan.setPreferredSize(new Dimension(300,0));
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
			sim.startSimulace();
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
}
