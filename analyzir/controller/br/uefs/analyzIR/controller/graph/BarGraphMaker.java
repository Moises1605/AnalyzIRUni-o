package br.uefs.analyzIR.controller.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.uefs.analyzIR.graph.GraphFactory;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.data.MeasureValue;
import br.uefs.analyzIR.measure.data.TopicResult;

public class BarGraphMaker extends GraphMaker{

	@Override
	public Graph create(HashMap<String, List<MeasureSet>> resultByRun, int measureAmount,

			String title, String name, String x_axis, String y_axis, List<String> iterations, int graphType) throws InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException {

						
		
		Set<String> runs = resultByRun.keySet();
		List<MeasureSet> result = resultByRun.get(runs.iterator().next());
		
		if(result.size() == 1){
			return createGraphOneMeasure(resultByRun, title, name,x_axis, y_axis);
		}else{
			if(measureAmount == 1){
				return createGraphOneMeasureSystem(resultByRun, title, name, x_axis, y_axis);
			}else {
				if(runs.size() == 1){
					return createGraphMultiMeasure(resultByRun, title, name, x_axis, y_axis);
				}else{
					return createGraphMultiMeasureSystem(resultByRun, title, name, x_axis, y_axis);
				}
			}
		}
	}



	private Graph createGraphOneMeasureSystem(
			HashMap<String, List<MeasureSet>> resultByRun, String title,
			String name, String x_axis, String y_axis) throws InvalidGraphTypeException, EmptyTopicMeasureValueException, InvalidItemNameException, ItemNotFoundException, GraphItemNotFoundException {
	
		String measure = "";
		Graph graph = this.getGenerator().getPlot(title, "", "", GraphFactory.BAR_1_PLOT, name);
		Set<String> runs = resultByRun.keySet(); 
		
		for(String run : runs){
			
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){
				
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					
					String item = run + "[" + topic.getTopicName().replaceAll("all", "AVG") + "]";
					if(!graph.containsGraphItem(item)){
						graph.addGraphItem(item);
					}
					MeasureValue value = topic.getValues().get(0);
					graph.addPoint(item, value.getX(), value.getValue());
				}
				
				measure = resultByMeasure.getMeasureName();
			}
		}
		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}

	private Graph createGraphOneMeasure(HashMap<String, List<MeasureSet>> resultByRun,
										String title, String name, String x_axis, String y_axis) throws InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException {
		
		String measure = "";
		Graph graph = this.getGenerator().getPlot(title, "Topics", "", GraphFactory.BAR_1_PLOT, name);
		Set<String> runs = resultByRun.keySet(); 
		
		for(String run : runs){
			
			graph.addGraphItem(run);
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){
				
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					
					MeasureValue value = topic.getValues().get(0);
					graph.addPoint(run, topic.getTopicName(), value.getValue());
				}
				measure = resultByMeasure.getMeasureName();
			}
		}
		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}
	
	private Graph createGraphMultiMeasure(HashMap<String, List<MeasureSet>> resultByRun,
										  String title, String name, String x_axis, String y_axis) throws InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException {
		
		Graph graph = this.getGenerator().getPlot(title, "Measures", "Result", GraphFactory.BAR_1_PLOT, name);
		Set<String> runs = resultByRun.keySet(); 
		
		for(String run : runs){
			
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){
				
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					
					if(!graph.containsGraphItem(topic.getTopicName())){
						graph.addGraphItem(topic.getTopicName());
					}
					MeasureValue value = topic.getValues().get(0);
					graph.addPoint(topic.getTopicName(), resultByMeasure.getMeasureName(), value.getValue());
				}
			}
		}
		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}
	
	/**
	 * Create a new Graph. This method use
	 * @param y_axis 
	 * @param x_axis 
	 * @throws GraphItemNotFoundException
	 * */
	private Graph createGraphMultiMeasureSystem(HashMap<String, List<MeasureSet>> resultByRun,
												String title, String name, String x_axis, String y_axis) throws InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException, ItemNotFoundException, GraphItemNotFoundException {
		
		Graph graph = this.getGenerator().getPlot(title, "Measures", "Result", GraphFactory.BAR_1_PLOT, name);
		Set<String> runs = resultByRun.keySet(); 
		
		for(String run : runs){
			
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){
				
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					
					String item = run + "[" + topic.getTopicName().replaceAll("all", "AVG") + "]";
					if(!graph.containsGraphItem(item)){
						graph.addGraphItem(item);
					}
					MeasureValue value = topic.getValues().get(0);
					graph.addPoint(item, resultByMeasure.getMeasureName(), value.getValue());
				}
			}
		}
		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}
	
}
