package br.uefs.analyzIR.ui.interactiveProject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.measure.data.MeasureSet;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.MeasuresGroupNotFoundException;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;
import br.uefs.analyzIR.ui.util.DataTableModel;
/**
 * First step screen for graph creation
 * @author nilson
 *
 */
public class InteractiveDataGraphSetting implements WizardStep{


	private Font font;
	private JFrame frame;
	
	//Tabelas dos itens
	private JTable tbRuns;
	private JTable tbSelectedRuns;
	private JTable tbTopics;
	private JTable tbSelectedTopics;
	private JTable tbInterations;
	private JTable tbSelectedInterations;
	private JTable tbMeasures;
	private JTable tbSelectedMeasures;

	//Painel onde terá as tabelas dos itens;
	private JScrollPane jspRuns;
	private JScrollPane jspSelectedRuns;
	private JScrollPane jspTopics;
	private JScrollPane jspSelectedTopics;
	private JScrollPane jspInteration;
	private JScrollPane jspSelectedInteration;
	private JScrollPane jspMeasure;
	private JScrollPane jspSelectedMeasures;
	
	//Botões de seções dos itens
	private JButton btnSingleR;
	private JButton btnAllR;
	private JButton btnSingleBackR;
	private JButton btnAllBackR;

	private JButton btnSingleT;
	private JButton btnAllT;
	private JButton btnSingleBackT;
	private JButton btnAllBackT;
	
	private JButton btnSingleI;
	private JButton btnAllI;
	private JButton btnSingleBackI;
	private JButton btnAllBackI;

	private JButton btnSingleM;
	private JButton btnAllM;
	private JButton btnSingleBackM;
	private JButton btnAllBackM;

	private JButton btnNext;
	private JButton btnCancel;
	//CheckBox para selecionar o teste estatístico para sistemas interativos.
	private JCheckBox cbStatistics;

	private Facade facade;
	private DataInfo info;
	private WizardProcess wProcess;
	final int measures_Selected = 1;
	final int runs_Selected = 0;
	

	
	public InteractiveDataGraphSetting() {
	}

	@Override
	public void show()  {
		
		initComponents();
		initLayout();
		loadInfo();
		loadDataInfo();
		initFunctions();
		//showNotes();

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	/*
	* Método responsável por mostrar observaçõesnecessárias antes do usuário começar a configurar o grafico.
	*/
	public void showNotes(){
		JOptionPane.showMessageDialog(null,
				"1º Como existe mais de uma medida, na representação do gráfico irá aparecer a média das medidas da consulta\n" +
						"2º Como o grafico escolhido é do tipo barra, caso exista relevancia a run irá conter um '*' na legenda\n" +
						"3º Para exibir essa janela novamente, aperte na '?' no canto inferior direito",
				"Notes for the user", JOptionPane.INFORMATION_MESSAGE);
	}

	private void initComponents() {

		font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

		frame = new JFrame("New Graph - Graph Settings");

		jspRuns = new JScrollPane();
		jspSelectedRuns = new JScrollPane();
		jspTopics = new JScrollPane();
		jspSelectedTopics = new JScrollPane();
		jspInteration =  new JScrollPane();
		jspSelectedInteration =  new JScrollPane();
		jspMeasure = new JScrollPane();
		jspSelectedMeasures = new JScrollPane();

		tbRuns = new JTable();
		tbSelectedRuns = new JTable();
		tbTopics = new JTable();
		tbSelectedTopics = new JTable();
		tbInterations = new JTable();
		tbSelectedInterations = new JTable();
		tbMeasures = new JTable();
		tbSelectedMeasures = new JTable();

		createScroll(jspRuns);
		createScroll(jspSelectedRuns);
		createScroll(jspTopics);
		createScroll(jspSelectedTopics);
		createScroll(jspInteration);
		createScroll(jspSelectedInteration);
		createScroll(jspMeasure);
		createScroll(jspSelectedMeasures);

		createTable(new ArrayList<String>(), tbRuns, jspRuns, "Runs");
		createTable(new ArrayList<String>(), tbSelectedRuns, jspSelectedRuns, "Selected Runs");
		createTable(new ArrayList<String>(), tbTopics, jspTopics, "Topics");
		createTable(new ArrayList<String>(), tbSelectedTopics,
				jspSelectedTopics, "Selected Topics");
		createTable(new ArrayList<String>(), tbInterations, jspInteration, "Interations");
		createTable(new ArrayList<String>(), tbSelectedInterations, jspSelectedInteration, "Selected Interations");
		createTable(new ArrayList<String>(), tbMeasures, jspMeasure, "Measures");
		createTable(new ArrayList<String>(), tbSelectedMeasures,
				jspSelectedMeasures, "Selected Measures");

		btnSingleR = new JButton(">");
		btnAllR = new JButton(">>");
		btnSingleBackR = new JButton("<");
		btnAllBackR = new JButton("<<");

		btnSingleT = new JButton(">");
		btnAllT = new JButton(">>");
		btnSingleBackT = new JButton("<");
		btnAllBackT = new JButton("<<");
		
		btnSingleI = new JButton(">");
		btnAllI = new JButton(">>");
		btnSingleBackI = new JButton("<");
		btnAllBackI = new JButton("<<");
		
		btnSingleM = new JButton(">");
		btnAllM = new JButton(">>");
		btnSingleBackM = new JButton("<");
		btnAllBackM = new JButton("<<");

		btnNext = new JButton("Next");
		btnCancel = new JButton("Cancel");
		cbStatistics = new JCheckBox("Statistical tests");

		font = new Font(Font.SANS_SERIF, Font.BOLD, 13);

		btnNext.setFont(font);
		btnCancel.setFont(font);
	}

	private void initLayout() {

		String columns = "20dlu, 200dlu, 20dlu, 30dlu, 20dlu, 100dlu, 60dlu, 5dlu, 50dlu, 30dlu, 10dlu";
		String rows = "20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, "
				+ "5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, "
				+ "5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu";

		FormLayout layout = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(layout);

		builder.add(jspRuns, CC.xywh(2, 1, 1, 7));
		builder.add(jspSelectedRuns, CC.xywh(6, 1, 5, 7));
		builder.add(jspTopics, CC.xywh(2, 9, 1, 7));
		builder.add(jspSelectedTopics, CC.xywh(6, 9, 5, 7));
		builder.add(jspInteration, CC.xywh(2, 17, 1, 7));
		builder.add(jspSelectedInteration, CC.xywh(6, 17, 5, 7));
		builder.add(jspMeasure, CC.xywh(2, 25, 1, 7));
		builder.add(jspSelectedMeasures, CC.xywh(6, 25, 5, 7));

		builder.add(btnSingleR, CC.xywh(4, 1, 1, 1));
		builder.add(btnAllR, CC.xywh(4, 3, 1, 1));
		builder.add(btnAllBackR, CC.xywh(4, 5, 1, 1));
		builder.add(btnSingleBackR, CC.xywh(4, 7, 1, 1));

		builder.add(btnSingleT, CC.xywh(4, 9, 1, 1));
		builder.add(btnAllT, CC.xywh(4, 11, 1, 1));
		builder.add(btnAllBackT, CC.xywh(4, 13, 1, 1));
		builder.add(btnSingleBackT, CC.xywh(4, 15, 1, 1));

		builder.add(btnSingleI, CC.xywh(4, 17, 1, 1));
		builder.add(btnAllI, CC.xywh(4, 19, 1, 1));
		builder.add(btnAllBackI, CC.xywh(4, 21, 1, 1));
		builder.add(btnSingleBackI, CC.xywh(4, 23, 1, 1));
		
		builder.add(btnSingleM, CC.xywh(4, 25, 1, 1));
		builder.add(btnAllM, CC.xywh(4, 27, 1, 1));
		builder.add(btnAllBackM, CC.xywh(4, 29, 1, 1));
		builder.add(btnSingleBackM, CC.xywh(4, 31, 1, 1));

		builder.add(btnNext, CC.xy(9, 33));
		builder.add(btnCancel, CC.xy(7, 33));
		builder.add(cbStatistics, CC.xy(2,33));

		frame.setContentPane(builder.getPanel());
		Check_Rules(runs_Selected);
	}
	
	/***
	 * 
	 */
	private void initCommumFunctions(){


		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<String> runs = new ArrayList<String>(); 
				List<String> topics = new ArrayList<String>();
				List<String> interations = new ArrayList<String>();
				List<String> measures = new ArrayList<String>(); 
				
				DataTableModel mRuns = (DataTableModel) tbSelectedRuns.getModel(); 
				DataTableModel mTopics = (DataTableModel) tbSelectedTopics.getModel(); 
				DataTableModel mInterations = (DataTableModel) tbSelectedInterations.getModel();
				DataTableModel mMeasures = (DataTableModel) tbSelectedMeasures.getModel();
				List<String> runs2 = new ArrayList<String>();

				int testRuns = mRuns.getRowCount();
				int testMeasures = mMeasures.getRowCount();
				int testInterations = mInterations.getRowCount();
				int testTopics = mTopics.getRowCount();
				boolean test = false;
				boolean testSelected = false;
				boolean selected = cbStatistics.isSelected();

				//Se o usuário escolheu grafico com testes estatiśticos.
				/*if (cbStatistics.isSelected()) {
					info.putData("confirmacao", true);
				} else
					info.putData("confirmacao", false);*/

				//Verifica se o usuário usou uma configuração válida para a criação do tipo grafico escolhido
				if(!selected){
					if(testRuns > 1 && testTopics > 1 && testInterations > 1){
						test = true;
					}
					if(testRuns > 1 && testInterations > 1 && testMeasures > 1){
						test = true;
					}
					if(testRuns > 1 && testMeasures > 1 && testTopics > 1){
						test = true;
					}
					if(testMeasures > 1 && testTopics > 1  && testInterations > 1 ) {
						test = true;
					}
				}else{
					if(testRuns < 2 || testMeasures > 1 /*|| testTopics == 1*/){
						testSelected = true;
					}
				}

				
				if(test == testSelected) {
					for (int j = 0; j < mTopics.getRowCount(); j++) {
						String topic = mTopics.getValueAt(j, 1).toString();
						topics.add(topic);
					}

					for (int j = 0; j < mInterations.getRowCount(); j++) {
						String interation = mInterations.getValueAt(j, 1).toString();
						interations.add(interation);
					}

					Collections.sort(interations);


					for (int i = 0; i < mMeasures.getRowCount(); i++) {
						String value = mMeasures.getValueAt(i, 1).toString();
						measures.add(value);
					}


					//Encontra o maximo de iterações por sistema
					Run[] runAuxIteration = facade.getProjectController().getRuns();
					HashMap<String, Integer> runsAux = new HashMap<String,Integer>();
					for(int h = 0;h<runAuxIteration.length;h++){
						for(int p = 0; p < mRuns.getRowCount(); p++){
							if(runAuxIteration[h].getName().equalsIgnoreCase(mRuns.getValueAt(p, 1).toString())){
								runsAux.put(runAuxIteration[h].getName(),runAuxIteration[h].getMaxInteration());
								break;
							}
						}
					}

					int sizeInteration = interations.size();
					for (int k = 0; k < mRuns.getRowCount(); k++) {
						int runIteration = runsAux.get(mRuns.getValueAt(k, 1).toString());//Vai substituir o sizeInteration
						runs2.add(mRuns.getValueAt(k, 1).toString());
						for (int i = 0; i < sizeInteration; i++){
							System.out.println(mRuns.getValueAt(k, 1).toString() + runIteration);
							if(Integer.parseInt(interations.get(i)) <= runIteration){
								runs.add(mRuns.getValueAt(k, 1).toString() + "_0" + interations.get(i));
							}

						}
					}
				    for(String run:runs){
						System.out.println("Teste iteração: " + run);
					}
					int type = 0;
					//Mudança para aceitar os testes estaticos para sistemas iterativos
					if (topics.size() > 1 && mRuns.getRowCount() > 1) {
						if(selected){
							if(interations.size() > 1){
								type = 2;
							}
							else{
								type = 3;
							}
						}
						else{
							type = 1;
						}
					} else if (mRuns.getRowCount() > 1 && interations.size() > 1) {
						type = 2;
					} else if (mRuns.getRowCount() > 1 && measures.size() > 1) {
						type = 3;
					} else if (topics.size() > 1 && interations.size() > 1) {
						type = 4;
					} else if (topics.size() > 1 && measures.size() > 1) {
						type = 5;
					} else if (interations.size() > 1 && measures.size() > 1) {
						type = 6;
					} else if (topics.size() > 1) {
						type = 1;
					} else if (interations.size() > 1) {
						type = 2;
					} else if (mRuns.getRowCount() > 1 || measures.size() > 1) {
						type = 3;
					} else {
						type = 3;
					}

					Collections.sort(runs);

					info.putData("measures", measures);
					info.putData("runs", runs);
					info.putData("topics", topics);
					info.putData("interations", interations);
					info.putData("type", type);
					info.putData("runs2", runs2);
					info.putData("confirmacao", cbStatistics.isSelected());
					info.putData("numbersIteration",runsAux);

					//Avisos para o usuário,dependendo do tipo de gráfico escolhido.
					if(selected && topics.size() > 1){
						String message = "1º As there is more than one query in the graph configuration, the average of the query measurements for the best data visualization will appear in its representation.\n" ;
						if(interations.size() == 1 ){
							message = "1º As there is more than one query in the graph configuration, the average of the query measurements for the best data visualization will appear in its representation.\n" +
									"2º As the chosen graphic is a bar type, if there is relevance, the run will contain an '*' in the legend.\n";
						}
						JOptionPane.showMessageDialog(null, message, "Notes for the user", JOptionPane.INFORMATION_MESSAGE);

					}

					stop();
				}
				//Caso a configuração escolhida pelo usuário não seja válida.
				else {
					String message = "Only two fields can have more than one value.";
					if(testSelected){
						message = "Invalid configuration, check the requested data. This type of graph only accepts more than one run and topics, and only accepts only one measure.";
					}
					JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void initFunctions() {
		initRunFunctions();
		initTopicFunctions();
		initInterationFunctions();
		initMeasureFunctions();
		initCommumFunctions();
	}
	
	private void loadInfo() {

		List<String> runs = facade.listRuns();
		List<String> topics = facade.listTopics();
		List<String> interations = facade.listInterations();
		List<String> measures = facade.listMeasures();
		

		DataTableModel dtruns = (DataTableModel) tbRuns.getModel();
		DataTableModel dttopics = (DataTableModel) tbTopics.getModel();
		DataTableModel dtinterations = (DataTableModel) tbInterations.getModel();
		DataTableModel dtmeasures = (DataTableModel) tbMeasures.getModel();

		for (String r : runs) {
			dtruns.add(r);
		}
		dtruns.fireTableDataChanged();

		for (String t : topics) {
			dttopics.add(t);
		}
		dttopics.fireTableDataChanged();
		
		for (String i : interations){
			dtinterations.add(i);
		}		
		dtinterations.fireTableDataChanged();

		for (String m : measures) {
			dtmeasures.add(m);
		}
		dtmeasures.fireTableDataChanged();
		
	}
	
	@SuppressWarnings("unchecked")
	private void loadDataInfo(){
	
		if(this.info.getDataInfoSize() != 0){
		
			List<String> measures = (List<String>) this.info.getData("measures");
			List<String> interations = (List<String>) this.info.getData("interations");
			List<String> topics = (List<String>) this.info.getData("topics");
			/*List<String> runs = (List<String>) this.info.getData("runs");*/
			List<String> runs = (List<String>) this.info.getData("runs2");

			DataTableModel selectedMeasures = (DataTableModel) tbSelectedMeasures.getModel();
			DataTableModel selectedInterations = (DataTableModel) tbSelectedInterations.getModel();
			DataTableModel selectedRuns = (DataTableModel) tbSelectedRuns.getModel();
			DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel(); 
			
			DataTableModel tbr = (DataTableModel) tbRuns.getModel();
			DataTableModel tbt = (DataTableModel) tbTopics.getModel();
			DataTableModel tbi = (DataTableModel) tbInterations.getModel();
			
			//initialize the selected runs table
			for(String run : runs){
				selectedRuns.add(run);
			}
			//
			for(String topic : topics){
				selectedTopics.add(topic);
			}	
			//initialize the selected measures table
			for(String measure : measures){
				selectedMeasures.add(measure);
			}
			
			//initialize the selected interations table
			for (String interation : interations){
				selectedInterations.add(interation);
			}
			
			//update measure table
			try {
				updateMeasures(measures);
			} catch (MeasuresGroupNotFoundException e) {
				
			}
			
			//update run table
			for(int i = (tbr.getRowCount() - 1); i >= 0; i--){
				String value = tbr.getValueAt(i, 1).toString();
				if(runs.contains(value)){
					tbr.remove(i, 1);
				}
			}
			
			for(int i = (tbt.getRowCount() - 1); i >= 0; i--){
				String value = tbt.getValueAt(i, 1).toString();
				if(runs.contains(value)){
					tbt.remove(i, 1);
				}
			}
			
			for (int i = (tbi.getRowCount() - 1); i >= 0; i--){
				String value = tbi.getValueAt(i, 1).toString();
				if(runs.contains(value)){
					tbi.remove(i, 1);
				}
			}
			
			tbr.fireTableDataChanged();
			tbt.fireTableDataChanged();
			tbi.fireTableDataChanged();


		}
	}

	private void initRunFunctions(){
		
		btnSingleR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
				int [] rows = tbRuns.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = runs.getValueAt(rows[i], 1).toString();
						selectedRuns.add(value);
						runs.remove(rows[i], 1);
					}
					runs.fireTableDataChanged();
					selectedRuns.fireTableDataChanged();
					Check_Rules(runs_Selected);
				}
			}
		});
		
		btnSingleBackR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
				int [] rows = tbSelectedRuns.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = selectedRuns.getValueAt(rows[i], 1).toString();
						runs.add(value);
						selectedRuns.remove(rows[i], 1);
					}
					runs.fireTableDataChanged();
					selectedRuns.fireTableDataChanged();
					Check_Rules(runs_Selected);
				}
			}
		});
		btnAllR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
			 
				for(int i = runs.getRowCount() - 1; i >= 0; i--){
					String value = runs.getValueAt(i, 1).toString();
					selectedRuns.add(value);
					runs.remove(i, 1);
				}
				runs.fireTableDataChanged();
				selectedRuns.fireTableDataChanged();
				Check_Rules(runs_Selected);

			}
		});

		btnAllBackR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataTableModel runs = (DataTableModel) tbRuns.getModel(); 
				DataTableModel selectedRuns =(DataTableModel) tbSelectedRuns.getModel(); 
			 
				for(int i = selectedRuns.getRowCount() - 1; i >= 0; i--){
					String value = selectedRuns.getValueAt(i, 1).toString();
					runs.add(value);
					selectedRuns.remove(i, 1);
				}
				runs.fireTableDataChanged();
				selectedRuns.fireTableDataChanged();
				Check_Rules(runs_Selected);
			}
		});
		
		
	}
	
	private void initTopicFunctions(){
		
		ActionListener pass = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel topics = (DataTableModel) tbTopics.getModel();				
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
				int [] rows = tbTopics.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = topics.getValueAt(rows[i], 1).toString();
						selectedTopics.add(value);
						topics.remove(rows[i], 1);
					}
					topics.fireTableDataChanged();
					selectedTopics.fireTableDataChanged();
					Check_Rules(runs_Selected);
				}
			}
		};
		
		ActionListener passAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
			 
				for(int i = topics.getRowCount() - 1; i >= 0; i--){
					String value = topics.getValueAt(i, 1).toString();
					selectedTopics.add(value);
					topics.remove(i, 1);
				}
				topics.fireTableDataChanged();
				selectedTopics.fireTableDataChanged();
				Check_Rules(runs_Selected);
			}
		};
		
		ActionListener back = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
				int [] rows = tbSelectedTopics.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = selectedTopics.getValueAt(rows[i], 1).toString();
						topics.add(value);
						selectedTopics.remove(rows[i], 1);
					}
					topics.fireTableDataChanged();
					selectedTopics.fireTableDataChanged();
					Check_Rules(runs_Selected);
				}
			}
		};
		
		ActionListener backAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
				DataTableModel selectedTopics =(DataTableModel) tbSelectedTopics.getModel(); 
			 
				for(int i = selectedTopics.getRowCount() - 1; i >= 0; i--){
					String value = selectedTopics.getValueAt(i, 1).toString();
					topics.add(value);
					selectedTopics.remove(i, 1);
				}
				topics.fireTableDataChanged();
				selectedTopics.fireTableDataChanged();
				Check_Rules(runs_Selected);
			}
		};
	
		btnSingleT.addActionListener(pass);
		btnSingleBackT.addActionListener(back);
		btnAllT.addActionListener(passAll);
		btnAllBackT.addActionListener(backAll);
	}
	
	private void initInterationFunctions(){
		
		ActionListener pass = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel interations = (DataTableModel) tbInterations.getModel(); 
				DataTableModel selectedInterations =(DataTableModel) tbSelectedInterations.getModel(); 
				int [] rows = tbInterations.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = interations.getValueAt(rows[i], 1).toString();
						selectedInterations.add(value);
						interations.remove(rows[i], 1);
					}
					interations.fireTableDataChanged();
					selectedInterations.fireTableDataChanged();
					Check_Rules(runs_Selected);
				}
			}
		};
		
		ActionListener passAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel interations = (DataTableModel) tbInterations.getModel(); 
				DataTableModel selectedInterations =(DataTableModel) tbSelectedInterations.getModel(); 
			 
				for(int i = interations.getRowCount() - 1; i >= 0; i--){
					String value = interations.getValueAt(i, 1).toString();
					selectedInterations.add(value);
					interations.remove(i, 1);
				}
				interations.fireTableDataChanged();
				selectedInterations.fireTableDataChanged();
				Check_Rules(runs_Selected);
			}
		};
		
		ActionListener back = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				DataTableModel interations = (DataTableModel) tbInterations.getModel(); 
				DataTableModel selectedInterations =(DataTableModel) tbSelectedInterations.getModel(); 
				int [] rows = tbSelectedInterations.getSelectedRows(); 
				
				if(rows.length > 0){
					int begin = (rows.length == 0 ? 0: rows.length -1); 
					for(int i = begin; i >= 0; i--){
						String value = selectedInterations.getValueAt(rows[i], 1).toString();
						interations.add(value);
						selectedInterations.remove(rows[i], 1);
					}
					interations.fireTableDataChanged();
					selectedInterations.fireTableDataChanged();
					Check_Rules(runs_Selected);
				}
			}
		};
		
		ActionListener backAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DataTableModel interations = (DataTableModel) tbInterations.getModel(); 
				DataTableModel selectedInterations =(DataTableModel) tbSelectedInterations.getModel(); 
			 
				for(int i = selectedInterations.getRowCount() - 1; i >= 0; i--){
					String value = selectedInterations.getValueAt(i, 1).toString();
					interations.add(value);
					selectedInterations.remove(i, 1);
				}
				interations.fireTableDataChanged();
				selectedInterations.fireTableDataChanged();
				Check_Rules(runs_Selected);
			}
		};
	
		btnSingleI.addActionListener(pass);
		btnSingleBackI.addActionListener(back);
		btnAllI.addActionListener(passAll);
		btnAllBackI.addActionListener(backAll);
	}
	
	
	
	
	private void initMeasureFunctions(){
		
		ActionListener pass = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				List<String> selected = new ArrayList<>();
				DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
				DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel(); 
				int [] rows = tbMeasures.getSelectedRows();
				
				for(int i = 0; i < rows.length; i++){
					String v = measures.getValueAt(rows[i], 1).toString();
					selected.add(v);
					measures.remove(i, 1);
					selectedMeasures.add(v);
				}
				measures.fireTableDataChanged();
				selectedMeasures.fireTableDataChanged();
				Check_Rules(measures_Selected);
				
			}
		};
		
		ActionListener passAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<String> selected = new ArrayList<>();
				DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
				DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel(); 
				 
				/*for(int i = measures.getRowCount() - 1; i >= 0; i--){
					String value = measures.getValueAt(i, 1).toString();
					selected.add(value);	
				}*/
				
				int type = facade.getMeasureType(selected);
				if(type != -1){
					for(int i = measures.getRowCount() - 1; i >= 0; i--){
						String value = measures.getValueAt(i, 1).toString();
						selectedMeasures.add(value);
						measures.remove(i, 1);
					}
				}
					
				measures.fireTableDataChanged();
				selectedMeasures.fireTableDataChanged();
				Check_Rules(measures_Selected);
			}
		};
		
		ActionListener back = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
				DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel(); 
				int [] rows = tbSelectedMeasures.getSelectedRows(); 
				
				if(rows.length > 0) {
					int begin = (rows.length == 0 ? 0 : rows.length - 1);
					for (int i = begin; i >= 0; i--) {
						String value = selectedMeasures.getValueAt(rows[i], 1).toString();
						measures.add(value);
						selectedMeasures.remove(rows[i], 1);
					}
					measures.fireTableDataChanged();
					selectedMeasures.fireTableDataChanged();

					if(selectedMeasures.getRowCount() == 0){
						updateMeasures();
					}
					Check_Rules(measures_Selected);
				}
			}
		};
		
		ActionListener backAll = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//updateMeasures();
				DataTableModel measures = (DataTableModel) tbMeasures.getModel();
				DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel();
				List<String> values = facade.listMeasures();

				for(int i = (selectedMeasures.getRowCount() - 1); i >= 0; i--){
					//measures.remove(i, 1);
					String value = selectedMeasures.getValueAt(i, 1).toString();
					measures.add(value);
					selectedMeasures.remove(i, 1);
				}

				measures.fireTableDataChanged();
				selectedMeasures.fireTableDataChanged();
				Check_Rules(measures_Selected);
			}
		};
		
		btnSingleM.addActionListener(pass);
		btnSingleBackM.addActionListener(back);
		btnAllM.addActionListener(passAll);
		btnAllBackM.addActionListener(backAll);
	}
	
	@SuppressWarnings("unused")
	private void removeExtraMeasures() {
		
		DataTableModel model = (DataTableModel) tbMeasures.getModel();
		
		for(int i = model.getRowCount() - 1; i >= 0; i--){
			String value = model.getValueAt(i, 1).toString();
			if(value.equalsIgnoreCase("gmap") || value.equalsIgnoreCase("GM_BPref")){
				model.remove(i, 1);
			}
		}
		
		model.fireTableDataChanged();
	}

	private void updateMeasures() {
		
		DataTableModel measures = (DataTableModel) tbMeasures.getModel();
		List<String> values = facade.listMeasures();

		for(int i = (measures.getRowCount() - 1); i >= 0; i--){
			measures.remove(i, 1);
		}
		for(String measure : values){
			measures.add(measure);
		}
		measures.fireTableDataChanged();
	}

	/*private void updateMeasures() {

		DataTableModel measures = (DataTableModel) tbMeasures.getModel();
		DataTableModel selectedMeasures =(DataTableModel) tbSelectedMeasures.getModel();
		List<String> values = facade.listMeasures();

		for(int i = (selectedMeasures.getRowCount() - 1); i >= 0; i--){
			//measures.remove(i, 1);
			String value = selectedMeasures.getValueAt(i, 1).toString();
			measures.add(value);
			selectedMeasures.remove(i, 1);
		}

		measures.fireTableDataChanged();
	}*/
	
	@SuppressWarnings("unused")
	private void updateTopics() {
		
		DataTableModel topics = (DataTableModel) tbTopics.getModel(); 
		DataTableModel selected = (DataTableModel) tbSelectedTopics.getModel();
		List<String> top = facade.listTopics();
		
		for(int i = (selected.getRowCount() - 1); i >= 0; i--){
			String value = selected.getValueAt(i, 1).toString();
			top.remove(value);
		}
		
		for(String v : top){
			topics.add(v);
		}
		
		topics.fireTableDataChanged();
	}

	private void updateMeasures(List<String> auxMeasures)throws MeasuresGroupNotFoundException {
	
		List<String> newMeasures = facade.getMeasureGroup(auxMeasures);
		DataTableModel measures = (DataTableModel) tbMeasures.getModel(); 
		
		for(int i = (measures.getRowCount() - 1); i >= 0; i--){
			String value = measures.getValueAt(i, 1).toString();
			if(!newMeasures.contains(value)){
				measures.remove(i, 1);
			}
		}
		measures.fireTableDataChanged();
	}

	private void createTable(List<String> items, JTable table,JScrollPane scroll, String name) {
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

	public boolean getCheckBox() {
		return this.cbStatistics.isSelected();
	}
	/*
	*	Esse método confere se as regras para fazer um teste estatisticos foram seguidas.
	 */

	private void Check_Rules(int type){

		boolean selected = cbStatistics.isSelected();
		int testRuns = ((DataTableModel) tbSelectedRuns.getModel()).getRowCount();
		int testMeasures = ((DataTableModel) tbSelectedMeasures.getModel()).getRowCount();
		boolean testSelected = false;
		cbStatistics.setEnabled(true);
		if(testRuns < 2 || testMeasures > 1 /*|| testTopics == 1*/){
			testSelected = true;
			cbStatistics.setEnabled(false);
		}

		if(selected){

			if(testSelected){
				cbStatistics.setSelected(false);
				JOptionPane.showMessageDialog(null, "Invalid configuration, so statistical tests have been disabled. This type of graph accepts only one measure.", "Error", JOptionPane.ERROR_MESSAGE);
			}

		}

	}

}