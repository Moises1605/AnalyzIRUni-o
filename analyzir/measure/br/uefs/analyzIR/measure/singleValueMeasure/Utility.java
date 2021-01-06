
package br.uefs.analyzIR.measure.singleValueMeasure;

import java.io.IOException;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.SingleValueMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.util.MeasureCommand;

public class Utility implements SingleValueMeasure {

	private MeasureCommand mCommand;
	private String name;
	private String description;
	private Context context;
	
	public Utility(String name) {
		this.name = name; 
		mCommand = MeasureCommand.getInstance();
	}

	@Override
	public MeasureSet getValue(String run) throws IOException,
			InterruptedException, InvalidItemNameException,
			ItemNotFoundException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -m utility " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command);
		topicSet.setMeasureName("utility");
		return topicSet;
	}

	@Override
	public MeasureSet getValue(String run, String[] topicNumber)
			throws IOException, InterruptedException, NumberFormatException, ItemNotFoundException, InvalidItemNameException {
		
		Run r = this.context.getRunByName(run);
		String command = "./trec_eval -q -m utility " + this.context.getQrels().getPathQrel() + " " + r.getPathRunFile();
		MeasureSet topicSet = mCommand.executeCommand(command, topicNumber);
		topicSet.setMeasureName("utility");
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
		this.description = "<font color=\"black\" size=\"4\"><b>utility: Set utility measure.</b></font>" +
				"<br><br>"+
				"\t  Set evaluation based on contingency table:<br>" +
				" \t\t\t\t\t\t                       relevant  nonrelevant<br>" +
				"\t\t       retrieved\t\t            a\t\t          b<br>" +
				"\t\t       nonretrieved\t\t         c\t\t         d<br>" +
				"\t    where  utility = p1 * a + p2 * b + p3 * c + p4 * d<br>" +
				"\t   and p1-4 are parameters (given on command line in that order).<br>" +
				"\t    Conceptually, each retrieved relevant doc is worth something positive to<br>" +
				"\t    a user, each retrieved nonrelevant doc has a negative worth, each <br>" +
				"\t    relevant doc not retrieved may have a negative worth, and each<br>" +
				"\t    nonrelevant doc not retrieved may have a (small) positive worth.<br>" +
				"\t    The overall measure is simply a weighted sum of these values.<br>" +
				"\t    If p4 is non-zero, then '-N num_docs_in_coll' may also be needed - the<br>" +
				"\t    standard results and rel_info files do not contain that information.<br>" +
				"\t   "
				+"<br><br>"+"Adapted from trec_eval code.";
		return this.description;
	}




}
