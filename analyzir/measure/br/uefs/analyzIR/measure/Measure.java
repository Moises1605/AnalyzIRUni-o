package br.uefs.analyzIR.measure;

import br.uefs.analyzIR.data.Context;

/**
 * Measure interface is the root of measures hierarchy. Every measure has to implement this interface. 
 * Each measure has a unique name that is used to search a measure in AnalyzIR tool. This interface
 * describes the minimum state of measures.
 * 
 * 
 * @author Analyzer
 * 
 */
public interface  Measure {
	
	/**
	 * Returns the measure name.
	 * @return
	 */
	public String getName();
	
	/**
	 * Sets the context object.
	 * @param context contains the run and qrels object
	 */
	public void setContext(Context context);

	/**
	 * Return the measure description
	 * @return
     */
	public String getDescription();
	
}
