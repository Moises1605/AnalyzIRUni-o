package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class GMBPref implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public GMBPref(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m gm_bpref " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("gm_bpref");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException {

		return null;
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
	public String getDescription(){
		this.description = "<font color=\"black\" size=\"4\"><b>gm_bpref: Binary preference (bpref), but using goemetric mean over topics.</b></font>" +
				"<br><br>"+
				"See the explanation for 'bpref' for the base measure for a single topic. " +
				"Gm_bpref uses the geometric mean to combine the single topic scores. " +
				"This rewards methods that are more consistent across topics as opposed to " +
				"high scores for some topics and low scores for others. " +
				"Gm_bpref is printed only as a summary measure across topics, not for the " +
				"individual topics. "
				+"<br><br>"+"Adapted from trec_eval code.";
		return this.description;
	}
}
