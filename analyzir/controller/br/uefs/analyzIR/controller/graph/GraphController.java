package br.uefs.analyzIR.controller.graph;

import java.util.HashMap;
import java.util.List;

import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.exception.EmptyTopicMeasureValueException;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidGraphTypeException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;

public class GraphController {
	
	public GraphController(){
	}
	
	

	public Graph create(int type, HashMap<String, List<MeasureSet>> resultByRun, int measureAmount, String title, String name, String x_axis, String y_axis, int graphType) throws
	InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException{

		
		
				GraphMaker maker = GraphMakerFactory.getMaker(type);
		
		Graph graph = maker.create(resultByRun,measureAmount, title, name, x_axis, y_axis, null, graphType);
		
		return graph;
	}

}
