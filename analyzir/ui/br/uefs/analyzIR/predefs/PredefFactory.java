package br.uefs.analyzIR.predefs;
import br.uefs.analyzIR.controller.Facade;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for defining which PreDef will be instantiated
 * @author Wanderson
 * @version 1.3
 */
public class PredefFactory {
	private Facade facade;
	private List chartNames;
	private int PRECISION = 1;
	private int BASIC_CURVE = 2;
	private int GAIN = 3;
	private int RANK_TOP = 4;
	
	public PredefFactory(Facade facade){
		this.facade = facade;
		this.chartNames = facade.listGraphs();
	}
	
	/**
	 * return a predef according to the type selected 
	 * 
	 * @param type
	 * @return predef
	 */
	public Predef getSingleGraphPredef (int type, String atValue){
		
		if (type == PRECISION ){
			return new BasicPrecision(facade);
		}else if(type == GAIN){
			return new GainPredef(facade);
		}else if(type == RANK_TOP){
			return new RankTopPredef(facade,atValue);
		}
		return null;
	}

	public Predef[] getmultGraphPredef(int type, String[] xValues){

		Predef predef[]= new Predef[3];

		for(int i=0; i<predef.length; i++)
			predef[i] = null;

		if(type == BASIC_CURVE){
			String status[] = {"Already exists!", "Already exists!", "Already exists!"};

			if (!chartNames.contains("Precision Curve")) {
				predef[0] = new PrecisionCurve(facade, xValues);
				status[0] = "Created.";
			}
			if (!chartNames.contains("Recall Curve")) {
				predef[1] = new RecallCurve(facade, xValues);
				status[1] = "Created.";

			}
			if (!chartNames.contains("PxR Curve")) {
				predef[2] = new PxRCurve(facade);
				status[2] = "Created.";
			}
			JOptionPane.showMessageDialog(null, "Precision curve: "+status[0]+"\n\n"+
					"Recall Curve: "+status[1]+"\n\n"+
					"PxR Curve: "+status[2], "Predef status", JOptionPane.INFORMATION_MESSAGE);
			return predef;
		}
		else return null;
	}
}