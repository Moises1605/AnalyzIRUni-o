package br.uefs.analyzIR.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/***
 * ClusterQrels represents growth truth file for diversity. A ClusterQrels (cQrels) contains all aspects that a document is 
 * relevant in a query. cQrels gives two methods to return the number of aspects of a document is relevant and the total of aspects 
 * in cQrels file.
 * 
 * @see Qrels
 * @see ClusterItem
 *
 */
public class ClusterQrels {
	
	/**Hold cluster file lines information	 */
	private HashMap<String, ClusterItem> items;
	/** Hold the cluster in cluster file for all topics*/
	private Set<String> clusters; 
	/** Save cluster file path*/
	private String path; 
	
	// methods
	/*
	 * initialize 
	 * loaditems - ler o arquivo 
	 * 
	 * 
	 * */
	
	/**
	 * Initialize a cluster qrels with cQrels file URL. 
	 * @param path cQrels file URL. 
	 */
	public ClusterQrels(String path){
		this.path = path;
		this.clusters = new HashSet<String>();
		this.items = new HashMap<>();
	}
	
	/**
	 * Returns cluster Qrels path.
	 * @return cluster qrels path. 
	 */
	public String getPath(){
		return this.path;
	}
	
	/**
	 * Initialize cluster qrels data from cqrels file. 
	 * @throws IOException *
	 * 
	 */
	public void initialize() throws IOException{
		File cqrels = new File(this.path);
		loadItems(cqrels);
	}
	
	/**
	 * Returns clusters amount for a docno in a topic. 
	 * 
	 * @param topic topic number
	 * @param docno document number
	 * @return clusters amount for docno in a topic. 
	 */
	public int getClusterNumber(String topic, String docno){
		String key = topic + "#" + docno;
		int size = this.items.get(key).getClusters().length;
		return size;
	}
	
	public ClusterItem getClusterItem(String topic, String docno){
		String key = topic + "#" + docno;
		return this.items.get(key);
	}
	/***
	 * Returns all cluster number.
	 * @return 0 - no clusters. 
	 * size - all cluster in cluster qrels. 
	 */
	public int getAllClusterNumber(){
		return this.clusters.size();
	}
	
	/**
	 * Reads cluster qrels file and load the information in cluster items objects. 
	 *  @param cQrels - cluster qrels file
	 * @throws IOException - reads problems
	 */
	private void loadItems(File cQrels) throws IOException{
		
		 // hold the clusters for a document in a topic, that means, give a topic and a docno you know which clusters the document is relevant  
		ArrayList<String> docno_clusters = null;
		// represent, temporarily, the cluster file lines
		HashMap<String, ArrayList<String>> temp_items = null; 
		String topic; // topic number
		String docno; // document number
		String key; // key = topic#docno
		String old_key; // hold a old key
		
		FileReader fReader = new FileReader(cQrels);
		@SuppressWarnings("resource")
		BufferedReader bReader = new BufferedReader(fReader);
				
		String line = bReader.readLine();

		// if there is the first line 
		if(line != null){
			
			temp_items = new HashMap<>(); // 
			docno_clusters = new ArrayList<String>();
			String [] splitted_line = null;
			
			line = line.trim().replace("\t", " ");
			splitted_line = line.split(" ");
			
			topic = splitted_line[0];
			docno = splitted_line[2];
			docno_clusters.add(splitted_line[1]);
			
			this.clusters.add(splitted_line[1]);
			
			key = topic + "#" + docno;// key value 
			
			temp_items.put(key, docno_clusters);
			
			old_key = key; 
			
			while((line = bReader.readLine()) != null){
				
				line = line.trim().replace("\t", " ");
				splitted_line = line.split(" ");
				
				topic = splitted_line[0];
				docno = splitted_line[2];
				
				this.clusters.add(splitted_line[1]);
				
				key = topic + "#" + docno;
				
				// if the new key is the same add else create a new list and put in the hash. 
				if(old_key.equals(key))
					docno_clusters.add(splitted_line[1]);
				else{
					docno_clusters = new ArrayList<String>();
					docno_clusters.add(splitted_line[2]);
					temp_items.put(key, docno_clusters);
				}
				old_key = key; 	
			}
		}
		
		createItems(temp_items);
		
	}


	/**
	 * Create cluster items object and put in items attribute . 
	 * @param temp_items values read from cluster qrels file
	 * 
	 * */
	private void createItems(HashMap<String, ArrayList<String>> temp_items) {
		
		Set<String> keys = temp_items.keySet(); 
		
		//for each key (topic#docno) create a ClusterItem object with a cluster name array. 
		for(String key : keys){
			
			ArrayList<String> items = temp_items.get(key);
			int size = items.size();
			
			String [] cluster = new String[size];
			cluster = items.toArray(cluster);
			
			ClusterItem item = new ClusterItem(cluster);
			
			this.items.put(key, item);
		}
	}

}
