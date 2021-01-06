package br.uefs.analyzIR.predefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.InvalidGraphNameException;
import br.uefs.analyzIR.ui.structure.DataInfo;

public class GainPredef implements Predef {

	private Facade facade;
	private List<String> runsList;
	private List<String> topicList;
	private List<String> measures;
	private DataInfo info;
	

	public GainPredef(Facade facade){
		this.facade = facade;
		info = new DataInfo();
		this.init();
		runsList = facade.listRuns();
		
	}
	
	@Override
	public void init() {
		measures = new ArrayList<String>();
		topicList = Arrays.asList("AVG");
		measures.add("G");
		measures.add("binG");
		measures.add("ndcg");
		putDataInfo(info);
		
	}

	@Override
	public void putDataInfo(DataInfo dataInfo) {

		dataInfo.putData("measures", measures);
		dataInfo.putData("runs", runsList);
		dataInfo.putData("topics", topicList);
		dataInfo.putData("name", "Gain");
	}

	@Override
	public void show() {

	}

	@Override
	public JPanel make() {
		try {

			int type = facade.getMeasureType(measures);
			

			JPanel plot = facade.createGraph(type, "Gain", measures , runsList, topicList, null, "Gain", null, "", "measure value", null, 0);

			return plot;

		} catch (InvalidGraphNameException e) {
			JOptionPane.showMessageDialog(null, "There is already a Gain Predef created", "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataInfo getDataInfo() {
		return info;
	}
}