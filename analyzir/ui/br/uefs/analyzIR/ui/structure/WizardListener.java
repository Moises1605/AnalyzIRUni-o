package br.uefs.analyzIR.ui.structure;

/**
 *
 */
public interface WizardListener {

	public void update();
	
	public void update(DataInfo info); 
	
	public void update(DataInfo info, ScreenView view); 
}
