package br.uefs.analyzIR.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.RunItem;
import br.uefs.analyzIR.data.Topic;
import br.uefs.analyzIR.exception.DiferentsSystemsException;
import br.uefs.analyzIR.exception.InvalidRunNumberException;
import br.uefs.analyzIR.exception.TopicNotFoundException;

public class ProjectAnalyzer {

	public void systemsChecker() {}


	public List<Topic> extractIntersection(Run [] runs) throws InvalidRunNumberException{
		
		if(runs.length > 1){
			
			List<Topic> topics = runs[0].getTopics();
			Set<Topic> values = new HashSet<Topic>();
			boolean contain = true;
			
			for(Topic t : topics){
				contain = true;
				for(int i = 0; i < runs.length && contain; i++){
					if(runs[i].getTopics().contains(t)){
						contain = true;
					}else{
						contain = false;
					}
				}
				if(contain){
					values.add(t);
				}
			}
			
			List<Topic> result = new ArrayList<Topic>(); 
			for(Topic t : values){
				result.add(t);
			}
			return result;
		}else{
			throw new InvalidRunNumberException("You have to choose minimum of 2 runs. Please try again.");
		}
	}
	
	public List<Topic> extractIntersection(ArrayList<Run> runs) throws InvalidRunNumberException{
		
		if(runs.size() > 1){
			
			List<Topic> topics = runs.get(0).getTopics();
			Set<Topic> values = new HashSet<Topic>();
			boolean contain = true;
			
			for(Topic t : topics){
				contain = true;
				for(int i = 0; i < runs.size() && contain; i++){
					if(runs.get(i).getTopics().contains(t)){
						contain = true;
					}else{
						contain = false;
					}
				}
				if(contain){
					values.add(t);
				}
			}
			
			List<Topic> result = new ArrayList<Topic>(); 
			for(Topic t : values){
				result.add(t);
			}
			return result;
		}else{
			throw new InvalidRunNumberException("You have to choose minimum of 2 runs. Please try again.");
		}
	}
	
	public Run[] createNewRuns(List<Topic> topics, Run[] oldRuns)
			throws TopicNotFoundException {

		Run[] runs = new Run[oldRuns.length];
		for (int i = 0; i < runs.length; i++) {
			runs[i] = new Run();
			runs[i].setName("run"+ i +":"+oldRuns[i].getName());
			runs[i].setTopics(topics);
			for (Topic topic : topics) {
				List<RunItem> items = oldRuns[i].getRunItems(topic.getName());
				runs[i].addRunItems(topic.getName(), items);
				runs[i].setMaxInteration(oldRuns[i].getMaxInteration());
			}
		}
		return runs;
	}
	
	public ArrayList<Run> createNewRuns(List<Topic> topics, ArrayList<Run> oldRuns)
			throws TopicNotFoundException {

		ArrayList<Run> runs = new ArrayList<Run>();
		for (int i = 0; i < oldRuns.size(); i++) {
			runs.add(new Run());
			runs.get(i).setName("run"+ i +":"+oldRuns.get(i).getName());
			runs.get(i).setTopics(topics);
			for (Topic topic : topics) {
				List<RunItem> items = oldRuns.get(i).getRunItems(topic.getName());
				runs.get(i).addRunItems(topic.getName(), items);
				runs.get(i).setMaxInteration(oldRuns.get(i).getMaxInteration());
			}
		}
		return runs;
	}

	public boolean checkSame(Run[] runs) throws DiferentsSystemsException {

		boolean check = false;
		for (int i = 0; i < runs.length; i++) {
			for (int j = i+1; j < runs.length; j++) {
				Run ri = runs[i];
				Run rj = runs[j];
				if (ri.getTopics().containsAll(rj.getTopics()) && rj.getTopics().containsAll(ri.getTopics())) {
					check =  true;
				}
			}
		}
		if(check)
			return true; 
		
		throw new DiferentsSystemsException(
				"The systems do not contain the same topics. Would you like to use the intersection?");
	}
	
	public boolean checkSame(ArrayList<Run> runs) throws DiferentsSystemsException {

		boolean check = false;
		for (int i = 0; i < runs.size(); i++) {
			for (int j = i+1; j < runs.size(); j++) {
				Run ri = runs.get(i);
				Run rj = runs.get(j);
				if (ri.getTopics().containsAll(rj.getTopics()) && rj.getTopics().containsAll(ri.getTopics())) {
					check =  true;
				}
			}
		}
		if(check)
			return true; 
		
		throw new DiferentsSystemsException(
				"The systems do not contain the same topics. Would you like to use the intersection?");
	}

	public boolean hasIntersection() {
	
		//TODO 
		return true;
	}
}
