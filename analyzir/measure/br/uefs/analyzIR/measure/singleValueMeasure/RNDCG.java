package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class RNDCG implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public RNDCG(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m Rndcg " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("Rndcg");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m Rndcg " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("Rndcg");
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
		this.description = "<font color=\"black\" size=\"4\"><b>Rndcg: Normalized Discounted Cumulative Gain at R levels.</b></font>" +
				"<br><br>"+
				"    Experimental measure\n" +
				"Compute a traditional nDCG measure according to Jarvelin and " +
				"Kekalainen (ACM ToIS v. 20, pp. 422-446, 2002), averaged at the various " +
				"R level points. The R levels are the number of docs at each non-negative " +
				"gain level in the judgments, with the gain levels sorted in decreasing " +
				"order. Thus if there are 5 docs with gain_level 3, 3 with gain 2, 10 " +
				"with gain 1, and 50 with gain 0, then " +
				"Rndcg = 1/4 (ndcg_at_5 + ndcg_at_8 + ndcg_at_18 + ndcg_at_68). " +
				"In this formulation, all unjudged docs have gain 0.0, and thus there is " +
				"a final implied R-level change at num_retrieved. " +
				"Idea behind Rndcg, is that the expected value of ndcg is a smoothly " +
				"decreasing function, with discontinuities upward at each transistion " +
				"between positive gain levels in the ideal ndcg.  Once the gain level " +
				"becomes 0, the expected value of ndcg then increases until all docs are " +
				"retrieved. Thus averaging ndcg is problematic, because these transistions " +
				"occur at different points for each topic.  Since it is not unusual for " +
				"ndcg to start off near 1.0, decrease to 0.25, and then increase to 0.75 " +
				"at various cutoffs, the points at which ndcg is measured are important. "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}


}
