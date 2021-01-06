package br.uefs.analyzIR.graph;

import java.util.List;

import br.uefs.analyzIR.statistics.StatisticsIteractiveInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Represents the minimal information used to create a graph. A Graph Info save the config state to recreate a graph whenever necessary.
 * A GraphInfo has information about chosen measures, topics, runs and at values. An object of GraphInfo is created always a new graph is created.
 * @author lucas
 * 
 */
@XStreamAlias("GraphInfo")
public class GraphInfo {

	private List<String> measures;
	private List<String> runs; 
	private List<String> topics;
	private List<String> iterations;
	private String[] xValues;
	private String atValue;
	private StatisticsIteractiveInfo statisticalTest;
	private Boolean statistical = false;


	/***
	 * Constructs a new GraphInfo with all basic info to recreate a graph.
	 * @param measures
	 * 				chosen measures
	 * @param runs
	 * 				chosen runs
	 * @param topics
	 * 				selected topics
	 * @param atValue
	 * 				single atValue for simple case. 
	 * @param xValues
	 * 				an array of atValue for a curve case.
	 * @param iterations
	 * 				elected interations
	 *
	 */
	public GraphInfo(List<String> measures, List<String> runs,	List<String> topics, String atValue, String [] xValues, List<String> iterations) {

		super(); 
		this.measures = measures; 
		this.runs = runs; 
		this.topics = topics; 
		this.atValue = atValue;
		this.xValues = xValues;
		this.iterations = iterations;
	}
	
	public GraphInfo(List<String> measures, List<String> runs,
			List<String> topics, String atValue, String [] xValues) {
		super(); 
		this.measures = measures; 
		this.runs = runs; 
		this.topics = topics; 
		this.atValue = atValue;
		this.xValues = xValues;		
	}

	public GraphInfo(List<String> measures, List<String> runs, List<String> topics, String atValue, String [] xValues, StatisticsIteractiveInfo statisticalTest, Boolean test) {

		super();
		this.measures = measures;
		this.runs = runs;
		this.topics = topics;
		this.atValue = atValue;
		this.xValues = xValues;
		//this.iterations = iterations;
		this.statisticalTest = statisticalTest;
		this.statistical = test;
	}

	/**
	 * Returns chosen measures. 
	 * @return measure name
	 */
	public List<String> getMeasures() {
		return measures;
	}

	/***
	 * Sets the different chosen measures. 
	 * @param measures new chosen measures. 
	 */
	public void setMeasures(List<String> measures) {
		this.measures = measures;
	}

	/**
	 * Returns an array of atValues.
	 * @return atValues
	 */
	public String[] getXValues() {
		return xValues;
	}

	/**
	 * Sets a new array of atValues. 
	 * @param xValues new atValues
	 */
	public void setXValues(String[] xValues) {
		this.xValues = xValues;
	}

	/**
	 * Returns the selected topics set. 
	 * @return selected topics.
	 */
	public List<String> getTopics() {
		return topics;
	}

	/**
	 * Sets a new selected Topics set.
	 * @param topics new selected topics
	 */
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	/**
	 * Returns chosen runs set.
	 * @return chosen runs.
	 */
	public List<String> getRuns(){
		return this.runs;
	}
	
	/**
	 * Sets a new chosen runs set.
	 * @param runs new chosen runs set.
	 */
	public void setRuns(List<String> runs){
		this.runs = runs;
	}
	
	/**
	 * Returns a single atValue for a simple case. 
	 * @return atValue
	 */
	public String getAtValue() {
		return this.atValue;
	}
	/**
	 * Returns chosen iteration set. 
	 * @return atValue
	 */
	public List<String> getIterations() {
		return this.iterations;
	}
	/**
	 * Sets a new iterations set. 
	 * @return atValue
	 */
	public void setIterations(List<String> iterations) {
		this.iterations = iterations;
	}

	public StatisticsIteractiveInfo getStatisticalTest(){
		return this.statisticalTest;
	}

	public Boolean getStatistical(){
		return this.statistical;
	}
	
	
}
