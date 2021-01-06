package br.uefs.analyzIR.ui.measure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.util.MeasureDataTableModel;

public class MeasureManagerUI {
	
	private JLabel lblMeasure;
	private JTextField txtPath;
	private JButton btnOpen;
	private JButton btnRemove;
	private JButton btnAdd;
	private JTable tbMeasures;
	private JScrollPane jspMeasures;
	private JFrame frame;
	private Font font;
	private String lastDirectory;
	private Facade facade; 
	
	public MeasureManagerUI(Facade facade){
		this.facade = facade; 
	}

	public void show(){
	
		createComponents();
		layout();
		initFunctions();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void createComponents(){
		
		frame = new JFrame("Measure Manager");

		lblMeasure = new JLabel("Measure:");
		txtPath = new JTextField(60);
		btnOpen = new JButton("..."); 
		btnRemove = new JButton("remove");
		btnAdd = new JButton("add");
		jspMeasures = new JScrollPane(); 
		tbMeasures = new JTable();
				
		createScroll(jspMeasures);
		createTable(initValues(), tbMeasures, jspMeasures);
		
		font = new Font(Font.SANS_SERIF, Font.BOLD, 13);
		
		lblMeasure.setFont(font);
		txtPath.setFont(font);
		btnOpen.setFont(font);
		btnRemove.setFont(font);
		btnAdd.setFont(font);
		jspMeasures.setFont(font);
		tbMeasures.setFont(font);
				
	}
	
	private List<String[]> initValues(){
		
		try {
			List<String[]> userMeasures = facade.listUserMesaures();
			return userMeasures;
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InstantiationException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalAccessException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
		return new ArrayList<String[]>();
	}
	
	private void layout(){
		
		String rows = "10dlu, 50dlu, 5dlu, 30dlu, 20dlu, 30dlu, 400dlu, 10dlu, 20dlu";
		String columns = "10dlu, 50dlu, 10dlu, 300dlu, 10dlu, 50dlu, 10dlu";
		
		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);

		builder.add(lblMeasure, CC.xy(2, 2));
		builder.add(txtPath, CC.xy(4, 2));
		builder.add(btnOpen, CC.xy(6, 2));
	
		builder.add(jspMeasures, CC.xywh(2, 4, 3, 5));
		builder.add(btnRemove, CC.xy(6, 6));
		builder.add(btnAdd, CC.xy(6, 4));

		frame.setContentPane(builder.getPanel());
	}
	
	
	private void initFunctions(){
		
		btnOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				open();
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		
	}
	
	
	private void open(){
		
		File qrel;
		JFileChooser chooser = new JFileChooser();
		
		chooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Measure -- *.jar";
			}
			@Override
			public boolean accept(File f) {
				return (f.getName().endsWith(".jar") || f.isDirectory());
			}
		});
		
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
		
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			qrel = chooser.getSelectedFile();
			txtPath.setText(qrel.getAbsolutePath());
			this.lastDirectory = qrel.getParent();
		}
	}
	
	private void add(){
		
		File f = new File(txtPath.getText().trim()); 
		
		try {
			
			String [] values = facade.add(f.getAbsolutePath(), f.getName());
			txtPath.setText("");
			MeasureDataTableModel model = (MeasureDataTableModel) tbMeasures.getModel(); 
			model.addValue(values);
			model.fireTableDataChanged();
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InstantiationException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalAccessException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void remove(){
		
		MeasureDataTableModel measures = (MeasureDataTableModel) tbMeasures.getModel();
		int [] rows = tbMeasures.getSelectedRows(); 
		
		if(rows.length > 0){
			for(int i = 0; i < rows.length; i++){
				
				String name = measures.getValueAt(rows[i], 0).toString();
				String url = measures.getValueAt(rows[i], 3).toString();
				
				measures.remove(rows[i]);
				
				facade.remove(url, name);
			}
			
			measures.fireTableDataChanged();
		}
		
	}
	
	private void createTable(List<String []> items, JTable table,JScrollPane scroll) {
		
		MeasureDataTableModel model = new MeasureDataTableModel(items);
		
		table.setModel(model);
		model.addTableModelListener(table);
		table.setBorder(new LineBorder(Color.black));
		table.setGridColor(Color.black);
		table.setShowGrid(true);
		table.setFont(font);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setHeaderValue("Measure name"); 
		table.getColumnModel().getColumn(1).setHeaderValue("Class name");
		table.getColumnModel().getColumn(2).setHeaderValue("Type");
		table.getColumnModel().getColumn(3).setHeaderValue("Path");
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
	
	public static void main(String [] v){
		
		MeasureManagerUI ui = new MeasureManagerUI(null); 
		ui.show();
	}

}
