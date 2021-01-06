package br.uefs.analyzIR.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Topic represents a query result on the run file. A Topic object saves the topic name or topic code presents on 
 * the run file. 
 * 
 * @author lucas
 * @see Run
 * @see Qrels
 *
 */
@XStreamAlias("topic")
public class Topic {

	private String name;
	private int numInteration;
	
	/***
	 * Initialize a Topic object with the query name specified in run file lines. 
	 * @param number number or name of topic (query)
	 */
	public Topic(String number){
		setName(number);
	}
	
	/**
	 * Sets a new name for the topic. 
	 * @param name new topic name
	 */
	private void setName(String name) {
		if(name != null && name.trim().length() != 0)
			this.name = name;
		else 
			this.name = "TOPIC DEFAULT";
	}

	/**
	 * Returns topic name. 
	 * @return topic name - tag specified in run file. 
	 */
	public String getName() {
		return name;
	}
	
	
	
	public int getNumInteration() {
		return this.numInteration;
	}

	public void setNumInteration(int numInteration) {
		this.numInteration = numInteration;
	}

	/**
	 * Compares this topic to the specified obj. The result is true if and only if this topic name is the same of obj. 
	 * @return true - if and only if this topic name is the same of obj. 
	 * 			
	 */
	@Override 
	public boolean equals(Object obj){
		if(obj instanceof Topic){
			Topic t = (Topic) obj; 
			if(t.getName().equals(this.getName()))
				return true; 
		}
		return false;
	}
	
}
