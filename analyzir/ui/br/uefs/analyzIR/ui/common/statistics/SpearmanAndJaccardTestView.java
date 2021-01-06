package br.uefs.analyzIR.ui.common.statistics;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.ui.util.DataTableModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpearmanAndJaccardTestView {

	private JFrame frame;
	private JComboBox<String> comboMeasures;
	private JComboBox<String> comboCorrelationMeasures;
	private JTable tbTopics;
	private JTable tbSystems;
	private JTable tbSelectedTopics;
	private JTable tbSelectedSystems;
	private JScrollPane jspSystems;
	private JScrollPane jspSelectedSystems;
	private JScrollPane jspTopics;
	private JScrollPane jspSelectedTopics;
	private JButton btnSingle;
	private JButton btnAll;
	private JButton btnBackSingle;
	private JButton btnBackAll;
	private JButton btnSingleSyst;
	private JButton btnAllSyst;
	private JButton btnBackSingleSyst;
	private JButton btnBackAllSyst;
	private Font font;
	private Facade facade;
	private JButton btnRun;
	private JButton btnClose;
	private JTextField txtAtValue;

	public SpearmanAndJaccardTestView(Facade facade){
		this.facade = facade;
	}
	
	public void show(){
		initComponents();
		initLayout();
		initFunctions();
		initValues();
		frame.pack();
		frame.setVisible(true);
	}
	
	private void initComponents(){
		
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
		
		this.frame = new JFrame("Rank Correlation Test");
		
		this.comboMeasures = new JComboBox<String>();
		this.comboCorrelationMeasures = new JComboBox<String>();
		
		this.txtAtValue = new JTextField(20);
		
		this.tbTopics = new JTable();
		this.tbSelectedTopics = new JTable();
		this.jspTopics = new JScrollPane();
		this.jspSelectedTopics = new JScrollPane();

		this.tbSystems = new JTable();
		this.tbSelectedSystems = new JTable();
		this.jspSystems = new JScrollPane();
		this.jspSelectedSystems = new JScrollPane();
		
		this.btnSingle = new JButton(">");
		this.btnAll = new JButton(">>"); 
		this.btnBackSingle = new JButton("<"); 
		this.btnBackAll = new JButton("<<");

		this.btnSingleSyst = new JButton(">");
		this.btnAllSyst = new JButton(">>");
		this.btnBackSingleSyst = new JButton("<");
		this.btnBackAllSyst = new JButton("<<");

		this.btnRun = new JButton("Run");
		this.btnClose = new JButton("Close");
	
		createTable(new ArrayList<String>(), tbTopics, jspTopics, "Topics");
		createScroll(jspTopics);
		createTable(new ArrayList<String>(), tbSelectedTopics, jspSelectedTopics, "Selected Topics");
		createScroll(jspSelectedTopics);

		createTable(new ArrayList<String>(), tbSystems, jspSystems, "Systems");
		createScroll(jspSystems);
		createTable(new ArrayList<String>(), tbSelectedSystems, jspSelectedSystems, "Selected Systems");
		createScroll(jspSelectedSystems);
	
		this.comboMeasures.setFont(font);
		this.comboCorrelationMeasures.setFont(font);
		
		this.txtAtValue.setFont(font);
		
		this.tbTopics.setFont(font);
		this.tbSelectedTopics.setFont(font);
		this.jspTopics.setFont(font);
		this.jspSelectedTopics.setFont(font);

		this.tbSystems.setFont(font);
		this.tbSelectedSystems.setFont(font);
		this.jspSystems.setFont(font);
		this.jspSelectedSystems.setFont(font);
		
		this.btnSingle.setFont(font);
		this.btnAll.setFont(font);
		this.btnBackSingle.setFont(font);
		this.btnBackAll.setFont(font);

		this.btnSingleSyst.setFont(font);
		this.btnAllSyst.setFont(font);
		this.btnBackSingleSyst.setFont(font);
		this.btnBackAllSyst.setFont(font);

		this.btnRun.setFont(font);
		this.btnClose.setFont(font);
	}
	
	private void initLayout(){
		
		String columns = "10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu, 40dlu, 10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu";
		String rows = "5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu,5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, "
				+ "30dlu, 5dlu, 50dlu, 5dlu, 30dlu,5dlu, 30dlu";
		
		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);

		builder.addLabel("Correl. Test:", CC.xy(2, 10));
		builder.add(comboCorrelationMeasures, CC.xy(4, 10));

		builder.add(jspSystems, CC.xywh(2, 2, 3, 7));
		builder.add(jspSelectedSystems, CC.xywh(8, 2, 3, 7));

		builder.add(btnSingleSyst, CC.xy(6, 2));
		builder.add(btnAllSyst, CC.xy(6, 4));
		builder.add(btnBackAllSyst, CC.xy(6, 6));
		builder.add(btnBackSingleSyst, CC.xy(6, 8));

		builder.addLabel("AtValue:", CC.xy(8, 10));
		builder.add(txtAtValue, CC.xyw(10, 10, 1));

		builder.add(jspTopics, CC.xywh(2, 12, 3, 7));
		builder.add(jspSelectedTopics, CC.xywh(8, 12, 3, 7));
		
		builder.add(btnSingle, CC.xy(6, 12));
		builder.add(btnAll, CC.xy(6, 14));
		builder.add(btnBackAll, CC.xy(6, 16));
		builder.add(btnBackSingle, CC.xy(6, 18));
		builder.add(btnRun, CC.xy(6, 19));
		builder.add(btnClose, CC.xy(6, 25));
		
		frame.setContentPane(builder.getPanel());
	}
	
	private void initFunctions(){
		
		btnSingle.addActionListener(new ActionListener() {
			
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
		});
		
		btnBackSingle.addActionListener(new ActionListener() {
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
		});
		
		btnAll.addActionListener(new ActionListener() {
			
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
		});
		
		btnBackAll.addActionListener(new ActionListener() {
			
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
		});

		btnSingleSyst.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DataTableModel systems = (DataTableModel) tbSystems.getModel();
				DataTableModel selectedSystems =(DataTableModel) tbSelectedSystems.getModel();
				int [] rows = tbSystems.getSelectedRows();

				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1);
					for(int i = begin; i >= 0; i--){
						String value = systems.getValueAt(rows[i], 1).toString();
						selectedSystems.add(value);
						systems.remove(rows[i], 1);
					}
					systems.fireTableDataChanged();
					selectedSystems.fireTableDataChanged();
				}
			}
		});

		btnBackSingleSyst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel systems = (DataTableModel) tbSystems.getModel();
				DataTableModel selectedSystems =(DataTableModel) tbSelectedSystems.getModel();
				int [] rows = tbSelectedSystems.getSelectedRows();

				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1);
					for(int i = begin; i >= 0; i--){
						String value = selectedSystems.getValueAt(rows[i], 1).toString();
						systems.add(value);
						selectedSystems.remove(rows[i], 1);
					}
					systems.fireTableDataChanged();
					selectedSystems.fireTableDataChanged();
				}
			}
		});

		btnAllSyst.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel systems = (DataTableModel) tbSystems.getModel();
				DataTableModel selectedSystems =(DataTableModel) tbSelectedSystems.getModel();

				for(int i = systems.getRowCount() - 1; i >= 0; i--){
					String value = systems.getValueAt(i, 1).toString();
					selectedSystems.add(value);
					systems.remove(i, 1);
				}
				systems.fireTableDataChanged();
				selectedSystems.fireTableDataChanged();
			}
		});

		btnBackAllSyst.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel systems = (DataTableModel) tbSystems.getModel();
				DataTableModel selectedSystems =(DataTableModel) tbSelectedSystems.getModel();

				for(int i = selectedSystems.getRowCount() - 1; i >= 0; i--){
					String value = selectedSystems.getValueAt(i, 1).toString();
					systems.add(value);
					selectedSystems.remove(i, 1);
				}
				systems.fireTableDataChanged();
				selectedSystems.fireTableDataChanged();
			}
		});
		
		btnRun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				List<String> measures = new ArrayList<String>(); 
				DataTableModel mTopics = (DataTableModel) tbSelectedTopics.getModel();
				DataTableModel mSystems = (DataTableModel) tbSelectedSystems.getModel();
				

					
					String atValue = txtAtValue.getText().trim();
					String [] values = checkValues(atValue);

					List<String> topics = new ArrayList<String>();
					for(int i = mTopics.getRowCount() - 1; i >= 0; i--){
						String value = mTopics.getValueAt(i, 1).toString();
						topics.add(value);
					}

					List<String> systems = new ArrayList<String>();
					for(int i = mSystems.getRowCount() - 1; i >= 0; i--){
						String value = mSystems.getValueAt(i, 1).toString();
						systems.add(value);
					}

					String correlationTestSelected = comboCorrelationMeasures.getSelectedItem().toString();

					try {
						String[][][] correlationMatrices = facade.correlationTest(systems,atValue,topics,correlationTestSelected);
						List<String> topicsPlusAverage = topics;
						topicsPlusAverage.add("Average");
                        SpearmanAndJaccardOutputView outputView = new SpearmanAndJaccardOutputView(correlationMatrices,systems,topicsPlusAverage);
						outputView.show();
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (TopicNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}

			}
		});
		
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}
	
	private void initValues(){
	
		List<String> topics = facade.listTopics();
		List<String> measures = facade.getMeasuresByType(0);
		List<String> runs = facade.listRuns();
		//List<String> wlcxBibs = facade.listWilcoxonBibs();
		DataTableModel mTopics = (DataTableModel) tbTopics.getModel();
		DataTableModel mSystems = (DataTableModel) tbSystems.getModel();
		
		topics.remove("AVG");
		measures.addAll(facade.getMeasuresByType(1));
		measures.remove("GMAP");
		measures.remove("GM_BPref");
		
		Collections.sort(topics);
		Collections.sort(measures);
		
		for(String t : topics){
			if(!t.equals("AVG")){
				mTopics.add(t);
			}
		}
		mTopics.fireTableDataChanged();

		for(String s : runs){
			mSystems.add(s);
		}
		mSystems.fireTableDataChanged();
		
		comboMeasures.addItem("Select a measure");
		//comboWilcoxonBibs.addItem("Select a Bib" );

		
		for(String m : measures){
			comboMeasures.addItem(m);
		}


        comboCorrelationMeasures.addItem("Jaccard");
        comboCorrelationMeasures.addItem("Spearman");
        //--------------------------------------------------------------------------
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

	private String [] checkValues(String atValue){
		
		List<String> result = new ArrayList<String>();
		String [] values = atValue.split(";");
		
		for(String each : values){
			String [] parts = each.split("-"); 
			if(parts.length == 2){
				double first = Double.parseDouble(parts[0]); 
				double last = Double.parseDouble(parts[1]);
				for(double v = first; v <= last; v += 1){
					result.add(v+"");
				}
			}else{
				result.add(parts[0]);
			}
		}
		String [] v = new String[result.size()];
		v = result.toArray(v);
		
		return v;
	}
}
