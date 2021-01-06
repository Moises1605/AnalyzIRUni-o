package br.uefs.analyzIR.measure;

import java.io.IOException;

import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;

/**
 * AtMeasure interface defines the AtMeasures behavior. Every AtMeasure is calculated  
 * to a depth of ranking (at value). The estimates can be to all topic or to others topics selected. 
 * Whatever class that implements the AtMeasure interface has to implement the behavior defined by
 * Measure interface.   
 *
 * @author lucas
 *
 */
public interface AtMeasure extends Measure {

	 /**
	  * Returns a MeasureSet object that contains the measure result to all topic on the atValue and 
	  * run specified.  
	  * 
	  * @param run - name of run
	  * @return MeasureSet - The object that encapsulates the measure result. 
	  * @throws IOException - If happened a problem when open the result file
	  * @throws InterruptedException -  
	  * @throws NumberFormatException -  If the data is not a number.
	  * @throws InvalidItemNameException -  If a topics with the same name exists. 
	  * @throws ItemNotFoundException - If a topic does not exist. 
	  * @throws QrelItemNotFoundException
	  * @throws TopicNotFoundException
	  * @throws InvalidQrelFormatException
	  */
	public MeasureSet getValue(String run, String at)
			throws IOException, InterruptedException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException, QrelItemNotFoundException, TopicNotFoundException,
			InvalidQrelFormatException;
	
	 /**
	  * Returns a MeasureSet object that contains the measure result to selected topic on the atValue and 
	  * run specified. 
	  *  
	  * @param run - name of run 
	  * @param atValue - depth of ranking
	  * @param topicNumber - selected topic name array 
	  * @return MeasureSet - The object that encapsulates the measure result. 
	  * @throws IOException - If happened a problem when open the result file
	  * @throws InterruptedException -  
	  * @throws NumberFormatException -  If the data is not a number.
	  * @throws InvalidItemNameException -  If a topics with the same name exists. 
	  * @throws ItemNotFoundException - If a topic does not exist. 
	  * @throws QrelItemNotFoundException 
	  * @throws TopicNotFoundException 
	  * @throws InvalidQrelFormatException
	  */
	public MeasureSet getValue(String run, String atValue, String[] topicNumber) throws IOException,
			InterruptedException, NumberFormatException, InvalidItemNameException, ItemNotFoundException, 
			QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException;
	

}
