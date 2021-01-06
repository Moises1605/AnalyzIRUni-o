package br.uefs.analyzIR.measure;

import java.io.IOException;

import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;

/**
 * 
 * A CurveMeasure is like a AtMeasure, but the behavior defined creates a curve of points (atValue, result) 
 * on a lot of xValues (atValues) specified by CurveMeasure interface users. Whatever class that implements the
 * CurveMeasure interface has to implement the behavior defined by Measure interface.   
 * @author lucas
 *
 */
public interface CurveMeasure extends Measure{
	
	/**
	 * Returns a MeasureSet object that contains the measure result to all topic and atValues specified on the run selected. 
	 * @param run - name of run
	 * @param xValues - atValues array. 
	 * @return MeasureSet - The object that encapsulates the measure result. 
	 * @throws IOException -If happened a problem when open the result file
	 * @throws InterruptedException - 
	 * @throws InvalidItemNameException - If the data is not a number.
	 * @throws ItemNotFoundException - If a topic does not exist. 
	 * @throws InvalidItemNameException -  If a topics with the same name exists. 
	 */
	public MeasureSet getValue(String run, String [] xValues)
			throws IOException, InterruptedException, NumberFormatException, ItemNotFoundException, 
			InvalidItemNameException, QrelItemNotFoundException, TopicNotFoundException,
			InvalidQrelFormatException;

	/**
	 * Returns a MeasureSet object that contains the measure result to selected topics and atValues specified on the run selected. 
	 * @param run - name of run
	 * @param xValues - atValues array. 
	 * @param topicNumbers - selected topic name array 
	 * @return MeasureSet - The object that encapsulates the measure result. 
	 * @throws IOException -If happened a problem when open the result file
	 * @throws InterruptedException - 
	 * @throws InvalidItemNameException - If the data is not a number.
	 * @throws ItemNotFoundException - If a topic does not exist. 
	 * @throws InvalidItemNameException -  If a topics with the same name exists. 
	 */
	public MeasureSet getValue(String run, String [] xValues,  String[] topicNumbers) throws IOException,
			InterruptedException, NumberFormatException, ItemNotFoundException, InvalidItemNameException, 
			QrelItemNotFoundException, TopicNotFoundException,
			InvalidQrelFormatException;
}
