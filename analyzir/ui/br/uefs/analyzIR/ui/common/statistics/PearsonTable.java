package br.uefs.analyzIR.ui.common.statistics;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.util.PearsonTableModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PearsonTable {

	private JFrame frame;
	private JTable criticalValueTable;
	private JScrollPane cvtScroll;
	private Font font;
	private Facade facade;
	private JButton btnClose;
	private JTextField txtAtValue;

	public PearsonTable(Facade facade){
		this.facade = facade;
	}
	
	public void show() throws IOException {
		initComponents();
		initLayout();
		initFunctions();
		initValues();
		frame.pack();
		frame.setVisible(true);
	}
	
	private void initComponents() throws IOException {
		
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		
		this.frame = new JFrame("Pearson's Critical Values");
		this.txtAtValue = new JTextField(20);
		
		this.criticalValueTable = new JTable();
		this.cvtScroll = new JScrollPane();

		this.btnClose = new JButton("Close");
		String[][] data = readTTable();
		createTable(data, 41, 4, criticalValueTable, cvtScroll, "Critical values");
		createScroll(cvtScroll);
		this.txtAtValue.setFont(font);
		this.criticalValueTable.setDragEnabled(false);

		this.criticalValueTable.setFont(font);
		this.cvtScroll.setFont(font);

		this.btnClose.setFont(font);
	}

	private String[][] readTTable() throws IOException {
		String[][] data = new String[41][4];
		FileReader read = new FileReader("PearsonTable.txt");
		//Reference:
		//http://www.statisticssolutions.com/table-of-critical-values-pearson-correlation/
		BufferedReader buffer = new BufferedReader(read);
		String[] line;

		DecimalFormat df1 = new DecimalFormat();
		DecimalFormat df2 = new DecimalFormat();
		df1.applyPattern("0");
		df2.applyPattern("0.000");

		for(int i = 0; i<41; i++){
			line = buffer.readLine().split(" ");
			for(int j = 0; j<4;j++){
				double value = Double.parseDouble(line[j]);
				if(j==0){
					data[i][j] = df1.format(value);
				}else
					data[i][j] = df2.format(value);
			}
		}

		return data;
	}

	private void initLayout(){
		
		String columns = "10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu, 40dlu, 10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu";
		String rows = "5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu,5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, "
				+ "30dlu, 5dlu, 50dlu, 5dlu, 30dlu,5dlu, 30dlu";
		
		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);

		builder.add(cvtScroll, CC.xywh(1, 1, 11, 22));

		builder.add(btnClose, CC.xy(6, 25));
		
		frame.setContentPane(builder.getPanel());
	}
	
	private void initFunctions(){
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}

	private void initValues() throws IOException {
		PearsonTableModel criticalValues = (PearsonTableModel) criticalValueTable.getModel();
		String[][] tTable = readTTable();

		for(int i=0;i<41;i++){
			for(int j=0;j<4;j++){
				criticalValues.add(tTable[i][j],i,j);
			}
		}
		criticalValues.fireTableDataChanged();
	}


	private void createTable(String[][] items, int x, int y, JTable table, JScrollPane scroll, String name) {
		PearsonTableModel model = new PearsonTableModel(items,x,y);
		table.setModel(model);
		model.addTableModelListener(table);
		table.setBorder(new LineBorder(Color.black));
		table.setGridColor(Color.black);
		table.setShowGrid(true);
		table.setFont(font);
		table.setRowHeight(25);
		table.getColumnModel().getColumn(0).setHeaderValue("df (N-2)");
		table.getColumnModel().getColumn(1).setHeaderValue("confidence = 90%");
		table.getColumnModel().getColumn(2).setHeaderValue("confidence = 95%");
		table.getColumnModel().getColumn(3).setHeaderValue("confidence = 99%");
		table.getTableHeader().resizeAndRepaint();
		table.setCellSelectionEnabled(false);
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
