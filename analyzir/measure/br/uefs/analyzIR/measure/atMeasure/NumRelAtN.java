package br.uefs.analyzIR.measure.atMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.AtMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class NumRelAtN implements AtMeasure {

	private String name;
	private String description;
	private MeasureCommand mCommand;
	private Context context;

	public NumRelAtN(String name) {
		this.name = name; 
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run, String atValue)
			throws IOException, InterruptedException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m num_rel." + atValue + " " + this.context.getQrels().getPathQrel()
				+ " " + r.getPathRunFile();

		String[] topic = { "all" };

		MeasureSet topicSet = mCommand.executeCommand(command, topic, atValue);
		topicSet.setMeasureName("num_rel_"+atValue);
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String atValue,
			String[] topicNumber) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m num_rel." + atValue + " " + this.context.getQrels().getPathQrel()
				+ " " + r.getPathRunFile();

		MeasureSet topicSet =  mCommand.executeCommand(command, topicNumber, atValue);
		topicSet.setMeasureName("num_rel_"+atValue);
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
				"Summary figure is sum of individual topics, not average." +
				"<br><br>" + "Adapted from trec_eval code.";
		return null;
	}

}
