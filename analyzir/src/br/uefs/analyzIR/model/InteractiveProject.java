package br.uefs.analyzIR.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import br.uefs.analyzIR.data.Qrels;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.RunItem;
import br.uefs.analyzIR.data.Topic;
import br.uefs.analyzIR.exception.CreateDirectoryException;
import br.uefs.analyzIR.exception.DiferentsSystemsException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.InvalidRunNumberException;
import br.uefs.analyzIR.exception.InvalidURLException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.RunItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphItem;
import br.uefs.analyzIR.graph.Point;
import br.uefs.analyzIR.graph.bars.BarGraph;
import br.uefs.analyzIR.graph.curves.CurveGraph;

/**
 * Interactibe Project Class;
 * @author nilson
 *
 */
@XStreamAlias("InteractiveProject")
public class InteractiveProject extends Project{
	
	private int protocol;	
	ProjectAnalyzer analyzer;
	Run[] contextRuns;
	ArrayList<Run> runs;
	
	/**
	 * Interactive project constructor 
	 * @param name - project name
	 * @param path - project directory
	 */
	public InteractiveProject (String name, String path){
		super(name, path);
	}
	
	/**
	 * Interactive project constructor 
	 * @param path project directory
	 */
	public InteractiveProject (String path){
		super(path);
	}

	@Override
	public void create(String qrelsPath, String[] runsPath, String cQrelsPath, boolean intersection, int protocol)
			throws InvalidURLException, CreateDirectoryException, FileNotFoundException, IOException,
			QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException, DiferentsSystemsException,
			TopicNotFoundException, InvalidRunNumberException {
		
		//Run[] runs;
		
		
		this.protocol = protocol;
		
		//runs = new Run[runsPath.length];
		runs = new ArrayList<Run>();
		for (int i = 0; i < runsPath.length; i++) {
			runs.add(new Run(runsPath[i]));
			runs.get(i).initialize();
		}
		
		analyzer = new ProjectAnalyzer();

		if (!intersection) {
			analyzer.checkSame(runs);
			config(runs, analyzer, qrelsPath);
		} else {
			if(analyzer.hasIntersection())
				config(runs, analyzer, qrelsPath);
		}
	}

	@Override
	public Project open(String projectPath)
			throws IOException, QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException {
		//Gson gson;
				XStream xStream = new XStream();
				String destiny;
				File data;

				destiny = projectPath + File.separator + "info" + File.separator
						+ "project";
				
				data = new File(destiny);
				
				xStream.processAnnotations(Run.class);
				xStream.processAnnotations(Qrels.class);
				xStream.processAnnotations(Topic.class);
				xStream.processAnnotations(Project.class);
				xStream.processAnnotations(SimpleProject.class);
				xStream.processAnnotations(StandardProject.class);
				xStream.processAnnotations(InteractiveProject.class);
				xStream.processAnnotations(Graph.class);
				xStream.processAnnotations(BarGraph.class);
				xStream.processAnnotations(CurveGraph.class);
				xStream.processAnnotations(GraphItem.class);
				xStream.processAnnotations(Point.class);
				xStream.processAnnotations(InteractiveProject.class);

				if (data.exists()) {

					InteractiveProject system = (InteractiveProject) xStream.fromXML(data);
					system.setPath(projectPath);
					
					String basicPath = projectPath + File.separator + 	"data";
					
					system.getQrels().setPath(basicPath + File.separator + "qrels");
					system.getQrels().initialize();
					for(Run r : system.getRuns()){
						try{
						r.setPath(basicPath + File.separator + r.getName());
						r.initialize();}
						catch(Exception e){
							String auxDirectory = r.getName().substring(0, r.getName().indexOf(":"));
							r.setPath(basicPath + File.separator +  auxDirectory + File.separator + r.getName());
							r.initialize();
						}
					}
					
					
					
					return system;
				}
				throw new FileNotFoundException(
						"Could not find the project.xml file in the project directory. It was deleted or the selected directory is not valid (not a project directory).");
	}
	
	private void saveRuns(ArrayList<Run> runs) throws IOException {
		for (Run run : runs) {
			List<RunItem> items = run.getRunItems();
			File r = new File(run.getPath());
			if (!r.exists()) {
				boolean ok = r.createNewFile();
				
				if (ok) {
					
					FileWriter fw = new FileWriter(r);
					BufferedWriter bw = new BufferedWriter(fw);
					for (RunItem item : items) {
						String line = item.getQuery_id() + "\t" + 0 + "\t"
								+ item.getDocno() + "\t" + item.getRank()
								+ "\t" + item.getSim() + "\t" + run.getName();
						bw.write(line);
						bw.newLine();
					}
					bw.close();
					fw.close();
				}
			}
		}
	}
	/**
	 * Method that separate the runs according to the interaction and protocol
	 * @param runs
	 * @throws IOException
	 */
	private void saveRunsInteration(ArrayList<Run> runs) throws IOException {
		int beforeSizeRun = runs.size();
		
		for (int b = 0; b < beforeSizeRun; b++) {
			Run run = runs.get(b);
			String path =  this.getPath() + File.separator + "data"
					+ File.separator + "run"+b;
			List<RunItem> items = run.getRunItems();
			List<List<RunItem>> interations = new ArrayList<List<RunItem>>();
			
			//Povoando a lista de itens, o índice desta lista conterá todas as consultas naquela interação
			for (int k = 0; k < run.getMaxInteration(); k++){
				List<RunItem> x = new ArrayList<RunItem>();
				interations.add(x);
			}
			//////eu fiz
			for(RunItem item:items){
				System.out.println(item.getQuery_id() + " " + item.getInteration() +" "
						+ item.getDocno() + " " + item.getRank()
						+ " " + item.getSim() + " " + run.getName());
			}
			//////
			//Separando por iteração
			for (RunItem item : items) {				
				interations.get(item.getInteration()-1).add(item);
			}
			
			
			
			
			
			if (protocol == 1){				
				for (int i = 1; i <= run.getMaxInteration(); i++){

					File r = new File(path, run.getName()+"_0"+i);
					if (!r.exists()) {

						boolean ok = r.createNewFile();					
						if (ok) {

							FileWriter fw = new FileWriter(r);
							BufferedWriter bw = new BufferedWriter(fw);						
							for (int k = 1; k <= i; k++){
								for (RunItem item : interations.get(k-1)) {							
									String line = item.getQuery_id() + "\t" + 0 + "\t"
											+ item.getDocno() + "\t" + item.getRank()
											+ "\t" + item.getSim() + "\t" + run.getName();
									//System.out.println(line);
									bw.write(line);
									bw.newLine();			
								}
								
							}
							bw.close();
							fw.close();
						}
					}
					//Criando a run para poder adicionar ao context do projeto;
					Run runAux = new Run(run.getName()+"_0"+i, path + File.separator + run.getName()+"_0"+i);
					runAux.initialize();					
					runs.add(runAux);
				}
				
				
			}else{
				for (int i = 1; i <= run.getMaxInteration(); i++){
					File r = new File(path, run.getName()+"_0"+i);
					//System.out.println("Run: " + run.getName() + i + " Resultado: " + r.exists());
					if (!r.exists()) {
						boolean ok = r.createNewFile();
						//System.out.println("Run: " + run.getName() + i + " Resultado: " + ok);
						if (ok) {						
							FileWriter fw = new FileWriter(r);
							BufferedWriter bw = new BufferedWriter(fw);
							//System.out.println(interations.get(i-1).size());
							for (RunItem item : interations.get(i-1)) {							
									String line = item.getQuery_id() + "\t" + 0 + "\t"
											+ item.getDocno() + "\t" + item.getRank()
											+ "\t" + item.getSim() + "\t" + run.getName();
									//System.out.println(line + "Interação: " + item.getInteration());
									bw.write(line);
									bw.newLine();							
							}
							bw.close();
							fw.close();
						}
					}
					//Criando a run para poder adicionar ao context do projeto;
					Run runAux = new Run(run.getName()+"_0"+i, path + File.separator + run.getName()+"_0"+i);
					runAux.initialize();					
					runs.add(runAux);
				}
			}
		}
	}
	/**
	 * 
	 * @param runs - list of runs 
	 * @param analyzer - created project
	 * @param qrelsPath - qrels directory
	 * @throws InvalidURLException
	 * @throws CreateDirectoryException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws QrelItemNotFoundException
	 * @throws InvalidQrelFormatException
	 * @throws TopicNotFoundException
	 * @throws InvalidRunNumberException
	 */
	private void config(ArrayList<Run> runs, ProjectAnalyzer analyzer, String qrelsPath)
			throws InvalidURLException, CreateDirectoryException,
			FileNotFoundException, IOException, QrelItemNotFoundException,
			InvalidQrelFormatException, TopicNotFoundException, InvalidRunNumberException {
		
		//create new runs 
		List<Topic> topics = analyzer.extractIntersection(runs);
		runs = analyzer.createNewRuns(topics, runs);
		
		//create the interactive directory from created new runs
		this.createInteractiveDirectory(runs);
		String qrelsDestiny = this.getPath() + File.separator + "data"
				+ File.separator + "qrels";
		this.copyFiles(qrelsPath, qrelsDestiny);
		
		//create new qrels
		Qrels qrels = new Qrels(qrelsDestiny);
		qrels.initialize();
		
		
		
		for(Run r : runs){
			
			String runPath = this.getPath() + File.separator + "data"
					+ File.separator + r.getName();
			r.setPath(runPath);
		}
		
		//TODO colocar no lugar desses um setContext (assim fica melhor)
		this.saveRuns(runs);
		this.saveRunsInteration(runs);
		this.setQrels(qrels);
		
		contextRuns = new Run[runs.size()];
		for (int k = 0; k < runs.size(); k++){
			contextRuns[k] = runs.get(k);			
		}
		
		
		this.setRuns(contextRuns);
		
	}

	@Override
	public void create(String qrelsPath, String[] runsPath, String cQrelsPath, boolean intersection)
			throws InvalidURLException, CreateDirectoryException, FileNotFoundException, IOException,
			QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException, DiferentsSystemsException,
			TopicNotFoundException, InvalidRunNumberException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<String> listRuns() {
		List<String> names = new ArrayList<>();
		for (Run run : this.context.getRuns()) {
			String runName = run.getName();
			String aux[] = runName.split("_");
			if (!names.contains(aux[0]+"_"+aux[1])){
				names.add(aux[0]+"_"+aux[1]);
			}
			
		}
		return names;
	}
	

}
