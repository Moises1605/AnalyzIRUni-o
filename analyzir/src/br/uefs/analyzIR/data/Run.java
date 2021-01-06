package br.uefs.analyzIR.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.analysis.function.Max;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import br.uefs.analyzIR.exception.TopicNotFoundException;

/**
 * A Run object is a machine research result organized using the standard, NIST evaluation procedures. 
 * The standard is the same use by TRECEval tool. A object run contains the main information: queries,
 * {@link RunItem} object (represents the line of a run file), name result and path. A Run object always has 
 * a run file related. 
 * @see Qrels
 * @see QrelItem
 * @version 1.0  
 * @author lucas
 *
 */
@XStreamAlias("Run")
public class Run {

	// hold the information the topics in a run.
	private List<Topic> topics;
	
	// hold all lines of the run File. Format of data: <Key => number of Topic,
	// MeasureValue => Line of the run File.>
	
	@XStreamOmitField
	private HashMap<String, List<RunItem>> items;
	
	// It choose for user or read of the Run File;
	private String name;
	
	@XStreamOmitField
	private String path;
	
	//the highest of all interaction value 
	private int maxInteration;

	/**
	 * Initialize a Run object with run file path. 
	 * @param pathRunFile
	 */
	public Run(String pathRunFile) {
		this.path = pathRunFile;
		this.items = new HashMap<>();
		this.topics = new ArrayList<Topic>();
		this.maxInteration = 0;
	}

	/**
	 * Initialize a Run object with a name and a run file path. 
	 * @param name
	 * @param pathRunFile
	 */
	public Run(String name, String pathRunFile) {
		setName(name);
		this.path = pathRunFile;
		this.items = new HashMap<>();
		this.topics = new ArrayList<Topic>();		
	}

	/**
	 * Construct a new Run object. 
	 */
	public Run() {
		this.items = new HashMap<>();
		this.topics = new ArrayList<Topic>();		
	}

	/**
	 * Returns a formatted run file path. 
	 * @return String - formatted run file path
	 */
	public String getPathRunFile() {
		String p = this.path;
		 p = p.replace(" ", "\\ ");
		return p;
	}
	
	
	/**
	 * Returns the real run file path. 
	 * @return String - real run file path. 
	 */
	public String getPath(){
		return this.path;
	}

	/**
	 * Initialize the rne of a run file),un object with run file information. This method create the RunItem and Topic 
	 * objects. 
	 * 
	 * @throws FileNotFoundException - If the path specified when created the run object is not a file. 
	 * @throws IOException - Problems during read run file.  
	 */
	public void initialize() throws FileNotFoundException, IOException{
		this.items = new HashMap<String, List<RunItem>>();
		this.topics = new ArrayList<Topic>();
		loadRunItems(new File(this.path));
		loadTopics();
	}	
	

	/**
	 * Add a new query result. 
	 * 
	 * @param topic - RunItem list.
	 * @param item - topic name.
	 */
	public void addRunItems(String topic, List<RunItem> item) {
		if (!items.containsKey(topic)) {
			items.put(topic, item);
		}
	}
	
	/**
	 * Sets a new run name. 
	 * @param name  a new run name.
	 */
	public void setName(String name) {
		if (((this.getName() == null) || (this.getName().trim().length() == 0))
				&& (name != null && name.trim().length() != 0))
			this.name = name;
	}

	/**
	 * Returns the run name.
	 * @return String  run name. 
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns a RunItem list. 
	 * @param topicNumber
	 * @return runItem list by topic.
	 * @throws TopicNotFoundException
	 */
	public List<RunItem> getRunItems(String topicNumber)
			throws TopicNotFoundException {
		
		if (items.containsKey(topicNumber)) {
			return items.get(topicNumber);
		} else {
			throw new TopicNotFoundException(
					"The topic "+topicNumber+" does not exist.");
		}
	}
	
	/**
	 * Return all RunItems in a list. 
	 * @return RunItem list. 
	 */
	public List<RunItem> getRunItems(){
		List<RunItem> items = new ArrayList<>(); 
		for(String topic : this.items.keySet()){
			if(!topic.equals("AVG"))
				items.addAll(this.items.get(topic));
		}
		return items;
	}

	/**
	 * Returns a topic list. 
	 * @return a topic list. 
	 */
	public List<Topic> getTopics() {
		return topics;
	}

	/**
	 * Sets a new topic list.
	 * @param topics - new topics. 
	 */
	public void setTopics(List<Topic> topics){
		this.topics =  topics;
	}
	
	/**
	 * Indicates whether some other object is "equal to" this one. A Run object is "equal to" other if and only if they have the same 
	 * name. 
	 * @param obj Run object 
	 * @return true - if and only if the obj is a Run object and has the same name like to this one. 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Run) {
			Run r = (Run) obj;
			if (this.getName().equals(r.getName()))
				return true;
		}
		return false;
	}

	/**
	 * Sets a new real Run path file. 
	 * @param path - new path.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	public int getMaxInteration() {
		return this.maxInteration;
	}

	public void setMaxInteration(int maxInteration) {
		this.maxInteration = maxInteration;
	}

	/***/
	
	
	private void loadRunItems(File runFile) throws FileNotFoundException,
	IOException {

		FileReader fReader = new FileReader(runFile);
		BufferedReader bReader = new BufferedReader(fReader);
		String line = bReader.readLine();
		String nTopic; // hold temporary number of topic.
		ArrayList<RunItem> itemsAll = new ArrayList<RunItem>();
		
		if (line != null) {
		
			ArrayList<RunItem> itemsByTopic = new ArrayList<>();
			System.out.println(line);
			line = line.trim().replace('\t', ' ');
			line = line.replaceAll("  ", " ");
			line = line.replaceAll("  ", " ");
		
			String[] item = line.split(" ");
		
			String query_id = item[0];
			int interation = Integer.parseInt(item[1]);
			String docno = item[2];
			int rank = Integer.parseInt(item[3]);
			float sim = Float.parseFloat(item[4]);
			
			if (interation > this.maxInteration){
				this.setMaxInteration(interation);
			}
		
			setName(item[5]);
			nTopic = query_id;
		
			RunItem rItem = new RunItem(query_id, interation, docno, rank, sim);
			itemsByTopic.add(rItem);
		
			while ((line = bReader.readLine()) != null) {
				System.out.println(line);
				line = line.trim().replace('\t', ' ');
				line = line.replaceAll("  ", " ");
				line = line.replaceAll("  ", " ");
				
				item = line.split(" ");
				query_id = item[0];
				interation = Integer.parseInt(item[1]);				
				docno = item[2];
				
				rank = Integer.parseInt(item[3]);
				sim = Float.parseFloat(item[4]);
				// Add a new set of runItem by Topic in "items" and creates a
				// new set for a new Topic that was read.
				if (!query_id.equals(nTopic)) {
					items.put(nTopic, itemsByTopic);
					itemsByTopic = new ArrayList<>();
					nTopic = query_id;
				}
				
				if (interation > this.maxInteration){
					this.setMaxInteration(interation);
				}
				
				rItem = new RunItem(query_id, interation, docno, rank, sim);
				itemsByTopic.add(rItem);
				itemsAll.add(rItem);
			}
			// Add the last topic.
			items.put(nTopic, itemsByTopic);
			// if file is empty a new exception is thrown, because doesn't have
			// result for analyze.
			items.put("AVG", itemsAll);
			Set<String> a = items.keySet();
			for(String b:a){
				List<RunItem> it = items.get(b);
				for(RunItem i:it){
					System.out.println(i.getQuery_id() + " " + i.getInteration() +" "
							+ i.getDocno() + " " + i.getRank()
							+ " " + i.getSim() + " " + "key: " + b);
				}
			}
			bReader.close();
			fReader.close();
		} 
		
		
	}

	/***/
	private void loadTopics() {
		
		Topic topic;
		Set<String> temp = items.keySet();
		topics = new ArrayList<Topic>();

		for (String number : temp) {
			topic = new Topic(number);
			topics.add(topic);
		}
	}	
}
