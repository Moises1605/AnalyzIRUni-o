package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class G implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public G(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m G " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("G");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m G " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("G");
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
		this.description = "<font color=\"black\" size=\"4\"><b>G: Normalized Gain.</b></font>" +
				"<br><br>"+
				"Experimental measure 4/10/2008" +
				"G is a gain related measure that combines qualities of MAP and NDCG." +
				"Contribution of doc doc retrieved at rank i is " +
				"G(doc) == gain (doc) / log2 (2+ideal_gain(i)-results_gain(i)) " +
				"where results_gain(i) is sum gain(doc) for all docs before i " +
				"and ideal_gain is the maximum possible results_gain(i) " +
				"G is the sum of G(doc) over all docs, normalized by max ideal_gain. " +
				"Gain values are set to the appropriate relevance level by default. " +
				"The default gain can be overridden on the command line by having " +
				"comma separated parameters 'rel_level=gain'. " +
				"Eg, 'trec_eval -m G.1=3.5,2=9.0,4=7.0 ... '" +
				"will give gains 3.5, 9.0, 3.0, 7.0 for relevance levels 1,2,3,4 " +
				"respectively (level 3 remains at the default). " +
				"Gains are allowed to be 0 or negative, and relevance level 0 " +
				"can be given a gain. " +
				"The idea behind G is that the contribution of a doc retrieved at i " +
				"should not be independent of the docs before. If most docs before have " +
				"higher gain, then the retrieval of this doc at i is nearly as good as " +
				"possible, and should be rewarded appropriately "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}

}
