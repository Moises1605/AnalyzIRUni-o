package br.uefs.analyzIR.ui.standardProject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.MeasuresGroupNotFoundException;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;
import br.uefs.analyzIR.ui.util.DataTableModel;

public class DataGraphSettingWizardStep implements WizardStep{


	private Font font;
	private JFrame frame;

	private JTable tbRuns;
	private JTable tbSelectedRuns;
	private JTable tbTopics;
	private JTable tbSelectedTopics;
	private JTable tbMeasures;
	private JTable tbSelectedMeasures;

	private JScrollPane jspRuns;
	private JScrollPane jspSelectedRuns;
	private JScrollPane jspTopics;
	private JScrollPane jspSelectedTopics;
	private JScrollPane jspMeasure;
	private JScrollPane jspSelectedMeasures;

	private JButton btnSingleR;
	private JButton btnAllR;
	private JButton btnSingleBackR;
	private JButton btnAllBackR;

	private JButton btnSingleT;
	private JButton btnAllT;
	private JButton btnSingleBackT;
	private JButton btnAllBackT;

	private JButton btnSingleM;
	private JButton btnAllM;
	private JButton btnSingleBackM;
	private JButton btnAllBackM;

	private JButton btnNext;
	private JButton btnCancel;

	private Facade facade;
	private DataInfo info;
	private WizardProcess wProcess; 

	
	public DataGraphSettingWizardStep() {
	}

	@Override
	public void show()  {
		
		initComponents();
		initLayout();
		loadInfo();
		loadDataInfo();
		initFunctions();
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private void initComponents() {

		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

		frame = new JFrame("Graph - Graph Settings");

		jspRuns = new JScrollPane();
		jspSelectedRuns = new JScrollPane();
		jspTopics = new JScrollPane();
		jspSelectedTopics = new JScrollPane();
		jspMeasure = new JScrollPane();
		jspSelectedMeasures = new JScrollPane();

		tbRuns = new JTable();
		tbSelectedRuns = new JTable();
		tbTopics = new JTable();
		tbSelectedTopics = new JTable();
		tbMeasures = new JTable();
		tbSelectedMeasures = new JTable();

		createScroll(jspRuns);
		createScroll(jspSelectedRuns);
		createScroll(jspTopics);
		createScroll(jspSelectedTopics);
		createScroll(jspMeasure);
		createScroll(jspSelectedMeasures);

		createTable(new ArrayList<String>(), tbRuns, jspRuns, "Runs");
		createTable(new ArrayList<String>(), tbSelectedRuns, jspSelectedRuns, "Selected Runs");
		createTable(new ArrayList<String>(), tbTopics, jspTopics, "Topics");
		createTable(new ArrayList<String>(), tbSelectedTopics,
				jspSelectedTopics, "Selected Topics");
		createTable(new ArrayList<String>(), tbMeasures, jspMeasure, "Measures");
		createTable(new ArrayList<String>(), tbSelectedMeasures,
				jspSelectedMeasures, "Selected Measures");

		btnSingleR = new JButton(">");
		btnAllR = new JButton(">>");
		btnSingleBackR = new JButton("<");
		btnAllBackR = new JButton("<<");

		btnSingleT = new JButton(">");
		btnAllT = new JButton(">>");
		btnSingleBackT = new JButton("<");
		btnAllBackT = new JButton("<<");

		btnSingleM = new JButton(">");
		btnAllM = new JButton(">>");
		btnSingleBackM = new JButton("<");
		btnAllBackM = new JButton("<<");

		btnNext = new JButton("Next");
		btnCancel = new JButton("Cancel");

		font = new Font(Font.SANS_SERIF, Font.BOLD, 13);

		btnNext.setFont(font);
		btnCancel.setFont(font);
	}

	private void initLayout() {

		String columns = "20dlu, 200dlu, 20dlu, 30dlu, 20dlu, 100dlu, 60dlu, 5dlu, 50dlu, 30dlu, 10dlu";
		String rows = "20dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, "
				+ "5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, "
				+ "5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu";

		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);

		builder.add(jspRuns, CC.xywh(2, 2, 1, 7));
		builder.add(jspSelectedRuns, CC.xywh(6, 2, 5, 7));
		builder.add(jspTopics, CC.xywh(2, 13, 1, 7));
		builder.add(jspSelectedTopics, CC.xywh(6, 13, 5, 7));
		builder.add(jspMeasure, CC.xywh(2, 24, 1, 7));
		builder.add(jspSelectedMeasures, CC.xywh(6, 24, 5, 7));

		builder.add(btnSingleR, CC.xy(4, 2));
		builder.add(btnAllR, CC.xy(4, 4));
		builder.add(btnAllBackR, CC.xy(4, 6));
		builder.add(btnSingleBackR, CC.xy(4, 8));

		builder.add(btnSingleT, CC.xy(4, 13));
		builder.add(btnAllT, CC.xy(4, 15));
		builder.add(btnAllBackT, CC.xy(4, 17));
		builder.add(btnSingleBackT, CC.xy(4, 19));

		builder.add(btnSingleM, CC.xy(4, 24));
		builder.add(btnAllM, CC.xy(4, 26));
		builder.add(btnAllBackM, CC.xy(4, 28));
		builder.add(btnSingleBackM, CC.xy(4, 30));

		builder.add(btnNext, CC.xy(9, 32));
		builder.add(btnCancel, CC.xy(7, 32));

		frame.setContentPane(builder.getPanel());
	}
	
	/***
	 * 
	 */
	private void initCommumFunctions(){
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<String> runs = new ArrayList<String>(); 
				List<String> topics = new ArrayList<String>();
				List<String> measures = new ArrayList<String>(); 
				
				DataTableModel mRuns = (DataTableModel) tbSelectedRuns.getModel(); 
				DataTableModel mTopics = (DataTableModel) tbSelectedTopics.getModel(); 
				DataTableModel mMeasures = (DataTableModel) tbSelectedMeasures.getModel(); 
				
				for(int i = 0; i < mRuns.getRowCount(); i++){
					String r = mRuns.getValueAt(i, 1).toString();
					runs.add(r);
				}
					
				for(int j = 0; j < mTopics.getRowCount();j++){
					String topic = mTopics.getValueAt(j, 1).toString();
					topics.add(topic);
				}
				
				for(int i = 0; i < mMeasures.getRowCount(); i++){
					String value = mMeasures.getValueAt(i, 1).toString();
					measures.add(value);
				}
				
				int type = facade.getMeasureType(measures);
				
				info.putData("measures", measures);
				info.putData("runs", runs);
				info.putData("topics", topics);
				info.putData("type", type);
			
				stop();
				
			}
		});
	}
	
	private void initFunctions() {
		initRunFunctions();
		initTopicFunctions();
		initMeasureFunctions();
		initCommumFunctions();
	}
	
	private void loadInfo() {

		List<String> runs = facade.listRuns();
		List<String> topics = facade.listTopics();
		List<String> measures = facade.listMeasures();

		DataTableModel dtruns = (DataTableModel) tbRuns.getModel();
		DataTableModel dttopics = (DataTableModel) tbTopics.getModel();
		DataTableModel dtmeasures = (DataTableModel) tbMeasures.getModel();

		for (String r : runs) {
			dtruns.add(r);
		}
		dtruns.fireTableDataChanged();

		for (String t : topics) {
			dttopics.add(t);
		}
		dttopics.fireTableDataChanged();

		for (String m : measures) {
			dtmeasures.add(m);
		}
		dtmeasures.fireTableDataChanged();
		
	}
	
	@SuppressWarnings("unchecked")
	private void loadDataInfo(){
	
		if(this.info.getDataInfoSize() != 0){
		
			List<String> measures = (List<String>) this.info.getData("measures");
			List<String> topics = (List<String>) this.info.getData("topics");
			List<String> runs = (List<String>) this.info.getData("runs");
		
			DataTableModel selectedMeasures = (DataTableModel) tbSelectedMeasures.getModel();
			DataTableModel selectedRuns = (DataTableModel) tbSelectedRuns.getModel();
			DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel(); 
			
			DataTableModel tbr = (DataTableModel) tbRuns.getModel();
			DataTableModel tbt = (DataTableModel) tbTopics.getModel(); 
			
			//initialize the selected runs table
			for(String run : runs){
				selectedRuns.add(run);
			}
			//
			for(String topic : topics){
				selectedTopics.add(topic);
			}	
			//initialize the selected measures table
			for(String measure : measures){
				selectedMeasures.add(measure);
			}
			
			//update measure table
			try {
				updateMeasures(measures);
			} catch (MeasuresGroupNotFoundException e) {
				
			}
			
			//update run table
			for(int i = (tbr.getRowCount() - 1); i >= 0; i--){
				String value = tbr.getValueAt(i, 1).toString();
				if(runs.contains(value)){
					tbr.remove(i, 1);
				}
			}
			
			for(int i = (tbt.getRowCount() - 1); i >= 0; i--){
				String value = tbt.getValueAt(i, 1).toString();
				if(runs.contains(value)){
					tbt.remove(i, 1);
				}
			}
			
			tbr.fireTableDataChanged();
			tbt.fireTableDataChanged();
		}
	}

	private void initRunFunctions(){
		
		btnSingleR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
				int [] rows = tbRuns.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = runs.getValueAt(rows[i], 1).toString();
						selectedRuns.add(value);
						runs.remove(rows[i], 1);
					}
					runs.fireTableDataChanged();
					selectedRuns.fireTableDataChanged();
				}
			}
		});
		
		btnSingleBackR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
				int [] rows = tbSelectedRuns.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = selectedRuns.getValueAt(rows[i], 1).toString();
						runs.add(value);
						selectedRuns.remove(rows[i], 1);
					}
					runs.fireTableDataChanged();
					selectedRuns.fireTableDataChanged();
				}
			}
		});
		btnAllR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
			 
				for(int i = runs.getRowCount() - 1; i >= 0; i--){
					String value = runs.getValueAt(i, 1).toString();
					selectedRuns.add(value);
					runs.remove(i, 1);
				}
				runs.fireTableDataChanged();
				selectedRuns.fireTableDataChanged();
			}
		});

		btnAllBackR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
			 
				for(int i = selectedRuns.getRowCount() - 1; i >= 0; i--){
					String value = selectedRuns.getValueAt(i, 1).toString();
					runs.add(value);
					selectedRuns.remove(i, 1);
				}
				runs.fireTableDataChanged();
				selectedRuns.fireTableDataChanged();
			}
		});
		
		
	}
	
	private void initTopicFunctions(){
		
		ActionListener pass = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
				int [] rows = tbTopics.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = topics.getValueAt(rows[i], 1).toString();
						selectedTopics.add(value);
						topics.remove(rows[i], 1);
					}
					topics.fireTableDataChanged();
					selectedTopics.fireTableDataChanged();
				}
			}
		};
		
		ActionListener passAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
			 
				for(int i = topics.getRowCount() - 1; i >= 0; i--){
					String value = topics.getValueAt(i, 1).toString();
					selectedTopics.add(value);
					topics.remove(i, 1);
				}
				topics.fireTableDataChanged();
				selectedTopics.fireTableDataChanged();
			}
		};
		
		ActionListener back = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
				int [] rows = tbSelectedTopics.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = selectedTopics.getValueAt(rows[i], 1).toString();
						topics.add(value);
						selectedTopics.remove(rows[i], 1);
					}
					topics.fireTableDataChanged();
					selectedTopics.fireTableDataChanged();
				}
			}
		};
		
		ActionListener backAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
			 
				for(int i = selectedTopics.getRowCount() - 1; i >= 0; i--){
					String value = selectedTopics.getValueAt(i, 1).toString();
					topics.add(value);
					selectedTopics.remove(i, 1);
				}
				topics.fireTableDataChanged();
				selectedTopics.fireTableDataChanged();
			}
		};
	
		btnSingleT.addActionListener(pass);
		btnSingleBackT.addActionListener(back);
		btnAllT.addActionListener(passAll);
		btnAllBackT.addActionListener(backAll);
	}
	
	private void initMeasureFunctions(){
		
		ActionListener pass = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				List<String> selected = new ArrayList<>();
				DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
				DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel(); 
				int [] rows = tbMeasures.getSelectedRows();
				
				for(int i = 0; i < rows.length; i++){
					String v = measures.getValueAt(rows[i], 1).toString();
					selected.add(v);
					measures.remove(i, 1);
					selectedMeasures.add(v);
				}
				measures.fireTableDataChanged();
				selectedMeasures.fireTableDataChanged();
				
			}
		};
		
		ActionListener passAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<String> selected = new ArrayList<>();
				DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
				DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel(); 
				 
				for(int i = measures.getRowCount() - 1; i >= 0; i--){
					String value = measures.getValueAt(i, 1).toString();
					selected.add(value);	
				}
				
				int type = facade.getMeasureType(selected);
				if(type != -1){
					for(int i = measures.getRowCount() - 1; i >= 0; i--){
						String value = measures.getValueAt(i, 1).toString();
						selectedMeasures.add(value);
						measures.remove(i, 1);
					}
				}
					
				measures.fireTableDataChanged();
				selectedMeasures.fireTableDataChanged();
			}
		};
		
		ActionListener back = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
				DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel(); 
				int [] rows = tbSelectedMeasures.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = selectedMeasures.getValueAt(rows[i], 1).toString();
						measures.add(value);
						selectedMeasures.remove(rows[i], 1);
					}
					measures.fireTableDataChanged();
					selectedMeasures.fireTableDataChanged();
					
					if(selectedMeasures.getRowCount() == 0){
						updateMeasures();
					}
				}
			}
		};
		
		ActionListener backAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				updateMeasures();
			}
		};
		
		btnSingleM.addActionListener(pass);
		btnSingleBackM.addActionListener(back);
		btnAllM.addActionListener(passAll);
		btnAllBackM.addActionListener(backAll);
	}
	
	@SuppressWarnings("unused")
	private void removeExtraMeasures() {
		
		DataTableModel model = (DataTableModel) tbMeasures.getModel();
		
		for(int i = model.getRowCount() - 1; i >= 0; i--){
			String value = model.getValueAt(i, 1).toString();
			if(value.equalsIgnoreCase("gmap") || value.equalsIgnoreCase("GM_BPref")){
				model.remove(i, 1);
			}
		}
		
		model.fireTableDataChanged();
	}

	private void updateMeasures() {
		
		DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
		List<String> values = facade.listMeasures();
		for(int i = (measures.getRowCount() - 1); i >= 0; i--){
			measures.remove(i, 1);
		}
		for(String measure : values){
			measures.add(measure);
		}
		measures.fireTableDataChanged();
	}
	
	@SuppressWarnings("unused")
	private void updateTopics() {
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
		DataTableModel selected = (DataTableModel) tbSelectedTopics.getModel();
		List<String> top = facade.listTopics();
		
		for(int i = (selected.getRowCount() - 1); i >= 0; i--){
			String value = selected.getValueAt(i, 1).toString();
			top.remove(value);
		}
		
		for(String v : top){
			topics.add(v);
		}
		
		topics.fireTableDataChanged();
	}

	private void updateMeasures(List<String> auxMeasures)throws MeasuresGroupNotFoundException {
	
		List<String> newMeasures = facade.getMeasureGroup(auxMeasures);
		DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
		
		for(int i = (measures.getRowCount() - 1); i >= 0; i--){
			String value = measures.getValueAt(i, 1).toString();
			if(!newMeasures.contains(value)){
				measures.remove(i, 1);
			}
		}
		measures.fireTableDataChanged();
	}

	private void createTable(List<String> items, JTable table,JScrollPane scroll, String name) {
		DataTableModel model = new DataTableModel(items);
		table.setModel(model);
		model.addTableModelListener(table);
		table.setBorder(new LineBorder(Color.black));
		table.setGridColor(Color.black);
		table.setShowGrid(true);
		table.setFont(font);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setHeaderValue(name);     
		table.getTableHeader().resizeAndRepaint();
		table.setAlignmentX(Component.LEFT_ALIGNMENT);
		scroll.setViewportView(table);
	}

	private void createScroll(JScrollPane scroll) {
		scroll.setPreferredSize(new Dimension(150, 150));
		scroll.setSize(new Dimension(150, 150));
		scroll.getViewport().setBorder(null);
		scroll.getViewport().setSize(50, 50);
		scroll.setFont(font);
		scroll.setVisible(true);
	}
	

	@Override
	public void putDataInfo(DataInfo dataInfo) {
		this.info = dataInfo;
	}

	@Override
	public JPanel make() {
		return null;
	}

	@Override
	public void addWizardProcessListener(WizardProcess wProcess) {
		this.wProcess = wProcess;
	}

	@Override
	public void run() {
		this.show();
	}

	@Override
	public void stop() {
		this.notifyWizardProcesses();
		this.frame.dispose();
	}

	@Override
	public void notifyWizardProcesses() {
		this.wProcess.update();
	}

	@Override
	public void putCommandProcess(Facade facade) {
		this.facade = facade;
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
	}
}