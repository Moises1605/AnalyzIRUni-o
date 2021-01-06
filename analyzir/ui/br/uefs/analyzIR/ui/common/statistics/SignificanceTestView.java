package br.uefs.analyzIR.ui.common.statistics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.remote.JMXConnectorFactory;
import javax.swing.*;
import javax.swing.border.LineBorder;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.ui.util.DataTableModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class SignificanceTestView {

	private JFrame frame;
	private JComboBox<String> comboSystem1;
	private JComboBox<String> comboSystem2;
	private JComboBox<String> comboMeasures;
	private JComboBox<String> comboTest;
	private JComboBox<String> comboWilcoxonLibs;
	private JTextField txtAlpha;
	private JTable tbTopics;
	private JTable tbSelectedTopics;
	private JScrollPane jspTopics;
	private JScrollPane jspSelectedTopics;
	private JButton btnSingle;
	private JButton btnAll;
	private JButton btnBackSingle;
	private JButton btnBackAll;
	private Font font;
	private Facade facade;
	private JTextArea txtResult;
	private JButton btnRun;
	private JButton btnClose;
	private JTextField txtAtValue;
	private JLabel labStatisticalLibs;

	public SignificanceTestView(Facade facade){
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
		
		this.frame = new JFrame("Significance Test");
		
		this.comboSystem1 = new JComboBox<String>();
		this.comboSystem2 = new JComboBox<String>();
		this.comboMeasures = new JComboBox<String>();
		this.comboTest = new JComboBox<String>();
		this.comboWilcoxonLibs = new JComboBox<String>();
		
		this.txtAlpha = new JTextField(20);
		this.txtResult = new JTextArea();
		this.txtAtValue = new JTextField(20);
		
		this.tbTopics = new JTable();
		this.tbSelectedTopics = new JTable();
		this.jspTopics = new JScrollPane();
		this.jspSelectedTopics = new JScrollPane();
		
		this.btnSingle = new JButton(">"); 
		this.btnAll = new JButton(">>"); 
		this.btnBackSingle = new JButton("<"); 
		this.btnBackAll = new JButton("<<");
		this.btnRun = new JButton("Run");
		this.btnClose = new JButton("Close");

		this.labStatisticalLibs = new JLabel("Statsc.Libs.:");
	
		createTable(new ArrayList<String>(), tbTopics, jspTopics, "Topics");
		createScroll(jspTopics);
		createTable(new ArrayList<String>(), tbSelectedTopics, jspSelectedTopics, "Selected Topics");
		createScroll(jspSelectedTopics);
	
		this.comboSystem1.setFont(font);
		this.comboSystem2.setFont(font);
		this.comboMeasures.setFont(font);
		this.comboWilcoxonLibs.setFont(font);
		this.comboTest.setFont(font);
		
		this.txtAlpha.setFont(font);
		this.txtResult.setFont(font);
		this.txtAtValue.setFont(font);
		
		this.tbTopics.setFont(font);
		this.tbSelectedTopics.setFont(font);
		this.jspTopics.setFont(font);
		this.jspSelectedTopics.setFont(font);
		
		this.btnSingle.setFont(font);
		this.btnAll.setFont(font);
		this.btnBackSingle.setFont(font);
		this.btnBackAll.setFont(font);
		this.btnRun.setFont(font);
		this.btnClose.setFont(font);

		this.labStatisticalLibs.setFont(font);
	}
	
	private void initLayout(){
		
		String columns = "10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu, 40dlu, 10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu";
		String rows = "5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu,5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, "
				+ "30dlu, 5dlu, 50dlu, 5dlu, 30dlu,5dlu, 30dlu";
		
		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);

		builder.addLabel("System 1:", CC.xy(2, 2));
		builder.add(comboSystem1, CC.xy(4, 2));
		builder.addLabel("System 2:", CC.xy(8, 2));
		builder.add(comboSystem2, CC.xy(10, 2)); 
		
		builder.addLabel("Alpha:", CC.xy(2, 4));
		builder.add(txtAlpha, CC.xy(4, 4));
		builder.addLabel("Signf. Test:", CC.xy(8, 4));
		builder.add(comboTest, CC.xy(10, 4));
		builder.addLabel("Measures:", CC.xy(2, 6));
		builder.add(comboMeasures, CC.xy(4, 6));
        builder.add(labStatisticalLibs, CC.xy(8, 6));
        builder.add(comboWilcoxonLibs, CC.xy(10, 6));
		
		builder.addLabel("AtValue:", CC.xy(2, 8));
		builder.add(txtAtValue, CC.xy(4, 8));
		builder.add(jspTopics, CC.xywh(2, 10, 3, 7));
		builder.add(jspSelectedTopics, CC.xywh(8, 10, 3, 7));
		
		builder.add(btnSingle, CC.xy(6, 10));
		builder.add(btnAll, CC.xy(6, 12));
		builder.add(btnBackAll, CC.xy(6, 14));
		builder.add(btnBackSingle, CC.xy(6, 16));
		builder.add(btnRun, CC.xy(6, 18));
		builder.addLabel("Result:", CC.xy(2, 19));
		
		builder.add(txtResult, CC.xywh(2,21,9,3));
		builder.add(btnClose, CC.xy(6, 25));
		
		frame.setContentPane(builder.getPanel());
	}
	
	private void initFunctions(){

		comboTest.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if(comboTest.getSelectedItem().equals("Mann Whitney U")) {
					comboWilcoxonLibs.setEnabled(false);
					labStatisticalLibs.setEnabled(false);
				}else {
					comboWilcoxonLibs.setEnabled(true);
					labStatisticalLibs.setEnabled(true);
				}
			}
		});


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
		
		btnRun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<String> measures = new ArrayList<String>(); 
				DataTableModel mTopics = (DataTableModel) tbSelectedTopics.getModel();
				
				if(!txtAlpha.getText().trim().equals("") && 
					!comboSystem1.getSelectedItem().toString().equals("Select a System") &&
                    !comboWilcoxonLibs.getSelectedItem().toString().equals("Select a Bib") &&
					!comboSystem1.getSelectedItem().toString().equals("Select a System") && 
					!comboMeasures.getSelectedItem().toString().equals("Select a measure")){
					
					String alpha = txtAlpha.getText().trim();
					String atValue = txtAtValue.getText().trim();
					String [] values = checkValues(atValue);
					String system1 = comboSystem1.getSelectedItem().toString();
					String system2 = comboSystem2.getSelectedItem().toString();
					String wilcoxonBib = comboWilcoxonLibs.getSelectedItem().toString();
					
					List<String> topic = new ArrayList<String>(); 
					for(int i = mTopics.getRowCount() - 1; i >= 0; i--){
						String value = mTopics.getValueAt(i, 1).toString();
						topic.add(value);
					}
						
					String measure = comboMeasures.getSelectedItem().toString();
					measures.add(measure);
					
					try {
					
						double [] value1 = facade.calculeValue(system1, topic, measures, atValue, values);
						double [] value2 = facade.calculeValue(system2, topic, measures, atValue, values); 
					
						double sum1, sum2, avg1, avg2; 
						sum1 = sum2 = 0;
						
						for(double v : value1){
							sum1 += v;
						}
						
						for(double v : value2){
							sum2 += v;
						}
						
						avg1 = sum1 / value1.length;
						avg2 = sum2 / value2.length;

						double p_value;

						double alphaD = Double.parseDouble(alpha);
                        if(comboTest.getSelectedItem().equals("Wilcoxon"))
							p_value = facade.wilcoxonTest(value1, value2, wilcoxonBib);
                        else if(comboTest.getSelectedItem().equals("Mann Whitney U"))
							p_value = facade.mannWhitneyTest(value1, value2);
						else
							p_value = 0.0;

						System.out.println("P_Value: "+p_value);
						String text = "System 1 "+measure+" average: "+String.format("%.4f", avg1)+"\n";
						text += "System 2 "+measure+" average: "+String.format("%.4f", avg2)+"\n";
						for(Object v:value1){
							System.out.println("V1"+ v);
						}
						for(Object b:value2){
							System.out.println("V2"+ b);
						}
						
						if(p_value == 0){
							text += "The vectors are equals.";
						}else if(p_value < alphaD){
							text += "P_value: "+p_value+" \n";
							text += "Reject H0: The data do not provide sufficient information to conclude that System1 and System2 \n average "+measure+" values are statistically  equivalent";
						}else{
							text += "P_value: "+p_value+" \n";
							text += "Accept H0: The data provide sufficient information to conclude that System1 and system2 \n average "+measure+" values are statistically equivalent";
						}
						txtResult.setText(text);
						
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InterruptedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (MeasureNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InvalidItemNameException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (ItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (EmptyTopicMeasureValueException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (NoGraphItemException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (QrelItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (TopicNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InvalidQrelFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} 
					
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
		DataTableModel mTopics = (DataTableModel) tbTopics.getModel();
		
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
		
		comboSystem1.addItem("Select a System");
		comboSystem2.addItem("Select a System");
		comboMeasures.addItem("Select a measure");
		comboTest.addItem("Wilcoxon");
		comboTest.addItem("Mann Whitney U");

		for(String r : runs){
			comboSystem1.addItem(r);
			comboSystem2.addItem(r);
		}
		
		for(String m : measures){
			comboMeasures.addItem(m);
		}

		//Future: Change this to read all the wilcoxon bibs types and put the names automatomatically.
        comboWilcoxonLibs.addItem("Apache Commons");
        comboWilcoxonLibs.addItem("JSC");
        comboWilcoxonLibs.addItem("MacKenzie");
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
