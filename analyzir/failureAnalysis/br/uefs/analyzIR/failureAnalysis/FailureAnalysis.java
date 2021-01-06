package br.uefs.analyzIR.failureAnalysis;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.exception.MeasureNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.measure.singleValueMeasure.G;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;
import com.sun.org.apache.xpath.internal.operations.Gt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class FailureAnalysis {

    private Context context;

    public FailureAnalysis() {
    }

    public ArrayList<Integer> calculateRpos(String runName, String topicNumber, int atValue) throws TopicNotFoundException, MeasureNotFoundException {

        RPos rpos = new RPos(context.getRunByName(runName), context.getQrels(), topicNumber);

        ArrayList<Integer> rposVector = new ArrayList();

        if (atValue > rpos.getV().size()) {

            atValue = rpos.getV().size();
        }

        for (int i = 0; i < atValue; i++) {
            rpos.calculateMinMaxIndex(i);
            rpos.calculateRPos(i);
            rposVector.add(i, rpos.getRPos());
        }

        return rposVector;
    }

    public ArrayList<Double> calculateAverageRpos(String runName, List<String> topics, int atValue) throws TopicNotFoundException, MeasureNotFoundException {

        ArrayList<Double> averageRPos = new ArrayList<>();
        ArrayList<ArrayList> allRPos = new ArrayList();
        for (int i = 0; i < topics.size(); i++) {
            allRPos.add(i, calculateRpos(runName, topics.get(i), atValue));
        }


        //iniciando todas as posições com 0
        for (int i = 0; i < allRPos.get(0).size(); i++) {
            averageRPos.add(i, (double) 0);
        }


        //soma os valores
        for (int i = 0; i < allRPos.get(0).size(); i++) {
            int soma = 0;

            for (int j = 0; j < allRPos.size(); j++) {
                soma = (Integer) allRPos.get(j).get(i);
                averageRPos.set(i, averageRPos.get(i) + soma);
            }
        }

        //se houver mais de uma consulta, faz a média
        if (allRPos.size() > 1) {

            for (int i = 0; i < allRPos.get(0).size(); i++) {

                averageRPos.set(i, averageRPos.get(i) / allRPos.size());
            }
        }
        return averageRPos;
    }

    public ArrayList<Integer> calculatefrel(String runName, String topicNumber, int atValue) throws TopicNotFoundException, MeasureNotFoundException{
        FRel frel = new FRel(context.getRunByName(runName), context.getQrels(), topicNumber);


        ArrayList<Integer> frelVector = new ArrayList();

        if (atValue > frel.getV().size()) {
            atValue = frel.getV().size();
        }
        for (int i = 0; i < atValue; i++) {
            frel.calculeteFRel(i);
            frelVector.add(i, frel.getFrel());
        }
        return frelVector;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}