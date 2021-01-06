package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class GMAP implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public GMAP(String name) {
		this.name = name;
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m gm_map " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		System.out.println(command);
		MeasureSet topicSet =  mCommand.executeCommand(command);
		topicSet.setMeasureName("gm_map");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException {

		return null;
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
		java.net.URL url = getClass().getResource("/gmap1.png");
		java.net.URL url2 = getClass().getResource("/gmap2.png");


		this.description = "<font color=\"black\" size=\"4\"><b>gm_map: Geometric Mean Average Precision.</b></font>" +
				"<br><br>"+
				"The GMAP measure is designed for situations where you want to highlight improvements for low-performing " +
				"topics. It was introduced in the TREC 2004 robust track. GMAP is the geometric mean of per-topic average " +
				"precision, in contrast with MAP which is the arithmetic mean. If a run doubles the average precision for " +
				"topic A from 0.02 to 0.04, while decreasing topic B from 0.4 to 0.38, the arithmetic mean is unchanged, but " +
				"the geometric mean will show an improvment.<br>"+
				"The geometric mean is defined as n-th root of the product of n values:<br>"+

				"<img src=" +url+ "></img><br>"+

				"where n is typically 50 for TREC tasks. Alternatively, it can be calculated as an arithmetic mean of logs:<br>"+

				"<img src=" +url2+ "></img><br>"+

				"In the trec eval implementation, all individual average precision scores that are less than 0.00001 are set to 0.00001 to avoid taking logs of 0.0."+
				"<br><br>"+
				"Source: <a href=\"http://trec.nist.gov/pubs/trec16/appendices/measures.pdf\">http://trec.nist.gov/pubs/trec16/appendices/measures.pdf</a> ";
		return this.description;
	}
}
