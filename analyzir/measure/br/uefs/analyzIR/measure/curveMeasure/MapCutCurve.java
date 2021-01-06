package br.uefs.analyzIR.measure.curveMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.CurveMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class MapCutCurve implements CurveMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public MapCutCurve(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run, String[] xValues)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m map_cut." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();

		String[] topic = { "all" };

		MeasureSet topicSet = mCommand.executeCommand(command, topic, xValues);
		topicSet.setMeasureName("map_cut");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run, String[] xValues,
			String[] topicNumbers) throws IOException, InterruptedException,
			NumberFormatException, ItemNotFoundException,
			InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m map_cut." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumbers, xValues);
		topicSet.setMeasureName("map_cut");
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
		this.description = "<font color=\"black\" size=\"4\"><b>map_cut: Mean Average Precision at cutoffs.</b></font>" +
				"<br><br>"+
				"Map measured at various doc level cutoffs in the ranking. " +
				"If the cutoff is larger than the number of docs retrieved, then " +
				"it is assumed nonrelevant docs fill in the rest. " +
				"Map itself is precision measured after each relevant doc is retrieved, " +
				"averaged over all relevant docs for the topic. " +
				"Cutoffs must be positive without duplicates "
				+"<br><br>"+"Adapted from trec_eval code. ";

		return this.description;
	}

}
