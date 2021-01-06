package br.uefs.analyzIR.data;

import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Qrels represents growth truth file. A Qrels object contains a set of {@link QrelItem} group by 
 * the query_id. Using Qrels is possible to verify if a {@link RunItem} is relevance or unknown. Qrels gives two methods 
 * to return the number of relevant of a query. The first {@code getNumberOfRelevants} receives the query_id and 
 * returns the total of relevances. The second receives the query_id and a at value and returns
 * the total of relevances on the at. 
 * 
 * @see QrelItem
 * @see Run
 * @see RunItem
 * @author lucas
 *
 */
@XStreamAlias("Qrels")
public class Qrels {

	@XStreamOmitField
	private HashMap<String, ArrayList<QrelItem>> items; //group by topic
	@XStreamOmitField
	private HashMap<String, Long[]> qtdRelevance;//group by topic
	@XStreamOmitField
	private String pathQrel;


	/**
	 * Initialize a Qrels object with qrels file path. 
	 * @param qrelsFile a qrels file URL
	 */
	public Qrels(String qrelsFile) {
		this.items = new HashMap<>();
		this.qtdRelevance = new HashMap<>();
		this.pathQrel = qrelsFile;
	}

	/**
	 * Returns the costumized path to terminal executation. 
	 * @return costumized path to terminal executation.
	 */
	public String getPathQrel() {
		
		String p = this.pathQrel;
		 p = p.replace(" ", "\\ ");
		return p;
	}
	
	/**
	 * Returns the real file path.
	 * @return real run file path
	 */
	public String getPath(){
		return this.pathQrel;
	}
	
	/**
	 * Sets the real qrels path file. 
	 * @param path new real path file.
	 */
	public void setPath(String path) {
		this.pathQrel = path;
	}

	/**
	 * Initialize the main information of a Qrels object. Creates all the QrelsItem 
	 * from the file specified.  
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidQrelFormatException
	 */
	public void initialize() throws FileNotFoundException, IOException,
			InvalidQrelFormatException {
		loadQrelItem(new File(this.pathQrel));
		loadRelevance();
	}

	
	/**
	 * Returns a Iterator to all {@link QrelItem} by a specified query. 
	 * @param topicNumber query number or query id.
	 * @return Iterator of QrelItem. 
	 */
	public Iterator<QrelItem> getElements(String topicNumber) {
		if (items != null)
			return items.get(topicNumber).iterator();
		return null;
	}

	/**
	 * Returns the number of relevant in a specified query. 
	 * @param topicNumber query number or query id. 
	 * @return Long number of relevant. 
	 */
	public Long getNumberOfRelevants(String topicNumber) {
		Long[] relevance = qtdRelevance.get(topicNumber);
		return relevance[0];
	}
	/**
	 * Returns the number of relevant in a specified query and a at on the rank. 
	 * @param topicNumber query number or query id
	 * @param atValue a index of rank
	 * @return Long number of relevant
	 */
	public Long getNumberOfRelevants(String topicNumber, int atValue){
		
		Long relevance = (long)0;
		ArrayList<QrelItem> qItems = items.get(topicNumber);
		
		for (int j = 0; qItems != null && j < atValue; j++) {
			
			QrelItem temp = qItems.get(j);		
			if (temp.getRelevance() > 0)
				relevance++;
		}
		return relevance;
	}

	/**
	 * Returns if a docno in a querys is relevance or unknown. 
	 * @param topicNumber query number or query id
	 * @param docno document number
	 * @return true - if and only if there is a item in qrels file that has the docno and query_id equals to the specified and the item is relevance.  
	 * @throws QrelItemNotFoundException - If there is no item in qurels file. 
	 * @throws TopicNotFoundException - If query_id there is not in qrels file. 
	 * @throws InvalidQrelFormatException - If the docno parameter is null or a blank word. 
	 */
	public boolean isRelevant(String topicNumber, String docno)
			throws QrelItemNotFoundException, TopicNotFoundException,
			InvalidQrelFormatException {
		if (items != null) {
			ArrayList<QrelItem> qItems = items.get(topicNumber);
			if (qItems != null) {
				QrelItem temp = new QrelItem(topicNumber, docno,1);
				for(QrelItem item : qItems){
					if(item.equals(temp)){
						return item.getRelevance() > 0;
					}
				}
				return false;
			} else {
				throw new TopicNotFoundException(
						"The topic "+topicNumber+" does not exist in the qrels. Please provide a valid one.");
			}
		} else {
			throw new QrelItemNotFoundException(
					"The qrels is empty of absent. It was not initialized or the file is empty.");
		}
	}
	
	/***
	 * Reads qrels file and create a list of QrelItem representing the lines. 
	 * @param qrelsFile - qrels file 
	 * @throws FileNotFoundException - if there is not a qrels file 
	 * @throws IOException  
	 * @throws InvalidQrelFormatException
	 */
	private void loadQrelItem(File qrelsFile) throws FileNotFoundException,
			IOException, InvalidQrelFormatException {
		
		FileReader fReader = new FileReader(qrelsFile);
		BufferedReader bReader = new BufferedReader(fReader);
		String line = bReader.readLine();
		String nTopic; // hold temporary number of topic.
		ArrayList<QrelItem> itemsAll = new ArrayList<>();
		
		if (line != null) {
			
			items = new HashMap<>();// hold temporary set of runItem.
			ArrayList<QrelItem> itemsByTopic = new ArrayList<>();
			String[] item;
			String docno, last;
			String topicNumber;
			
			line = line.trim().replace("\t", " ");
			item = line.split(" ");
			
			nTopic = item[0];
			docno = item[2];
			last = item[3];
			int relevance = Integer.parseInt(last);
			
			QrelItem qItem = new QrelItem(nTopic, docno, relevance);
			itemsByTopic.add(qItem);
			
			while ((line = bReader.readLine()) != null) {
				
				line = line.trim().replace('\t', ' ');
				item = line.split(" ");
				
				last = item[3];
				topicNumber = item[0];
				docno = item[2];
			
				relevance = Integer.parseInt(last);
				// Add a new set of runItem by Topic in "items" and creates a
				// new set for a new Topic that was read.
				if (!topicNumber.equals(nTopic)) {
					items.put(nTopic, itemsByTopic);
					itemsByTopic = new ArrayList<>();
					nTopic = topicNumber;
				}
				qItem = new QrelItem(topicNumber, docno, relevance);
				itemsByTopic.add(qItem);
				itemsAll.add(qItem);
				
			}
			items.put(nTopic, itemsByTopic);
			items.put("AVG", itemsAll);
			bReader.close();
			fReader.close();
			// if file is empty a new exception is thrown, because doesn't have
			// result for analyze.
		} 
	}

	/*
	 * Loads amount of relevant documents and unknown relevance. 
	 */
	private void loadRelevance() {

		int counter_relevance = 0;// number of relevant documents
		int counter_unknown = 0;// number of unknown relevance documents
		Long[] relevance;// array of 2 position save the final counter value
		
		qtdRelevance = new HashMap<>(); // save the number of relevant and unknown relevance documents by topic

		// Verify for each topic which documents are relevant or not. 
		for (String key : items.keySet()) {

			ArrayList<QrelItem> qItems = items.get(key);
			relevance = new Long[2];

			for (int j = 0; qItems != null && j < qItems.size(); j++) {
				QrelItem temp = qItems.get(j);
				
				//if relevance >= 1 the document is relevant.
				if (temp.getRelevance() > 0)
					counter_relevance++;
				else
					counter_unknown++; // not relevant or unknown relevance
			}
			
			relevance[0] = (long) counter_relevance;
			relevance[1] = (long) counter_unknown;
			qtdRelevance.put(key, relevance);

			counter_relevance = 0; 
			counter_unknown = 0;
		}
	}

    /**
     * Returns a QrelItem list.
     *
     * @param topicNumber
     * @return QrelItem list by topic
     * @throws TopicNotFoundException
     */
    public List<QrelItem> getQrelsItems(String topicNumber)
            throws TopicNotFoundException {

        if (items.containsKey(topicNumber)) {
            return items.get(topicNumber);
        } else {
            throw new TopicNotFoundException(
                    "There wasn't a topic with this number entered.");
        }
    }
}
