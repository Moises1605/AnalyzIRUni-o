package br.uefs.analyzIR.ui.graph.wizard;

import java.util.List;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.interactiveProject.InteractiveDataGraphSetting;
import br.uefs.analyzIR.ui.structure.WizardListener;
import br.uefs.analyzIR.ui.structure.WizardProcess;

public class CreateInteractiveGraphProcess extends WizardProcess{

	public CreateInteractiveGraphProcess(Facade facade) {
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
			if(stepIndex == 1) {

				/*Caso o usuário selecione a realização de testes estatísticos o fluxo é redirecionado
				para a janela de testes estátisticos.*/
				InteractiveDataGraphSetting DataGraph = (InteractiveDataGraphSetting)steps.get(0);
				Boolean confirmação = (Boolean) steps.get(0).getDataInfo().getData("confirmacao");
				if(confirmação) {

					steps.get(stepIndex).putDataInfo(data);
					steps.get(stepIndex).putCommandProcess(facade);
					steps.get(stepIndex).run();
					stepIndex++;

				}else{
					stepIndex = 2;
					List<String> measures = (List<String>) data.getData("measures");
					if (facade.getInteractiveMeasureType(measures) == 1){

						stepIndex = 4;
						steps.get(stepIndex).putDataInfo(data);
						steps.get(stepIndex).putCommandProcess(facade);
						steps.get(stepIndex).run();
						stepIndex++;
					}else{
						steps.get(stepIndex).putDataInfo(data);
						steps.get(stepIndex).putCommandProcess(facade);
						steps.get(stepIndex).run();
						stepIndex++;
						steps.remove(4);
					}
				}
			}
			else if (stepIndex == 2){
				List<String> measures = (List<String>) data.getData("measures");
				if (facade.getInteractiveMeasureType(measures) == 1){

					stepIndex = 4;
					steps.get(stepIndex).putDataInfo(data);
					steps.get(stepIndex).putCommandProcess(facade);
					steps.get(stepIndex).run();
					stepIndex++;
				}else{
					steps.get(stepIndex).putDataInfo(data);
					steps.get(stepIndex).putCommandProcess(facade);
					steps.get(stepIndex).run();
					stepIndex++;
					steps.remove(4);
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


