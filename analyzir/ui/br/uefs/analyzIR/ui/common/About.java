package br.uefs.analyzIR.ui.common;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class About {

	private JFrame frame; 
	private JLabel lblInfo; 
	
	public void show(){
		
		initValue();
		initLayout();
		frame.pack();
		frame.setVisible(true);
	}
	
	private void initValue(){
	
		frame = new JFrame("About");
		lblInfo = new JLabel("<html><body><p align=\"center\">AnalyzIR 1.0b  "
				+ "<br /> Release date: Feb 18th, 2015."
				+ "<br /> Free for non-commercial use and distribution."
				+ "<br /> Website: <a href=\"url\">https://sites.google.com/site/analyzir/</a> "
				+ "<br /> Tutorial: <a href=\"url\">https://sites.google.com/site/analyzir/quick-start-tutorial</a> "
				+ "<br /> Contact and bug report: <a href=\"url\">analyzir@gmail.com</a> </center> </p></body></html>");
	}
	
	private void initLayout(){
		
		String columns = "10dlu,200dlu,10dlu,";
		String rows = "10dlu,100dlu,10dlu";

		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);
	
		builder.add(lblInfo, CC.xy(2,2));
	
		frame.setContentPane(builder.getPanel());
	}
	
	
	public static void main(String [] args){
		new About().show();
	}
}
