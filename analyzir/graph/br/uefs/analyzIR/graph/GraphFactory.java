package br.uefs.analyzIR.graph;

import br.uefs.analyzIR.graph.bars.BarGraph;
import br.uefs.analyzIR.graph.curves.CurveGraph;
import br.uefs.analyzIR.exception.InvalidGraphTypeException;

/***
 * GraphFactory creates objects of concrete classes that implements Graph abstract class.
 */
public class GraphFactory {

	public static final int BAR_PLOT = 0;
	public static final int BAR_1_PLOT = 1;
	public static final int CURVE_PLOT = 2;

	public GraphFactory() {
	}

	/**
	 * Creates a new graph of a requested type. This method returned an object of implementations of Graph abstract
	 * class.
	 * @param title graph title
	 * @param labelX x axis title
	 * @param labelY y axis title
	 * @param type implemented graph type
	 * @param name graph file name
	 * @return a grph
	 * @throws InvalidGraphTypeException if type requested was not implemented
	 */
	public Graph getPlot(String title, String labelX, String labelY, final int type, final String name) throws InvalidGraphTypeException {
		if(type == BAR_PLOT){
			return new BarGraph(title, labelX, labelY, name);
		}else if(type == BAR_1_PLOT){
			return new BarGraph(title, labelX, labelY, name);
		} else if(type == CURVE_PLOT){
			return new CurveGraph(title, labelX, labelY, name);
		}else{
			throw new InvalidGraphTypeException("There is not the graph type. Please change the type.");
		}
	}
}
