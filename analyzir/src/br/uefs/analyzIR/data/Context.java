package br.uefs.analyzIR.data;

/**
 * Context is a interface between model application and measures. The System has one and only one
 * context shared to all measures created in application. With Context object a measure can use the {@link Run} and 
 * {@link Qrels} data to compute the result. Basically a Context is a global value that can not be edit, but can be read. 
 * 
 * @see Run 
 * @see Qrels
 * @see Topic
 * @author lucas
 *
 */
public class Context {

	private Run [] runs; 
	private Qrels qrels;
	private ClusterQrels cqrels;
	
	/**
	 * Initialize a Context with run and qrels objects. 
	 * @param runs Run array to represent the result files. 
	 * @param qrels growth truth object. 
	 * @param cqrels TODO
	 * @see Run
	 * @see Qrels
	 */
	public Context(Run [] runs, Qrels qrels, ClusterQrels cqrels){
		this.runs =  runs; 
		this.qrels = qrels;
		this.cqrels = cqrels;
	}
	
	/**
	 * Returns Run array. 
	 * @return Run array. 
	 * @see Run
	 */
	public Run[] getRuns() {
		return runs;
	}
	
	public void setRuns(Run [] runs){
		this.runs = runs;
	}
	/**
	 * Returns Qrels object. 
	 * @return Qrels - represents a growth truth file. 
	 * @see Qrels
	 */
	public Qrels getQrels() {
		return qrels;
	}
	
	/***
	 * Returns ClusterQrels object. 
	 * @return ClusterQrels - represents a cluster ground truth file. 
	 */
	public ClusterQrels getCqrels() {
		return cqrels;
	}


	public void setCqrels(ClusterQrels cQrels){
		this.cqrels = cQrels;
	}
	/**
	 * Returns a Run by name. 
	 * @param runName  name of run 
	 * @return Run  object that has the name requested.
	 * 				 Null - If there is not a run with name specified.  
	 * @see Run
	 * 
	 */
	public Run getRunByName(String runName){
		for(Run r : runs){
			if(r.getName().equals(runName)){
				return r;
			}
		}
		return null;
	}

	public void setQrels(Qrels qrels2) {
		this.qrels = qrels2;
	}
}
