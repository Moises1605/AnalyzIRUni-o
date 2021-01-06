package br.uefs.analyzIR.failureAnalysis.graph;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.MeasureNotFoundException;
import br.uefs.analyzIR.exception.MeasuresGroupNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.failureAnalysis.FailureAnalysis;
import br.uefs.analyzIR.ui.util.DataTableModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wanderson on 02/06/16.
 */
public class ConfigPlotRPos {

    private Font font;
    private JFrame frame;

    private Facade facade;

    private JTable tbRuns;
    private JTable tbSelectedRuns;
    private JTable tbTopics;
    private JTable tbSelectedTopics;

    private JScrollPane jspRuns;
    private JScrollPane jspSelectedRuns;
    private JScrollPane jspTopics;
    private JScrollPane jspSelectedTopics;

    private JButton btnSingleR;
    private JButton btnAllR;
    private JButton btnSingleBackR;
    private JButton btnAllBackR;

    private JButton btnSingleT;
    private JButton btnAllT;
    private JButton btnSingleBackT;
    private JButton btnAllBackT;

    private JButton btnGenerate;
    private JButton btnClose;

    private JTextField txtAtVAlue;
    private JLabel lblAtVAlue;

    private ArrayList<String> runsName;
    private ArrayList<String> topicsNumber;


    public ConfigPlotRPos(Facade facade) {
        runsName = new ArrayList<String>();
        topicsNumber = new ArrayList<String>();
        this.facade = facade;
    }

    public void show() throws MeasuresGroupNotFoundException {

        initComponents();
        initLayout();
        loadInfo();
        initFunctions();
        closeWindow();
        showRPOS();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initComponents() {

        font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

        frame = new JFrame("New Graph - Graph Settings");

        jspRuns = new JScrollPane();
        jspSelectedRuns = new JScrollPane();
        jspTopics = new JScrollPane();
        jspSelectedTopics = new JScrollPane();


        tbRuns = new JTable();
        tbSelectedRuns = new JTable();
        tbTopics = new JTable();
        tbSelectedTopics = new JTable();

        createScroll(jspRuns);
        createScroll(jspSelectedRuns);
        createScroll(jspTopics);
        createScroll(jspSelectedTopics);

        createTable(new ArrayList<String>(), tbRuns, jspRuns, "Runs");
        createTable(new ArrayList<String>(), tbSelectedRuns, jspSelectedRuns, "Selected Runs");
        createTable(new ArrayList<String>(), tbTopics, jspTopics, "Topics");
        createTable(new ArrayList<String>(), tbSelectedTopics,
                jspSelectedTopics, "Compute Average For");


        btnSingleR = new JButton(">");
        btnAllR = new JButton(">>");
        btnSingleBackR = new JButton("<");
        btnAllBackR = new JButton("<<");

        btnSingleT = new JButton(">");
        btnAllT = new JButton(">>");
        btnSingleBackT = new JButton("<");
        btnAllBackT = new JButton("<<");

        btnGenerate = new JButton("Generate");
        btnClose = new JButton("Close");

        font = new Font(Font.SANS_SERIF, Font.BOLD, 13);

        btnGenerate.setFont(font);
        btnClose.setFont(font);

        lblAtVAlue = new JLabel("Rank Depth:");
        txtAtVAlue = new JTextField(20);
    }


    private void createTable(java.util.List<String> items, JTable table, JScrollPane scroll, String name) {
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

    private void loadInfo() {

        List<String> runs = facade.listRuns();
        List<String> topics = facade.listTopics();


        DataTableModel dtruns = (DataTableModel) tbRuns.getModel();
        DataTableModel dttopics = (DataTableModel) tbTopics.getModel();

        for (String r : runs) {
            dtruns.add(r);
        }
        dtruns.fireTableDataChanged();

        for (String t : topics) {
            if ( t != "AVG") {
                dttopics.add(t);
            }
        }
        dttopics.fireTableDataChanged();
    }

    private void initLayout() {
        String columns = "20dlu, 100dlu, 20dlu, 30dlu, 20dlu, 80dlu, 80dlu, 20dlu";
        String rows = "20dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, "
                    + "5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 20dlu, 5dlu, ";

        FormLayout layout = new FormLayout(columns, rows);
        PanelBuilder builder = new PanelBuilder(layout);

        builder.add(jspRuns, CC.xywh(2, 2, 1, 7));
        builder.add(jspSelectedRuns, CC.xywh(6, 2, 2, 7));
        builder.add(jspTopics, CC.xywh(2, 13, 1, 7));
        builder.add(jspSelectedTopics, CC.xywh(6, 13, 2, 7));

        builder.add(btnSingleR, CC.xy(4, 2));
        builder.add(btnAllR, CC.xy(4, 4));
        builder.add(btnAllBackR, CC.xy(4, 6));
        builder.add(btnSingleBackR, CC.xy(4, 8));

        builder.add(btnSingleT, CC.xy(4, 13));
        builder.add(btnAllT, CC.xy(4, 15));
        builder.add(btnAllBackT, CC.xy(4, 17));
        builder.add(btnSingleBackT, CC.xy(4, 19));

        builder.add(txtAtVAlue, CC.xy(2, 22));
        builder.add(lblAtVAlue, CC.xy(2, 21));


        builder.add(btnGenerate, CC.xy(7, 22));
        builder.add(btnClose, CC.xy(6, 22));

        frame.setContentPane(builder.getPanel());
    }

    private void initFunctions() {
        initRunFunctions();
        initTopicFunctions();
    }


    private void initRunFunctions() {

        btnSingleR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DataTableModel runs = (DataTableModel) tbRuns.getModel();
                DataTableModel selectedRuns = (DataTableModel) tbSelectedRuns.getModel();
                int[] rows = tbRuns.getSelectedRows();

                if (rows.length > 0) {
                    int begin = (rows.length == 0 ? 0 : rows.length - 1);
                    for (int i = begin; i >= 0; i--) {
                        String value = runs.getValueAt(rows[i], 1).toString();
                        selectedRuns.add(value);
                        runs.remove(rows[i], 1);
                    }
                    runs.fireTableDataChanged();
                    selectedRuns.fireTableDataChanged();
                }
            }
        });

        btnSingleBackR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataTableModel runs = (DataTableModel) tbRuns.getModel();
                DataTableModel selectedRuns = (DataTableModel) tbSelectedRuns.getModel();
                int[] rows = tbSelectedRuns.getSelectedRows();

                if (rows.length > 0) {
                    int begin = (rows.length == 0 ? 0 : rows.length - 1);
                    for (int i = begin; i >= 0; i--) {
                        String value = selectedRuns.getValueAt(rows[i], 1).toString();
                        runs.add(value);
                        selectedRuns.remove(rows[i], 1);
                    }
                    runs.fireTableDataChanged();
                    selectedRuns.fireTableDataChanged();
                }
            }
        });
        btnAllR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataTableModel runs = (DataTableModel) tbRuns.getModel();
                DataTableModel selectedRuns = (DataTableModel) tbSelectedRuns.getModel();

                for (int i = runs.getRowCount() - 1; i >= 0; i--) {
                    String value = runs.getValueAt(i, 1).toString();
                    selectedRuns.add(value);
                    runs.remove(i, 1);
                }
                runs.fireTableDataChanged();
                selectedRuns.fireTableDataChanged();
            }
        });

        btnAllBackR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataTableModel runs = (DataTableModel) tbRuns.getModel();
                DataTableModel selectedRuns = (DataTableModel) tbSelectedRuns.getModel();

                for (int i = selectedRuns.getRowCount() - 1; i >= 0; i--) {
                    String value = selectedRuns.getValueAt(i, 1).toString();
                    runs.add(value);
                    selectedRuns.remove(i, 1);
                }
                runs.fireTableDataChanged();
                selectedRuns.fireTableDataChanged();
            }
        });


    }

    private void initTopicFunctions() {
        btnSingleT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DataTableModel topics = (DataTableModel) tbTopics.getModel();
                DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();
                int[] rows = tbTopics.getSelectedRows();

                if (rows.length > 0) {
                    int begin = (rows.length == 0 ? 0 : rows.length - 1);
                    for (int i = begin; i >= 0; i--) {
                        String value = topics.getValueAt(rows[i], 1).toString();
                        selectedTopics.add(value);
                        topics.remove(rows[i], 1);
                    }
                    topics.fireTableDataChanged();
                    selectedTopics.fireTableDataChanged();
                }
            }
        });

        btnAllT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataTableModel topics = (DataTableModel) tbTopics.getModel();
                DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();

                for (int i = topics.getRowCount() - 1; i >= 0; i--) {
                    String value = topics.getValueAt(i, 1).toString();
                    selectedTopics.add(value);
                    topics.remove(i, 1);
                }
                topics.fireTableDataChanged();
                selectedTopics.fireTableDataChanged();
            }
        });

        btnSingleBackT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataTableModel topics = (DataTableModel) tbTopics.getModel();
                DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();
                int[] rows = tbSelectedTopics.getSelectedRows();

                if (rows.length > 0) {
                    int begin = (rows.length == 0 ? 0 : rows.length - 1);
                    for (int i = begin; i >= 0; i--) {
                        String value = selectedTopics.getValueAt(rows[i], 1).toString();
                        topics.add(value);
                        selectedTopics.remove(rows[i], 1);
                    }
                    topics.fireTableDataChanged();
                    selectedTopics.fireTableDataChanged();
                }
            }
        });

        btnAllBackT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataTableModel topics = (DataTableModel) tbTopics.getModel();
                DataTableModel selectedTopics = (DataTableModel) tbSelectedTopics.getModel();

                for (int i = selectedTopics.getRowCount() - 1; i >= 0; i--) {
                    String value = selectedTopics.getValueAt(i, 1).toString();
                    topics.add(value);
                    selectedTopics.remove(i, 1);
                }
                topics.fireTableDataChanged();
                selectedTopics.fireTableDataChanged();
            }
        });


    }

    public void showRPOS() {


        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                List<String> runs = new ArrayList<String>();
                List<String> topics = new ArrayList<String>();

                DataTableModel mRuns = (DataTableModel) tbSelectedRuns.getModel();
                DataTableModel mTopics = (DataTableModel) tbSelectedTopics.getModel();


                for (int i = 0; i < mRuns.getRowCount(); i++) {
                    String r = mRuns.getValueAt(i, 1).toString();
                    runs.add(r);
                }

                for (int j = 0; j < mTopics.getRowCount(); j++) {
                    String topic = mTopics.getValueAt(j, 1).toString();
                    topics.add(topic);
                }

                FailureAnalysis failure = facade.getFailureAnalysis();
                ArrayList rposList = new ArrayList();
                Integer atValue = Integer.parseInt(txtAtVAlue.getText());
                try {
                    for (int i = 0; i < runs.size(); i++) {
                        rposList.add((failure.calculateAverageRpos(runs.get(i), topics ,atValue)));
                    }
                } catch (TopicNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (MeasureNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                RPosStackedBarChart staked = new RPosStackedBarChart("R_POS GRAPH", rposList, runs);

                staked.show(rposList, runs);
            }
        });
    }

    public void closeWindow(){
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }


}