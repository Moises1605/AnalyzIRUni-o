package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class NDCGRel implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public NDCGRel(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m ndcg_rel " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("ndcg_rel");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m ndcg_rel " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("ndcg_rel");
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
		this.description = "\n" +
				"<font color=\"black\" size=\"4\"><b>ndcg_rel: Normalized Discounted Cumulative Gain averaged over rel docs.</b></font>" +
				"<br><br>"+
				"Experimental measure\n" +
				"Compute a traditional nDCG measure according to Jarvelin and " +
				"Kekalainen (ACM ToIS v. 20, pp. 422-446, 2002), averaged at rel docs. " +
				"Idea behind ndcg_rel, is that the expected value of ndcg is a smoothly " +
				"decreasing function, with discontinuities upward at each transistion " +
				"between positive gain levels in the ideal ndcg.  Once the gain level " +
				"becomes 0, the expected value of ndcg then increases until all rel docs are " +
				"retrieved. Thus averaging ndcg is problematic, because these transistions " +
				"occur at different points for each topic.  Since it is not unusual for " +
				"ndcg to start off near 1.0, decrease to 0.25, and then increase to 0.75 " +
				"at various cutoffs, the points at which ndcg is measured are important. " +
				"This version averages ndcg over each relevant doc, where relevant is " +
				"defined as expected gain > 0.  If a rel doc is not retrieved, then " +
				"ndcg for the doc is the dcg at the end of the retrieval / ideal dcg. "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}

}
