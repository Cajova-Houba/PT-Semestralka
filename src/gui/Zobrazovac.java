package gui;


import graf.Pivovar;
import graf.Prekladiste;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import main.Simulator;

public class Zobrazovac extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int DEF_SIRKA = 500;
	
	private static final int DEF_VYSKA = 500;
	
	private final int SIRKA = 1500;
	
	private final int VYSKA = 1500;
	
	public final float Xmeritko = SIRKA/DEF_SIRKA;
	
	public final float Ymeritko = VYSKA/DEF_VYSKA;
	
	//Odkaz na instanci okna
	private final Okno okno;
	
	//prave vybrany uzel
	public int vybranyUzel = -1;
	
	//private boolean mapaVykreslena = false;
	
	public Zobrazovac(Okno okno)
	{
		this.setPreferredSize(new Dimension(SIRKA, VYSKA));
		this.setVisible(true);
		this.addMouseListener(new Klikatko());
		this.okno = okno; 
	}
	
	
	
	/**
	 * Metoda vykresli mapu na panel a nastavi atribut mapaVykreslena na true. Tim nebude dochazet ke stalemu
	 * prekreslovani mapy
	 */
	private void vykresliMapu(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, SIRKA, VYSKA);
		
		//vykresleni cest
		for(int i=0; i<Simulator.objekty.length; i++)
		{
			Simulator.objekty[i].vykresliCesty(g2,Xmeritko,Ymeritko);
		}
		
		//vykresleni uzlu
		for(int i=0; i<Simulator.objekty.length; i++)
		{
			Simulator.objekty[i].vykresliSe(g2,Xmeritko,Ymeritko);
		}
		//this.mapaVykreslena = true;
	}
	
	private void vykresliVozy(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
 		
		//vykresleni vozu pivovaru
		((Pivovar)Simulator.objekty[0]).vykresliVozy(g2, Xmeritko, Ymeritko);
		
		//vykresleni vozu prekladist
		for(int i=4001;i<4009;i++)
		{
			((Prekladiste)Simulator.objekty[i]).vykresliVozy(g2, Xmeritko, Ymeritko);
		}
	}
	
	public void paint(Graphics g)
	{
		vykresliMapu(g);
		vykresliVozy(g);
		//aktualizuje sklad ve vyberovem panelu v Okno
		okno.updateVyberPanel(vybranyUzel);
	}

	/**
	 * Metoda zjisti zda je na zadanych souradnicich (relativnich k panelu) uzel. Pokud ano, vrati jeho index v poli Simulator.objekty.
	 * Pokud vrati -1, na zadanych souradnicich nic nelezi.
	 * @param x X, relativni k panelu.
	 * @param y Y, relativni k panelu.
	 */
	public int najdiUzel(int x, int y)
	{
		int id = -1;
		int xo = (int)(x/Xmeritko);
		int yo = (int)(y/Ymeritko);
		
		for (int i = 0; i < Simulator.objekty.length; i++) {
			
			if(Simulator.objekty[i].lezisTady(xo, yo))
			{
				id = i;
			}
			
			Simulator.objekty[i].jeVybrany = false;
		}
		
		return id;
	}
	
	/**
	 * Metoda vybere uzel na zadanych souradnicich.
	 * @param x X, relativni k panelu.
	 * @param y Y, relativni k panelu.
	 */
	public void vyberUzel(int x, int y)
	{
		int id = najdiUzel(x, y);
		if(id != -1)
		{
			Simulator.objekty[id].jeVybrany = true;
			this.vybranyUzel = id;
		}
		else
		{
			this.vybranyUzel = -1;
		}
		okno.setVyberPanel(id);
	}
	
	/**
	 * Trida pro ziskani pozice mysi relativni vuci panelu.
	 * @author Zdendek Vales
	 *
	 */
	private class Klikatko extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseClicked(e);
			int x = e.getPoint().x;
			int y = e.getPoint().y;
			vyberUzel(x, y);
		}
	}
	
}
