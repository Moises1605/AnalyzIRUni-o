package br.uefs.analyzIR.data;

/***
 * ClusterItem represents all aspects that a document is relevant in a query. 
 * @author lucas
 *
 */
public class ClusterItem {

	/**
	 * Clusters array for a document in a query.
	 */
	private String [] clusters;
	
	/**
	 * Initialize an item with all cluster that a document is relevant in a query. 
	 * @param clusters clusters that a document is relevant.
	 */
	public ClusterItem(String[] clusters) {
		super();
		this.clusters = clusters;
	}
	
	/**
	 * Returns cluster array with all aspects that a document is relevant in a query. 
	 * @return clusters that a document is relevant. 
	 */
	public String[] getClusters() {
		return clusters;
	}
	
	/**
	 * Sets the clusters that a document is relevant in a query. 
	 * @param clusters - clusters that a document is relevant.
	 */
	public void setClusters(String[] clusters) {
		this.clusters = clusters;
	} 
}
