package br.uefs.analyzIR.ui.screenView;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.ScreenView;
import br.uefs.analyzIR.ui.structure.WizardListener;

public class ScreenViewManager implements WizardListener{

	private TabManager manager;
	private SideBar sideBar; 
	
	public ScreenViewManager(Facade facade){
		this.manager = new TabManager();
		this.sideBar = new SideBar(facade); 
	}
	
	public void addScreenView(String name, ScreenView view){
		view.getDataInfo().putData("listener", this);
		this.sideBar.addItem(name);
		this.manager.addTab(view);
	}
	
	public void replaceScreenView(String oldName, ScreenView view){
		
		String indexData = view.getDataInfo().getData("index").toString();
		String newName = view.getDataInfo().getData("name").toString();
		int index = Integer.parseInt(indexData);
		
		manager.replaceTab(index, view);
		
		System.err.println("Vai remover " + oldName + " da sidebar");
		sideBar.removeItem(oldName);
		sideBar.addItem(newName);
	}
	
	public void removeScreenView(int index){
		this.manager.removeTab(index);
	}
	
	public void removeScreenView(String name){
		this.sideBar.removeItem(name);
		this.manager.removeTab(name);
	}

	public JPanel makeSideBar() {
		return this.sideBar.make();
	}

	public void removeAllComboInfo() {
		this.sideBar.removeAll();
	}

	public void removeAllTabs() {
		this.manager.removeAllTabs();
	}

	public JTabbedPane getTabbedPane() {
		return this.manager.getTabbedPane();
	}

	@Override
	public void update() {
	
		
	}

	@Override
	public void update(DataInfo info) {
		
		JPanel graph = (JPanel) info.getData("view");
		Integer index = (info.getData("index") == null ? -1 : Integer.parseInt(info.getData("index").toString()));
		
		Facade facade = (Facade)info.getData("facade");
		
		ScreenView view = new GraphScreenView(graph, facade);		
		view.putDataInfo(info);
	
		if(index == -1){
			String name = info.getData("name").toString();
			this.addScreenView(name, view);
		}else{
			/*String oldName = info.getData("oldName").toString();
			this.replaceScreenView(oldName, view);*/
			String name = info.getData("name").toString();
			this.replaceScreenView(name, view);
		}	
	}

	@Override
	public void update(DataInfo info, ScreenView view) {
		
		
	}
	
}
