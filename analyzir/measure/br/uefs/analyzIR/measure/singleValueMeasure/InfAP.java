package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class InfAP implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public InfAP(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m infAP " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("infAP");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m infAP " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("infAP");
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
		this.description = "<font color=\"black\" size=\"4\"><b>infAP: Inferred AP.</b></font>" +
				"<br><br>"+
				"A measure that allows sampling of judgement pool: Qrels/results divided " +
				"into unpooled, pooled_but_unjudged, pooled_judged_rel,pooled_judged_nonrel. " +
				"My intuition of infAP: " +
				"Assume a judgment pool with a random subset that has been judged. " +
				"Calculate P at rel doc using only the judged higher retrieved docs, " +
				"then average in 0's from higher docs that were not in the judgment pool. " +
				"(Those in the pool but not judged are ignored, since they are assumed " +
				"to be relevant in the same proportion as those judged.) " +
				"Cite:    'Estimating Average Precision with Incomplete and Imperfect " +
				"Judgments', Emine Yilmaz and Javed A. Aslam. CIKM "
				+"<br><br>"+"Adapted from trec_eval code. ";
		return this.description;
	}

}
