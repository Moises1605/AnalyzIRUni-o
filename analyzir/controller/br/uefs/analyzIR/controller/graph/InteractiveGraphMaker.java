package br.uefs.analyzIR.controller.graph;

import java.util.*;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;

import br.uefs.analyzIR.exception.EmptyTopicMeasureValueException;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.exception.InvalidGraphTypeException;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphFactory;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.data.MeasureValue;
import br.uefs.analyzIR.measure.data.TopicResult;
/**
 * Class to draw interactive graphs.
 * 
 * @author nilson
 *
 */
public class InteractiveGraphMaker extends GraphMaker{

	@Override
	public Graph create(HashMap<String, List<MeasureSet>> resultByRun, int measureAmount, String title, String name,
			String x_axis, String y_axis, List<String> iterations, int graphType)
			throws InvalidGraphTypeException, InvalidItemNameException, EmptyTopicMeasureValueException,
			ItemNotFoundException, GraphItemNotFoundException {
		
		
		Set<String> runs = resultByRun.keySet();
		
		if (graphType == 1){	
			return type1(resultByRun, title, name, x_axis, y_axis, iterations);
		}else if (graphType == 2){
			return type2(resultByRun, title, name, x_axis, y_axis, iterations);
		}else if (graphType == 3){
			return type3(resultByRun, title, name, x_axis, y_axis, iterations);
		}else if (graphType == 4){
			return type4(resultByRun, title, name, x_axis, y_axis, iterations);
		}else if (graphType == 5){
			return type5(resultByRun, title, name, x_axis, y_axis, iterations);
		}else if (graphType == 6){
			return type6(resultByRun, title, name, x_axis, y_axis, iterations);
		}else
			return null;
		
	}

	/**
	 * Method to draw interactive graph type 1.
	 * @param resultByRun - data graph
	 * @param title - graph title
	 * @param name - graph name
	 * @param x_axis - label x_axis
	 * @param y_axis - label y-axis
	 * @param iterations - interation amount
	 * @return graph.
	 * @throws InvalidGraphTypeException
	 * @throws EmptyTopicMeasureValueException
	 * @throws GraphItemNotFoundException
	 */
	private Graph type1(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis, List<String> iterations) throws InvalidGraphTypeException, EmptyTopicMeasureValueException, GraphItemNotFoundException{
		
		String measure = "";
		Graph graph = this.getGenerator().getPlot(title, "Topics", "", GraphFactory.BAR_1_PLOT, name);
		Set<String> runs = resultByRun.keySet();
		
		
		for(String run : runs){		
			
			List<MeasureSet> result = resultByRun.get(run);
			
			String item = run.substring(0, run.lastIndexOf("_"));
			graph.addGraphItem(item);
			
			for(MeasureSet resultByMeasure : result){				
				for(TopicResult topic : resultByMeasure.getTopicResults()){					
					MeasureValue value = topic.getValues().get(0);
					graph.addPoint(item, topic.getTopicName(), value.getValue());
				}
				measure = resultByMeasure.getMeasureName();
			}
		}
		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}
	
	/**
	 * Method to draw interactive graph type 2.
	 * @param resultByRun - data graph
	 * @param title - graph title
	 * @param name - graph name
	 * @param x_axis - label x_axis
	 * @param y_axis - label y-axis
	 * @param iterations - interation amount
	 * @return graph.
	 * @throws InvalidGraphTypeException
	 * @throws EmptyTopicMeasureValueException
	 * @throws GraphItemNotFoundException
	 */
	private Graph type2(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis, List<String> iterations) throws InvalidGraphTypeException, EmptyTopicMeasureValueException, GraphItemNotFoundException{
		Graph graph = this.getGenerator().getPlot(title, "", "", GraphFactory.CURVE_PLOT, name);


		Set<String> keys = resultByRun.keySet();
		//mundança
		List<String> keysAux = new ArrayList<String>();
		for(String key:keys){
			keysAux.add(key);
		}
		//mudei o keys por keysAux
		for(String run : keysAux){
			
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){
				
				TopicResult topic = resultByMeasure.getTopicResults()[0];				
					
					String item = run.substring(0, run.lastIndexOf("_"));					
					if(!graph.containsGraphItem(item)){
						graph.addGraphItem(item);
					}
					
					//String xValue = run.substring(run.length()-1);
					String xValue = run.substring(run.lastIndexOf("_")+2);
					List<MeasureValue> values = topic.getValues();
					
					for(MeasureValue value : values){
						graph.addPoint(item, xValue, value.getValue());

					}

				
			}
		}
		
		
		graph.setLabelX(x_axis);
		graph.setLabelY(y_axis);
		
		return graph;
	}
	
	/**
	 * Method to draw interactive graph type 3.
	 * @param resultByRun - data graph
	 * @param title - graph title
	 * @param name - graph name
	 * @param x_axis - label x_axis
	 * @param y_axis - label y-axis
	 * @param iterations - interation amount
	 * @return graph.
	 * @throws InvalidGraphTypeException
	 * @throws EmptyTopicMeasureValueException
	 * @throws GraphItemNotFoundException
	 */
	private Graph type3(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis, List<String> iterations) throws InvalidGraphTypeException, EmptyTopicMeasureValueException, GraphItemNotFoundException{
		
		Graph graph = this.getGenerator().getPlot(title, "Measures", "Result", GraphFactory.BAR_1_PLOT, name);
		Set<String> runs = resultByRun.keySet();
		//mundança
		List<String> keysAux = new ArrayList<String>();
		for(String key:runs){
			keysAux.add(key);
		}
		//mudei o runs por keysAux
		
		for(String run : keysAux){
			
			List<MeasureSet> result = resultByRun.get(run); 
			String item = run.substring(0, run.lastIndexOf("_"));
			if(!graph.containsGraphItem(item)){
				graph.addGraphItem(item);
			}
			for(MeasureSet resultByMeasure : result){				
				for(TopicResult topic : resultByMeasure.getTopicResults()){				
					MeasureValue value = topic.getValues().get(0);
					graph.addPoint(item, resultByMeasure.getMeasureName(), value.getValue());
				}
			}
		}
		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}
	
	/**
	 * Method to draw interactive graph type 4.
	 * @param resultByRun - data graph
	 * @param title - graph title
	 * @param name - graph name
	 * @param x_axis - label x_axis
	 * @param y_axis - label y-axis
	 * @param iterations - interation amount
	 * @return graph.
	 * @throws InvalidGraphTypeException
	 * @throws EmptyTopicMeasureValueException
	 * @throws GraphItemNotFoundException
	 */
	private Graph type4(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis, List<String> iterations) throws InvalidGraphTypeException, EmptyTopicMeasureValueException, GraphItemNotFoundException{
		
		Graph graph = this.getGenerator().getPlot(title, "", "", GraphFactory.CURVE_PLOT, name);	
		Set<String> runs = resultByRun.keySet();


		for(String run : runs){			
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){				
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					String item = "topic" + topic.getTopicName();
					if(!graph.containsGraphItem(item)){
						graph.addGraphItem(item);
					}					
					String xValue = run.substring(run.length()-1);
					//String xValue = run.substring(run.lastIndexOf("_")+2);
					MeasureValue value = topic.getValues().get(0);					
					graph.addPoint(item, xValue, value.getValue());

				}
			}
		}
		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}
	
	/**
	 * Method to draw interactive graph type 5.
	 * @param resultByRun - data graph
	 * @param title - graph title
	 * @param name - graph name
	 * @param x_axis - label x_axis
	 * @param y_axis - label y-axis
	 * @param iterations - interation amount
	 * @return graph.
	 * @throws InvalidGraphTypeException
	 * @throws EmptyTopicMeasureValueException
	 * @throws GraphItemNotFoundException
	 */
	private Graph type5(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis, List<String> iterations) throws InvalidGraphTypeException, EmptyTopicMeasureValueException, GraphItemNotFoundException{
		Graph graph = this.getGenerator().getPlot(title, "Measures", "Result", GraphFactory.BAR_1_PLOT, name);
		Set<String> runs = resultByRun.keySet(); 
		
		for(String run : runs){
			
			List<MeasureSet> result = resultByRun.get(run); 			
			for(MeasureSet resultByMeasure : result){				
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					String item = "topic" + topic.getTopicName();
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
	
	/**
	 * Method to draw interactive graph type 6.
	 * @param resultByRun - data graph
	 * @param title - graph title
	 * @param name - graph name
	 * @param x_axis - label x_axis
	 * @param y_axis - label y-axis
	 * @param iterations - interation amount
	 * @return graph.
	 * @throws InvalidGraphTypeException
	 * @throws EmptyTopicMeasureValueException
	 * @throws GraphItemNotFoundException
	 */
	private Graph type6(HashMap<String, List<MeasureSet>> resultByRun,
			String title, String name, String x_axis, String y_axis, List<String> iterations) throws InvalidGraphTypeException, EmptyTopicMeasureValueException, GraphItemNotFoundException{
		
		Graph graph = this.getGenerator().getPlot(title, "", "", GraphFactory.CURVE_PLOT, name);	
		Set<String> runs = resultByRun.keySet();

		for(String run : runs){			
			List<MeasureSet> result = resultByRun.get(run); 
			
			for(MeasureSet resultByMeasure : result){
				String item = resultByMeasure.getMeasureName();
				if(!graph.containsGraphItem(item)){
					graph.addGraphItem(item);
				}
				for(TopicResult topic : resultByMeasure.getTopicResults()){
					
					
					String xValue = run.substring(run.length()-1);
					//String xValue = run.substring(run.lastIndexOf("_")+2);
					MeasureValue value = topic.getValues().get(0);
					
					graph.addPoint(item, xValue, value.getValue());
				}
			}
		}		
		graph.setLabelY(y_axis);
		graph.setLabelX(x_axis);
		
		return graph;
	}


}
