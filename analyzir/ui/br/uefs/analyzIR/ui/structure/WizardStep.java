package br.uefs.analyzIR.ui.structure;

import br.uefs.analyzIR.controller.Facade;

/**
 *
 */
public interface WizardStep extends ScreenView{

	/**
	 * adiciona o processo de configuração que deve ser alertado de seu fim
	 * 
	 * @param wProcess a wizardProcess 
	 */
	public void addWizardProcessListener(WizardProcess wProcess); 
	
	/**
	 * inicia as atividades com a apresentação da screenview - sempre chama o metodo show ou make. 
	 */
	public void run(); 
	
	/**
	 * é chamada quando finaliza toda a atividade. Ao finalizar a atividade esse método deve chamar o método de notificação do processo - para que o proximo
	 * passo seja executado
	 */
	public void stop(); 
	
	/**
	 * notifica ao processo que o estágio ou etapa acabou e que pode chamar o proximo. 
	 */
	public void notifyWizardProcesses(); 
	
	
	public void putCommandProcess(Facade facade);
	
	
}
