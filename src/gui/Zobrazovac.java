package gui;


import graf.Pivovar;
import graf.Prekladiste;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

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
	
	private final float Xmeritko = SIRKA/DEF_SIRKA;
	
	private final float Ymeritko = VYSKA/DEF_VYSKA;
	
	//private boolean mapaVykreslena = false;
	
	public Zobrazovac()
	{
		this.setPreferredSize(new Dimension(SIRKA, VYSKA));
		this.setVisible(true);
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
	}
	
	
}
