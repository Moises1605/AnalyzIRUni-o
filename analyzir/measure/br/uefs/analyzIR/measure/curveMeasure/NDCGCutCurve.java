package br.uefs.analyzIR.measure.curveMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.CurveMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class NDCGCutCurve implements CurveMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public NDCGCutCurve(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run, String[] xValues)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m ndcg_cut." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();

		String[] topic = { "all" };

		MeasureSet topicSet = mCommand.executeCommand(command, topic, xValues);
		topicSet.setMeasureName("ndcg_cut");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] xValues,
			String[] topicNumbers) throws IOException, InterruptedException,
			NumberFormatException, ItemNotFoundException,
			InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m ndcg_cut." + xValues[0];
		for (int i = 1; i < xValues.length; i++) {
			if(xValues[i] != null && !xValues[i].equals("null"))
				command += "," + xValues[i];
		}
		command += " " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumbers, xValues);
		topicSet.setMeasureName("ndcg_cut");
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


	public String getDescription() {
		this.description = "<font color=\"black\" size=\"4\"><b>ndcg_cut: Normalized Discounted Cumulative Gain at cutoffs.</b></font>" +
				"<br><br>"+
				"Compute a traditional nDCG measure according to Jarvelin and " +
				"Kekalainen (ACM ToIS v. 20, pp. 422-446, 2002) at cutoffs. " +
				"See comments for ndcg. " +
				"Gain values are the relevance values in the qrels file.  For now, if you " +
				"want different gains, change the qrels file appropriately. " +
				"Cutoffs must be positive without duplicates "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}

}
