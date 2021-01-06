package br.uefs.analyzIR.measure.curveMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.CurveMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class RelativePCurve implements CurveMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public RelativePCurve(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run, String[] xValues)
			throws IOException, InterruptedException, NumberFormatException, ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m relative_P." + xValues[0].replace("0.", ".");;
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i].replace("0.", ".");;
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();

		String[] topic = { "all" };

		MeasureSet topicSet = mCommand.executeCommand(command, topic, xValues);
		topicSet.setMeasureName("relative_P");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run,
			String[] xValues, String[] topicNumbers) throws IOException,
			InterruptedException, NumberFormatException, ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m relative_P." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumbers, xValues);
		topicSet.setMeasureName("relative_P");
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
		this.description = "<font color=\"black\" size=\"4\"><b>relative_P: Relative Precision at cutoffs.</b></font>" +
				"<br><br>"+
				"Precision at cutoff relative to the maximum possible precision at that " +
				"cutoff. Equivalent to Precision up until R, and then recall after R " +
				"Cutoffs must be positive without duplicates "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}
}
