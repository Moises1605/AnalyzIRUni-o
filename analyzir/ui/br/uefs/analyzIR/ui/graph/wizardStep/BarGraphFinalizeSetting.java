package br.uefs.analyzIR.ui.graph.wizardStep;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.uefs.analyzIR.exception.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;

public class BarGraphFinalizeSetting implements WizardStep{

	private JFrame frame; 
	
	private JTextField txtAtValue;
	private JLabel lblAtValue;
	
	private JButton btnCancel; 
	private JButton btnOk; 
	
	private Facade facade;
	private DataInfo info;
	private WizardProcess wProcess;
	
	
	public BarGraphFinalizeSetting() {
	}
	public void show(){
		createComponents();
		layout(); 
		initFunctions();
		loadInfo();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void layout(){
	
		String columns = "10dlu, 60dlu, 5dlu, 40dlu, 5dlu, 50dlu, 5dlu, 40dlu, 10dlu";
		String rows = "10dlu, 30dlu, 5dlu, 20dlu, 10dlu, 30dlu, 10dlu"; 
		
		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);
		
		builder.add(lblAtValue, CC.xy(2, 2));
		builder.add(txtAtValue, CC.xyw(4, 2, 5));
		
		builder.addLabel("*Data format accepted: n1 (for a single value) or n1-n2 ", CC.xyw(2, 4, 7));
		builder.addLabel("(for a range between n1 and n2) or n1;n2;n3 (for differents values)", CC.xyw(2, 5, 7));
		builder.add(btnCancel, CC.xy(6, 6));
		builder.add(btnOk, CC.xy(8, 6));
		
		frame.setContentPane(builder.getPanel());
	}
	
	private void createComponents(){
		
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
		
		frame = new JFrame("Graph - Final Settings");
	
		txtAtValue = new JTextField(20);
		lblAtValue= new JLabel("At Value:");
		
		btnCancel = new JButton("Cancel"); 
		btnOk = new JButton("Finish"); 
		
		txtAtValue.setFont(font);
		btnCancel.setFont(font);
		btnOk.setFont(font);
	
		lblAtValue.setToolTipText("This value can be a range or a single value, por exampĺe: 20-22, will be [20,21,22] and 20;30 will be [20,30]");
		
		txtAtValue.setText("");
	}
	
	private void loadInfo(){
		
		Integer index = (info.getData("index") == null ? -1 : Integer.parseInt(info.getData("index").toString()));
		if(index != -1){
			txtAtValue.setText("");
		}else{
			if(info.getData("atValue") != null){
				String at = info.getData("atValue").toString();
				txtAtValue.setText(at);
			}
		}
	}
	
	private void initFunctions(){
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
						
				try 
				{	
					
					String atValue = null;
					String [] xValues = null;
					JPanel graph = null;
					
					Object ind = info.getData("index"); //hold the index string value
					Integer index = (ind == null ? -1 : Integer.parseInt(ind.toString()));
					
					List<String> measures = (List<String>) info.getData("measures");
					List<String> runs = (List<String>) info.getData("runs");
					List<String> topics = (List<String>) info.getData("topics");					
					String name = info.getData("name").toString();
					String title = info.getData("title").toString();
					String x_axis = info.getData("x-axis").toString();
					String y_axis = info.getData("y-axis").toString();
					
					int type = facade.getMeasureType(measures);
					System.out.println("Type: " + type);
					
					if(type != 0){
						
						atValue = txtAtValue.getText().trim();
						xValues = checkValues(atValue);
						
						info.putData("xValues", xValues);
						info.putData("atValue", atValue);
					}
					
					//Index == -1 implies we are creating a new graph
					if(index == -1)
					{
						graph = facade.createGraph(type, title, measures, runs, topics, xValues, name, atValue, x_axis, y_axis, null, 1);
						info.putData("view", graph);
						stop();
					}else
					{
						String oldName = info.getData("oldName").toString();
						System.err.println("Vai replace " + oldName);
						graph = facade.replaceGraph(oldName, title, measures, runs, topics, xValues, name, atValue, x_axis, y_axis, 1);
						info.putData("view", graph);
						stop();
					}
					
					frame.dispose();
				
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InterruptedException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (MeasureNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidGraphTypeException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidDataFormatException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidItemNameException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (ItemNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (EmptyTopicMeasureValueException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (NoGraphItemException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidGraphNameException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (GraphNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (QrelItemNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (TopicNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidQrelFormatException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (GraphItemNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoGraphPointException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
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

