package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class Avg11pt implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public Avg11pt(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m 11pt_avg " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("avg11pt");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m 11pt_avg " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber); 
		topicSet.setMeasureName("avg11pt");
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
		java.net.URL url = getClass().getResource("/sun.png");
		java.net.URL url2 = getClass().getResource("/11ptavg.png");

		this.description = "<font color=\"black\" size=\"4\"><b>11pt_avg: Interpolated Precision averaged over 11 recall points.</b></font>" +
				"<br><br>"+
				"OThe precision averages at 11 standard recall levels are used to compare the performance of" +
				"different systems and as the input for plotting the recall-precision graph (see below). Each" +
				"recall-precision average is computed" +
				"by summing the interpolated precisions at the specified" +
				"recall cutoff value (denoted by <img src=" +url+ " width=\"20\" height=\"15\"></img>"+
				"P<font size=\"2\">λ</font> where P<font size=\"2\">λ</font> is the interpolated precision at recall level λ) and" +
				"then dividing by the number of topics.<br>"+
				"<img src=" +url2+ "></img><br>"+
				"<br><br>"+
				"Source: <a href=\"http://trec.nist.gov/pubs/trec16/appendices/measures.pdf\">http://trec.nist.gov/pubs/trec16/appendices/measures.pdf</a> ";

		return this.description;
	}

}
