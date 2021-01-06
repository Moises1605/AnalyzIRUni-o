package br.uefs.analyzIR.controller.graph;

import java.util.HashMap;
import java.util.List;

import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphFactory;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;


public abstract class GraphMaker {

	private GraphFactory generator;
	
	public GraphMaker(){
		this.generator = new GraphFactory();
	}
	
	public abstract Graph create(HashMap<String, List<MeasureSet>> resultByRun, int measureAmount, String title, String name, String x_axis, String y_axis, List<String> iterations, int graphType) throws
		InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException;


	public GraphFactory getGenerator() {
		return generator;
	}

}
