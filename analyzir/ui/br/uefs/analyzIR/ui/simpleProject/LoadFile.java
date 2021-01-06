package br.uefs.analyzIR.ui.simpleProject;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.CreateDirectoryException;
import br.uefs.analyzIR.exception.DiferentsSystemsException;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.exception.InvalidProjectTypeException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.InvalidRunNumberException;
import br.uefs.analyzIR.exception.InvalidURLException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.RunItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.ui.common.ProjectView;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class LoadFile {

	private JFrame frame;
	private JPanel jpMain;
	private JTextField txtNameProject;
	private JTextField txtQrels;
	private JTextField txtRun;
	private JTextField txtPath;
	private Facade controller;
	private JButton btnAddQrels;
	private JButton btnAddRun;
	private JButton btnAddPath;
	private JButton btnFinish;
	private Font font;
	private JLabel lblQrels;
	private JLabel lblRun;
	private String lastDirectory;
	private int type;
	private int protocol;

	public LoadFile(Facade controller, int type) {
		this.controller = controller;
		this.type = type;
		this.protocol = 0;
	}
	
	

	public void show() {
		createFrame();
		createJpMain();
		initComponents();
		initLayout();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void createFrame() {
		frame = new JFrame("New Project - Single System");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void createJpMain() {
		jpMain = (JPanel) frame.getContentPane();
		SpringLayout layout = new SpringLayout();
		jpMain.setLayout(layout);
	}
	
	private void initLayout(){

		String columns = "5dlu, right:60dlu, 5dlu, left:100dlu, 5dlu, 40dlu, 20dlu";
		String rows = "5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu";

		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);

		builder.addLabel("Project name:", CC.xy(2, 2));
		builder.add(lblQrels, CC.xy(2,4));
		builder.add(lblRun, CC.xy(2, 6));
		builder.addLabel("Workspace:", CC.xy(2, 8));
		
		builder.add(txtNameProject, CC.xywh(4, 2, 3, 1));		
		builder.add(txtQrels, CC.xy(4, 4));
		builder.add(btnAddQrels, CC.xy(6, 4));
		builder.add(txtRun, CC.xy(4,6));
		builder.add(btnAddRun, CC.xy(6, 6));
		builder.add(txtPath, CC.xy(4, 8));
		builder.add(btnAddPath, CC.xy(6, 8));
		builder.add(btnFinish, CC.xy(6, 10));
		
		lblQrels.setToolTipText("Ground-truth (trec_eval compatible file)");
		
		frame.setContentPane(builder.getPanel());
	}

	private void initComponents() {

		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
		
		txtNameProject = new JTextField(25);
		txtQrels = new JTextField(20);
		txtRun = new JTextField(20);
		txtPath = new JTextField(16);

		btnAddQrels = new JButton("...");
		btnAddRun = new JButton("...");
		btnAddPath = new JButton("...");
		btnFinish = new JButton("Finish");
		
		lblQrels = new JLabel("Qrels: ");
		lblRun = new JLabel("Run: ");
		
		btnAddQrels.setFont(font);
		btnAddRun.setFont(font);
		btnFinish.setFont(font);
		btnAddPath.setFont(font);

		txtNameProject.setFont(font);
		txtQrels.setFont(font);
		txtRun.setFont(font);
		txtPath.setFont(font);
		
		lblQrels.setFont(font);
		lblRun.setFont(font);
		
		lblQrels.setToolTipText("Ground-truth (trec_eval compatible file)");
		lblRun.setToolTipText("Trec_eval compatible result file");

		
		btnAddQrels.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addQrelsFile();
			}
		});
		btnAddRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addRunFile();
			}
		});
		btnAddPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addPath();
			}
		});
		btnFinish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createProject();
			}
		});
	}

	private void addQrelsFile() {
		
		File qrel;
		JFileChooser chooser = new JFileChooser();
		
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
		
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			qrel = chooser.getSelectedFile();
			txtQrels.setText(qrel.getAbsolutePath());
		}
	}

	private void addRunFile() {
		
		File qrel;
		JFileChooser chooser = new JFileChooser();
		
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
		
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			qrel = chooser.getSelectedFile();
			txtRun.setText(qrel.getAbsolutePath());
		}
	}

	private void addPath() {
		
		File qrel;
		JFileChooser chooser = new JFileChooser();
		
		if(lastDirectory != null)
			chooser.setCurrentDirectory(new File(lastDirectory));
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retorno = chooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			qrel = chooser.getSelectedFile();
			txtPath.setText(qrel.getAbsolutePath());
		}
	}

	private void createProject() {
		
		String[] runFile = { txtRun.getText() };		
		try {
			controller.createProject(runFile, txtQrels.getText(),
					txtNameProject.getText(), txtPath.getText(), false, null, type, protocol);
			ProjectView view = ProjectView.getInstance(null);
			view.setController(controller);
			view.enableFunctions();
			frame.dispose();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (RunItemNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (QrelItemNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidQrelFormatException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidURLException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (CreateDirectoryException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (TopicNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (NoGraphItemException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (InvalidProjectTypeException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (DiferentsSystemsException e) {
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
		} catch (InvalidRunNumberException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
