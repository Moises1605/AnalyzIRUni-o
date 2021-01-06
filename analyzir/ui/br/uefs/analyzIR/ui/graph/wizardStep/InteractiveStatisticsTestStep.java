package br.uefs.analyzIR.ui.graph.wizardStep;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

/*
 * Step para a configuração dos testes estatísticos.
 * 
 * @author Moisés
 */

public class InteractiveStatisticsTestStep implements WizardStep{
	
	private JFrame frame;
	private JButton btnNext;
	private JButton btnCancel;
	private JTextField alpha;
	private JTextField liberty;
	private JLabel alphaLabel;
	//private JLabel libertyLabel;
	private JLabel runsLabel;
	private JLabel testsLabel;
	private JLabel libraryLabel;
	private JLabel alphaTStudentLabel;
	private JComboBox<String> library;
	private JComboBox<String> listTests;
	private JComboBox<String> listRuns;
	private JComboBox<String> alphaTStudent;
	//private	JComboBox<String> listRunsTest;
	
	private DataInfo info;
	private Facade facade; 
	private WizardProcess wProcess;
	
	@Override
	public void show() {
		initComponents();
		initLayout();
		initFunction();
		//loadDataInfo();
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);		
	}
	
	private void initComponents() { 
		
		frame = new JFrame("Statiscs Tests Interactive "); 
		btnNext = new JButton("Next");
		btnCancel = new JButton("Cancel");
		listTests = new JComboBox<String>();
		listRuns = new JComboBox<String>();
		alphaTStudent = new JComboBox<String>();
		alpha = new JTextField();
		alphaLabel =  new JLabel("Alpha");
		runsLabel = new JLabel("Baseline:");
		testsLabel = new JLabel("Test:");
		alphaTStudentLabel = new JLabel("Alpha(t de Student)");
		library = new JComboBox<String>();
		libraryLabel = new JLabel("Library");

		//listRunsTest = new JComboBox<String>();		
		
	}
	
	
	private void initLayout(){
		
		String columns = "10dlu, 80dlu, 5dlu, 40dlu, 5dlu, 90dlu, 40dlu, 5dlu, 40dlu, 10dlu";
		String rows = "10dlu, 30dlu, 10dlu, 30dlu, 10dlu, 30dlu";
		
		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);

		listTests.addItem("Select test");
		listTests.addItem("Wilcoxon");
		listTests.addItem("Mann Whitney U");
		listTests.addItem("T-Student");

		alphaTStudent.addItem("Select a Alpha");
		alphaTStudent.addItem("0.90");
		alphaTStudent.addItem("0.95");
		alphaTStudent.addItem("0.975");
		alphaTStudent.addItem("0.99");
		alphaTStudent.addItem("0.995");
		alphaTStudent.addItem("0.999");
		
		List<String> runs = (List<String>) info.getData("runs2");
		listRuns.addItem("Select system");
		for(String run:runs) {
			listRuns.addItem(run);
			//listRunsTest.addItem(run);
		}

		library.setEnabled(false);
		alphaTStudent.setEnabled(false);
		//listRunsTest.setEnabled(false);
		
		/*builder.add(lblName, CC.xy(2, 2));
		builder.add(txtName, CC.xyw(4, 2, 5));
		builder.add(lblTitle, CC.xy(2, 4)); 
		builder.add(txtTitle, CC.xyw(4, 4, 5));*/

        builder.add(runsLabel,CC.xy(6, 1));
        builder.add(testsLabel,CC.xy(2, 1));
        builder.add(alphaLabel,CC.xy(2, 3));
        builder.add(libraryLabel,CC.xy(6, 3));
		builder.add(alphaTStudentLabel,CC.xy(2, 5));
        builder.add(library,CC.xy(6, 4));
        builder.add(listTests,CC.xy(2, 2));
		builder.add(listRuns,CC.xy(6, 2));
        builder.add(alpha,CC.xy(2, 4));
		builder.add(alphaTStudent,CC.xy(2, 6));
        builder.add(btnCancel, CC.xy(7, 6));
		builder.add(btnNext, CC.xy(9, 6));
		
		frame.setContentPane(builder.getPanel());
		
	}
	
private void initFunction(){

        listTests.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
				library.removeAllItems();
                if(!listTests.getSelectedItem().equals("Mann Whitney U")) {
                    library.setEnabled(true);
                    if(listTests.getSelectedItem().equals("T-Student")){
                        //library.removeAllItems();
                        library.addItem("Apache Commons");
                        library.addItem("JSC");
                        alphaTStudent.setEnabled(true);
                        alpha.setEnabled(false);
                    }
                    else{
                        //library.removeAllItems();
                        library.addItem("Apache Commons");
                        library.addItem("JSC");
                        library.addItem("Mackenzie");
						alphaTStudent.setEnabled(false);
						alpha.setEnabled(true);
                    }
                }else if(listTests.getSelectedItem().equals("Select test")){

				}
                else{
                	library.addItem("Default");
                    library.setEnabled(false);
					alphaTStudent.setEnabled(false);
					alpha.setEnabled(true);
                }
            }
        });
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String baseline = listRuns.getItemAt(listRuns.getSelectedIndex());	
				String test = listTests.getItemAt(listTests.getSelectedIndex());
				String lib = library.getItemAt(library.getSelectedIndex());
				double alpha_D = 0;
				if(test.equalsIgnoreCase("T-Student")){
					alpha_D = Double.parseDouble(alphaTStudent.getItemAt(alphaTStudent.getSelectedIndex()));
				}else if(!(test.equalsIgnoreCase("Select test") || alpha.getText().trim().equalsIgnoreCase(""))){
					alpha_D = Double.parseDouble(alpha.getText());
				}
				System.out.println("Alpha" + alpha_D);
				if((!baseline.equalsIgnoreCase("Select system")) && (!test.equalsIgnoreCase("Select test"))){
					info.putData("baseline", baseline);
					info.putData("Test", test);
					info.putData("lib",lib);
					info.putData("alpha_D",alpha_D);
					stop();
				}
			}
		});
	}

	@Override
	public void putDataInfo(DataInfo dataInfo) {
		this.info = dataInfo;
		
	}

	@Override
	public DataInfo getDataInfo() {
		return this.info;
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
		//this.facade = facade;
		
	} 
	
}
