package br.uefs.analyzIR.measure.atMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.AtMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class NDCGCutAtN implements AtMeasure {

	private String name;
	private String description;
	private MeasureCommand mCommand;
	private Context context;

	public NDCGCutAtN(String name) {
		this.name = name; 
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run, String atValue)
			throws IOException, InterruptedException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m ndcg_cut." + atValue + " " + this.context.getQrels().getPathQrel()
				+ " " + r.getPathRunFile();

		String[] topic = { "all" };

		MeasureSet topicSet = mCommand.executeCommand(command, topic, atValue);
		topicSet.setMeasureName("ndcg_cut_"+atValue);
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run, String atValue,
			String[] topicNumber) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m ndcg_cut." + atValue + " " + this.context.getQrels().getPathQrel()
				+ " " + r.getPathRunFile();

		MeasureSet topicSet =  mCommand.executeCommand(command, topicNumber, atValue);
		topicSet.setMeasureName("ndcg_cut_"+atValue);
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
		this.description = "<font color=\"black\" size=\"4\"><b>ndcg_cut: Normalized Discounted Cumulative Gain at cutoffs.</b></font>" +
				"<br><br>"+
				"Compute a traditional nDCG measure according to Jarvelin and " +
				"Kekalainen (ACM ToIS v. 20, pp. 422-446, 2002) at cutoffs. " +
				"See comments for ndcg. " +
				"Gain values are the relevance values in the qrels file.  For now, if you " +
				"want different gains, change the qrels file appropriately." +
				"Cutoffs must be positive without duplicates" +
				"<br><br>" + "Adapted from trec_eval code.";
		return this.description;
	}

}
