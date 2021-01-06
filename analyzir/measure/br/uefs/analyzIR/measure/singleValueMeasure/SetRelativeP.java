package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class SetRelativeP implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public SetRelativeP(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m set_relative_P " + this.context.getQrels().getPathQrel() + " "
				+ r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("set_relative_P");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m set_relative_P " + this.context.getQrels().getPathQrel() + " "
				+ r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("set_relative_P");
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
		this.description ="<font color=\"black\" size=\"4\"><b>set_relative_P: Relative Set Precision.</b></font>" +
				"<br><br> " +
				"set_relative_P =  P / (Max possible P for this size set) " +
				"Relative precision over all docs retrieved for a topic. " +
				"Was known as exact_relative_prec in earlier versions of trec_eval "
				+ "<br><br> " + "Adapted from trec_eval code.";
		return this.description;
	}


}
