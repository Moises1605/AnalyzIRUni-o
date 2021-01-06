package br.uefs.analyzIR.measure.curveMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.CurveMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class IPrecAtRecallCurve implements CurveMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public IPrecAtRecallCurve(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run, String[] xValues)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m iprec_at_recall." + xValues[0];
		
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		
		String[] topic = { "all" };
		
		MeasureSet topicSet = mCommand.executeCommand(command, topic, xValues);
		topicSet.setMeasureName("iprec_at_recall");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run, String[] xValues,
			String[] topicNumbers) throws IOException, InterruptedException,
			NumberFormatException, ItemNotFoundException,
			InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m iprec_at_recall." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
			command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumbers, xValues);
		topicSet.setMeasureName("iprec_at_recall");
		return topicSet;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public String getDescription() {
		this.description = "<font color=\"black\" size=\"4\"><b>iprec_at_recall: Interpolated Precision at recall cutoffs.</b></font>" +
				"<br><br>"+
				"This is the data shown in the standard Recall-Precision graph. " +
				"The standard cutoffs and interpolation are needed to average data over " +
				"multiple topics; otherwise, how is a topic with 5 relevant docs averaged " +
				"with a topic with 3 relevant docs for graphing purposes?  The Precision " +
				"interpolation used here is " +
				"Int_Prec (rankX) == MAX (Prec (rankY)) for all Y >= X. "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}

}
