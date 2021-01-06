package br.uefs.analyzIR.ui.graph.wizardStep;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;

public class DataGraphLabelsSettingWizardStep implements WizardStep{

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
	
	private Facade facade;
	
	@Override
	public void show() {
		
		//Save old name
		this.info.putData("oldName", this.info.getData("name"));
		System.err.println("Salvou oldName como: " + this.info.getData("name"));
		
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
		frame = new JFrame("Graph - Graph Settings");
		
		this.txtName =  new JTextField(); 
		this.txtTitle = new JTextField();
		this.txtXLabel = new JTextField();
		this.txtYLabel = new JTextField(); 
		
		this.lblName = new JLabel("Graph Name:");
		this.lblTitle= new JLabel("Graph Title:");
		this.lblXLabel = new JLabel("X-axis Label:");
		this.lblYLabel = new JLabel("Y-axis Label:");
		
		btnCancel = new JButton("Cancel"); 
		btnNext = new JButton("Next"); 
		
		txtName.setFont(font);
		txtTitle.setFont(font);
		txtXLabel.setFont(font);
		txtYLabel.setFont(font);
		
		lblName.setFont(font);
		lblTitle.setFont(font);
		lblXLabel.setFont(font);
		lblYLabel.setFont(font);
		
		
		lblName.setToolTipText("File graph name. Used to export a graph");
		lblTitle.setToolTipText("The graph title.");
		lblXLabel.setToolTipText("The graph X-axis label.");
		lblYLabel.setToolTipText("The graph Y-axis label.");
	}
	
	private void initLayout(){
		
		String columns = "10dlu, 60dlu, 5dlu, 40dlu, 5dlu, 50dlu, 5dlu, 40dlu, 10dlu";
		String rows = "10dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 5dlu, 30dlu, 10dlu"; 
		
		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);
		
		builder.add(lblName, CC.xy(2, 2));
		builder.add(txtName, CC.xyw(4, 2, 5));
		builder.add(lblTitle, CC.xy(2, 4)); 
		builder.add(txtTitle, CC.xyw(4, 4, 5));
		builder.add(lblXLabel, CC.xy(2, 6)); 
		builder.add(txtXLabel, CC.xyw(4, 6, 5));
		builder.add(lblYLabel, CC.xy(2, 8)); 
		builder.add(txtYLabel, CC.xyw(4, 8, 5));
		
		
		builder.add(btnCancel, CC.xy(6, 10));
		builder.add(btnNext, CC.xy(8, 10));
		
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
				
				String newGraphName = txtName.getText().trim();
				
				//oldName is null when a new graph is being created, otherwise it has the name of the graph is being edited.
				String oldName = info.getData("oldName") == null ? "" : info.getData("oldName").toString();
				
				//Checks if the newName is available or the new name is the same as the old name 
				if(oldName.equals(newGraphName) || facade.isGraphNameAvailable(newGraphName)){
					info.putData("name", newGraphName);
					info.putData("title", txtTitle.getText().trim() );
					info.putData("x-axis", txtXLabel.getText().trim());
					info.putData("y-axis", txtYLabel.getText().trim());
					stop();
				}
				else{
					String message = "New graph name (\"" + newGraphName + "\") already exists.\n Please enter a new name.";
					JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
					txtName.setSelectionColor(new Color(244, 66, 66));
					txtName.selectAll();
					txtName.requestFocus();					
				}
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
		this.facade = facade;
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
	}
}
