package br.uefs.analyzIR.graph;

import java.util.ArrayList;
import java.util.List;

import br.uefs.analyzIR.exception.NoGraphPointException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/***
 * Represents a point set that has a unique tag identifier.
 * 
 * @since 1.0
 * @author lucas
 * */

@XStreamAlias("GraphItem")
public class GraphItem {

	private String name;
	private List<Point> points;

	/**
	 * Constructs a new Item with a name and a empty Point set.
	 * 
	 * @param name
	 *            GraphItem name
	 * @since 1.0
	 * @author lucas
	 * */
	public GraphItem(String name) {
		this.name = name;
		this.points = new ArrayList<Point>();
	}

	/**
	 * Appends the specified point (x,y) to the end of Point list. 
	 * 
	 * @param x
	 *            a value
	 * @param y
	 *            a value
	 * @since 1.0
	 * @author lucas
	 * */
	public void addPoint(String x, double y) {
		Point point = new Point(x, y);
		points.add(point);
	}

	/**
	 * Returns all points (ordered pairs). 
	 * 
	 * @retun all points.
	 * @since 1.0
	
	 * @throws NoGraphPointException
	 *              if Point list is empty
	 * */
	public Point[] getPoints() throws NoGraphPointException {
		if (points.size() != 0) {
			Point[] ps = new Point[points.size()];
			ps = points.toArray(ps);
			return ps;
		} else {
			throw new NoGraphPointException("This GraphItem [" + getName()
					+ "] is empty.");
		}
	}

	/**
	 * Returns name Item.
	 * 
	 * @return name Item
	 * @since 1.0
	 * @author lucas
	 * */
	public String getName() {
		return this.name;
	}

	/**
	 * Compares this graphItem to the specified object. 
	 * @param obj The object to compare this GraphItem against
	 * 
	 * @return  if the given object represents a GraphItem and have the same name, false otherwise
	 * @since 1.0

	 */
	@Override 
	public boolean equals(Object obj){
		if(obj instanceof GraphItem){
			GraphItem item = (GraphItem)obj;
			if(this.getName().equals(item.getName())){
				return true;
			}
		}
		return false;
	}
}
