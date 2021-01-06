package br.uefs.analyzIR.ui.common.statistics;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.util.DataTableModel;
import br.uefs.analyzIR.ui.util.TTableModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TTable {

	private JFrame frame;
	private JTable criticalValueTable;
	private JScrollPane cvtScroll;
	private Font font;
	private Facade facade;
	private JButton btnClose;
	private JTextField txtAtValue;
	private String alpha;

	public TTable(Facade facade,String alpha){
		this.facade = facade;
		this.alpha = alpha;
	}
	
	public void show() throws IOException {
		initComponents();
		initLayout();
		initFunctions();
		initValues(this.alpha);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void initComponents() throws IOException {
		
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		
		this.frame = new JFrame("Student's t-Test");
		this.txtAtValue = new JTextField(20);
		
		this.criticalValueTable = new JTable();
		this.cvtScroll = new JScrollPane();

		this.btnClose = new JButton("Close");
		String[][] data = readTTable();
		//createTTable(data, 101, 7, criticalValueTable, cvtScroll, "Critical values");
		createTTable(data, 101, 2, criticalValueTable, cvtScroll, "Critical values",this.alpha);
		createScroll(cvtScroll);
		this.txtAtValue.setFont(font);
		this.criticalValueTable.setDragEnabled(false);

		this.criticalValueTable.setFont(font);
		this.cvtScroll.setFont(font);

		this.btnClose.setFont(font);
	}

	private String[][] readTTable() throws IOException {
		String[][] data = new String[101][7];
		FileReader read = new FileReader("TStudentTable.txt");
		BufferedReader buffer = new BufferedReader(read);
		String[] line;

		DecimalFormat df1 = new DecimalFormat();
		DecimalFormat df2 = new DecimalFormat();
		df1.applyPattern("0");
		df2.applyPattern("0.000");

		for(int i = 0; i<101; i++){
			line = buffer.readLine().split(" ");
			for(int j = 0; j<7;j++){
				double value = Double.parseDouble(line[j]);
				if(j==0){
					data[i][j] = df1.format(value);
				}else
					data[i][j] = df2.format(value);
			}
		}

		data[data.length-1][0]= "Infinity";
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

	private void initValues(String alpha) throws IOException {
		TTableModel criticalValues = (TTableModel) criticalValueTable.getModel();
		int columm =0;
		switch (alpha){
			case "0.90":
				columm = 1;
				break;
			case "0.95":
				columm = 2;
				break;
			case "0.975":
				columm = 3;
				break;
			case "0.99":
				columm = 4;
				break;
			case "0.995":
				columm = 5;
				break;
			case "0.999":
				columm = 6;
				break;
			default:

		}
		String[][] tTable = readTTable();
		for(int i=0;i<101;i++){
			//for(int j=0;j<7;j++){
				criticalValues.add(tTable[i][columm],i,1);
			//}
		}
		criticalValues.fireTableDataChanged();
	}


	/*private void createTTable(String[][] items,int x, int y, JTable table,JScrollPane scroll, String name) {
		TTableModel model = new TTableModel(items,x,y);
		table.setModel(model);
		model.addTableModelListener(table);
		table.setBorder(new LineBorder(Color.black));
		table.setGridColor(Color.black);
		table.setShowGrid(true);
		table.setFont(font);
		table.setRowHeight(25);
		table.getColumnModel().getColumn(0).setHeaderValue("df (N-1)");
		table.getColumnModel().getColumn(1).setHeaderValue("0.90");
		table.getColumnModel().getColumn(2).setHeaderValue("0.95");
		table.getColumnModel().getColumn(3).setHeaderValue("0.975");
		table.getColumnModel().getColumn(4).setHeaderValue("0.99");
		table.getColumnModel().getColumn(5).setHeaderValue("0.995");
		table.getColumnModel().getColumn(6).setHeaderValue("0.999");
		table.getTableHeader().resizeAndRepaint();
		table.setCellSelectionEnabled(false);
		table.setAlignmentX(Component.LEFT_ALIGNMENT);
		scroll.setViewportView(table);
	}*/
	private void createTTable(String[][] items,int x, int y, JTable table,JScrollPane scroll, String name, String alpha) {
		TTableModel model = new TTableModel(items,x,y);
		table.setModel(model);
		model.addTableModelListener(table);
		table.setBorder(new LineBorder(Color.black));
		table.setGridColor(Color.black);
		table.setShowGrid(true);
		table.setFont(font);
		table.setRowHeight(25);
		table.getColumnModel().getColumn(0).setHeaderValue("df (N-1)");
		table.getColumnModel().getColumn(1).setHeaderValue(alpha);
		/*table.getColumnModel().getColumn(1).setHeaderValue("0.90");
		table.getColumnModel().getColumn(2).setHeaderValue("0.95");
		table.getColumnModel().getColumn(3).setHeaderValue("0.975");
		table.getColumnModel().getColumn(4).setHeaderValue("0.99");
		table.getColumnModel().getColumn(5).setHeaderValue("0.995");
		table.getColumnModel().getColumn(6).setHeaderValue("0.999");*/
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
