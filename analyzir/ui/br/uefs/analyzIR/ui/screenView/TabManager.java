package br.uefs.analyzIR.ui.screenView;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

import br.uefs.analyzIR.ui.structure.ScreenView;

public class TabManager {
	
	private JTabbedPane tbpPlots;
	private ActionListener listener;
	private int index;
	
	public TabManager() {
		tbpPlots = new JTabbedPane();
		index = 0;
		initListener();
	}
	
	public JTabbedPane getTabbedPane(){
		return tbpPlots;
	}
		
	public void addTab(ScreenView screenView) {
			
		String name = screenView.getDataInfo().getData("name").toString();

		if(!hasTab(name)){
			screenView.getDataInfo().putData("index", index);
			tbpPlots.add(name, screenView.make());
			tbpPlots.setTabComponentAt(index, new Tab(name, this.listener));
			tbpPlots.setSelectedIndex(index);

			index++;
		}
	}

	public void removeTab(int index) {
			
		tbpPlots.remove(index);
		this.index--;
	}

	public void replaceTab(int index, ScreenView newView) {
		
		String name = newView.getDataInfo().getData("name").toString();
		
		tbpPlots.remove(index);
		tbpPlots.add(newView.make(),name, index); 
		tbpPlots.setTabComponentAt(index, new Tab(name,this.listener));
		tbpPlots.setSelectedIndex(index);	
	}

	public void removeTab(String name) {
		
		boolean verify = false;
		for(int i = 0; i < tbpPlots.getTabCount() && !verify; i++){	
			Tab tab = (Tab)tbpPlots.getTabComponentAt(i);
			if(tab.getName().equals(name)){
				this.removeTab(i);
				verify = true;
			}
		}
	}

	protected boolean hasTab(String name){
		for(int i = 0; i < tbpPlots.getTabCount(); i++){
			Tab tab = (Tab)tbpPlots.getTabComponentAt(i);
			if(tab.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	public void removeAllTabs() {
		for(int i = tbpPlots.getTabCount() - 1; i >= 0; i--){
			tbpPlots.remove(i);
		}
		index = 0;
	}
	
	private void initListener(){
		this.listener = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Component button = (Component)e.getSource();
	        	Tab aux = (Tab)button.getParent();
	        	
	        	int i = tbpPlots.indexOfTabComponent(aux);
	            if (i != -1) {
	            	removeTab(i);
	            }
			}
		};
	}

}
