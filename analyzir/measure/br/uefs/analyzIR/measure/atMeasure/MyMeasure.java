package br.uefs.analyzIR.measure.atMeasure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Qrels;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.RunItem;
import br.uefs.analyzIR.data.Topic;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.RunItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.measure.AtMeasure;
import br.uefs.analyzIR.measure.data.MeasureSet;


public class MyMeasure implements AtMeasure{

	private Context context;
	private String description;
	
	public String getName() {
		return "My Precision";
	}

	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public MeasureSet getValue(String run, String at) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException, ItemNotFoundException {
		
		Run r1 = this.context.getRunByName(run); 
		Qrels q1 = this.context.getQrels(); 
		
		int atValue = Integer.parseInt(at);
		
		Long relevant = (long) 0;
		double v = 0;
		
		try {
		
			for(Topic topic : r1.getTopics()){
			
				List<RunItem> items = r1.getRunItems(topic.getName());
				
				for(int i = 0; i < atValue; i++){
					if(q1.isRelevant(items.get(i).getQuery_id(), items.get(i).getDocno())){
						relevant++;
					}
				}
			
				double c = relevant / (double)atValue;
				v += c;
			}
			
			System.out.println("o valor de v ="+v );
			v =  v / r1.getTopics().size();
			
			MeasureSet set = new MeasureSet("My Precision");
			set.addTopicResult("all");
			set.addValue("all", at, v);
			
			
			return set;
		} catch (TopicNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QrelItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidQrelFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public MeasureSet getValue(String run, String at, String[] topics) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException, ItemNotFoundException {
		
		Run r1 = this.context.getRunByName(run); 
		Qrels q1 = this.context.getQrels(); 
		int atValue = Integer.parseInt(at);
		MeasureSet set = new MeasureSet("My Precision");
		
		for(String topic: topics){
			
			try {
				
				List<RunItem> items = r1.getRunItems(topic);
				Long relevant = (long) 0;
			
				for(int i = 0; i < atValue; i++){
					if(q1.isRelevant(items.get(i).getQuery_id(), items.get(i).getDocno())){
						relevant++;
					}
				}
				
				double v = relevant / (double)atValue;
			
			set.addTopicResult(topic);
			set.addValue(topic, at, v);
			
			return set;
			
		} catch (TopicNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QrelItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidQrelFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		return null;
	}

	public static void main(String [] args) throws FileNotFoundException, IOException, RunItemNotFoundException, QrelItemNotFoundException, InvalidQrelFormatException, NumberFormatException, InterruptedException, InvalidItemNameException, ItemNotFoundException{
		
		Run r [] = {new Run("/home/lucas/teste de evidencia/data/System1_runs")};
		Qrels q = new Qrels("/home/lucas/teste de evidencia/data/fullqrels.trec");
		
		r[0].initialize();
		q.initialize();
		
		
		Context c = new Context(r,q, null); 
		
		MyMeasure m = new MyMeasure(); 
		m.setContext(c);
		
		MeasureSet set = m.getValue(r[0].getName(), "20");
		
		PAtN p = new PAtN("at");
		p.setContext(c);
		
		MeasureSet set2 = p.getValue(r[0].getName(), "20");
		
		
		System.out.println("My Precision: "+set.getTopicResult("AVG").getValues().get(0).getValue());
		System.out.println("Precision: "+set2.getTopicResult("AVG").getValues().get(0).getValue());
		
		
		
	}


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}