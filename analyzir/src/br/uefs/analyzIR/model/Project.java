package br.uefs.analyzIR.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphInfo;
import br.uefs.analyzIR.graph.bars.BarGraph;
import br.uefs.analyzIR.graph.curves.CurveGraph;
import br.uefs.analyzIR.data.ClusterItem;
import br.uefs.analyzIR.data.ClusterQrels;
import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Qrels;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.Topic;
import br.uefs.analyzIR.exception.CreateDirectoryException;
import br.uefs.analyzIR.exception.DiferentsSystemsException;
import br.uefs.analyzIR.exception.InvalidGraphNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.InvalidRunNumberException;
import br.uefs.analyzIR.exception.InvalidURLException;
import br.uefs.analyzIR.exception.GraphNotFoundException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.RunItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;

import br.uefs.analyzIR.graph.GraphItem;
import br.uefs.analyzIR.graph.Point;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * @author lucas
 *
 */
@XStreamAlias("Project")
public abstract class Project {

	private String name;
	
	@XStreamOmitField
	private String path;
	
	protected Context context;
	protected List<Graph> plots;

	/**
	 * 
	 * @param name
	 * @param path
	 */
	public Project(String name, String path) {
		this.name = name;
		this.path = path;
		this.plots = new ArrayList<>();
		this.context = new Context(null, null, null);
	}

	/**
	 * 
	 * @param directory
	 */
	public Project(String directory) {
		this.path = directory;
		this.context = new Context(null, null, null);
	}

	public abstract void create(String qrelsPath, String[] runsPath, String cQrelsPath,
			boolean intersection) throws InvalidURLException,
			CreateDirectoryException, FileNotFoundException, IOException,
			QrelItemNotFoundException, InvalidQrelFormatException,
			RunItemNotFoundException, DiferentsSystemsException,
			TopicNotFoundException, InvalidRunNumberException;
	
	public abstract void create(String qrelsPath, String[] runsPath, String cQrelsPath,
			boolean intersection, int protocol) throws InvalidURLException,
			CreateDirectoryException, FileNotFoundException, IOException,
			QrelItemNotFoundException, InvalidQrelFormatException,
			RunItemNotFoundException, DiferentsSystemsException,
			TopicNotFoundException, InvalidRunNumberException;

	/**
	 * 
	 * @param projectPath
	 * @return
	 * @throws IOException
	 * @throws QrelItemNotFoundException
	 * @throws InvalidQrelFormatException
	 * @throws RunItemNotFoundException
	 */
	public abstract Project open(String projectPath) throws IOException, QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException;

	/**
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
	
		//Gson gson;
		XStream xStream = new XStream();
		String xml, destiny; 
		File file;
		FileOutputStream fOut;

		//defined the url file 
		destiny = this.getPath() + File.separator + "info" + File.separator + "project";
		
		//defined alias 

		xStream.processAnnotations(Run.class);
		xStream.processAnnotations(Qrels.class);
		xStream.processAnnotations(DiversityProject.class);
		xStream.processAnnotations(Topic.class);
		xStream.processAnnotations(Project.class);
		xStream.processAnnotations(SimpleProject.class);
		xStream.processAnnotations(StandardProject.class);
		xStream.processAnnotations(InteractiveProject.class);
		xStream.processAnnotations(Graph.class);
		xStream.processAnnotations(GraphInfo.class);
		xStream.processAnnotations(BarGraph.class);
		xStream.processAnnotations(CurveGraph.class);
		xStream.processAnnotations(GraphItem.class);
		xStream.processAnnotations(Point.class);
		xStream.processAnnotations(ClusterItem.class);
		xStream.processAnnotations(ClusterQrels.class);
		xStream.processAnnotations(Context.class);
		xStream.processAnnotations(DiversityProject.class);
		

		xml = xStream.toXML(this);
		file = new File(destiny);

		if (!file.exists()) {
			file.createNewFile();
		}

		fOut = new FileOutputStream(file);
		fOut.write(xml.getBytes());
		fOut.close();
	}

	/**
	 * 
	 * @throws InvalidURLException
	 * @throws FileAlreadyExistsException
	 * @throws CreateDirectoryException
	 */
	protected void createDirectory() throws InvalidURLException,
			FileAlreadyExistsException, CreateDirectoryException {

		File file = new File(this.path);

		if (file.exists() && file.isDirectory()) {
			String separator = java.io.File.separator;
			String project = this.path + separator + getName();
			String data = project + separator + "data";
			String aux = project + separator + "info";
			String export = project + separator + "export";
			this.path = project;
			File dirProject = new File(project);

			if (!dirProject.mkdir()) {
				throw new FileAlreadyExistsException(
						"The project "+getName()+" directory could not be created.");
			} else {
				File dirData = new File(data);
				File dirAux = new File(aux);
				File dirExport = new File(export);

				if (!dirData.mkdir()) {
					dirProject.delete();
					throw new CreateDirectoryException(
							"It was not possible to create the DATA directory: "+data+". Please check if the path is correct and the read/write permissions.");
				}
				if (!dirAux.mkdir()) {
					dirData.delete();
					dirProject.delete();
					throw new CreateDirectoryException(
							"It was not possible to create the INFO directory: "+data+". Please check if the path is correct and the read/write permissions.");
				}
				if (!dirExport.mkdir()) {
					dirData.delete();
					dirProject.delete();
					throw new CreateDirectoryException(
							"It was not possible to create the EXPORT directory: "+export+". Please check if the path is correct and the read/write permissions.");
				}
			}
		} else {
			throw new InvalidURLException(
					"This URL is not a directory,  please choose a valid URL.");
		}
	}
	/**
	 * Method to create the interactie diretory
	 * @param runs - list of runs to be organized
	 * @throws InvalidURLException
	 * @throws FileAlreadyExistsException
	 * @throws CreateDirectoryException
	 */
	protected void createInteractiveDirectory(ArrayList<Run> runs) throws InvalidURLException,
	FileAlreadyExistsException, CreateDirectoryException {

		File file = new File(this.path);
		
		if (file.exists() && file.isDirectory()) {
			String separator = java.io.File.separator;
			String project = this.path + separator + getName();
			String data = project + separator + "data";
			String aux = project + separator + "info";
			String export = project + separator + "export";
			String[] runsDir = new String[runs.size()];
			for (int i = 0; i < runs.size(); i++){
				runsDir[i] = data + separator + "run" +i;				
			}
			this.path = project;
			File dirProject = new File(project);
		
			if (!dirProject.mkdir()) {
				throw new FileAlreadyExistsException(
						"The project "+getName()+" directory could not be created.");
			} else {
				File dirData = new File(data);
				File dirAux = new File(aux);
				File dirExport = new File(export);				
				
		
				if (!dirData.mkdir()) {
					dirProject.delete();
					throw new CreateDirectoryException(
							"It was not possible to create the DATA directory: "+data+". Please check if the path is correct and the read/write permissions.");
				}
				if (!dirAux.mkdir()) {
					dirData.delete();
					dirProject.delete();
					throw new CreateDirectoryException(
							"It was not possible to create the INFO directory: "+aux+". Please check if the path is correct and the read/write permissions.");
				}
				if (!dirExport.mkdir()) {
					dirData.delete();
					dirProject.delete();
					throw new CreateDirectoryException(
							"It was not possible to create the EXPORT directory: "+export+". Please check if the path is correct and the read/write permissions.");
				}
				
				File[] dirRuns = new File[runs.size()];
				for (int j = 0; j < runs.size(); j++){
					dirRuns[j] = new File(runsDir[j]);
					if (!dirRuns[j].mkdir()) {
						dirData.delete();
						dirProject.delete();
						throw new CreateDirectoryException(
								"It was not possible to create the RUN directory: "+dirRuns[j]+". Please check if the path is correct and the read/write permissions.");
					}
				}
			}
		} else {
			throw new InvalidURLException(
					"This URL is not a directory,  please choose a valid URL.");
		  }
	}

	protected void copyFiles(String urlSource, String urlDestiny)
			throws FileNotFoundException, IOException {

		FileInputStream source;
		FileOutputStream destiny;
		FileChannel sourceChannel;
		FileChannel destinyChannel;
		String file = new File(urlSource).getName();

		try {
			source = new FileInputStream(urlSource);
			destiny = new FileOutputStream(urlDestiny);
			
			sourceChannel = source.getChannel();
			destinyChannel = destiny.getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinyChannel);
			source.close();
			destiny.close();
		} catch (FileNotFoundException erro) {
			throw new FileNotFoundException(
					"The file "+file+" was not found. Please confirm if the provided file path is correct.");
		} catch (IOException erro) {
			throw new IOException(
					"A problem occurred during the transfer of file "+file+" to directory "+urlDestiny+". Please check the file/directory read/write permissions.");
		}
	}
	
	/***
	 * 
	 * @param p - graph to be added
	 * @throws InvalidGraphNameException
	 */
	public void addGraph(Graph p) throws InvalidGraphNameException {
		
		//Checks if the graph name is already in use and remove the old graph with the same name
		for (Graph plot : plots) {
			if (plot.getName().equals(p.getName())) {
				//removeGraph(p.getName());
				throw new InvalidGraphNameException("The project already includes a graph named "+p.getName()+". Please enter another graph name.");
			}			
		}			
		
		this.plots.add(p);
	}

	/**
	 * 
	 * @param name - gaph name to be deleted
	 */
	public void removeGraph(String name) {
		for (Graph p : plots) {
			if (p.getName().equals(name)) {
				plots.remove(p);
				break;
			}
		}
	}

	/**
	 * 
	 * @param p
	 * @param name
	 * @throws InvalidGraphNameException
	 */
	public void replaceGraph(Graph p, String oldPlot) throws InvalidGraphNameException {
		removeGraph(oldPlot);
		plots.add(p);
	}
	
	public boolean isGraphNameAvailable(String name){
		for (int i = 0; i < plots.size(); i++)
			if (((Graph) plots.get(i)).getName().equals(name))
				return false;
			
		return true;
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws GraphNotFoundException
	 */
	public Graph searchGraph(String name) throws GraphNotFoundException {
		for (Graph p : plots) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		throw new GraphNotFoundException("The graph "+name+" was removed!");
	}

	/**
	 * 
	 * @return
	 */
	public List<String> listGraphs() {
		List<String> names = new ArrayList<String>();
		for (Graph plot : plots) {
			names.add(plot.getName());
		}
		return names;
	}

	// ******************** Values project *************************

	/**
	 * 
	 * @return
	 */
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path){
		this.path = path;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public List<String> listRuns() {
		List<String> names = new ArrayList<>();
		for (Run run : this.context.getRuns()) {
			names.add(run.getName());
		}
		Collections.sort(names);
		return names;
	}

	public Run[] getRuns(String[] names) {
		Run[] temp = new Run[names.length];
		Run[] runs = this.context.getRuns();
		for (int i = 0; i < names.length; i++) {
			for (int j = 0; j < runs.length; j++) {
				if (names[i].equals(runs[j].getName())) {
					temp[i] = runs[j];
				}
			}
		}
		return temp;
	}

	public Run [] getRuns(){
		return this.context.getRuns();
	}

	public void setRuns(Run[] newRuns) {
		if (newRuns != null)
			this.context.setRuns(newRuns);
	}

	public List<String> listTopics() {

		List<String> topics = new ArrayList<>();
		Run [] runs = this.context.getRuns();
		
 		for (Run run : runs) {
			List<Topic> temp = run.getTopics();
			for (Topic topic : temp) {
				topics.add(topic.getName());
			}
		}

		Collections.sort(topics);
		return topics;
	}

	public Qrels getQrels() {
		return this.context.getQrels();
	}

	public void setQrels(Qrels qrels) {
		this.context.setQrels(qrels);
	}

	public int getQueriesAmount() {
		return this.context.getRuns()[0].getTopics().size()-1;
	}
	

	public Context getContext() {
		return context;
	}
	/**
	 * 
	 * @return the highest of all interaction value
	 */
	public List <String> getMaxInteration(){
		int maxInteration = 0;
		List<String> interations = new ArrayList<String>();
		for (Run run : this.context.getRuns()){
			if (run.getMaxInteration() > maxInteration){
				maxInteration = run.getMaxInteration();
			}
		}
		
		for (int i = 1; i <= maxInteration; i++){
			interations.add(""+i);
		}
		return interations;
	}

}