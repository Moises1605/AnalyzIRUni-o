package br.uefs.analyzIR.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.exception.MeasureNotFoundException;
import br.uefs.analyzIR.exception.MeasuresGroupNotFoundException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.measure.MeasureManager;
import br.uefs.analyzIR.measure.data.MeasureSet;

public class MeasureController {

	private MeasureManager manager;
	private Context context; 
	
	public  MeasureController() {
		this.manager = new MeasureManager();
		
	}
	
	public void initMeasures(Context context) throws ClassNotFoundException, 
	InstantiationException, IllegalAccessException, IOException{
		this.manager.init(context);
	}
	
	public String [] addMeasure(String url, String nameFile) throws FileNotFoundException, ClassNotFoundException,
																					InstantiationException, IllegalAccessException, IOException{
		return this.manager.addMeasure(url, nameFile, this.context);
	}
	
	public void removeMeasure(String url, String name){
		this.manager.removeMeasure(url, name);
	}
	
	public List<String[]> listUserMeasures() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		return this.manager.listUserMeasures(); 
	}
	/**
	 * 
	 * @param type - 0 - single, 1- at, 2- curve
	 * @return
	 */
	public List<String> getMeasuresByType(int type){
			return manager.getMeasuresByType(type);
	}
	public List<String> getMeasures(){
		return this.manager.listMeasures();
	}
	
	public List<String> getMeasureGroup(List<String> measures) throws MeasuresGroupNotFoundException{
		return this.manager.listMeasureGroup(measures);
	}
	
	/**
	 * 
	 * @param measures
	 * @return
	 */
	public int getMeasureType(List<String> measures){
		return this.manager.getMeasureGraphGroup(measures);
	}
	
	public int getInteractiveMeasureType(List<String> measures){
		return this.manager.getInteractiveMeasureGraphGroup(measures);
	}
	public HashMap<String, List<MeasureSet>> calculateMeasures(List<String> measures,
			List<String> runs, List<String> topics, String atValue,
			String[] xValues, int type) throws NumberFormatException, IOException, InterruptedException, MeasureNotFoundException, InvalidItemNameException,
			ItemNotFoundException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException {
		
		HashMap<String, List<MeasureSet>> result = new HashMap<String, List<MeasureSet>>();
		String [] t = null;
		
		if(topics.size() == 1 && topics.get(0).equals("AVG")){
			t = null;
		}else{
			t = new String[topics.size()];
			t = topics.toArray(t);
		}
			
		if(type == 0 || type == 1){
			for(String run: runs){
				List<MeasureSet> aux = new ArrayList<MeasureSet>(); 
				for(String measure: measures){
					if(xValues != null){ // if user chose many at values
						for(String x : xValues){
							MeasureSet set = this.manager.calculateMeasure(measure, run, t, x, null);
							aux.add(set);
						}
					}else{
						MeasureSet set = this.manager.calculateMeasure(measure,run, t, atValue,  xValues);
						aux.add(set);
					}
				}
				result.put(run, aux);
			}
		}else if(type == 2){
			for(String run : runs){
				List<MeasureSet> aux = new ArrayList<MeasureSet>(); 
				for(String measure : measures){
					MeasureSet set = this.manager.calculateMeasure(measure, run, t, atValue, xValues);
					aux.add(set);
				}
				result.put(run, aux);
			}
			
		}
		
		return result;
	}

	public String getMeasureDescription(String measureName) throws MeasureNotFoundException {
		String description = manager.getMeasure(measureName).getDescription();
		return description;
	}

	public List<MeasureSet> calculateMeasures(List<String> measures,
			String system, String[] t, String atValue, String[] xValues) throws NumberFormatException, IOException, 
			InterruptedException, MeasureNotFoundException, InvalidItemNameException, ItemNotFoundException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException {
		
		List<MeasureSet> aux = new ArrayList<MeasureSet>();
		for(String measure: measures){
			if(xValues != null){
				for(String x : xValues){
					MeasureSet set = this.manager.calculateMeasure(measure, system, t, x, null);
					aux.add(set);
				}
			}else{
				MeasureSet set = this.manager.calculateMeasure(measure, system, t, atValue,  xValues);
				aux.add(set);
			}
		}

		return aux;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<String> getDiversifyMeasures() {

		return this.manager.listDiversifyMeasure();
	}
}
