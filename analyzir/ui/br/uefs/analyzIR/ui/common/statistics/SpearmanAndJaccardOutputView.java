package br.uefs.analyzIR.ui.common.statistics;

import br.uefs.analyzIR.ui.common.statistics.heatMap.HeatMapDemo;
import br.uefs.analyzIR.ui.util.DataTableModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class SpearmanAndJaccardOutputView {

	private JFrame frame;
	private JComboBox comboTopics;
	private Font font;
	private JTextArea txtResult;
	private JButton btnClose;
	private JButton btnHeatMap;
	private String[][][] correlationMatrices;
	private List<String> topics;
	private List<String> systems;

	public SpearmanAndJaccardOutputView(String[][][] correlationMatrices, List<String> systems, List<String> topics){
		this.correlationMatrices = correlationMatrices;
		this.systems = systems;
		this.topics = topics;
	}
	
	public void show(){
		initComponents();
		initLayout();
		initFunctions();
		initValues();
		showResults(topics.get(0));
		frame.pack();
		frame.setVisible(true);
	}

	private void initComponents(){
		
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
		
		this.frame = new JFrame("Correlation Test");

		this.txtResult = new JTextArea();

		this.btnClose = new JButton("Close");

		this.btnHeatMap = new JButton("HeatMap");

		this.comboTopics = new JComboBox();

		this.comboTopics.setFont(font);

		this.txtResult.setFont(font);

		this.btnClose.setFont(font);

		this.btnHeatMap.setFont(font);
	}
	
	private void initLayout(){
		
		String columns = "10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu, 40dlu, 10dlu, left:50dlu, 5dlu, left:100dlu, 10dlu";
		String rows = "5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu,5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, "
				+ "30dlu, 5dlu, 50dlu, 5dlu, 30dlu,5dlu, 30dlu";
		
		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);

		builder.addLabel("Topics:", CC.xy(8, 2));
		builder.add(comboTopics, CC.xy(10, 2));

		builder.add(txtResult, CC.xywh(2,4,9,6));

		builder.add(btnClose, CC.xy(6, 12));

		builder.add(btnHeatMap, CC.xy(8, 12));
		
		frame.setContentPane(builder.getPanel());
	}
	
	private void initFunctions() {

		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		comboTopics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResults(comboTopics.getSelectedItem().toString());
			}
		});

		btnHeatMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				double[][] matrixSelected = new double[systems.size()][systems.size()];
				for (int i=0;i<matrixSelected.length;i++){
					for (int j=0;j<matrixSelected.length;j++){
						double value = Double.parseDouble(correlationMatrices[comboTopics.getSelectedIndex()][i][j]);
						matrixSelected[i][j] = value;
					}
				}

				HeatMapDemo hm = null;
				String[] systemsNames = new String[systems.size()];
				for (int i = 0; i<systems.size();i++)
					systemsNames[i] = systems.get(i);
				try {
					hm = new HeatMapDemo(matrixSelected,systemsNames);
				} catch (Exception e) {
					e.printStackTrace();
				}
				hm.showData();
			}
		});
	}


	private void initValues(){
		for(String t : topics){
			comboTopics.addItem(t);
		}
	}


	private void showResults(String topicSelected){
		String resultMatrix = "                              ";

		System.out.println("RESULTS");
		//Making Title
		for(int i = 0;i<systems.size();i++){
			resultMatrix = resultMatrix + systems.get(i) + "|";
		}

		resultMatrix += "\n";

		int a = getTopicIndex(topicSelected);
			for (int b=0; b<systems.size();b++){
				System.out.print(systems.get(b) + " ");
				resultMatrix+=systems.get(b)+"|";
				for(int c=0; c<systems.size();c++){
					System.out.print(correlationMatrices[a][b][c] + "|");
					DecimalFormat dm = new DecimalFormat("0.0000");
					String matrixStringValue = correlationMatrices[a][b][c];
					double value = Double.parseDouble(matrixStringValue);
					String formatedValue = dm.format(value);
					resultMatrix+="         " + formatedValue +"         " + "|";
				}
				System.out.println();
				resultMatrix+="\n";
			}
		//}
		txtResult.setText(resultMatrix);
	}

	private int getTopicIndex(String topic){
		for (int i=0;i<topics.size();i++){
			if (topics.get(i).equals(topic))
				return i;
		}
		return 9999;
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
