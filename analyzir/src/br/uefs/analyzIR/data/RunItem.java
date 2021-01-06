package br.uefs.analyzIR.data;


/**
 * RunItem represents a line of Run file. A run line model contains in this sequence: 
 * query_id, iter, docno, rank, sim, run_id  delimited by spaces. Query_id is the query number. 
 * The iter is the feedback iteration (almost always zero and not used). Docno or Document numbers are string value like FR940104-0-00001. 
 * The Similarity (sim) is a float value. Rank is a integer from 0 to 1000. Run_id is the name of run
 * and it is storage in Run object.
 *   
 */
public class RunItem {

	private String query_id;
	private int interation;
	private String docno;
	private int rank;
	private float sim;

	/**
	 * Initialize a RunItem object with query_id, docno, rank and sim values. 
	 * @param query_id query number
	 * @param interation interation number
	 * @param docno document number
	 * @param rank value between 0 and 1000
	 * @param sim a float value that represent the distance between the query value and obtained. 
	 */
	public RunItem(String query_id, int interation, String docno, int rank, float sim) {
		
		setInteration(interation);
		setQuery_id(query_id);
		setDocno(docno);
		setRank(rank);
		setSim(sim);
	}

	/**
	 * Returns query number. 
	 * @return String query number. 
	 */
	public String getQuery_id() {
		return query_id;
	}
	/**
	 * Returns interation number. 
	 * @return int interation number. 
	 */
	
	public int getInteration(){
		return interation;
	}

	/**
	 * Returns document number.
	 * @return String document number.
	 */
	public String getDocno() {
		return docno;
	}

	/**
	 * Returns rank value.
	 * @return int rank value between 0 and 1000.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Returns similarity float value. 
	 * @return float similarity. 
	 */
	public float getSim() {
		return sim;
	}


	/**
	 * Sets a new queryNumber if and only if the query_id is different of zero.  
	 * @param query_id new query number to a run line. 
	 */
	public void setQuery_id(String query_id) {
		if (!query_id.equals("0"))
			this.query_id = query_id;
		else
			this.query_id = "0";
	}
	
	/**
	 * Sets a new interationNumber 
	 * @param interation number to a run line.
	 */
	public void setInteration(int interation){
		this.interation = interation;
	}

	/**
	 * Sets a document number if and only if the new docno is a String with length different of zero. 
	 * @param docno new document number
	 */
	public void setDocno(String docno) {
		if ((docno != null) && (docno.trim().length() != 0))
			this.docno = docno;
	}

	/**
	 * Sets a rank number between 0 and 1000. If the value is not in this range the rank is not change.  
	 * @param rank number between 0 and 1000. 
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * Sets a new similarity. 
	 * @param sim new similarity. 
	 */
	public void setSim(float sim) {
		if (sim >= 0)
			this.sim = sim;
	}
	
	/**
	 * Indicates whether some other object is "equal to" this one. A RunItem object is "equal to" other if and only if they have the same 
	 * docno and the same query_id. 
	 * @param o a RunItem object
	 * @return true - if and only if o parameter is a RunItem and has the same docno and query_id this one. 
	 */
	@Override 
	public boolean equals(Object o){
		if(o instanceof RunItem){
			RunItem item = (RunItem) o; 
			if(item.getDocno().equals(this.docno) && item.getQuery_id().equals(this.query_id)){
				return true;
			}else{
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns a string representation of the object. Shows: query_id, docno and rank value. 
	 * @return a String representation of the object. 
	 */
	@Override
	public String toString(){
		String value = "[query_id = " + this.query_id + " interation = " + this.interation + " docno = " + this.docno + " rank = " + this.rank + "]";
		return value;
	}

}
