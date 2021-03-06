package br.uefs.analyzIR.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphItem;
import br.uefs.analyzIR.graph.bars.BarGraph;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import br.uefs.analyzIR.graph.curves.CurveGraph;
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
import br.uefs.analyzIR.graph.Point;

@XStreamAlias("StandardProject")
public class StandardProject extends Project {

	public StandardProject(String name, String path) {
		super(name, path);
	}

	public StandardProject(String directory) {
		super(directory);
	}

	@Override
	public void create(String qrelsPath, String[] runsPath, String cQrelsPath, boolean intersection)
			throws InvalidURLException, CreateDirectoryException,
			FileNotFoundException, IOException, QrelItemNotFoundException,
			InvalidQrelFormatException, RunItemNotFoundException,
			DiferentsSystemsException, TopicNotFoundException, InvalidRunNumberException {

		
		Run[] runs;
		ProjectAnalyzer analyzer;

		runs = new Run[runsPath.length];

		for (int i = 0; i < runs.length; i++) {
			runs[i] = new Run(runsPath[i]);
			runs[i].initialize();
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
	public Project open(String systemPath) throws IOException, QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException {

		//Gson gson;
		XStream xStream = new XStream();
		String destiny;
		File data;

		destiny = systemPath + File.separator + "info" + File.separator
				+ "project";
		
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

		if (data.exists()) {

			StandardProject system = (StandardProject) xStream.fromXML(data);
			system.setPath(systemPath);
			
			String basicPath = systemPath + File.separator + 	"data";
			
			system.getQrels().setPath(basicPath + File.separator + "qrels");
			system.getQrels().initialize();
			for(Run r : system.getRuns()){
				r.setPath(basicPath + File.separator + r.getName());
				r.initialize();
			}
			
			
			return system;
		}
		throw new FileNotFoundException(
				"Could not find the project.xml file in the project directory. It was deleted or the selected directory is not valid (not a project directory).");
	}


	

	private void saveRuns(Run[] runs) throws IOException {
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

	private void config(Run[] runs, ProjectAnalyzer analyzer, String qrelsPath)
			throws InvalidURLException, CreateDirectoryException,
			FileNotFoundException, IOException, QrelItemNotFoundException,
			InvalidQrelFormatException, TopicNotFoundException, InvalidRunNumberException {

		this.createDirectory();
		String qrelsDestiny = this.getPath() + File.separator + "data"
				+ File.separator + "qrels";
		this.copyFiles(qrelsPath, qrelsDestiny);

		Qrels qrels = new Qrels(qrelsDestiny);
		qrels.initialize();
		
		List<Topic> topics = analyzer.extractIntersection(runs);
		runs = analyzer.createNewRuns(topics, runs);
		
		for(Run r : runs){
			
			String runPath = this.getPath() + File.separator + "data"
					+ File.separator + r.getName();
			r.setPath(runPath);
		}
		
		//TODO colocar no lugar desses um setContext (assim fica melhor)
		this.saveRuns(runs);
		this.setQrels(qrels);
		this.setRuns(runs);
		
	}

	@Override
	public void create(String qrelsPath, String[] runsPath, String cQrelsPath, boolean intersection, int protocol)
			throws InvalidURLException, CreateDirectoryException, FileNotFoundException, IOException,
			QrelItemNotFoundException, InvalidQrelFormatException, RunItemNotFoundException, DiferentsSystemsException,
			TopicNotFoundException, InvalidRunNumberException {
		// TODO Auto-generated method stub
		
	}
}
