package br.uefs.analyzIR.controller;

import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Qrels;
import br.uefs.analyzIR.data.Run;

import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.graph.GraphInfo;
import br.uefs.analyzIR.model.StandardProject;
import br.uefs.analyzIR.model.SimpleProject;
import br.uefs.analyzIR.model.DiversityProject;
import br.uefs.analyzIR.model.InteractiveProject;
import br.uefs.analyzIR.model.Project;
import br.uefs.analyzIR.model.ProjectFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProjectController {

	private Project project;
	private ProjectFactory factory;

	public ProjectController() {
		factory = new ProjectFactory();
	}

	// ********************** Basics project operations *******************/

	public Context create(String nameProject, String urlDirectory,
			String qrelsFile, String[] runFile, boolean intersection, String cqrelsPath, int type, int protocol) throws InvalidURLException,
			CreateDirectoryException, FileNotFoundException, IOException,
			QrelItemNotFoundException, InvalidQrelFormatException,
			RunItemNotFoundException, NoGraphItemException,
			InvalidProjectTypeException, DiferentsSystemsException, TopicNotFoundException, InvalidRunNumberException {
		
		
		Context context = null;		
		
		project = factory.getProject(type, nameProject, urlDirectory);
		if (project instanceof InteractiveProject){
			project.create(qrelsFile, runFile,cqrelsPath, intersection, protocol);
		}else{
			project.create(qrelsFile, runFile,cqrelsPath, intersection);
		}
		
		project.save();
		
		context = project.getContext();
		
		return context;

	}

	public Context open(String directory, int type) throws ClassNotFoundException,
			FileNotFoundException, IOException, QrelItemNotFoundException,
			InvalidQrelFormatException, RunItemNotFoundException,
            NoGraphItemException {
		
		if(type == 0){
			this.project = new SimpleProject(directory);
		}else if(type == 1){
			this.project = new StandardProject(directory);
		}else if(type == 2){
			this.project = new DiversityProject(directory);
		}else if (type == 3){
			this.project = new InteractiveProject(directory);
		}
		
		this.project = project.open(directory);
		Context context = this.project.getContext();
		
		return context;
	}

	public void save() throws FileNotFoundException, IOException,
            NoGraphItemException {
		if(this.project == null){
			throw new NoGraphItemException("There is no project. Please, create or open a project.");
		}else{
			this.project.save();
		}
	}

    // ***************************** Project Items operation ******************//Lucas, depois vamos arrumar isso, ok ?*

	public List<String> listTopics() {
		List<String> values =  project.listTopics();
		values.add(0, "AVG");
		return values;
	}

	public List<String> listRuns() {
		return project.listRuns();
	}

	/*public List<String> listWilcoxonBibs() {return project.listWilcoxonBibs();
	}*/

	public Run[] getRuns(String[] runs) {
		return this.project.getRuns(runs);
	}

	public Run[] getRuns() {
		return this.project.getRuns();
	}

	// ****************************** Plot methods in project ******************
	public List<String> listPlots() {
		return this.project.listGraphs();
	}

	public void addGraph(Graph plot) throws InvalidGraphNameException {
		this.project.addGraph(plot);
	}

	public Graph searchGraph(String name) throws GraphNotFoundException {
		return this.project.searchGraph(name);
	}

	public void removeGraph(String nameFilePlot) {
		this.project.removeGraph(nameFilePlot);
	}

	public void replaceGraph(Graph newPlot, String oldPlot) throws InvalidGraphNameException {
		this.project.replaceGraph(newPlot, oldPlot);
	}
	
	public boolean isGraphNameAvailable(String name) {
		return this.project.isGraphNameAvailable(name);
	}

	public HashMap<String, List<String>> getGraphValues(List<String> keys, String namePlot)
			throws GraphNotFoundException {

		HashMap<String, List<String>> values;
		Graph plot;
		GraphInfo maker;

		plot = this.searchGraph(namePlot);
		maker = plot.getGraphInfo();
		values = new HashMap<>();

		for (String key : keys) {
			switch (key) {
			case "MEASURES": {
				values.put("MEASURES", maker.getMeasures());
				break;
			}
			
			case "RUNS":{
				List<String> runs;
				runs = maker.getRuns();
				values.put("RUNS", runs);
				break;
			}
			case "TOPICS": {

				List<String> topics;
				topics = maker.getTopics();
				values.put("TOPICS", topics);
				
				break;
			}
			case "XVALUES": {

				List<String> xValues;
				String[] x;

				xValues = new ArrayList<>();
				x = maker.getXValues();

				if(x != null){
					for (String xvalue : x) {
						xValues.add(xvalue);
					}
				}
				values.put("XVALUES", xValues);
				break;
			}
			case "TITLE": {

				List<String> title;
				title = new ArrayList<>();
				title.add(plot.getTitle());
				values.put("TITLE", title);

				break;
			}
			case "ATVALUE":{
				List<String> atValue =  new ArrayList<String>();
				String at = plot.getGraphInfo().getAtValue();
				atValue.add(at);
				values.put("ATVALUE", atValue);
			}
			}
		}
		
		return values;

	}

	// ****************************** Others operations
	// *************************

	public Qrels getQrels() {
		return this.project.getQrels();
	}

	public int getType() {
		if(project instanceof SimpleProject)
			return 0; 
		else if(project instanceof StandardProject)
			return 1; 
		else if(project instanceof DiversityProject)
			return 2;
		else if (project instanceof InteractiveProject)
			return 3;
		else
            return -1;
    }

	public void close() {
		this.project = null;
	}

	public String[] getResume() {
		
		String [] values = new String[2];
		
		values [0] = this.project.getName();
		values [1] = ""+this.project.getQueriesAmount(); 
		
		return values;
	}
	/**
	 * 
	 * @return highest of all interaction value
	 */
	public List<String> getInterations(){
		return project.getMaxInteration();
	}

}
