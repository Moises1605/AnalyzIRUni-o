package br.uefs.analyzIR.ui.graph.wizardStep;

import javax.swing.JPanel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;
/**
 * Second step screen for graph creation
 * @author nilson
 *
 */
public class InteractiveDataGraphLabelsSetting implements WizardStep{
	
	private JFrame frame;
	private Font font; 
	
	private JTextField txtName; 
	private JTextField txtTitle; 
	private JTextField txtXLabel;
	private JTextField txtYLabel; 
	
	private JLabel lblName;
	private JLabel lblTitle;
	private JLabel lblXLabel;
	private JLabel lblYLabel; 
	
	private JButton btnCancel; 
	private JButton btnNext;
	
	private DataInfo info;
	private WizardProcess wProcess;

	@Override
	public void show() {
		initComponents();
		initLayout();
		initFunction();
		loadDataInfo();
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);		
	}
	
	private void loadDataInfo() {
		
		Integer index = (info.getData("index") == null ? -1 : Integer.parseInt(info.getData("index").toString()));
		
		if(index != -1){

			String name = (this.info.getData("name") == null ? "" : this.info.getData("name").toString()); 
			String title = (this.info.getData("title") == null ? "" : this.info.getData("title").toString());
			String xlabel = (this.info.getData("x-axis") == null ? "" : this.info.getData("x-axis").toString());
			String ylabel = (this.info.getData("y-axis") == null ? "" : this.info.getData("y-axis").toString());

			this.txtName.setText(name);
			this.txtTitle.setText(title);
			this.txtXLabel.setText(xlabel);
			this.txtYLabel.setText(ylabel);
		}
	}
	
	private void initComponents() {
		
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
		frame = new JFrame("New Graph - Graph Settings");
		
		this.txtName =  new JTextField(); 
		this.txtTitle = new JTextField();
		this.txtXLabel = new JTextField();
		this.txtYLabel = new JTextField();
		
		int type = (int) info.getData("type");
		if(type == 1 || type == 3){					
			txtTitle.setText("Interation " + ((List<String>) info.getData("interations")).get(0));				
		}else if (type == 4 || type == 6){
			String[] run = ((List<String>) info.getData("runs")).get(0).toString().split("_");
			String title = run[0];
			for (int i = 1; i < run.length-1; i++){
				if (i-1 < run.length - 2){
					title+="_";
				}
				title+=run[i];				
			}	
			txtTitle.setText(title);
		}else if (type == 5){
			String[] run = ((List<String>) info.getData("runs")).get(0).toString().split("_");
			String title = run[0];
			for (int i = 1; i < run.length-1; i++){
				if (i-1 < run.length - 2){
					title+="_";
				}
				title+=run[i];				
			}
			
			title+=" in Interation " + ((List<String>) info.getData("interations")).get(0);
			txtTitle.setText(title);
		}
		
		
		
		this.lblName = new JLabel("Graph Name:");
		this.lblTitle= new JLabel("Graph Title:");	
		
		btnCancel = new JButton("Cancel"); 
		btnNext = new JButton("Next"); 
		
		txtName.setFont(font);
		txtTitle.setFont(font);		
		
		lblName.setFont(font);
		lblTitle.setFont(font);			
		
		lblName.setToolTipText("File graph name. Used to export a graph");
		lblTitle.setToolTipText("The graph title.");
	}
	
	private void initLayout(){
		
		String columns = "10dlu, 60dlu, 5dlu, 40dlu, 5dlu, 50dlu, 5dlu, 40dlu, 10dlu";
		String rows = "10dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu"; 
		
		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);
		
		builder.add(lblName, CC.xy(2, 2));
		builder.add(txtName, CC.xyw(4, 2, 5));
		builder.add(lblTitle, CC.xy(2, 4)); 
		builder.add(txtTitle, CC.xyw(4, 4, 5));		
		
		builder.add(btnCancel, CC.xy(6, 6));
		builder.add(btnNext, CC.xy(8, 6));
		
		frame.setContentPane(builder.getPanel());
		
	}
	
	private void initFunction(){
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int type = (int) info.getData("type");
				info.putData("name", txtName.getText().trim());
				info.putData("title", txtTitle.getText().trim());							
				stop();
			}
		});
	}
	
	@Override
	public void putDataInfo(DataInfo dataInfo) {
		this.info = dataInfo;
	}

	@Override
	public JPanel make() {
		return null;
	}

	@Override
	public void addWizardProcessListener(WizardProcess wProcess) {
		this.wProcess = wProcess;
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
		this.wProcess.update();
	}

	@Override
	public void putCommandProcess(Facade facade) {
		
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
	}

}
