package br.uefs.analyzIR.ui.graph.wizardStep;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
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
import br.uefs.analyzIR.exception.NoGraphPointException;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;

public class CurveGraphFinalizeSetting implements WizardStep {

	private JFrame frame;
	private JTextField txtFrom;
	private JTextField txtTo;
	private JTextField txtStep;
	private JLabel lblFrom;
	private JLabel lblTo;
	private JLabel lblStep;

    private JButton btnCancel;
	private JButton btnOk;

	private Facade facade;
	private DataInfo info;
	private WizardProcess wProcess;
	
	
	public CurveGraphFinalizeSetting() {
	}

	public void show() {
		createComponents();
		layout();
		initFunctions();
		loadInfo();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private void layout() {

		String columns = "10dlu, 50dlu, 5dlu, 20dlu, 10dlu, right:50dlu, 5dlu, 50dlu, 10dlu";
		String rows = "10dlu, 20dlu, 10dlu, 20dlu, 10dlu, 20dlu, 10dlu";

		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);

		builder.add(lblFrom, CC.xy(2, 2));
		builder.add(txtFrom, CC.xyw(4, 2, 2));
		builder.add(lblTo, CC.xy(6, 2));
		builder.add(txtTo, CC.xy(8, 2));
		builder.add(lblStep, CC.xy(2, 4));
		builder.add(txtStep, CC.xyw(4, 4, 2));
		builder.add(btnCancel, CC.xy(6, 6));
		builder.add(btnOk, CC.xy(8, 6));

		frame.setContentPane(builder.getPanel());
	}

	private void createComponents() {

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

		frame = new JFrame("Graph - Final Settings");
		txtFrom = new JTextField();
		txtTo = new JTextField();
		txtStep = new JTextField();
		btnCancel = new JButton("Cancel");
		
		lblFrom = new JLabel("From:");
		lblTo = new JLabel("To:");
		lblStep = new JLabel("Step:");
		
		btnOk = new JButton("Finish");

		txtFrom.setFont(font);
		txtTo.setFont(font);
		txtStep.setFont(font);
		btnCancel.setFont(font);
		btnOk.setFont(font);
		
		lblFrom.setToolTipText("Curve initial point (x-axis) or initial depth for the measure computation.");
		lblTo.setToolTipText("Curve final point (x-axis) or final depth for the measure computation.");
		lblStep.setToolTipText("Itermediate points (common difference).");
		
		txtFrom.setText("0");
		txtTo.setText("1");
		txtStep.setText("0.1");	
	}

	private void loadInfo() {
		
		Integer index = (info.getData("index") == null ? -1 : Integer.parseInt(info.getData("index").toString()));
		
		if(index != -1){
		
			if(info.getData("xvalues") != null){
				String [] range = ((String [])info.getData("xvalues"));
				String begin = range[0];
				String second = range[1];
				String end = range[range.length - 1];
				
				double n1 = Double.parseDouble(begin);
				double n2 = Double.parseDouble(second);
				double step = n2 -n1;
				
				DecimalFormat format = new DecimalFormat("0.00");
				String valueStep = format.format(step).replace(',', '.').trim();
				
				txtFrom.setText(begin);
				txtTo.setText(end);
				txtStep.setText(valueStep);
			}
		}else{
			
			txtFrom.setText("0");
			txtTo.setText("1");
			txtStep.setText("0.1");	
		}
	}

	private void initFunctions() {
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
					
					try {
						
						String [] xValues = getXValues();
						info.putData("xvalues", xValues);
						
						if(index == -1){
							graph = facade.createGraph(type, title, measures, runs,topics, xValues, name, null, x_axis, y_axis, null, 1);
							info.putData("view",graph);
							stop();
						}else{
							String oldName = info.getData("oldName").toString();
							graph = facade.replaceGraph(oldName, title, measures, runs,topics, xValues, name, null, x_axis, y_axis, 1);
							info.putData("view", graph);
							stop();
						}		
						
						frame.dispose();
					
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InterruptedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (MeasureNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidGraphTypeException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidDataFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidItemNameException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (ItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (EmptyTopicMeasureValueException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NoGraphItemException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidGraphNameException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (GraphNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (QrelItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (TopicNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidQrelFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (GraphItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NoGraphPointException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidGraphDataFormat e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			
		});	
	}

	
	private String [] getXValues() throws InvalidGraphDataFormat{

		double begin = Double.parseDouble(txtFrom.getText().trim());
		double to = Double.parseDouble(txtTo.getText().trim());
		double step = Double.parseDouble(txtStep.getText().trim());

		if (to <= begin) {
			throw new InvalidGraphDataFormat("You must choose a ending value bigger than start.");
		} else {

			List<String> values = new ArrayList<String>();
			DecimalFormat format = new DecimalFormat("0.00");
			String [] xValues = null;
			
			for (double i = begin; i < to; i += step) {
				String value = format.format(i).replace(',', '.').trim();
				values.add(value);
			}	
			
			String end = format.format(to).replace(',', '.').trim();
			
			if(!values.contains(end))
				values.add(format.format(to).replace(',', '.').trim());
			
			xValues = new String[values.size()];
			xValues = values.toArray(xValues);
			
			return xValues;
		}
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
	public DataInfo getDataInfo() {
		return this.info;
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
}
