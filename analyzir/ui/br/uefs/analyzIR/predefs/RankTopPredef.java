package br.uefs.analyzIR.predefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.InvalidGraphNameException;
import br.uefs.analyzIR.ui.structure.DataInfo;

public class RankTopPredef implements Predef {

	private Facade facade;
	private List<String> runsList;
	private List<String> topicList;
	private List<String> measures;
	private String atValue;
	private DataInfo info;
	

	public RankTopPredef(Facade facade, String atValue){
		this.facade = facade;
		this.info = new DataInfo();
		this.init();
		this.runsList = facade.listRuns();
		this.atValue = atValue;
		
	}
	@Override
	public void init() {
		measures = new ArrayList<String>();
		topicList = Arrays.asList("AVG");
		measures.add("P@N");
		measures.add("recall@N");
		measures.add("ndcg_cut");
		measures.add("map_cut");
		putDataInfo(info);
		
	}


	public void putDataInfo(DataInfo dataInfo) {
		dataInfo.putData("measures", measures);
		dataInfo.putData("runs", runsList);
		dataInfo.putData("topics", topicList);
		dataInfo.putData("name", "Rank Top");
	}

	@Override
	public void show() {

	}


	@Override
	public JPanel make() {
		try {

			int type = facade.getMeasureType(measures);

			JPanel plot = facade.createGraph(type, "Rank Top", measures, runsList, topicList, null, "Rank Top", atValue, "", "measure value", null, 0);			return plot;


		} catch (InvalidGraphNameException e) {
			JOptionPane.showMessageDialog(null, "There is already a Rank Top Predef created", "Error",
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
}
