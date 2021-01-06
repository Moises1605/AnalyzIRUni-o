package br.uefs.analyzIR.failureAnalysis;

import br.uefs.analyzIR.data.QrelItem;
import br.uefs.analyzIR.data.Qrels;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.RunItem;
import br.uefs.analyzIR.exception.TopicNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wanderson on 27/09/16.
 * F_Rel - Find relevants
 */
public class FRel {

    private ArrayList<GTItem> GT;
    private ArrayList<VItem> V;
    private Run run;
    private Qrels qrels;
    private int frel;

    public FRel(Run run, Qrels qrels, String topicNumber) throws TopicNotFoundException {
        this.GT = new ArrayList<GTItem>();
        this.V = new ArrayList<VItem>();
        this.run = run;
        this.qrels = qrels;
        createGT(topicNumber);
        createV(topicNumber);
    }

    public void createV(String topicNumber) throws TopicNotFoundException {
        List<RunItem> runItens = this.run.getRunItems(topicNumber);
        for (int i = 0; i < runItens.size(); i++) {
            this.V.add(i, new VItem(runItens.get(i).getDocno()));
        }
    }

    public void createGT(String topicNumber) throws TopicNotFoundException {
        List<QrelItem> qrelItem = qrels.getQrelsItems(topicNumber);
        for (int i = 0; i < qrelItem.size(); i++) {
            this.GT.add(i, new GTItem(qrelItem.get(i).getDocno(), qrelItem.get(i).getRelevance()));
        }
    }

    public void calculeteFRel(int posV) {
        for (int i = 0; i < V.size(); i++) {
            if (V.get(posV).getName().equals(GT.get(i).getName()))
                this.frel = GT.get(i).getRelevance();
        }
    }

    public int getFrel() {
        return frel;
    }

    public ArrayList<VItem> getV() {
        return V;
    }

    public ArrayList<GTItem> getGT() {
        return GT;
    }
}