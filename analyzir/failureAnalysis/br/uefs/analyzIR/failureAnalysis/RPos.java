package br.uefs.analyzIR.failureAnalysis;

import br.uefs.analyzIR.data.QrelItem;
import br.uefs.analyzIR.data.Qrels;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.RunItem;
import br.uefs.analyzIR.exception.TopicNotFoundException;

import java.util.ArrayList;
import java.util.List;


public class RPos {

    private int rpos;
    private int minIndex;
    private int maxIndex;
    private ArrayList<GTItem> GT;
    private ArrayList<VItem> V;
    private Run run;
    private Qrels qrels;


    public RPos(Run run, Qrels qrels, String topicNumber) throws TopicNotFoundException {

        this.GT = new ArrayList<GTItem>();
        this.V = new ArrayList<VItem>();
        this.run = run;
        this.qrels = qrels;
        createGT(topicNumber);
        createV(topicNumber);

        //createGT2();
        //createV2();
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



    public void calculateMinMaxIndex(int posV) {

        //Calculate Min_Index
        int relevance = 0;

        for (int x = 0; x < V.size(); x++) {

            if (this.V.get(posV).getName().equals(this.GT.get(x).getName())) {
                relevance = GT.get(x).getRelevance();
            }
        }
        for (int i = 0; i < V.size(); i++) {
            if (GT.get(i).getRelevance() == relevance) {
                this.minIndex = i;
                break;
            }
        }

        //Calculate MaxI_ndex
        relevance = 0;

        for (int x = V.size() - 1; x >= 0; x--) {

            if (this.V.get(posV).getName().equals(this.GT.get(x).getName())) {
                relevance = GT.get(x).getRelevance();

            }
        }
        for (int i = V.size() - 1; i >= 0; i--) {
            if (GT.get(i).getRelevance() == relevance) {
                this.maxIndex = i;
                break;
            }
        }
    }

    public void calculateRPos(int posV) {


        if (this.minIndex <= posV && posV <= this.maxIndex) {
            this.rpos = 0;
        } else if (posV < this.minIndex) {
            this.rpos = this.minIndex - posV;
        } else if (posV > this.maxIndex) {
            this.rpos = this.maxIndex - posV;
        }
    }



    public int getRPos() {
        return this.rpos;
    }

    public void createGT2() {

        GT.add(0, new GTItem("A", 10));
        GT.add(1, new GTItem("B", 10));
        GT.add(2, new GTItem("C", 10));
        GT.add(3, new GTItem("D", 10));
        GT.add(4, new GTItem("E", 5));
        GT.add(5, new GTItem("F", 5));
        GT.add(6, new GTItem("G", 5));
        GT.add(7, new GTItem("H", 1));
        GT.add(8, new GTItem("I", 1));
        GT.add(9, new GTItem("J", 0));

    }


    public void createV2() {

        V.add(0, new VItem("A"));
        V.add(1, new VItem("C"));
        V.add(2, new VItem("E"));
        V.add(3, new VItem("J"));
        V.add(4, new VItem("B"));
        V.add(5, new VItem("D"));
        V.add(6, new VItem("F"));
        V.add(7, new VItem("H"));
        V.add(8, new VItem("I"));
        V.add(9, new VItem("G"));

    }

    public ArrayList<VItem> getV() {
        return V;
    }

    public ArrayList<GTItem> getGT() {
        return GT;
    }
}

	
