package br.uefs.analyzIR.ui.diversify;

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

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.ui.common.ProjectView;
import br.uefs.analyzIR.ui.util.DataTableModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class LoadClusterFiles {

	private JFrame frame;
	private JTextField txtName;
	private JTextField txtQrels;
	private JTextField txtRun;
	private Font font;
	private JTable tbRuns;
	private JScrollPane jspRuns;
	private JButton btnQrels;
	private JButton btnRun;
	private JButton btnRemove;
	private JButton btnCancel;
	private JButton btnOk;
	private JButton btnDirectory;
	private Facade facade;
	private JTextField txtDirectory;
	private JLabel lblQrels;
	private JLabel lblWorkspace;
	private JLabel lblProject;
	private String lastDirectory;
	private JTextField txtcQrels;
	private JButton btncQrels;
	private JLabel lblcQrels;
	private int type;
	private int protocol;

	public LoadClusterFiles(Facade facade, int type) {
		this.facade = facade;
		this.type = type;
		this.protocol = 0;
	}

	public void show() {

		initComponents();
		initLayout();
		initFunctions();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void initLayout() {

		String columns = "10dlu, 80dlu, 5dlu, 60dlu, 10dlu, 35dlu, 5dlu, 50dlu, 5dlu, 10dlu";
		String rows = "20dlu, 20dlu, 10dlu, 30dlu, 10dlu, 30dlu,10dlu, 30dlu,10dlu, 30dlu, 10dlu, 30dlu, 10dlu,"
				+ "30dlu, 10dlu, 10dlu,10dlu, 10dlu, 30dlu, 10dlu";

		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);

		builder.add(lblProject, CC.xy(2, 2));
		builder.add(txtName, CC.xyw(4, 2, 5));
		
		builder.add(lblWorkspace, CC.xy(2, 4));
		builder.add(txtDirectory, CC.xyw(4, 4, 3));
		builder.add(btnDirectory, CC.xy(8, 4));
		builder.add(lblQrels, CC.xy(2, 6));
		builder.add(txtQrels, CC.xyw(4, 6, 3));
		builder.add(btnQrels, CC.xy(8, 6));
		
		builder.add(lblcQrels, CC.xy(2, 8));
		builder.add(txtcQrels, CC.xyw(4, 8, 3));
		builder.add(btncQrels, CC.xy(8, 8));
		
		builder.add(jspRuns, CC.xywh(2, 10, 5, 8));
		builder.add(btnRun, CC.xy(8, 10));
		builder.add(btnRemove, CC.xy(8, 12));
		builder.add(btnCancel, CC.xyw(5, 19, 2));
		builder.add(btnOk, CC.xy(8, 19));


		frame.setContentPane(builder.getPanel());
	}

	private void initComponents() {

		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

		frame = new JFrame("New Diversity Project");

		lblQrels = new JLabel("Qrels:");
		lblWorkspace = new JLabel("Workscape:");
		lblProject = new JLabel("Project name:");
		lblcQrels = new JLabel("Cluster Qrels: ");
		
		txtName = new JTextField();
		txtQrels = new JTextField();
		txtRun = new JTextField();
		txtDirectory = new JTextField();
		txtcQrels = new JTextField();

		btnQrels = new JButton("...");
		btncQrels = new JButton("...");
		btnRun = new JButton("Add");
		btnRemove = new JButton("Remove");
		btnCancel = new JButton("Cancel");
		btnOk = new JButton("Finish");
		btnDirectory = new JButton("...");

		tbRuns = new JTable();
		jspRuns = new JScrollPane();

		createScroll(jspRuns);
		createTable(new ArrayList<String>(), tbRuns, jspRuns, "Runs");
		
		jspRuns.setToolTipText("Trec_eval compatible result file");

		lblQrels.setFont(font);
		lblWorkspace.setFont(font);
		lblProject.setFont(font);
		
		txtName.setFont(font);
		txtQrels.setFont(font);
		txtRun.setFont(font);
		txtDirectory.setFont(font);
		txtcQrels.setFont(font);

		btnQrels.setFont(font);
		btnRun.setFont(font);
		btnRemove.setFont(font);
		btnOk.setFont(font);
		btnCancel.setFont(font);
		btnDirectory.setFont(font);
		btncQrels.setFont(font);
		
		lblQrels.setToolTipText("Ground-truth (trec_eval compatible file)");
		lblWorkspace.setToolTipText("The project path");
		jspRuns.setToolTipText("Trec_eval compatible result file");
		
	}

	private void initFunctions() {

		btnQrels.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addQrelsFile();
			}
		});

		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRunFile();
			}
		});

		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeRun();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnDirectory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addDirectory();
			}
		});
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createProject();
			}
		});
		btncQrels.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				addcQrelsFile();
			}
		});
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

	// operations methods

	private void addQrelsFile() {

		File qrel;
		JFileChooser chooser = new JFileChooser();
		
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
		
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			qrel = chooser.getSelectedFile();
			txtQrels.setText(qrel.getAbsolutePath());
			this.lastDirectory = qrel.getParent();
		}
	}
	
	private void addcQrelsFile() {

		File cqrel;
		JFileChooser chooser = new JFileChooser();
		
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
		
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			cqrel = chooser.getSelectedFile();
			txtcQrels.setText(cqrel.getAbsolutePath());
			this.lastDirectory = cqrel.getParent();
		}
	}


	private void addDirectory() {
		
		File qrel;
		
		JFileChooser chooser = new JFileChooser();
		
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			qrel = chooser.getSelectedFile();
			txtDirectory.setText(qrel.getAbsolutePath());
			this.lastDirectory = qrel.getParent();
		}
	}

	private void addRunFile() {

		File [] runs;
		JFileChooser chooser = new JFileChooser();
	
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
	
		chooser.setMultiSelectionEnabled(true);
		
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
		
			runs = chooser.getSelectedFiles();
			for(File run : runs){
				txtRun.setText(run.getAbsolutePath());
				DataTableModel model = (DataTableModel) tbRuns.getModel();
				model.add(run.getAbsolutePath());
				model.fireTableDataChanged();
				this.lastDirectory = run.getParent();
			}
			
		}
	}

	private void removeRun() {

		int row = tbRuns.getSelectedRow();
		if (row != -1) {
			DataTableModel model = (DataTableModel) tbRuns.getModel();
			model.remove(row, 1);
			model.fireTableDataChanged();
		}
	}

	private void createProject() {

		DataTableModel data = (DataTableModel) tbRuns.getModel();
		String[] runs = new String[data.getRowCount()];

		for (int i = 0; i < data.getRowCount(); i++) {
			runs[i] = ((String) data.getValueAt(i, 1));
		}

		String qrels = txtQrels.getText();
		String cQrels = txtcQrels.getText();
		boolean control = true;
		boolean mode = false;
		

		while (control) {
			try {
				facade.createProject(runs, qrels, txtName.getText(),
						txtDirectory.getText(), mode, cQrels, type, protocol);
				control = false;
				ProjectView project = ProjectView.getInstance(facade);
				project.setController(facade);
				project.enableFunctions();
				frame.dispose();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (RunItemNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (QrelItemNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (InvalidQrelFormatException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (InvalidURLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (CreateDirectoryException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (NoGraphItemException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (InvalidProjectTypeException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (DiferentsSystemsException e) {
				int value = JOptionPane.showConfirmDialog(null, e.getMessage());
				if (value == JOptionPane.YES_OPTION) {
					control = true;
					mode = true;
				}else{
					control = false;
					mode = false;
				}
			} catch (TopicNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro 9",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro 9",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (InstantiationException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro 9",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (IllegalAccessException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro 9",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			} catch (InvalidRunNumberException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro 9",
						JOptionPane.ERROR_MESSAGE);
				control = false;
			}
		}
	}

}
