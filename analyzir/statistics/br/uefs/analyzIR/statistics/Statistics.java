package br.uefs.analyzIR.statistics;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.data.Run;
import br.uefs.analyzIR.data.RunItem;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import java.io.IOException;
import java.util.*;

/** Class used as intermediary to calculate statistical tests
 *
 */
public class Statistics {
    private Context context;

    /** Wilcoxon Test
     *
     * @param value1 The first set of values
     * @param value2 The second set of values
     * @param typeSelected The library chosen to execute the test
     * @return Returns the P-value associated found after running the test
     * @throws IOException
     * @throws InterruptedException
     */
    public double wilcoxonTest(double[] value1, double[] value2, String typeSelected) throws IOException, InterruptedException {
        //This names, like "acm" and "jsc", can change after.

        if(typeSelected.equals("Apache Commons")){
            ApacheCommonsWilcoxonSignedRankTest acmWilcoxon = new ApacheCommonsWilcoxonSignedRankTest(value1,value2);

            double pValue = acmWilcoxon.runTest();
            return pValue;

        }else if(typeSelected.equals("JSC")){
            JSCWilcoxonSignedRankTest jscWilcoxon = new JSCWilcoxonSignedRankTest(value1,value2);

            double pValue = jscWilcoxon.runTest();
            return pValue;
        }else {
            MacKenzieWilcoxonSignedRankTest wil2 = new MacKenzieWilcoxonSignedRankTest(value1, value2);
            wil2.createFile();

            double pValue = wil2.runTest();
            return pValue;
        }
    }

    /** Jaccard Test
     *
     * @param vector1 The first set of values
     * @param vector2 The second set of values
     * @return Returns Jaccard's similarity index
     */
    public double jaccardTest(String[] vector1, String[] vector2){
        JaccardTest jaccardTestKhaick = new JaccardTest();
        double jaccardIndex = jaccardTestKhaick.calculateIndex(vector1,vector2);
        return jaccardIndex;
    }

    /** Spearman's Test
     *
     * @param vector1 The first set of values
     * @param vector2 The second set of values
     * @return Return Spearman's correlation index
     */
    public double spearmanTest(String[] vector1, String[] vector2){
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();

        List vectorsAdapted = vectorsAdapting(vector1,vector2);

        ArrayList<Double> array1 = (ArrayList<Double>) vectorsAdapted.get(0);
        ArrayList<Double> array2 = (ArrayList<Double>) vectorsAdapted.get(1);

        double[] vectorA = new double[array1.size()];
        double[] vectorB = new double[array2.size()];

        for (int i=0; i<array1.size();i++){
            vectorA[i] = array1.get(i);
            vectorB[i] = array2.get(i);
        }

        double spearmanIndex = spearmansCorrelation.correlation(vectorA,vectorB);

        return spearmanIndex;
    }

    /** Pearson's Test
     *
     * @param vector1 The first set of values
     * @param vector2 The second set of values
     * @return Return Pearson's p-value
     */
    public double pearsonTest(double[] vector1, double[] vector2){
        PearsonsCorrelation pearsonTest = new PearsonsCorrelation();
        double pvalue = pearsonTest.correlation(vector1,vector2);
        return pvalue;
    }

    /** Mann Whitney's Test
     *
     * @param vector1 The first set of values
     * @param vector2 The second set of values
     * @return Return Mann Whitney's p-value
     */
    public double mannWhitneyTest(double[] vector1, double[] vector2){
        MannWhitneyUTest mannWTest = new MannWhitneyUTest();
        double value = mannWTest.mannWhitneyUTest(vector1,vector2);
        return value;
    }

    /** Vectors adapting method
     * Used to adapt vectors that need to have the same length and same elements,
     * regardless the order, but they dont have.
     * @param v1 The first set of elements
     * @param v2 The second set of elements
     * @return Returns two new vectors after adapting process
     */
    private List vectorsAdapting(String[] v1, String[] v2){
        ArrayList<String> a1 = new ArrayList<String>(Arrays.asList(v1));
        ArrayList<String> a2 = new ArrayList<String>(Arrays.asList(v2));

        HashSet<String> union = new HashSet<String>();
        union.addAll(a1);
        union.addAll(a2);

        ArrayList<Double> x = new ArrayList<>(), y = new ArrayList<>();

        for (String item: union) {
            int posA1 = a1.indexOf(item);
            int posA2 = a2.indexOf(item);

            if(posA1 == -1)
                posA1 = (2 * a1.size()) - posA2 - 1;

            if(posA2 == -1)
                posA2 = (2 * a2.size()) - posA1 - 1;

            x.add(posA1+1.0);
            y.add(posA2+1.0);
        }

        List vectors = new LinkedList();
        vectors.add(x);
        vectors.add(y);

        return vectors;
    }

    /** Multiple Systems Correlation's Tests
     *
     * @param systems Systems chosen to be applied the test
     * @param topicsWithoutAverage Topics chosen to extract ranks
     * @param atValue Depth reference value
     * @param testSelected The selected test to be applied
     * @return Returns a three-dimensional correlation matrix
     * @throws TopicNotFoundException
     */
    public String[][][] multipleCorrelationTest(List<String> systems, List<String> topicsWithoutAverage, int atValue, String testSelected) throws TopicNotFoundException {
        String[][][] correlationByTopicsMatrices = new String[topicsWithoutAverage.size()+1][systems.size()][systems.size()];
        List<String> topics = new LinkedList<>();


        for(int cont=0;cont<topicsWithoutAverage.size();cont++){
            topics.add(topicsWithoutAverage.get(cont));
        }
        topics.add("Average");

        for(int i=0;i<systems.size();i++){
            for (int j=0;j< systems.size();j++){
                correlationByTopicsMatrices[topicsWithoutAverage.size()][i][j] = String.valueOf(0.0);
            }
        }


            for(int topicCounter = 0; topicCounter<topics.size()-1;topicCounter++) {
            String topic = topics.get(topicCounter);

            for (int i = 0; i < systems.size(); i++) {
                String system1 = systems.get(i);
                //System.out.print(system1 + " -> ");
                String[] vector1 = getRankByTopic(system1, topic, atValue);

                for (int j = 0; j < systems.size(); j++) {
                    if (i != j) {
                        String system2 = systems.get(j);
                        //------
                        //System.out.print(system2);
                        //------
                        String[] vector2 = getRankByTopic(system2, topic, atValue);

                        double correlValue;

                        if(testSelected.equals("Jaccard")) {
                            correlValue = jaccardTest(vector1, vector2);
                        }else if(testSelected.equals("Spearman")){
                            correlValue = spearmanTest(vector1, vector2);
                        }else
                            correlValue = 0.0;


                        double average = Double.parseDouble(correlationByTopicsMatrices[topicsWithoutAverage.size()][i][j]);
                        average += correlValue;
                        correlationByTopicsMatrices[topicsWithoutAverage.size()][i][j] = String.valueOf(average);

                        //System.out.println("AVERAGE: "+average + "Actual Corel Value: " + correlValue);
                        //----
                        //System.out.print(" " + correlValue + "|");
                        String value = Double.toString(correlValue);
                        correlationByTopicsMatrices[topicCounter][i][j] = value;
                    }else {
                        double correlValue = 1;
                        correlationByTopicsMatrices[topicCounter][i][j] = Double.toString(correlValue);

                        double average = Double.parseDouble(correlationByTopicsMatrices[topicsWithoutAverage.size()][i][j]);
                        average += correlValue;
                        correlationByTopicsMatrices[topicsWithoutAverage.size()][i][j] = String.valueOf(average);
                    }
                }
                //System.out.println("");
            }
        }

        for(int i=0;i<systems.size();i++){
            for (int j=0;j< systems.size();j++){
                double average = Double.parseDouble(correlationByTopicsMatrices[topicsWithoutAverage.size()][i][j]);
                average = average/topicsWithoutAverage.size();
                correlationByTopicsMatrices[topicsWithoutAverage.size()][i][j] = String.valueOf(average);
            }
        }

        return correlationByTopicsMatrices;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Run getRunByName(String runName){
        Run run = context.getRunByName(runName);
        return run;
    }

    public String[] getRankByTopic(String runName, String topicNumber, int atValue) throws TopicNotFoundException {
        Run run = getRunByName(runName);
        List<RunItem> runItens = run.getRunItems(topicNumber);
        String[] rank = new String[atValue];
        for (int i = 0; i < atValue; i++) {
             rank[i] = runItens.get(i).getDocno();
        }
        return rank;
    }
}