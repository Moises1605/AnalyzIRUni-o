package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class RPrec implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public RPrec(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue( String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m Rprec " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("Rprec");
		return topicSet;
	}

	@Override
	public MeasureSet getValue( String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException,
			ItemNotFoundException, InvalidItemNameException {

		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m Rprec " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("Rprec");
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
		this.description = "<font color=\"black\" size=\"4\"><b>Rprec: Precision after R documents have been retrieved.</b></font>" +
				"<br><br>"+
				"R - Precision is the precision after R documents have been retrieved, where R is the number of " +
				"relevant documents for the topic. It de-emphasizes the exact ranking of the retrieved relevant " +
				"documents, which can be particularly useful in TREC where there are large numbers of relevant " +
				"documents.<br> " +
				"The average R-Precision for a run is computed by taking the mean of the R-Precisions of the " +
				"individual topics in the run. For example, assume a run consists of two topics, one with 50 " +
				"relevant documents and another with 10 relevant documents. If the retrieval system returns 17 " +
				"relevant documents in the top 50 documents for the first topic, and 7 relevant documents in the " +
				"top 10 for the second topic, then the run’s R-Precision would be " +
				" <math><mfrac>(17/50 + 7/10)/2 or 0.52</mfrac> " +
				"<br><br>"+
				"Source: <a href=\"http://trec.nist.gov/pubs/trec16/appendices/measures.pdf\">http://trec.nist.gov/pubs/trec16/appendices/measures.pdf</a> ";

		return this.description;
	}
}