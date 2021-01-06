package br.uefs.analyzIR.measure.data;

import java.util.ArrayList;
import java.util.List;

/***
 * TopicResult is a MeasureValue set and organize the result 
 * @author lucas
 *
 */
public class TopicResult {

	private List<MeasureValue> measureValue;
	private final String topic; 
	
	/***
	 * 
	 * @param topic
	 */
	public TopicResult(String topic) {
		this.measureValue = new ArrayList<>();
		this.topic = topic; 
	}
	
	/***
	 * 
	 * @return
	 */
	public String getTopicName(){
		return this.topic;
	}

	/**
	 * 
	 * @param v
	 */
	public void addValue(double v){
		this.measureValue.add(new MeasureValue(v));
	}
	
	/***
	 * 
	 * @param x
	 * @param v
	 */
	public void addValue(String x, double v){
		this.measureValue.add(new MeasureValue(x, v));
	}

	/**
	 * 
	 * @return
	 * @throws 
	 */
	public List<MeasureValue> getValues()  {
		return this.measureValue;
	}
}
