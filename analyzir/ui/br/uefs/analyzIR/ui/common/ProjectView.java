package br.uefs.analyzIR.ui.common;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.failureAnalysis.graph.ConfigPlotFRel;
import br.uefs.analyzIR.failureAnalysis.graph.ConfigPlotRPos;
import br.uefs.analyzIR.predefs.Predef;
import br.uefs.analyzIR.predefs.PredefFactory;
import br.uefs.analyzIR.ui.common.statistics.*;
import br.uefs.analyzIR.ui.diversify.LoadClusterFiles;
import br.uefs.analyzIR.ui.graph.wizard.CreateGraphProcess;
import br.uefs.analyzIR.ui.graph.wizard.CreateInteractiveGraphProcess;
import br.uefs.analyzIR.ui.graph.wizardStep.*;
import br.uefs.analyzIR.ui.interactiveProject.InteractiveDataGraphSetting;
import br.uefs.analyzIR.ui.interactiveProject.LoadInteractiveFiles;
import br.uefs.analyzIR.ui.measure.MeasureManagerUI;
import br.uefs.analyzIR.ui.screenView.GraphScreenView;
import br.uefs.analyzIR.ui.screenView.ScreenViewManager;
import br.uefs.analyzIR.ui.simpleProject.GraphSettingSimpleProject;
import br.uefs.analyzIR.ui.standardProject.DataGraphSettingWizardStep;
import br.uefs.analyzIR.ui.standardProject.LoadFiles;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.ScreenView;
import br.uefs.analyzIR.ui.structure.WizardListener;
import br.uefs.analyzIR.ui.structure.WizardProcess;
import br.uefs.analyzIR.ui.structure.WizardStep;

public class ProjectView implements WizardListener{

	private JFrame frame;
	private JPanel panel;
	private JPanel sideBar;
	private JTabbedPane tbpPlots;
	private JMenu file;
	private JMenu newProject;
	private JMenuBar jMenu;
	private JMenu statistics;
	private JMenu measures;
	private JMenu measure;
	private JMenu graphMenu;
	
	private JMenuItem exit;
	private JMenu open;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem closeProject;
	private JMenuItem single;
	private JMenuItem standard;
	private JMenuItem diversity;
	private JMenuItem interactive;
	private JMenu about;
	private JMenu help;
	private JMenuItem measureDescription;
	
	private static ProjectView project;
	
	private ScreenViewManager screenViewManager;
	private Facade facade;
	

	private ProjectView(Facade facade) {
		this.facade = facade;
		this.screenViewManager = new ScreenViewManager(facade);
	}
	
	public static ProjectView getInstance(Facade facade){
		if(project == null)
			project = new ProjectView(facade);
		return project;
	}
	
	public void show() {
		
		defineStyle();
		initComponents();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
	private void defineStyle(){
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
	}

	private void initComponents(){
		
		frame = new JFrame("AnalyzIR: Project View");
		panel = new JPanel(); 
				
		BorderLayout border = new BorderLayout();
		panel.setLayout(border);
		frame.setContentPane(panel);
		
		initMenu();
		initCenter();
		
		if(facade.getType() != -1)
			initSideBar();
		
		panel.add(new JPanel(), BorderLayout.SOUTH);
		panel.add(new JPanel(), BorderLayout.EAST);
	}
	
	private void initSideBar() {
		
		this.sideBar = this.screenViewManager.makeSideBar();
		this.frame.add(this.sideBar, BorderLayout.WEST);
	}

	private void initMenu() {

		initMenuBar();
		initFileMenu();
		initFileMenuFunction();
		initNewProject();
		initOpenProject();
		initStatisticsMenu();
		initPlotMeasure();
		initMeasureMenu();
		initPredef();
        initFailureAnalysis();
		initMeasureDescription();
		initAboutMenu();

		if(facade.getType() == -1){
			measures.setEnabled(false);
			graphMenu.setEnabled(false);
			statistics.setEnabled(false);
			measureDescription.setEnabled(false);
		}
	}

	private void initMeasureMenu() {
	
		JMenuItem addM = new JMenuItem("Manager Measures");
		
		addM.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new MeasureManagerUI(facade).show();
			}
		});
		
		measures.add(addM);
		
	}

	private void initAboutMenu() {

		JMenuItem aboutUs = new JMenuItem("About us");
		
		aboutUs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new About().show();
			}
		});
		
		about.add(aboutUs);
	}

	private void initCenter() {
		
		tbpPlots = screenViewManager.getTabbedPane();
		panel.add(tbpPlots, BorderLayout.CENTER);
	}

	private void initStatisticsMenu(){
		
		JMenuItem significanceMenu = new JMenuItem("Significance Test");
		JMenuItem tStudentMenu = new JMenuItem("Student's t-Test");
		JMenuItem correlationMenu = new JMenuItem("Jaccard/Spearman Test");
		JMenuItem correlation2Menu = new JMenuItem("Pearson Test");

		significanceMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SignificanceTestView test = new SignificanceTestView(facade);
				test.show();
			}
		});


		tStudentMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TStudentView test = new TStudentView(facade);
				test.show();
			}
		});


		correlationMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SpearmanAndJaccardTestView test = new SpearmanAndJaccardTestView(facade);
				test.show();
			}
		});

		correlation2Menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PearsonTestView test = new PearsonTestView(facade);
				test.show();
			}
		});

		statistics.add(significanceMenu);
		statistics.add(tStudentMenu);
		statistics.add(correlationMenu);
		statistics.add(correlation2Menu);
	
	}
	private void initMenuBar() {

		jMenu = new JMenuBar();
		file = new JMenu("File");
		//measure = new JMenu("Measure");
		graphMenu = new JMenu("Graphs");
		about = new JMenu("About");
		statistics = new JMenu("Statistics");
		measures = new JMenu("Measures");
		help = new JMenu("Help");

		jMenu.add(file);
		jMenu.add(graphMenu);
		//jMenu.add(measures);
		jMenu.add(statistics);
		jMenu.add(help);
		jMenu.add(about);

		this.frame.setJMenuBar(jMenu);
	}

	// Menu operations
	private void initFileMenu() {

		this.newProject = new JMenu("New Project");
		this.open = new JMenu("Open Project");
		this.save = new JMenuItem("Save");
		saveAs = new JMenuItem("Save As");
		closeProject = new JMenuItem("Close Project");
		this.exit = new JMenuItem("Exit");

		file.add(newProject);
		file.add(open);
		file.add(closeProject);
		file.add(save);
		file.add(saveAs);
		file.add(exit);
		
		if(facade.getType() == -1){
			save.setEnabled(false);
			saveAs.setEnabled(false);
			closeProject.setEnabled(false);
		}
	}
	
	private void initFileMenuFunction(){
		
		this.exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		this.save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					facade.save();
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (NoGraphItemException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		this.closeProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

					try {
						
						int value = JOptionPane.showConfirmDialog(null,"Would you like save changes?", "Close Project", JOptionPane.YES_NO_CANCEL_OPTION);
						if (value == JOptionPane.YES_OPTION) {
							facade.closeProject(true);
							disableFunctions();
						}else if(value != JOptionPane.CANCEL_OPTION){
							facade.closeProject(false);
							disableFunctions();
						}
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (NoGraphItemException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
			}
		});
	}
	
	private void initOpenProject(){
		
		standard = new JMenuItem("Standard Project");
		diversity = new JMenuItem("Diversity Project");
		interactive = new JMenuItem("Interactive Project");
		
		standard.addActionListener(new ActionListener() {
			
			private String lastDirecotry;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				
				if(lastDirecotry != null){
					chooser.setCurrentDirectory(new File(lastDirecotry));
				}
				
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retorno = chooser.showOpenDialog(null);

				if (retorno == JFileChooser.APPROVE_OPTION) {
				
					String  path = chooser.getSelectedFile().getAbsolutePath();
					this.lastDirecotry = chooser.getSelectedFile().getParent();
				
					try {
						
						facade.openProject(path, 1);
						enableFunctions();
					
					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (QrelItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InvalidQrelFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (RunItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (NoGraphItemException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InstantiationException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IllegalAccessException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}catch(java.lang.ClassCastException e1){
						JOptionPane.showMessageDialog(null, "The project is not the correct type.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		
		diversity.addActionListener(new ActionListener() {
			
			private String lastDirecotry;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				
				if(lastDirecotry != null){
					chooser.setCurrentDirectory(new File(lastDirecotry));
				}
				
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retorno = chooser.showOpenDialog(null);

				if (retorno == JFileChooser.APPROVE_OPTION) {
				
					String  path = chooser.getSelectedFile().getAbsolutePath();
					this.lastDirecotry = chooser.getSelectedFile().getParent();
				
					try {
						
						facade.openProject(path, 2);
						enableFunctions();
					
					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (QrelItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InvalidQrelFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (RunItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (NoGraphItemException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InstantiationException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IllegalAccessException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}catch(java.lang.ClassCastException e1){
						JOptionPane.showMessageDialog(null, "The project is not the correct type.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		interactive.addActionListener(new ActionListener() {
			
			private String lastDirecotry;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				
				if(lastDirecotry != null){
					chooser.setCurrentDirectory(new File(lastDirecotry));
				}
				
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retorno = chooser.showOpenDialog(null);

				if (retorno == JFileChooser.APPROVE_OPTION) {
				
					String  path = chooser.getSelectedFile().getAbsolutePath();
					this.lastDirecotry = chooser.getSelectedFile().getParent();
				
					try {
						
						facade.openProject(path, 3);
						enableFunctions();
					
					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (QrelItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InvalidQrelFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (RunItemNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (NoGraphItemException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (InstantiationException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IllegalAccessException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}catch(java.lang.ClassCastException e1){
						JOptionPane.showMessageDialog(null, "The project is not the correct type.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});


		open.add(standard);
		open.add(diversity);
		open.add(interactive);
	}
	
	private void initNewProject(){
		

		JMenuItem standard = new JMenuItem("Standard Project");
		JMenuItem diversity = new JMenuItem("Diversity Project");
		JMenuItem interactive = new JMenuItem("Interactive Project");

		this.newProject.add(standard);
		this.newProject.add(diversity);
		this.newProject.add(interactive);
		

		standard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoadFiles load = new LoadFiles(facade, 1); 
				load.show();
			}
		});

		diversity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoadClusterFiles load = new LoadClusterFiles(facade, 2); 
				load.show();
			}
		});
		
		interactive.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LoadInteractiveFiles load = new LoadInteractiveFiles(facade, 3); 
				load.show();				
			}
		});
		
	}


	private void initPlotMeasure() {

		JMenuItem newGraphMenuItem = new JMenuItem("New Graph");
		graphMenu.add(newGraphMenuItem);
		
		newGraphMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int type = facade.getType();
				
				if(type == 0){
					new GraphSettingSimpleProject(facade, null).show();
				}else if (type < 3){
					//new DataGraphSetting().show();
					WizardProcess createGraph = new CreateGraphProcess(facade);
					
					WizardStep sp1 = new DataGraphSettingWizardStep();
					WizardStep sp2 = new DataGraphLabelsSettingWizardStep();
					WizardStep sp3 = new BarGraphFinalizeSetting();
					WizardStep sp4 = new CurveGraphFinalizeSetting();
					
					sp1.addWizardProcessListener(createGraph);
					sp2.addWizardProcessListener(createGraph);
					sp3.addWizardProcessListener(createGraph);
					sp4.addWizardProcessListener(createGraph);
					
					createGraph.addWizardStep(sp1);
					createGraph.addWizardStep(sp2);
					createGraph.addWizardStep(sp3);
					createGraph.addWizardStep(sp4);
					
					createGraph.addWizardListener(screenViewManager);
					createGraph.start();
					
				}else if (type == 3){
					WizardProcess createGraph = new CreateInteractiveGraphProcess(facade);
					
					/*WizardStep sp1 = new InteractiveDataGraphSetting();
					WizardStep sp2 = new InteractiveDataGraphLabelsSetting();
					WizardStep sp3 = new InteractiveGraphFinalizeSetting();
					WizardStep sp4 = new InteractiveGraphFinalizeSettingSingle();
					WizardStep sp5 = new InteractiveStatisticsTestStep();*/

					WizardStep sp1 = new InteractiveDataGraphSetting();
					WizardStep sp2 = new InteractiveStatisticsTestStep();
					WizardStep sp3 = new InteractiveDataGraphLabelsSetting();
					WizardStep sp4 = new InteractiveGraphFinalizeSetting();
					WizardStep sp5 = new InteractiveGraphFinalizeSettingSingle();


					sp1.addWizardProcessListener(createGraph);
					sp2.addWizardProcessListener(createGraph);
					sp3.addWizardProcessListener(createGraph);
					sp4.addWizardProcessListener(createGraph);
					sp5.addWizardProcessListener(createGraph);

					createGraph.addWizardStep(sp1);
					createGraph.addWizardStep(sp2);
					createGraph.addWizardStep(sp3);
					createGraph.addWizardStep(sp4);
					createGraph.addWizardStep(sp5);

					createGraph.addWizardListener(screenViewManager);
					createGraph.start();
				}
			}
		});
	}
	

	/**
	 * This method creates the options menu to select the predef
	 * and plots a graph according to the selected PreDef
	 */
	private void initPredef() {


		JMenu addPredefs = new JMenu("New Predef");
		graphMenu.add(addPredefs);

		JMenuItem precision = new JMenuItem("Basic Precision");
		JMenuItem basicCurve = new JMenuItem("Basic Curve");
		JMenuItem gain = new JMenuItem("Gain");
		JMenuItem topRank = new JMenuItem("Rank Top");

		addPredefs.add(precision);
		addPredefs.add(basicCurve);
		addPredefs.add(gain);
		addPredefs.add(topRank);


		precision.addActionListener(new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent e) {

				PredefFactory pf = new PredefFactory(facade);
				Predef predef = pf.getSingleGraphPredef(1, null);
				if(predef!=null) {
					DataInfo info = predef.getDataInfo();
					info.putData("listener", screenViewManager);
					GraphScreenView graphScreenView = new GraphScreenView(predef.make(), facade);
					graphScreenView.putDataInfo(info);
					addGraph(graphScreenView);
				}
			}
		});
		basicCurve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				FinalizePredefCurve finalizePredefCurve = new FinalizePredefCurve();
				String[] xValues = finalizePredefCurve.calculatexValues();

				PredefFactory pf = new PredefFactory(facade);
				Predef predef[] = pf.getmultGraphPredef(2, xValues);
				DataInfo info[] = new DataInfo[3];
				GraphScreenView plotView[] = new GraphScreenView[3];

				for (int i = 0; i < predef.length; i++) {
					if (predef[i] != null) {
						info[i] = predef[i].getDataInfo();
						info[i].putData("listener", screenViewManager);
						plotView[i] = new GraphScreenView(predef[i].make(), facade);
						plotView[i].putDataInfo(info[i]);
						addGraph(plotView[i]);
					}

				}
			}
		});

		gain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PredefFactory pf = new PredefFactory(facade);
				Predef predef = pf.getSingleGraphPredef(3, null);
				if(predef!=null) {
					DataInfo info = predef.getDataInfo();
					info.putData("listener", screenViewManager);
					GraphScreenView plotview = new GraphScreenView(predef.make(), facade);
					plotview.putDataInfo(info);
					addGraph(plotview);
				}

			}
		});

		topRank.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String atValue = JOptionPane.showInputDialog("Rank Depth");
				PredefFactory pf = new PredefFactory(facade);
				Predef predef = pf.getSingleGraphPredef(4, atValue);
				if(predef!=null) {
					DataInfo info = predef.getDataInfo();
					info.putData("listener", screenViewManager);
					GraphScreenView plotview = new GraphScreenView(predef.make(), facade);
					plotview.putDataInfo(info);
					addGraph(plotview);
				}

			}
		});


	}

    public void initFailureAnalysis(){

        JMenu failureMenu = new JMenu("Failure Analysis");
        graphMenu.add(failureMenu);

        JMenuItem rposMenu = new JMenuItem("R_Pos");
        JMenuItem frelMenu = new JMenuItem("F_Rel");

        failureMenu.add(rposMenu);
        failureMenu.add(frelMenu);

        rposMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    new ConfigPlotRPos(facade).show();

                } catch (MeasuresGroupNotFoundException e1) {
                    e1.printStackTrace();
                }

            }
        });
        frelMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    new ConfigPlotFRel(facade).show();

                } catch (MeasuresGroupNotFoundException e1) {
                    e1.printStackTrace();
                }

            }
        });
    }

    public void initMeasureDescription() {
        measureDescription = new JMenuItem("Measure Description");
        help.add(measureDescription);

        measureDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MeasureDescriptionView mdv = new MeasureDescriptionView(facade);
                mdv.show();

            }
        });

    }

    public void enableFunctions() {

        graphMenu.setEnabled(true);
        open.setEnabled(true);
        measures.setEnabled(true);
        measureDescription.setEnabled(true);
        closeProject.setEnabled(true);
        if (facade.getType() != 0)
            statistics.setEnabled(true);
        saveAs.setEnabled(true);
        save.setEnabled(true);
        initSideBar();

    }

    public void disableFunctions() {
        graphMenu.setEnabled(false);
        closeProject.setEnabled(false);
        saveAs.setEnabled(false);
        save.setEnabled(false);
        statistics.setEnabled(false);
        measures.setEnabled(false);
        removeSideBar();
        closeAllPlots();
    }

    private void closeAllPlots() {
        this.screenViewManager.removeAllTabs();
    }

    private void removeSideBar() {
        this.sideBar.setVisible(false);
        this.screenViewManager.removeAllComboInfo();
    }

    public void addGraph(ScreenView graph) {
        screenViewManager.addScreenView(graph.getDataInfo().getData("name").toString(), graph);
    }

    public void setController(Facade facade) {
        this.facade = facade;
    }

    public void replaceGraph(ScreenView view, String oldName) {
        screenViewManager.replaceScreenView(oldName, view);
    }

    public void removeGraph(String name) {
        facade.removeGraph(name);
        screenViewManager.removeScreenView(name);
    }

    @Override
    public void update() {

    }

    @Override
    public void update(DataInfo info) {
        JPanel graph = (JPanel) info.getData("view");
        GraphScreenView view = new GraphScreenView(graph, facade);
        view.putDataInfo(info);
        addGraph(view);
    }

    @Override
    public void update(DataInfo info, ScreenView view) {


    }

}
