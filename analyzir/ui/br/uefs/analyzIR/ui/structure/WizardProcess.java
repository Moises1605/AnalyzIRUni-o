package br.uefs.analyzIR.ui.structure;

import java.util.ArrayList;
import java.util.List;

import br.uefs.analyzIR.controller.Facade;

/**
 * WizardProcess represents an user functionality divided in small steps. In a WizarProcess the number of steps could
 * be variant. A WizardProcess object is as flow as could be changed depending on data info values and how
 * {@link #update} method is overriding. Each WizardProcess has a set of listeners waiting the process activity finish.
 * A WizardProcess has a relationship with Facade class because some requests should be done by a functionality.
 * WizardProcess share a single key-value {@link DataInfo} object for all steps in a process. It helps write and load
 * data more easily between the steps.
 */
public abstract class WizardProcess {
	
	protected List<WizardStep> steps;
	protected Facade facade;
	protected List<WizardListener> listeners;
	protected DataInfo data; // shared memory
	protected int stepIndex; // control wizardProcess flow

	/**
	 * Constructs a new Process with Facade access.
	 * @param facade facade of controllers objects
	 */
	public WizardProcess(Facade facade){
		this.steps = new ArrayList<WizardStep>(); 
		this.listeners = new ArrayList<>();
		this.data = new DataInfo();
		this.stepIndex = 0; //starts in step 0
		this.facade = facade;
	}

	/**
	 * Starts WizardProcess if steps list has a step and update step index to next step.
	 */
	public final void start(){
		if(steps.size() > 0){
			steps.get(0).putDataInfo(data);
			steps.get(0).putCommandProcess(facade);
			steps.get(0).run();
			stepIndex++;
		}
	}

	/**
	* Finishes the WizardProcess and notifies all of listeners that activity is completed.
	* */
	public abstract void finish();

	/**
	 * Updates WizardProcess to next step depending of flow condition control. This method must be
	 * overridden if a process has more than one flow, preferentially use info from dataInfo object to
	 * control the execution.
	 */
	public void update(){
		if(stepIndex == this.steps.size()){
			finish();
		}else{
			steps.get(stepIndex).putDataInfo(data);
			steps.get(stepIndex).putCommandProcess(facade);
			steps.get(stepIndex).run();
			stepIndex++;
		}
	}

	/**
	 * Sets the WizardProcess shared memory.
	 * @param info a key-value object that stores data of all steps.
	 */
	public void setDataInfo(DataInfo info){
		this.data = info;
	}

	/***
	 * Adds a WizardListener.
	 * @param wizard
	 */
	public void addWizardListener(WizardListener wizard){
		this.listeners.add(wizard);
	}

	/**
	 * Removes a WizardListener.
	 * @param wizard
	 */
	public void removeWizardListener(WizardListener wizard){
		this.listeners.remove(wizard);
	}

	/**
	 * Adds a new WizardStep to WizardProcess flow.
	 * @param wStep
	 */
	public void addWizardStep(WizardStep wStep){
		this.steps.add(wStep);
	}
	
	/**
	 * Removes a WizardStep of WizardProcess flow.
	 * @param wStep
	 */
	public void removeWizardStep(WizardStep wStep){
		this.steps.remove(wStep);
	}
	
	/**
	 * Removes a WizardStep of WizardProcess at wStepIndex.
	 * @param wStepIndex
	 */
	public void removeWizardStep(int wStepIndex){
		this.steps.remove(wStepIndex);
	}
	
	
}
