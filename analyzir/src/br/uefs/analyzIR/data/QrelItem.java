package br.uefs.analyzIR.data;

import br.uefs.analyzIR.exception.InvalidQrelFormatException;

/**
 * QrelItem represents a line of Qrels file. A qrels line model contains in this sequence: query_id, 
 * iter, docno and relevance. Like the run file ({@link Run}) the iter field  is the feedback iteration 
 * (almost always zero and not used) is ignored here. Therefore, a QrelItem object encapsulates basic
 * informations about query (topic), docno and relevance. 
 * 
 * @see Qrels
 * @see RunItem
 * @see Run
 * @author lucas
 *
 */
public class QrelItem{

	/**Topic number*/
	private String query_id;
	/**Document name*/
	private String docno;
	/**
	 * Relevance number. 
	 * */
	private int relevance;

	/**
	 * Initialize a QrelItem object with topicNumber (query_id), docno and relevance value. 
	 * @param query_id query number
	 * @param docno document number 
	 * @param relevance value of relevance (RELEVANCE or UNKNOW) 
	 * @throws InvalidQrelFormatException
	 */
	public QrelItem(String query_id, String docno, int relevance)
			throws InvalidQrelFormatException {
		setTopicNumber(query_id);
		setDocno(docno);
		setRelevance(relevance);
	}

	/**
	 * Returns query number. 
	 * @return String - query number or query id. 
	 */
	public String getQuery() {
		return query_id;
	}

	/**
	 * Returns document number. 
	 * @return String - document number 
	 */
	public String getDocno() {
		return docno;
	}

	/**
	 * Returns relevance. 
	 * @return 1 - if docno is relevant
	 * 		   0 - if docno is not relevant
	 */
	public int getRelevance() {
		return relevance;
	}

	/**
	 * Sets a topic number (query_id). 
	 * @param topicNumber query id
	 */
	public void setTopicNumber(String topicNumber) {
		this.query_id = topicNumber;
	}

	
	/**
	 * Sets a document number. 
	 * @param docno new document number. 
	 * @throws InvalidQrelFormatException If the new docno is null or a blank word.
	 */
	public void setDocno(String docno) throws InvalidQrelFormatException {
		if (docno != null && docno.trim().length() != 0)
			this.docno = docno;
		else
			throw new InvalidQrelFormatException(
					"Exception: Your Qrels file is invalid format. A line don't have the name of docno. "
							+ "Please make a file in valid format.");
	}

	/**
	 * Sets a new relevance to this item. 
	 * @param relevance relevance value (RELEVANCE or UNKNOW) 
	 */
	public void setRelevance(int relevance) {
			this.relevance = relevance;
	}

	/***
	 * Indicates whether some other object is "equal to" this one. A QrelItem object is "equal to" other if and only if they have the same 
	 * query_id, docno and relevance. 
	 * @return true - if and only if obj parameter is a QrelItem and obj has the same query_id, docno and relevance like this one. 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QrelItem) {
			QrelItem temp = (QrelItem) obj;
			if (this.getQuery().equals(temp.getQuery())
					&& this.getDocno().equals(temp.getDocno()))
				return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of the object. Shows: query_id, docno and relevance. 
	 * @return a String representation of the object. 
	 */
	@Override 
	public String toString(){
		return "[query_id = "+ this.query_id + " docno = " + this.docno + " relevance = "+this.relevance+"]";
	}
}
