package br.uefs.analyzIR.measure.data;

/***
 * 
 * A MeasureValue object contains a pair of values [x, value] where x can be a topic name 
 * or a 'at' value while value is the measure result. 
 *
 * @author Lucas
 */
public class MeasureValue {

	private String x; 
	private final double value; 
	
	/**
	 * Constructs a new  MeasureValue by x and measure result. 
	 * @param x topic name or at value
	 * @param value measure result for x value
	 */
	public MeasureValue(String x, double value) {
		this.x = x;
		this.value = value;
	}
	
	/**
	 * Initializes a newly created MeasureValue object with measure result. 
	 * @param value measure result
	 */
	public MeasureValue(double value){
		this.value = value;
		this.x = "";
	}

	/**
	 * Returns a topic name or a 'at' value.
	 * @return String - the topic name or a 'at' value.
	 */
	public String getX() {
		return x;
	}
	
	/**
	 * Sets a new x value. The value can be a topic name or a 'at' value.
	 * @param x - topic name or a 'at' value. 
	 */
	public void setX(String x){
		this.x = x; 
	}
	
	/**
	 * Returns the measure result.
	 * @return double - the measure result. 
	 */
	public double getValue() {
		return value;
	}
	
}
