package br.uefs.analyzIR.ui.screenView;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import br.uefs.analyzIR.ui.common.InteractiveGraphFinalizeSettingSingle;
import br.uefs.analyzIR.ui.graph.wizardStep.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.ui.common.ProjectView;
import br.uefs.analyzIR.ui.graph.wizard.EditGraphWizard;
import br.uefs.analyzIR.ui.graph.wizard.EditInteractiveGraphProcess;
import br.uefs.analyzIR.ui.graph.wizard.ExportGraphProcess;
import br.uefs.analyzIR.ui.interactiveProject.InteractiveDataGraphSetting;
import br.uefs.analyzIR.ui.standardProject.DataGraphSettingWizardStep;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.ScreenView;
import br.uefs.analyzIR.ui.structure.WizardListener;
import br.uefs.analyzIR.ui.structure.WizardStep;

public class GraphScreenView implements ScreenView {

	// Options
	private JPanel panel;
	private JPanel graph;
	private JButton btnExport;
	private JButton btnEdit;
	private JButton btnOpen;
	private JButton btnEditLabels;
	private Font font;
	private String lblCombo;
	private JComboBox<String> comboPlots;

	// plot
	private Facade facade;
	private DataInfo info;
	
	public GraphScreenView(JPanel graph, Facade facade) {
		this.graph = graph;
		this.facade = facade;
	}

	@Override
	public void putDataInfo(DataInfo dataInfo) {
		this.info = dataInfo;
	}

	@Override
	public void show() {
	}

	@Override
	public JPanel make() {
		
		initComponents();
		initLayout();
		initFunctions();
		initCombo();
		initComboFunction();
		return this.panel;
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {

		font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

		btnEdit = new JButton("Edit");
		btnExport = new JButton("Export");
		btnOpen = new JButton("New Window");
		btnEditLabels = new JButton("Edit Labels");
		comboPlots = new JComboBox<>();
		comboPlots.addItem("-- Select an item --");

		btnEdit.setFont(font);
		btnExport.setFont(font);
		btnOpen.setFont(font);
		btnEditLabels.setFont(font);
		comboPlots.setFont(font);
		
		List<String> measures = (List<String>)this.info.getData("measures");
		
		if(measures.size() > 1){
			this.lblCombo = "Measures:";
		}else{
			this.lblCombo = "Topics:";
		}
	}

	private void initLayout() {

		String columns = "10dlu, left:50dlu, 2dlu, left:50dlu, 5dlu, left:70dlu, 5dlu, left:60dlu, 5dlu, left:40dlu, 5dlu, left:300dlu, 150dlu, 30dlu";
		String rows = "5dlu, 370dlu, 20dlu, 20dlu, 10dlu";

		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);

		builder.add(graph, CC.xywh(2, 2, 11, 1));
		builder.add(btnEdit, CC.xy(2, 3));
		builder.add(btnExport, CC.xy(4, 3));
		builder.add(btnOpen, CC.xy(6, 3));
		builder.add(btnEditLabels, CC.xy(8, 3));
		builder.addLabel(lblCombo, CC.xy(10, 3));
		builder.add(comboPlots, CC.xy(12, 3));

		this.panel = builder.getPanel();
	}

	private void initFunctions() {

		btnExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ExportGraphProcess exportProcess = new ExportGraphProcess(facade);
				
				WizardStep step1 = new ExportGraph(); 
				
				exportProcess.addWizardStep(step1);
				step1.addWizardProcessListener(exportProcess);
				
				exportProcess.setDataInfo(info);
				exportProcess.start();
			}
		});

		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int projectType = facade.getType();
								
				if (projectType < 3){

					EditGraphWizard editProcess = new EditGraphWizard(facade);					
					
					WizardStep sp1 = new DataGraphSettingWizardStep();
					WizardStep sp2 = new DataGraphLabelsSettingWizardStep();
					WizardStep sp3 = new BarGraphFinalizeSetting();
					WizardStep sp4 = new CurveGraphFinalizeSetting();
					
					sp1.addWizardProcessListener(editProcess);
					sp2.addWizardProcessListener(editProcess);
					sp3.addWizardProcessListener(editProcess);
					sp4.addWizardProcessListener(editProcess);
					
					editProcess.addWizardStep(sp1);
					editProcess.addWizardStep(sp2);
					editProcess.addWizardStep(sp3);
					editProcess.addWizardStep(sp4);
					
					editProcess.setDataInfo(info);
					editProcess.addWizardListener((WizardListener) info.getData("listener"));
					editProcess.start();
					
				}else if(projectType == 3){

					EditInteractiveGraphProcess editProcess = new EditInteractiveGraphProcess(facade);
					
					/*WizardStep sp1 = new InteractiveDataGraphSetting();
					WizardStep sp2 = new InteractiveDataGraphLabelsSetting();
					WizardStep sp3 = new InteractiveGraphFinalizeSetting();
					
					editProcess.addWizardStep(sp1);
					editProcess.addWizardStep(sp2);
					editProcess.addWizardStep(sp3);*/
					WizardStep sp1 = new InteractiveDataGraphSetting();
					WizardStep sp2 = new InteractiveStatisticsTestStep();
					WizardStep sp3 = new InteractiveDataGraphLabelsSetting();
					WizardStep sp4 = new InteractiveGraphFinalizeSetting();
					//Não tinha antes
					WizardStep sp5 = new InteractiveGraphFinalizeSettingSingle();

					//Não tinha antes
					sp1.addWizardProcessListener(editProcess);
					sp2.addWizardProcessListener(editProcess);
					sp3.addWizardProcessListener(editProcess);
					sp4.addWizardProcessListener(editProcess);
					sp5.addWizardProcessListener(editProcess);


					editProcess.addWizardStep(sp1);
					editProcess.addWizardStep(sp2);
					editProcess.addWizardStep(sp3);
					editProcess.addWizardStep(sp4);
					editProcess.addWizardStep(sp5);
					
					editProcess.setDataInfo(info);
					editProcess.addWizardListener((WizardListener) info.getData("listener"));
					editProcess.start();
				}
				
			}
		});

		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openNewWindow();
			}
		});
		
		btnEditLabels.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}


	private void openNewWindow() {
		JFrame frame = new JFrame("Graph: ");
		frame.setSize(600, 400);
		frame.setContentPane(graph);
		frame.setVisible(true);
	}



	private void initCombo() {

		/*if (maker.listMeasures().size() <= 1) {
			List<String> topics = maker.getTopics();
			for (String topic : topics) {
				comboPlots.addItem(topic);
			}
		}else{
			for(String measure: maker.listMeasures()){
				comboPlots.addItem(measure);
			}
		}*/

		
	}
	
	private void initComboFunction(){
		
		comboPlots.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String value = "";
				String name = "";
				List<String> measures = null;
				List<String> topics = null;
				
				/*if(maker.listMeasures().size() <= 1){
					
					value = comboPlots.getSelectedItem().toString();
					name = maker.getName() + " ("+value+")"; 
					measures = maker.listMeasures();
					
					topics = new ArrayList<String>();
					topics.add(value);
					
				}else{
					
					measures = new ArrayList<String>();
					value = comboPlots.getSelectedItem().toString(); 
					measures.add(value);
					name = maker.getName() + " ("+value+")";
					
					topics = maker.getTopics();
				}*/
				
				//try {
					
					//JPanel panel = facade.createPlot(maker.getTitle(), measures, maker.getRuns(), topics, maker.getXValues(), name, maker.getAtValue(), "", "");
					//PlotGUIMaker m = new PlotGUIMaker(measures, maker.getRuns(), topics, name, maker.getXValues(), maker.getAtValue());
					GraphScreenView view = new GraphScreenView(panel, facade);
					ProjectView.getInstance(null).addGraph(view);
					
				/*} catch (NumberFormatException e1) {
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
				} catch (InvalidFormatDataException e1) {
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
				}*/
			}
		});
		
	}
}