package br.uefs.analyzIR.ui.structure;

import javax.swing.JPanel;

/**
 * ScreenView is a representation of a full or part of wizard form. Every ScreenView in AnalyzIR tool have to
 * implement this interface to be integrated. A ScreenView object saves all user data entered or temporary values
 * in key-value {@link DataInfo} object. The {@link #show()} method has to show up a full wizard form.
 * On the other hand the {@link #make()} method has to create a part of a wizard form (a content pane).
 * @author lucas
 *
 */
public interface ScreenView {

	/**
	 * Puts data info objects used to save user entered or temporary process values.
	 * @param dataInfo wizard local memory
	 */
	public void putDataInfo(DataInfo dataInfo);

	/**
	 * Returns ScreenView local memory.
	 * @return all user data entered saved and/or temporary values
	 */
	public DataInfo getDataInfo();

	/**
	 * Shows up a full wizard form.
	 */
	public void show(); 
	
	/**
	 * Returns a part of a wizard form.
	 * @return wizard form panel
	 */
	public JPanel make();

}
