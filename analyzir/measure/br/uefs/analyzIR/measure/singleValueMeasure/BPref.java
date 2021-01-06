package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class BPref implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;

	public BPref(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m bpref " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("bpref");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m bpref " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("bpref");
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
		java.net.URL url = getClass().getResource("/bpref.png");
		this.description = "<font color=\"black\" size=\"4\"><b>bpref: binary preference-based measure.</b></font>" +
				"<br><br>"+
				"The bpref measure is designed for situations where relevance judgments are known to be far from complete." +
				"It was introduced in the TREC 2005 terabyte track. bpref computes a preference relation of whether judged" +
				"relevant documents are retrieved ahead of judged irrelevant documents. Thus, it is based on the relative" +
				"ranks of judged documents only. The bpref measure is defined as.<br>"+
				"<img src=" +url+ "></img><br>"+
				"where R is the number of judged relevant documents, N is the number of judged irrelevant documents, r is" +
				"a relevant retrieved document, and n is a member of the first R irrelevant retrieved documents. Note that" +
				"this definition of bpref is different from that which is commonly cited, and follows the actual implementation" +
				"in trec eval version 8.0; see the file bpref bug in the trec eval distribution for details."+
				"<br><br>"+
				"Source: <a href=\"http://trec.nist.gov/pubs/trec16/appendices/measures.pdf\">http://trec.nist.gov/pubs/trec16/appendices/measures.pdf</a> ";
		return this.description;
	}

}
