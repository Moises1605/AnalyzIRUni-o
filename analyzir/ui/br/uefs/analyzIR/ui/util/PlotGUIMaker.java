package br.uefs.analyzIR.ui.util;

import java.util.List;

public class PlotGUIMaker {

	private List<String> measures;
	private List<String> runs;
	private List<String> topics;
	private String[] xValues;
	private String title;
	private String name; 
	private String atValue;
	private int index; 
	
	public PlotGUIMaker(){
		this.index = -1;
	}
	
	public PlotGUIMaker(List<String> measures,
			List<String> runs, List<String> topics) {
		super();
		this.measures = measures;
		this.topics = topics;
		this.runs = runs;
		this.index =  -1;
	}
	
	public PlotGUIMaker(List<String> measures,
			List<String> runs, List<String> topics, String [] xValues) {
		super();
		this.measures = measures;
		this.topics = topics;
		this.runs = runs;
		this.xValues = xValues;
		this.index = -1;
	}

	public PlotGUIMaker(List<String> measures,
			List<String> runs, List<String> topics, String name) {
		super();
		this.measures = measures;
		this.topics = topics;
		this.runs = runs;
		this.name = name;
		this.index = -1;
	}
	
	public PlotGUIMaker(List<String> measures,
			List<String> runs, List<String> topics, String name, String[] xValues, String atValue) {
		super();
		this.measures = measures;
		this.topics = topics;
		this.runs = runs;
		this.xValues = xValues;
		this.name = name;
		this.index = -1; 
		this.atValue = atValue;
	}

	public List<String> getMeasures() {
		return measures;
	}

	public void setMeasures(List<String> measures) {
		this.measures = measures;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	
	public List<String> getRuns(){
		return this.runs;
	}
	
	public void setRuns(List<String> runs){
		this.runs = runs;
	}

	public String[] getXValues() {
		return xValues;
	}

	public void setXValues(String[] xValues) {
		this.xValues = xValues;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex(){
		return this.index;
	}

	public String getAtValue() {
		return atValue;
	}

	public void setAtValue(String atValue) {
		this.atValue = atValue;
	}
	
	
}
