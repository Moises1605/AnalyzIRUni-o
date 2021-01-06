package br.uefs.analyzIR.measure.atMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.AtMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class RelativePAtN implements AtMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public RelativePAtN(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run, String atValue)
			throws IOException, InterruptedException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m relative_P." + atValue + " " + this.context.getQrels().getPathQrel() + " "+ r.getPathRunFile();
		String[] topic = { "all" };
		MeasureSet topicSet = mCommand.executeCommand(command, topic, atValue);
		topicSet.setMeasureName("relative_P_"+atValue);
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String atValue,
			String[] topicNumber) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m relative_P." + atValue + " "
		+ this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber, atValue);
		topicSet.setMeasureName("relative_P_"+atValue);
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
				"Precision at cutoff relative to the maximum possible precision at that" +
				"cutoff.  Equivalent to Precision up until R, and then recall after R" +
				"Cutoffs must be positive without duplicates"
				+"<br><br>"+"Adapted from trec_eval code.";;
		return this.description;
	}
}
