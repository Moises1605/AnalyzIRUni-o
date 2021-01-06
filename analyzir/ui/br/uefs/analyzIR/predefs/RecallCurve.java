package br.uefs.analyzIR.predefs;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.ui.structure.DataInfo;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wanderson on 25/04/17.
 */
public class RecallCurve implements Predef {

    private Facade facade;
    private List<String> runsList;
    private List<String> topicList;
    private List<String> measures;
    private String[] xValues;
    private DataInfo info;

    public RecallCurve(Facade facade, String[] xValues) {
        this.facade = facade;
        this.info = new DataInfo();
        this.init();
        this.xValues = xValues;
        runsList = facade.listRuns();
    }
    @Override
    public void init() {
        measures = new ArrayList<String>();
        topicList = Arrays.asList("AVG");
        measures.add("recall@N Curve");
        putDataInfo(info);
    }

    @Override
    public void putDataInfo(DataInfo dataInfo) {
        dataInfo.putData("measures", measures);
        dataInfo.putData("runs", runsList);
        dataInfo.putData("topics", topicList);
        dataInfo.putData("name", "Recall Curve");
    }

    @Override
    public JPanel make() {
        try {
        	int type = facade.getMeasureType(measures);
             JPanel plot = facade.createGraph(type, "Recall curve", measures , runsList, topicList, xValues, "Recall Curve", null, "Rank depth", "Recall", null, 2);
             return plot;
        } catch (InvalidGraphNameException e) {
            JOptionPane.showMessageDialog(null, "There is already a Recall Curve Predef created", "Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DataInfo getDataInfo() {
        return this.info;
    }

    @Override
    public void show() {

    }
}
