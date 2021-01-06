package br.uefs.analyzIR.measure;

import java.io.IOException;

import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;

/**
 * SingleValueMeasure is kind of measure that result a unique value to a ranking. Therefore, a SingleValue 
 * does not depends of a at value (depth of ranking). Whatever class that implements the SingleValue
 * interface has to implement the behavior defined by Measure interface.
 * 
 * @author lucas
 *
 */
public interface SingleValueMeasure extends Measure{

	/**
	 * Returns a MeasureSet object that contains the measure result to all topic on the run specified.  
	 * @param run - name of run 
	 * @return MeasureSet - The object that encapsulates the measure result. 
	 * @throws IOException -If happened a problem when open the result file
	 * @throws InterruptedException - 
	 * @throws InvalidItemNameException - If the data is not a number.
	 * @throws ItemNotFoundException - If a topic does not exist. 
	 */
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException,QrelItemNotFoundException, TopicNotFoundException,
			InvalidQrelFormatException;

	/**
	 * Returns a MeasureSet object that contains the measure result to selected topics on the run specified.  
	 * @param run - name of run 
	 * @param topicNumber - selected topic name array 
	 * @return MeasureSet - The object that encapsulates the measure result. 
	 * @throws IOException -If happened a problem when open the result file
	 * @throws InterruptedException - 
	 * @throws InvalidItemNameException - If the data is not a number.
	 * @throws ItemNotFoundException - If a topic does not exist. 
	 * @throws InvalidItemNameException -  If a topics with the same name exists. 
	 */
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException, QrelItemNotFoundException, TopicNotFoundException,
			InvalidQrelFormatException;
}
