package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class RecipRank implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public RecipRank(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m recip_rank " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("recip_rank");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m recip_rank " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("recip_rank");
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
	public String getDescription(){
		this.description = "" +
				"<font color=\"black\" size=\"4\"><b>recip_rank: Reciprocal Rank of the first relevant retrieved doc.</b></font>" +
				"<br><br>"+
				"Measure is most useful for tasks in which there is only one relevant " +
				"doc, or the user only wants one relevant doc. "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}

}
