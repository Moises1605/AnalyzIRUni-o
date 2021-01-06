package br.uefs.analyzIR.measure.curveMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.CurveMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class PAtNCurve implements CurveMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public PAtNCurve(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run, String[] xValues)
			throws IOException, InterruptedException, NumberFormatException, ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m P." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		System.out.println(command);
		String[] topic = { "all" };

		MeasureSet topicSet = mCommand.executeCommand(command, topic, xValues);
		topicSet.setMeasureName("P");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run,
			String[] xValues, String[] topicNumbers) throws IOException,
 InterruptedException,
			NumberFormatException, ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m P." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumbers, xValues);
		topicSet.setMeasureName("P");
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
		this.description = "<font color=\"black\" size=\"4\"><b>P: Precision.</b></font>" +
				"<br><br>"+
				"Precision measured at various doc level cutoffs in the ranking. " +
				"If the cutoff is larger than the number of docs retrieved, then " +
				"it is assumed nonrelevant docs fill in the rest.  Eg, if a method " +
				"retrieves 15 docs of which 4 are relevant, then P20 is 0.2 (4/20). " +
				"Precision is a very nice user oriented measure, and a good comparison " +
				"number for a single topic, but it does not average well. For example, " +
				"P20 has very different expected characteristics if there 300 " +
				"total relevant docs for a topic as opposed to 10. "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}

}
