package br.uefs.analyzIR.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import br.uefs.analyzIR.graph.GraphItem;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.curves.CurveGraph;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import br.uefs.analyzIR.data.Qrels;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.Topic;
import br.uefs.analyzIR.exception.CreateDirectoryException;
import br.uefs.analyzIR.exception.DiferentsSystemsException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.InvalidRunNumberException;
import br.uefs.analyzIR.exception.InvalidURLException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.RunItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.graph.Point;
import br.uefs.analyzIR.graph.bars.BarGraph;

@XStreamAlias("SingleProject")
public class SimpleProject extends Project {

	public SimpleProject(String name, String path) {
		super(name, path);
	}

	public  SimpleProject(String path) {
		super(path);
	}

	@Override
	public void create(String qrelsPath, String[] runsPath, String cQrelsPath, boolean intersection)
			throws InvalidURLException, CreateDirectoryException,
			FileNotFoundException, IOException, QrelItemNotFoundException,
			InvalidQrelFormatException, RunItemNotFoundException {

		String qrelsDestiny, runPathDestiny;
		Run run;
		Qrels qrels;

		this.createDirectory();
		
		qrelsDestiny = this.getPath() + File.separator + "data" + File.separator + "qrels";
		runPathDestiny = this.getPath() + File.separator + "data" + File.separator + "run";
		
		this.copyFiles(qrelsPath, qrelsDestiny);
		this.copyFiles(runsPath[0], runPathDestiny);

		qrels = new Qrels(qrelsDestiny);
		qrels.initialize();

		run = new Run(runPathDestiny);
		run.initialize();

		Run[] runs = { run };

		this.setQrels(qrels);
		this.setRuns(runs);
	}

	@Override
	public Project open(String systemPath) throws IOException, QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException {
		

		XStream xStream = new XStream();
		String destiny;
		File data; 
		
		destiny = systemPath + File.separator + "info" + File.separator + "project"; 
		data = new File(destiny);
	
		xStream.processAnnotations(Run.class);
		xStream.processAnnotations(Qrels.class);
		xStream.processAnnotations(Topic.class);
		xStream.processAnnotations(Project.class);
		xStream.processAnnotations(SimpleProject.class);
		xStream.processAnnotations(StandardProject.class);
		xStream.processAnnotations(Graph.class);
		xStream.processAnnotations(BarGraph.class);
		xStream.processAnnotations(CurveGraph.class);
		xStream.processAnnotations(GraphItem.class);
		xStream.processAnnotations(Point.class);
		
		if(data.exists()){
			
			SimpleProject system = (SimpleProject) xStream.fromXML(data);
			system.setPath(systemPath);
			
			String basicPath = systemPath + File.separator + "data";
			
			system.getQrels().setPath(basicPath + File.separator + "qrels");
			system.getQrels().initialize();
			Run r = system.getRuns()[0];
			r.setPath(basicPath + File.separator + "run");
			r.initialize();
		
			return system;
		}
		throw new FileNotFoundException("Could not find the project.xml file in the project directory. It was deleted or the selected directory is not valid (not a project directory).");
	}

	@Override
	public void create(String qrelsPath, String[] runsPath, String cQrelsPath, boolean intersection, int protocol)
			throws InvalidURLException, CreateDirectoryException, FileNotFoundException, IOException,
			QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException, DiferentsSystemsException,
			TopicNotFoundException, InvalidRunNumberException {
		// TODO Auto-generated method stub
		
	}

}
