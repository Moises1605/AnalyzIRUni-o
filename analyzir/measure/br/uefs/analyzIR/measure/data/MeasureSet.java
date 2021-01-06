package br.uefs.analyzIR.measure.data;

import java.util.HashMap;

import br.uefs.analyzIR.exception.EmptyTopicMeasureValueException;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;

/**
 * 
 * @author lucas
 *
 */
public class MeasureSet {

	private String measure; 
	private HashMap<String, TopicResult> set;
	
	/**
	 * 
	 */
	public MeasureSet() {
		set = new HashMap<>();
	}

	public MeasureSet(String measureName){
		this.measure = measureName; 
		set = new HashMap<>(); 
	}
	/***
	 * 
	 * @param topic
	 * @throws InvalidItemNameException
	 */
	public void addTopicResult(String topic) throws InvalidItemNameException {
		if (!set.containsKey(topic)) {
			TopicResult base = new TopicResult(topic);
			set.put(topic, base);
		} else {
			throw new InvalidItemNameException(
					"There is a TopicMeasureValue with name " + topic
							+ "in a TopicSet. A value was repeated during a calculation of measure. Try again");
		}

	}

	/***
	 * 
	 * @param topic
	 * @param x
	 * @param value
	 * @throws ItemNotFoundException
	 */
	public void addValue(String topic, String x, double value)
			throws ItemNotFoundException {
		if (set.containsKey(topic)) {
			TopicResult base = set.get(topic);
			base.addValue(x, value);
		} else {
			throw new ItemNotFoundException("There isn't a TopicMeasuresValue with name = "
					+ topic
					+ ".Because of this, it was not possible input a new point.");
		}
	}

	/***
	 * 
	 * @param topic
	 * @param value
	 * @throws ItemNotFoundException
	 */
	public void addValue(String topic, double value)
			throws ItemNotFoundException {
		if (set.containsKey(topic)) {
			TopicResult base = set.get(topic);
			base.addValue(value);
		} else {
			throw new ItemNotFoundException("There is not a TopicMeasuresValue with name "+ topic +"."
					+ " Because of this, it was not possible input a new point.");
		}
	}
	
	/***
	 * 
	 * @return
	 * @throws EmptyTopicMeasureValueException
	 */
	public TopicResult[] getTopicResults() throws EmptyTopicMeasureValueException {
		if (set.size() != 0) {
			TopicResult[] b = new TopicResult[set.size()];
			b = set.values().toArray(b);
			return b;
		}
		throw new EmptyTopicMeasureValueException("There isn't a TopicMeasuresValues in this result. It happened because a topic was not calculated.");
	}
	
	/***
	 * 
	 * @param name
	 * @return
	 * @throws ItemNotFoundException
	 */
	public TopicResult getTopicResult(String name) throws ItemNotFoundException {
		if (set.size() != 0 && set.containsKey(name)) {
			return set.get(name);
		} else {
			throw new ItemNotFoundException("There isn't a TopicMeasuresValues with name = "
					+ name + ". It happened because a topic was not calculated.");
		}
	}

	public void setMeasureName(String measureName) {
		this.measure = measureName;
	}
	
	public String getMeasureName(){
		return this.measure;
	}
}
