package br.uefs.analyzIR.measure.curveMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.CurveMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class NumRelCurve implements CurveMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public NumRelCurve(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run, String[] xValues)
			throws IOException, InterruptedException, NumberFormatException, ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m num_rel." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		System.out.println(command);
		String[] topic = { "all" };

		MeasureSet topicSet = mCommand.executeCommand(command, topic, xValues);
		topicSet.setMeasureName("num_rel");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run,
			String[] xValues, String[] topicNumbers) throws IOException,
 InterruptedException,
			NumberFormatException, ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m num_rel." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumbers, xValues);
		topicSet.setMeasureName("num_rel");
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
		this.description = "<font color=\"black\" size=\"4\"><b>num_rel: Number of relevant documents for topic.</b></font>" +
				"<br><br>"+
				"May be affected by Judged_docs_only and Max_retrieved_per_topic command " +
				"line parameters (as are most measures). " +
				"Summary figure is sum of individual topics, not average. "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return null;
	}

}
