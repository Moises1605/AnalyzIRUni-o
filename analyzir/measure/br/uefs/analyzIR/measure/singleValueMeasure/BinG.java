package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class BinG implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public BinG(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m binG " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		String[] topic = { "all" };
		MeasureSet topicSet = mCommand.executeCommand(command, topic);
		topicSet.setMeasureName("bingG");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m binG " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("bingG");
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
		this.description = "<font color=\"black\" size=\"4\"><b>bingG: Binary G.</b>" +
				"<br><br>"+
				"Experimental measure. (4/10/2008)" +
				"G is a gain related measure that combines qualities of MAP and NDCG. " +
				"G(doc) == rel_level_gain (doc) / log2 (2+num_nonrel retrieved before doc)" +
				"G is the average of G(doc) over all docs, normalized by " +
				"sum (rel_level_gain)." +
				"BinG restricts the gain to either 0 or 1 (nonrel or rel), and thus is the " +
				"average over all rel docs of (1 / log2 (2+num_nonrel before doc))"
				+"<br><br>"+"Adapted from trec_eval code.";
		return this.description;
	}

}
