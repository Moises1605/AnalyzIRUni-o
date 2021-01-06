package br.uefs.analyzIR.predefs;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.ui.structure.DataInfo;


public class BasicPrecision implements Predef {

	private Facade facade;
	private List<String> runsList;
	private List<String> topicList;
	private List<String> measures;
	private DataInfo info;


	public BasicPrecision(Facade facade){
		this.facade = facade;
		this.info = new DataInfo();
		this.init();
		this.runsList = facade.listRuns();
		this.topicList = Arrays.asList("AVG");
	}

	@Override
	public void init() {

		measures = new ArrayList<String>();

		topicList = Arrays.asList("AVG");

		measures.add("GMAP");
		measures.add("map");
		measures.add("recip_rank");

		putDataInfo(info);
	}

	@Override
	public void putDataInfo(DataInfo dataInfo) {
		dataInfo.putData("measures", measures);
		dataInfo.putData("runs", runsList);
		dataInfo.putData("topics", topicList);
		dataInfo.putData("name", "Basic_Precision");
	}

	@Override
	public JPanel make() {
		try {

			int type = facade.getMeasureType(measures);
			JPanel plot = facade.createGraph(type, "Basic Precision", measures , runsList, topicList, null, "Basic_Precision", null, "", "measure value", null, 0);

			return plot;

		} catch (InvalidGraphNameException e) {
			JOptionPane.showMessageDialog(null, "There is already a Precision Predef created", "Error",
					JOptionPane.ERROR_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
	}


	@Override
	public void show() {}
}
