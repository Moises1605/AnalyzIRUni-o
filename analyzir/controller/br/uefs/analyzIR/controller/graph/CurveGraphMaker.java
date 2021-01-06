package br.uefs.analyzIR.controller.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphFactory;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.data.MeasureValue;
import br.uefs.analyzIR.measure.data.TopicResult;

public class CurveGraphMaker extends GraphMaker{

	@Override
	public Graph create(HashMap<String, List<MeasureSet>> resultByRun, int measureAmount,

			String title, String name, String x_axis, String y_axis, List<String> iterations, int graphType) throws InvalidGraphTypeException, 

			InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException {
		
		Set<String> runs = resultByRun.keySet();
		
		if(runs.size() == 1){
			return createGraphOneSystem(resultByRun, title, name, x_axis, y_axis);
		}else{
			return createGraphMultiSystem(resultByRun, title, name, x_axis, y_axis);
		}
	}


	private Graph createGraphOneSystem(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis) throws InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException{

		
		Graph graph = this.getGenerator().getPlot(title, "", "", GraphFactory.CURVE_PLOT, name);

		String run = resultByRun.keySet().iterator().next(); 
		List<MeasureSet> result = resultByRun.get(run);
		
		for(MeasureSet resultByMeasure : result){
			
			for(TopicResult topic : resultByMeasure.getTopicResults()){
				
				if(!graph.containsGraphItem(topic.getTopicName())){
					graph.addGraphItem(topic.getTopicName());
				}
				
				List<MeasureValue> values = topic.getValues();
				
				for(MeasureValue value : values){
					graph.addPoint(topic.getTopicName(), value.getX() , value.getValue());
				}
			}

		}
		
		graph.setLabelX(x_axis);
		graph.setLabelY(y_axis);
		
		return graph;
	}

	private Graph createGraphMultiSystem(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis) throws InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException{

		Graph graph = this.getGenerator().getPlot(title, "", "", GraphFactory.CURVE_PLOT, name);
		
		Set<String> keys = resultByRun.keySet(); 
		
		for(String run : keys){
			
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){
				
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					
					String item = run + "[" + topic.getTopicName().replaceAll("all", "AVG") + "]";
					if(!graph.containsGraphItem(item)){
						graph.addGraphItem(item);
					}
					
					List<MeasureValue> values = topic.getValues();
					
					for(MeasureValue value : values){
						graph.addPoint(item, value.getX(), value.getValue());
					}
				}

			}
		}

		
		graph.setLabelX(x_axis);
		graph.setLabelY(y_axis);
		
		return graph;
	}

	

}
