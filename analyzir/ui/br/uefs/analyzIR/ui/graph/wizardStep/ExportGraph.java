package br.uefs.analyzIR.ui.graph.wizardStep;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import br.uefs.analyzIR.exception.NoGraphPointException;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.GraphNotFoundException;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;

public class ExportGraph implements WizardStep {

	private JFrame frame; 
	private JTextField txtExportDestination; 
	private JButton btnBrowse; 
	private JButton btnCancel; 
	private JButton btnFinish; 
	private JTree treeTypes; 
	private DefaultMutableTreeNode dmtnTypes;
	private DefaultMutableTreeNode dmtnExcel;
	private DefaultMutableTreeNode dmtnCSV; 
	private DefaultMutableTreeNode dmtnImagePNG;
	private DefaultMutableTreeNode dmtnImageJPG; 
	
	private DataInfo info;
	private WizardProcess process;
	private Facade facade;
	
	@Override
	public void putDataInfo(DataInfo dataInfo) {
		this.info = dataInfo;
	}

	@Override
	public void show() {
		
		initComponents();
		initLayout();
		initFunctions();
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public JPanel make() {
		
		return null;
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
	}

	@Override
	public void addWizardProcessListener(WizardProcess wProcess) {
		this.process = wProcess;
	}

	@Override
	public void run() {
		this.show();
	}

	@Override
	public void stop() {
		this.notifyWizardProcesses();
		this.frame.dispose();
	}

	@Override
	public void notifyWizardProcesses() {
		this.process.update();
	}

	@Override
	public void putCommandProcess(Facade facade) {
		this.facade = facade;
	}

	
	private void initComponents(){
		
		this.frame = new JFrame("Export");
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
		
		this.txtExportDestination = new JTextField();
		
		this.dmtnTypes = new DefaultMutableTreeNode("Types:");
		this.dmtnExcel = new DefaultMutableTreeNode("Microsfot Excel (*.xls)");
		this.dmtnCSV = new DefaultMutableTreeNode("CSV");
		this.dmtnImagePNG = new DefaultMutableTreeNode("Image PNG (*.png)");
		this.dmtnImageJPG = new DefaultMutableTreeNode("Image JPG (*.jpg)");
		
		this.btnBrowse = new JButton("Browse..."); 
		this.btnCancel = new JButton("Cancel"); 
		this.btnFinish = new JButton("Finish"); 
		
		
		dmtnTypes.add(dmtnExcel);
		dmtnTypes.add(dmtnCSV);
		dmtnTypes.add(dmtnImagePNG);
		dmtnTypes.add(dmtnImageJPG);
		
		this.treeTypes = new JTree(dmtnTypes);
		
		this.treeTypes.setFont(font);
		this.txtExportDestination.setFont(font);
		this.btnBrowse.setFont(font);
		this.btnCancel.setFont(font);
		this.btnFinish.setFont(font);
		
	}
	
	private void initLayout(){
		
		String columns = "10dlu, 80dlu, 10dlu, 40dlu, 10dlu, 60 dlu, 10dlu, 60dlu, 10dlu";
		String rows = "10dlu, 40dlu, 5dlu, 20dlu, 5dlu, 80dlu, 10dlu, 40dlu, 10dlu";
		
		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);
		
		builder.addLabel("Export destination: ", CC.xy(2, 2));
		builder.add(txtExportDestination, CC.xyw(4, 2, 3));
		builder.add(btnBrowse, CC.xy(8, 2));
		builder.addLabel("Select a Type:", CC.xy(2, 4));
		builder.add(treeTypes, CC.xyw(2, 6, 7));
		builder.add(btnCancel, CC.xy(6, 8));
		builder.add(btnFinish, CC.xy(8, 8));
		
		frame.setContentPane(builder.getPanel());
	}
	
	private void initFunctions(){
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				//update process call all listeners
			}
		});
		
		btnFinish.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DefaultMutableTreeNode selectType = (DefaultMutableTreeNode) treeTypes.getLastSelectedPathComponent();
				String url = txtExportDestination.getText().trim();
				String name = info.getData("name").toString();
				
				if(selectType != null){
				
					if(!selectType.isRoot()){
						
						try {
							String type = selectType.toString();
							if(type.equals("Microsfot Excel (*.xls)")){
								facade.exportPlot(name, url, ".xls");
							}else if(type.equals("CSV")){
								facade.exportPlot(name, url, "csv");
							}else if(type.equals("Image PNG (*.png)")){
								facade.exportPlot(name, url, ".png");
							}else{
								facade.exportPlot(name, url, ".jpg");
							}
							
							stop();
						} catch (GraphNotFoundException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						} catch (NoGraphItemException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						} catch (NoGraphPointException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}else{
						JOptionPane.showMessageDialog(null, "Sorry, but you must select a type.", "Sorry!", JOptionPane.WARNING_MESSAGE);
					}
				}
				
			}
		});
		
		btnBrowse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				File qrel;
				JFileChooser chooser = new JFileChooser();
				int retorno = chooser.showSaveDialog(null);

				if (retorno == JFileChooser.APPROVE_OPTION) {
					qrel = chooser.getSelectedFile();
					txtExportDestination.setText(qrel.getAbsolutePath());
				}
			}
		});
	}
}
