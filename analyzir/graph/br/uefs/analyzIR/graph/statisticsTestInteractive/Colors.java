package br.uefs.analyzIR.graph.statisticsTestInteractive;

import java.awt.*;
import java.util.ArrayList;

/*
 * Responsável por criar uma paleta de cores  especifica para as curvas relacionadas com os sitemas e para o sistema que servirá
 * como baseline;
 * @author Moisés
 */
public class Colors {
	
	public ArrayList<Color> getColors() {

		ArrayList<Color> colors = new ArrayList();
		colors.add(new Color(0,0,0));
		colors.add(new Color(147,112,219));
		colors.add(new Color(152,251,152));
		colors.add(new Color(255,105,180));
		colors.add(new Color(139,69,19));
		colors.add(new Color(218,112,214));
		colors.add(new Color(75,0,130));
		colors.add(new Color(255,215,0));
		colors.add(new Color(128,128,128));
		//colors.add(new Color(0,255,255));
		colors.add(new Color(139,0,139));
		colors.add(new Color(0,100,0));
		colors.add(new Color(255,99,71));
		colors.add(new Color(219,112,147));
		//colors.add(new Color(100,149,237));
		colors.add(new Color(124,252,0));
		colors.add(new Color(160,82,45));
		colors.add(new Color(240,230,140));
		colors.add(new Color(255,127,80));
		colors.add(new Color(47,79,79));
		colors.add(new Color(54,54,54));
		colors.add(new Color(147,112,219));
		colors.add(new Color(50,205,50));
		colors.add(new Color(244,164,96));
		colors.add(new Color(79,79,79));
		colors.add(new Color(60,179,113));
		colors.add(new Color(143,188,143));
		colors.add(new Color(148,0,211));
		colors.add(new Color(255	,69,0));
		colors.add(new Color(186,85,211));
		colors.add(new Color(255,20,147));
		colors.add(new Color(105,105,105));
		colors.add(new Color(123,104,238));
		colors.add(new Color(128,128,128));
		colors.add(new Color(0,139,139));
		//colors.add(new Color(176,224,230));
		colors.add(new Color(255,255,0));
		colors.add(new Color(112,128,144));
		colors.add(new Color(95,158,160));
		colors.add(new Color(0,255,127));
		colors.add(new Color(0,128,0));
		colors.add(new Color(205,92,92));
		colors.add(new Color(154,205,50));
		colors.add(new Color(107,142,35));
		colors.add(new Color(106,90,205));
		colors.add(new Color(218,165,32));
		colors.add(new Color(188,143,143));
		colors.add(new Color(205,133,63));
		colors.add(new Color(222,184,135));
		colors.add(new Color(138,43,226));
		colors.add(new Color(255,105,180));
		colors.add(new Color(205,92,92));
		colors.add(new Color(255,140,0));
		colors.add(new Color(216,191,216));
		colors.add(new Color(250	,128,114));
		//colors.add(new Color(173,216,230));
		colors.add(new Color(72,61,139));


		return colors;
	} 

	public Color baselineColorLow(){
		return Color.red;
	}


	public Color baslineColorHigh(){
		return Color.blue;
	}
}
