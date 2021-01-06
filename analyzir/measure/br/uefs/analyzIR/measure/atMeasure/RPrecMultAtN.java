package br.uefs.analyzIR.measure.atMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.AtMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class RPrecMultAtN implements AtMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public RPrecMultAtN(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run, String atValue)
			throws IOException, InterruptedException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m Rprec_mult." + atValue + " " + this.context.getQrels().getPathQrel() + " "+ r.getPathRunFile();
		String[] topic = { "all" };
		MeasureSet topicSet = mCommand.executeCommand(command, topic, atValue);
		topicSet.setMeasureName("Rprec_mult_"+atValue);
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String atValue,
			String[] topicNumber) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m Rprec_mult." + atValue + " "
		+ this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber, atValue);
		topicSet.setMeasureName("Rprec_mult_"+atValue);
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
		this.description = "<font color=\"black\" size=\"4\"><b>Rprec_mult: Precision measured at multiples of R (num_rel)</b></font>" +
				"<br><br>"+
				"This is an attempt to measure topics at the same multiple milestones " +
				"in a retrieval (see explanation of R-prec), in order to determine " +
				"whether methods are precision oriented or recall oriented.  If method A " +
				"dominates method B at the low multiples but performs less well at the " +
				"high multiples then it is precision oriented (compared to B). "
				+"<br><br>"+"Adapted from trec_eval code.";
		return this.description;
	}
}
