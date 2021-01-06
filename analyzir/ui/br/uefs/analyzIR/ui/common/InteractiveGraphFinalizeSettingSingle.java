package br.uefs.analyzIR.ui.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.EmptyTopicMeasureValueException;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.exception.GraphNotFoundException;
import br.uefs.analyzIR.exception.InvalidDataFormatException;
import br.uefs.analyzIR.exception.InvalidGraphNameException;
import br.uefs.analyzIR.exception.InvalidGraphTypeException;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.exception.MeasureNotFoundException;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.exception.NoGraphPointException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;

public class InteractiveGraphFinalizeSettingSingle implements WizardStep {
	
	private Facade facade;
	private DataInfo info;
	private WizardProcess wProcess;
	
	public InteractiveGraphFinalizeSettingSingle(){
		
	}	
	
	public void initialize (){
		try 
		{	
			
			String atValue = null;
			String [] xValues = null;
			JPanel graph = null;
			
			Object ind = info.getData("index"); //hold the index string value
			Integer index = (ind == null ? -1 : Integer.parseInt(ind.toString()));
			
			List<String> measures = (List<String>) info.getData("measures");
			List<String> runs = (List<String>) info.getData("runs");
			List<String> topics = (List<String>) info.getData("topics");
			String name = info.getData("name").toString();
			String title = info.getData("title").toString();
			
			
			if(facade.getMeasureType(measures) != 0){
				
										
				atValue = "";
				xValues = checkValues(atValue);
				
				info.putData("xValues", xValues);
				info.putData("atValue", atValue);
			}
			
			int type = facade.getMeasureType(measures);
			int graphType = (int) info.getData("type");
			

			if(graphType == 1){					
				info.putData("x-axis", "Topic");
				info.putData("y-axis", info.getData("measures").toString().substring( 1 , info.getData("measures").toString().length()-2) + info.getData("atValue"));
			}else if (graphType == 2 || graphType == 4){
				info.putData("x-axis", "Interation");
				info.putData("y-axis", info.getData("measures").toString().substring( 1 , info.getData("measures").toString().length()-2) + info.getData("atValue"));
			}else if (graphType == 3 || graphType == 5){
				info.putData("x-axis", "Measures");
				info.putData("y-axis", "Value");
			}else if (graphType == 6){
				info.putData("x-axis", "Interation");
				info.putData("y-axis", "Value");
			}
			
			String x_axis = info.getData("x-axis").toString();
			String y_axis = info.getData("y-axis").toString();
			
			if(index == -1)	{
				graph = facade.createGraph(3, title, measures, runs, topics, xValues, name, atValue, x_axis, y_axis, (List<String>) info.getData("interations"), graphType);
				info.putData("view", graph);
				stop();
			}else
			{
				graph = facade.replaceGraph(title, measures, runs, topics, xValues, name, atValue, x_axis, y_axis, graphType, (List<String>) info.getData("interations"));
				info.putData("view", graph);
				stop();
			}			
		
		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InterruptedException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (MeasureNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidGraphTypeException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidDataFormatException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidItemNameException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (ItemNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (EmptyTopicMeasureValueException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (NoGraphItemException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidGraphNameException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (GraphNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (QrelItemNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (TopicNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidQrelFormatException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (GraphItemNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoGraphPointException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
	
	
	private String [] checkValues(String atValue){
		
		List<String> result = new ArrayList<String>();
		String [] values = atValue.split(";");
		
		for(String each : values){
			String [] parts = each.split("-"); 
			if(parts.length == 2){
				double first = Double.parseDouble(parts[0]); 
				double last = Double.parseDouble(parts[1]);
				for(double v = first; v <= last; v += 1){
					result.add(v+"");
				}
			}else{
				result.add(parts[0]);
			}
		}
		String [] v = new String[result.size()];
		v = result.toArray(v);
		
		return v;
	}

	@Override
	public void putDataInfo(DataInfo dataInfo) {
		this.info = dataInfo;
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public JPanel make() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addWizardProcessListener(WizardProcess wProcess) {
		this.wProcess = wProcess;

	}

	@Override
	public void run() {
		initialize();
	}

	@Override
	public void stop() {
		this.notifyWizardProcesses();

	}

	@Override
	public void notifyWizardProcesses() {
		this.wProcess.update();

	}

	@Override
	public void putCommandProcess(Facade facade) {
		this.facade = facade;

	}

}
