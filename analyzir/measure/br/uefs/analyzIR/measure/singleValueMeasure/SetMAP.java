package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class SetMAP implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public SetMAP(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m set_map " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("set_map");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m set_map " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("set_map");
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
		this.description = "<font color=\"black\" size=\"4\"><b>Set map: num_relevant_retrieved**2 / (num_retrieved*num_rel).</b></font>" +
				"<br><br>"+
				"Unranked set map, where the precision due to all relevant retrieved docs " +
				"is the set precision, and the precision due to all relevant not-retrieved " +
				"docs is set to 0. " +
				"Was known as exact_unranked_avg_prec in earlier versions of trec_eval. " +
				"Another way of loooking at this is  Recall * Precision on the set of " +
				"docs retrieved for a topic. "
				+"<br><br>"+"Adapted from trec_eval code.";;
		return this.description;
	}

}
