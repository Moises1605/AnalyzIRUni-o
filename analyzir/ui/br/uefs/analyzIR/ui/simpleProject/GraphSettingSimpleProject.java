package br.uefs.analyzIR.ui.simpleProject;

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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.MeasuresGroupNotFoundException;
import br.uefs.analyzIR.ui.graph.wizardStep.BarGraphFinalizeSetting;
import br.uefs.analyzIR.ui.graph.wizardStep.CurveGraphFinalizeSetting;
import br.uefs.analyzIR.ui.util.DataTableModel;
import br.uefs.analyzIR.ui.util.PlotGUIMaker;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class GraphSettingSimpleProject {

	private Facade facade;

	private Font font;
	private JFrame frame;

	private JTable tbTopics;
	private JTable tbSelectedTopics;
	private JTable tbMeasures;
	private JTable tbSelectedMeasures;

	private JScrollPane jspTopics;
	private JScrollPane jspSelectedTopics;
	private JScrollPane jspMeasure;
	private JScrollPane jspSelectedMeasures;

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

	private PlotGUIMaker maker;

	public GraphSettingSimpleProject(Facade facade, PlotGUIMaker maker) {
		this.facade = facade;
		this.maker =  maker;
	}

	
	public void show() {
		
		initComponents();
		initLayout();
		if(this.maker != null){
			try {
				loadInfoMaker();
			} catch (MeasuresGroupNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}else{
			loadInfo();
		}
		initFunctions();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private void initComponents() {

		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

		frame = new JFrame("New Graph - Graph Settings");

		jspTopics = new JScrollPane();
		jspSelectedTopics = new JScrollPane();
		jspMeasure = new JScrollPane();
		jspSelectedMeasures = new JScrollPane();

		tbTopics = new JTable();
		tbSelectedTopics = new JTable();
		tbMeasures = new JTable();
		tbSelectedMeasures = new JTable();

		createScroll(jspTopics);
		createScroll(jspSelectedTopics);
		createScroll(jspMeasure);
		createScroll(jspSelectedMeasures);

		createTable(new ArrayList<String>(), tbTopics, jspTopics, "Topics");
		createTable(new ArrayList<String>(), tbSelectedTopics,
				jspSelectedTopics, "Selected Topics");
		createTable(new ArrayList<String>(), tbMeasures, jspMeasure, "Measures");
		createTable(new ArrayList<String>(), tbSelectedMeasures,
				jspSelectedMeasures, "Selected Measures");
	
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

	private void loadInfo() {

		List<String> topics = facade.listTopics();
		List<String> measures = facade.listMeasures();

		DataTableModel dttopics = (DataTableModel) tbTopics.getModel();
		DataTableModel dtmeasures = (DataTableModel) tbMeasures.getModel();

		for (String t : topics) {
			dttopics.add(t);
		}
		dttopics.fireTableDataChanged();

		for (String m : measures) {
			dtmeasures.add(m);
		}
		dtmeasures.fireTableDataChanged();
	}
	
	private void loadInfoMaker() throws MeasuresGroupNotFoundException{
		
		List<String> measures = maker.getMeasures(); 
		List<String> topics = maker.getTopics(); 
		List<String> lTopics = facade.listTopics();
		
		DataTableModel dtsTopics = (DataTableModel) tbSelectedTopics.getModel(); 
		DataTableModel dtTopics = (DataTableModel) tbTopics.getModel();
		DataTableModel dtSelectedMeasures = (DataTableModel) tbSelectedMeasures.getModel();
		DataTableModel dtsMeasures = (DataTableModel) tbMeasures.getModel();
		
		for(String measure : measures){
			dtSelectedMeasures.add(measure);
		}
		
		List<String> group = facade.getMeasureGroup(measures);
		for(String m : group){
			if(!measures.contains(m)){
				dtsMeasures.add(m);
			}
		}
		
		for(String t : lTopics){
			if(!topics.contains(t)){
				dtTopics.add(t);
			}
		}
		
		for(String topic : topics){
			dtsTopics.add(topic);
		}
		
		dtsMeasures.fireTableDataChanged();
		dtsTopics.fireTableDataChanged();
		dtTopics.fireTableDataChanged();
		dtSelectedMeasures.fireTableDataChanged();
		
	}

	private void initLayout() {

		String columns = "20dlu, 200dlu, 20dlu, 30dlu, 20dlu, 100dlu, 50dlu, 5dlu, 50dlu, 20dlu";
		String rows = "20dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, "
				+ "5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu";

		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);

		builder.add(jspTopics, CC.xywh(2, 2, 1, 7));
		builder.add(jspSelectedTopics, CC.xywh(6, 2, 4, 7));
		builder.add(jspMeasure, CC.xywh(2, 13, 1, 7));
		builder.add(jspSelectedMeasures, CC.xywh(6, 13, 4, 7));

		builder.add(btnSingleT, CC.xy(4, 2));
		builder.add(btnAllT, CC.xy(4, 4));
		builder.add(btnAllBackT, CC.xy(4, 6));
		builder.add(btnSingleBackT, CC.xy(4, 8));

		builder.add(btnSingleM, CC.xy(4, 13));
		builder.add(btnAllM, CC.xy(4, 15));
		builder.add(btnAllBackM, CC.xy(4, 17));
		builder.add(btnSingleBackM, CC.xy(4, 19));

		builder.add(btnNext, CC.xy(9, 23));
		builder.add(btnCancel, CC.xy(7, 23));

		frame.setContentPane(builder.getPanel());
	}

	private void initFunctions() {
		initTopicFunctions();
		initMeasureFunctions();
		initCommonFunctions();
	}
	
	private void updateMeasures(List<String> auxMeasures) throws MeasuresGroupNotFoundException{
		
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
	
	private void initCommonFunctions(){
	
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<String> topics = new ArrayList<String>();
				List<String> measures = new ArrayList<String>(); 
				List<String> runs = new ArrayList<String>();
				DataTableModel mTopics = (DataTableModel) tbSelectedTopics.getModel(); 
				DataTableModel mMeasures = (DataTableModel) tbSelectedMeasures.getModel(); 

				String run = facade.listRuns().get(0);
				runs.add(run);
				
				for(int i = 0; i < mTopics.getRowCount(); i++){
					String value = mTopics.getValueAt(i, 1).toString();
					topics.add(value);
				}
				
				for(int i = 0; i < mMeasures.getRowCount(); i++){
					String value = mMeasures.getValueAt(i, 1).toString();
					measures.add(value);
				}
				
				if(maker == null){
					maker = new PlotGUIMaker(measures,runs, topics);
				}else{
					maker.setMeasures(measures);
					maker.setTopics(topics);
				}
				
				int type = facade.getMeasureType(measures);
				
				if(type == 0 || type == 1){
					new BarGraphFinalizeSetting().show();
				}else{
					new CurveGraphFinalizeSetting().show();
				}
				frame.dispose();
			}
		});
	
	}
	
	private void initTopicFunctions(){
		btnSingleT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkerTopicRule();
			}
		});
		
		btnAllT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkerAllTopics();
			}
		});
		btnSingleBackT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkerSingleBackTopics();
			}
		});
		
		btnAllBackT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkerAllBakTopics();
			}
		});
	}

	private void initMeasureFunctions(){
		
		btnSingleM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					checkerMeasureRule();
				} catch (MeasuresGroupNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAllM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkerAllMeasures();
			}
		});
		btnSingleBackM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkerSingleBackMeasures();
			}
		});
		btnAllBackM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkerAllBackMeasures();
			}
		});	
	}
	
	
	private void checkerTopicRule(){
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel();
		DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();
		DataTableModel measures = (DataTableModel) tbSelectedMeasures.getModel();
		int [] rows = tbTopics.getSelectedRows();
		
		if(rows.length > 0){
			if(rows.length == 1){
				String value = topics.getValueAt(rows[0],1).toString();
				if(!value.equals("AVG")){
					removeExtraMeasures();
				}else{
					btnSingleT.setEnabled(false);
				}
				if(measures.getRowCount() > 1){
					btnSingleT.setEnabled(false);
					btnAllT.setEnabled(false);
				}else if(measures.getRowCount() == 1){
					String measure = measures.getValueAt(0, 1).toString();
					if(measure.equals("Precision x Recall Curve")){
						btnSingleT.setEnabled(false);
						btnAllT.setEnabled(false);
					}
				}
			}else{
				removeExtraMeasures();
				if(rows.length == topics.getRowCount()){
					btnAllT.setEnabled(false);
					btnSingleT.setEnabled(false);
				}
				btnAllM.setEnabled(false);
			}
			
			for(int i = rows.length - 1; i >= 0; i--){
				String value = topics.getValueAt(rows[i], 1).toString();
				topics.remove(rows[i], 1);
				selectedTopics.add(value);
			}
			
			topics.fireTableDataChanged();
			selectedTopics.fireTableDataChanged();
		}
	}
	
	private void checkerAllTopics(){
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel();
		DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();
		DataTableModel measures = (DataTableModel) tbMeasures.getModel();
		
		for(int i = topics.getRowCount() - 1; i >= 0; i--){
			String value = topics.getValueAt(i, 1).toString();
			topics.remove(i, 1);
			selectedTopics.add(value);
		}
		
		if(measures.getRowCount() == 1){
			btnSingleM.setEnabled(false);
		}
		removeExtraMeasures();
		btnAllM.setEnabled(false);
		
		topics.fireTableDataChanged();
		selectedTopics.fireTableDataChanged();
	}
	
	private void checkerSingleBackTopics(){
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel();
		DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();
		DataTableModel measures = (DataTableModel) tbSelectedMeasures.getModel();
		int [] rows = tbSelectedTopics.getSelectedRows();
		
		if(rows.length > 0){
			int lines = selectedTopics.getRowCount() - rows.length;
			if(lines <= 1){
				
				btnSingleT.setEnabled(true);
				btnAllT.setEnabled(true);
				btnSingleM.setEnabled(true);
				btnAllM.setEnabled(true);
				
				if(measures.getRowCount() == 0){
					updateMeasures();
				}else if(measures.getRowCount() == 1){
					String value = measures.getValueAt(0, 1).toString();
					if(value.equals("Precision x Recall Curve")){
						btnAllT.setEnabled(false);
						btnAllM.setEnabled(false);
						btnSingleM.setEnabled(false);
					}else if(value.equalsIgnoreCase("GMAP") || value.equalsIgnoreCase("gm_bpref")){
						btnAllT.setEnabled(false);
						btnSingleM.setEnabled(false);
						btnAllM.setEnabled(false);
					}
				}else{
					btnAllT.setEnabled(false);
				}
			}else {
				if(measures.getRowCount() == 1)
					btnSingleM.setEnabled(false);
				else
					btnAllM.setEnabled(false);
			}
			
			for(int i = rows.length - 1; i >= 0; i--){
				String value = selectedTopics.getValueAt(rows[i], 1).toString();
				selectedTopics.remove(rows[i], 1);
				topics.add(value);
			}
			
			topics.fireTableDataChanged();
			selectedTopics.fireTableDataChanged();
		}
	}
	
	private void checkerAllBakTopics(){
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel();
		DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();
		DataTableModel measures = (DataTableModel) tbSelectedMeasures.getModel();
		
		for(int i = selectedTopics.getRowCount() - 1; i >= 0; i--){
			String value = selectedTopics.getValueAt(i, 1).toString();
			selectedTopics.remove(i, 1);
			topics.add(value);
		}
		
		if(measures.getRowCount() == 1){
			String value = measures.getValueAt(0, 1).toString();
			if(value.equals("Precision x Recall Curve")){
				btnSingleM.setEnabled(false);
				btnAllM.setEnabled(false);
			}else{
				btnSingleM.setEnabled(true);
				btnAllBackM.setEnabled(true);
			}
			btnSingleT.setEnabled(true);
		}else if(measures.getRowCount() == 0){
			btnAllM.setEnabled(true);
			btnSingleM.setEnabled(true);
			btnAllT.setEnabled(true);
			btnSingleT.setEnabled(true);
			updateMeasures();
		}else{
			btnAllM.setEnabled(true);
			btnSingleM.setEnabled(true);
			btnSingleT.setEnabled(true);
		}
		
		topics.fireTableDataChanged();
		selectedTopics.fireTableDataChanged();
	}
	
	private void checkerMeasureRule() throws MeasuresGroupNotFoundException{
		
		DataTableModel measures = (DataTableModel) tbMeasures.getModel();
		DataTableModel selectedMeasures = (DataTableModel) tbSelectedMeasures.getModel();
		DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();
		int [] rows = tbMeasures.getSelectedRows();
		
		if(rows.length > 0){
			
			if(rows.length == 1){
				String value = measures.getValueAt(rows[0], 1).toString(); 
				if(value.equalsIgnoreCase("GMAP") || value.equalsIgnoreCase("gm_bpref")){
					removeTopics();
					btnAllM.setEnabled(false);
					btnSingleM.setEnabled(false);
					btnAllT.setEnabled(false);
				}else if(value.equals("Precision x Recall Curve")){
					btnSingleM.setEnabled(false);
					btnAllM.setEnabled(false);
					btnAllT.setEnabled(false);
				}
				if(selectedMeasures.getRowCount() > 1){
					if(selectedTopics.getRowCount() >= 1){
						btnAllT.setEnabled(false);
						btnSingleT.setEnabled(false);
					}else{
						btnAllT.setEnabled(false);
					}
						
				}
				if(selectedTopics.getRowCount() > 1){
					btnSingleM.setEnabled(false);
				}
			}else{
				if(selectedTopics.getRowCount() > 1){
					JOptionPane.showMessageDialog(null, "You cannot select more than a measure. Pleasure choose only a measure", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else if(selectedTopics.getRowCount() == 1){
					btnSingleT.setEnabled(false);
					btnAllT.setEnabled(false);
				}else{
					btnAllT.setEnabled(false);
				}
			}
			
			int begin = (rows.length == 0 ? 0 : rows.length - 1);
			List<String> auxMeasures = new ArrayList<String>();
			for(int i = begin; i >= 0 ; i--){
				String value = measures.getValueAt(rows[i], 1).toString(); 
				auxMeasures.add(value);
			}
			
			try{
				updateMeasures(auxMeasures);
				for(String value : auxMeasures){
					selectedMeasures.add(value);
				}
				selectedMeasures.fireTableDataChanged();
				measures.fireTableDataChanged();
			}catch(MeasuresGroupNotFoundException error){
				JOptionPane.showMessageDialog(null, error.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void checkerAllMeasures(){
		
		DataTableModel selectedMeasures = (DataTableModel) tbSelectedMeasures.getModel(); 
		DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
		
		if(selectedMeasures.getRowCount() > 0){
			for(int i = (measures.getRowCount() - 1); i >= 0; i--){
				String value = measures.getValueAt(i, 1).toString(); 
				selectedMeasures.add(value);
				measures.remove(i, 1);
			}
			selectedMeasures.fireTableDataChanged();
			measures.fireTableDataChanged();
		}else{
			JOptionPane.showMessageDialog(null, "Error: You cannot select measures of differents types.");
		}
	}
	
	private void checkerAllBackMeasures(){
		
		DataTableModel selectedMeasures = (DataTableModel) tbSelectedMeasures.getModel(); 
		if(selectedMeasures.getRowCount() > 0){
			updateMeasures();
			for(int i = (selectedMeasures.getRowCount() - 1); i >= 0;i--){
				selectedMeasures.remove(i, 1);
			}
			btnAllT.setEnabled(true);
			btnSingleT.setEnabled(true);
			selectedMeasures.fireTableDataChanged();
		}
	}
	
	private void checkerSingleBackMeasures(){
		
		DataTableModel measures = (DataTableModel) tbMeasures.getModel();
		DataTableModel selectedMeasures = (DataTableModel) tbSelectedMeasures.getModel();
	
		int [] rows = tbSelectedMeasures.getSelectedRows();
		
		if(rows.length > 0){		
			int lines = selectedMeasures.getRowCount() - rows.length;
			if(lines == 0){
				btnAllM.setEnabled(false);
				btnSingleM.setEnabled(true);
				btnSingleT.setEnabled(true);
				btnAllT.setEnabled(true);
				updateMeasures();
				
				if(selectedMeasures.getRowCount() == 1){
					updateTopics();
				}
			}else{
				
			}
		}
			
		int begin = (rows.length == 0 ? 0 : rows.length - 1);
		for(int i = begin; i >= 0; i--){
			String value = selectedMeasures.getValueAt(rows[i],1).toString();
			measures.add(value);
			selectedMeasures.remove(rows[i], 1);
		}
		measures.fireTableDataChanged();
		selectedMeasures.fireTableDataChanged();
	
	}
	private void updateTopics() {
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
		DataTableModel selected = (DataTableModel) tbSelectedTopics.getModel();
		List<String> values = facade.listTopics();
		
		if(selected.getRowCount() > 0){
			for(int i = selected.getRowCount() - 1; i >= 0; i--){
				String value = selected.getValueAt(i, 1).toString();
				if(values.contains(value)){
					values.remove(value);
				}
			}
		}
		
		for(String v : values){
			topics.add(v);
		}
		topics.fireTableDataChanged();
	}


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

	private void removeTopics() {
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
		DataTableModel selected = (DataTableModel) tbSelectedTopics.getModel();
		
		for(int i = (topics.getRowCount() - 1); i >= 0; i--){
			topics.remove(i,1);
		}
		
		if(selected.getRowCount() > 0){
			String value = selected.getValueAt(0,1).toString();
			if(!value.equals("AVG")){
				topics.add("AVG");
			}
		}else{
			topics.add("AVG");
		}
		topics.fireTableDataChanged();
	}


	private void createTable(List<String> items, JTable table,
			JScrollPane scroll, String name) {

		DataTableModel model = new DataTableModel(items);
		table.setModel(model);
		model.addTableModelListener(table);
		table.setBorder(new LineBorder(Color.black));
		table.setGridColor(Color.black);
		table.setShowGrid(true);
		table.setFont(font);
		table.setRowHeight(30);
		table.setAlignmentX(Component.LEFT_ALIGNMENT);
		table.getColumnModel().getColumn(0).setHeaderValue(name);     
		table.getTableHeader().resizeAndRepaint();
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
}