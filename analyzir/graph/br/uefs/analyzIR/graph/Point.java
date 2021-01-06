package br.uefs.analyzIR.graph;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * An ordered pair of values. 
 * @since 1.0 
 * @author lucas 
 * */

@XStreamAlias("Point")
public class Point {

	
	private String x; 
	private double y; 
	
	/**
	 * Constructs a new point with a ordered pair of values. 
	 * @param x a value 
	 * @param y a value 
	 * @since 1.0
	 * @author lucas
	 * */
	public Point(String x, double y) {
		this.x = x;
		this.y = y; 
	}
	
	/**
	 * Returns this x value. 
	 * @return x value. 
	 * @since 1.0
	 * @author lucas
	 * */
	public String getX(){
		return this.x;
	}

	/**
	 * Returns this y value. 
	 * @return y value.
	 * @since 1.0
	 * */
	public String getY(){
		return this.y+""; 
	}
}
