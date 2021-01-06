package br.uefs.analyzIR.ui.common.statistics;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.ui.util.DataTableModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JaccardTest {

	private JFrame frame;
	private JComboBox<String> comboSystem1;
	private JComboBox<String> comboSystem2;
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

	public JaccardTest(Facade facade){
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
		
		this.frame = new JFrame("Jaccard Correlation Test");
		
		this.comboSystem1 = new JComboBox<String>();
		this.comboSystem2 = new JComboBox<String>();

		this.txtResult = new JTextArea();
		this.txtAtValue = new JTextField(20);
		
		this.tbTopics = new JTable();
		this.tbSelectedTopics = new JTable();
		this.jspTopics = new JScrollPane();
		this.jspSelectedTopics = new JScrollPane();
		
		this.btnSingle = new JButton(">"); 
		this.btnRun = new JButton("Run");
		this.btnClose = new JButton("Close");
	
		createTable(new ArrayList<String>(), tbTopics, jspTopics, "Topics");
		createScroll(jspTopics);
		createTable(new ArrayList<String>(), tbSelectedTopics, jspSelectedTopics, "Selected Topics");
		createScroll(jspSelectedTopics);
	
		this.comboSystem1.setFont(font);
		this.comboSystem2.setFont(font);

		this.txtResult.setFont(font);
		this.txtAtValue.setFont(font);
		
		this.tbTopics.setFont(font);
		this.tbSelectedTopics.setFont(font);
		this.jspTopics.setFont(font);
		this.jspSelectedTopics.setFont(font);
		
		this.btnSingle.setFont(font);
		this.btnRun.setFont(font);
		this.btnClose.setFont(font);
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
		

		builder.addLabel("AtValue:", CC.xy(2, 4));
		builder.add(txtAtValue, CC.xy(4, 4));
		builder.add(jspTopics, CC.xywh(2, 10, 3, 7));
		builder.add(jspSelectedTopics, CC.xywh(8, 10, 3, 7));
		
		builder.add(btnSingle, CC.xy(6, 14));
		builder.add(btnRun, CC.xy(6, 18));
		builder.addLabel("Result:", CC.xy(2, 19));
		
		builder.add(txtResult, CC.xywh(2,21,9,3));
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
		

		btnRun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DataTableModel mTopics = (DataTableModel) tbSelectedTopics.getModel();
				
				if(!comboSystem1.getSelectedItem().toString().equals("Select a System") &&
					!comboSystem1.getSelectedItem().toString().equals("Select a System")					){
					
					String atValue = txtAtValue.getText().trim();
					String [] values = checkValues(atValue);
					String system1 = comboSystem1.getSelectedItem().toString();
					String system2 = comboSystem2.getSelectedItem().toString();

					List<String> topic = new ArrayList<String>(); 
					for(int i = mTopics.getRowCount() - 1; i >= 0; i--){
						String value = mTopics.getValueAt(i, 1).toString();
						topic.add(value);
					}
						
					try {

						double jaccardIndex = facade.jaccardTest(system1,system2,atValue,topic.get(0));

						String text = "Jaccard index value: " + jaccardIndex;
						txtResult.setText(text);
						
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (TopicNotFoundException e1) {
						e1.printStackTrace();
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
		List<String> runs = facade.listRuns();
		DataTableModel mTopics = (DataTableModel) tbTopics.getModel();
		
		topics.remove("AVG");

		Collections.sort(topics);

		for(String t : topics){
			if(!t.equals("AVG")){
				mTopics.add(t);
			}
		}
		mTopics.fireTableDataChanged();
		
		comboSystem1.addItem("Select a System");
		comboSystem2.addItem("Select a System");

		for(String r : runs){
			comboSystem1.addItem(r);
			comboSystem2.addItem(r);
		}


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
