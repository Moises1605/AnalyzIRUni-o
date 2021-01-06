package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class NDCG implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public NDCG(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m ndcg " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("ndcg");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m ndcg " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("ndcg");
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
		this.description= "<font color=\"black\" size=\"4\"><b>ndcg: Normalized Discounted Cumulative Gain.</b></font>" +
				"<br><br>"+
				"Compute a traditional nDCG measure according to Jarvelin and " +
				"Kekalainen (ACM ToIS v. 20, pp. 422-446, 2002) " +
				"Gain values are set to the appropriate relevance level by default. " +
				"The default gain can be overridden on the command line by having " +
				"comma separated parameters 'rel_level=gain'. " +
				"Eg, 'trec_eval -m ndcg.1=3.5,2=9.0,4=7.0 ...' " +
				"will give gains 3.5, 9.0, 3.0, 7.0 for relevance levels 1,2,3,4 " +
				"respectively (level 3 remains at the default). " +
				"Gains are allowed to be 0 or negative, and relevance level 0 " +
				"can be given a gain. "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}


}
