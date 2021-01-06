package br.uefs.analyzIR.ui.graph.wizard;

import java.util.List;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.structure.WizardListener;
import br.uefs.analyzIR.ui.structure.WizardProcess;

public class CreateGraphProcess extends WizardProcess {
	
	public CreateGraphProcess(Facade facade) {
		super(facade);
	}

	@Override
	public void finish() {
		for(WizardListener listener : this.listeners){
			this.data.putData("listener", listener);
			listener.update(this.data);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override 
	public void update(){
		if(stepIndex == this.steps.size()){
			this.data.putData("facade", facade);
			finish();
		}else{
			if(stepIndex == 2){
				List<String> measures = (List<String>) data.getData("measures");
				if(facade.getMeasureType(measures) == 2){
					stepIndex = 3;
					steps.get(stepIndex).putDataInfo(data);
					steps.get(stepIndex).putCommandProcess(facade);
					steps.get(stepIndex).run();
					stepIndex++;
				}else{
					steps.get(stepIndex).putDataInfo(data);
					steps.get(stepIndex).putCommandProcess(facade);
					steps.get(stepIndex).run();
					stepIndex++;
					steps.remove(3);
				}
			}else{
				steps.get(stepIndex).putDataInfo(data);
				steps.get(stepIndex).putCommandProcess(facade);
				steps.get(stepIndex).run();
				stepIndex++;
			}
		}
	}
}
