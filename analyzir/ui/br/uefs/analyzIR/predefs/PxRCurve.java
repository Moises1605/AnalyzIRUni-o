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
public class PxRCurve implements Predef {

    private Facade facade;
    private List<String> runsList;
    private List<String> topicList;
    private List<String> measures;
    private DataInfo info;

    public PxRCurve(Facade facade) {
        this.facade = facade;
        this.info = new DataInfo();
        this.init();
        runsList = facade.listRuns();
    }

    @Override
    public void init() {
        measures = new ArrayList<String>();
        topicList = Arrays.asList("AVG");
        measures.add("Precision x Recall Curve");
        putDataInfo(info);
    }

    @Override
    public void putDataInfo(DataInfo dataInfo) {
        dataInfo.putData("measures", measures);
        dataInfo.putData("runs", runsList);
        dataInfo.putData("topics", topicList);
        dataInfo.putData("name", "PxR Curve");
    }

    @Override
    public void show() {

    }

    @Override
    public JPanel make() {
        String[] decimalxValues = {".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9", "1"};

        try {
        	int type = facade.getMeasureType(measures);
            JPanel plot = facade.createGraph(type, "P x R curve", measures , runsList, topicList, decimalxValues, "PxR Curve", null, "Recall", "Precision", null, 2);
            return plot;
        } catch (InvalidGraphNameException e) {
            JOptionPane.showMessageDialog(null, "There is already a P x R Curve Predef created", "Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DataInfo getDataInfo() {
        return info;
    }
}
