package br.uefs.analyzIR.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe responsável por guardar os dados relacionados aos testes estatísticos.
 *
 * @author moises
 */
public class StatisticsIteractiveInfo {

    private String test;
    private List testValues;
    private String baseline;
    private HashMap<Integer,boolean[]> testResults;
    private HashMap<Integer,boolean[]> testResultsRelevance;
    private String significant;

    /**
     *
     * @param test nome do teste escolhido
     * @param significant aplha escolhido
     */
    public StatisticsIteractiveInfo(String test,String significant){
        this.test = test;
        this.significant = significant;
        testValues = new ArrayList();

    }

    /**
     *
     * @param value valor encontrado no teste estatístico
     */
    public void addValues(double value){
        testValues.add(value);
    }

    /**
     *
     * @param baseline nome da baseline escolhida
     */
    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    /**
     *
     * @param testResults resultado da relevancia entre a baseline e as outras runs
     */
    public void setTestResults(HashMap<Integer,boolean[]>  testResults) {
        this.testResults = testResults;
    }

    /**
     *
     * @param testResultsRelevance resultado da comparação de valores entre a baseline e os outros sistemas
     * se o valor da medida encontrado é maior do que a do baseline.
     */
    public void setTestResultsRelevance(HashMap<Integer,boolean[]>  testResultsRelevance) {
        this.testResultsRelevance = testResultsRelevance;
    }

    /**
     *
     * @return o nome do teste esttístico escolhido
     */
    public String getTest() {
        return test;
    }

    /**
     *
     * @return resultado da relevância entre a baseline e as outras runs
     */
    public HashMap<Integer,boolean[]>  getTestResults() {
        return testResults;
    }

    /**
     *
     * @return resultado da comparação dos valores das medidas entre a baseline e as outras runs
     */
    public HashMap<Integer,boolean[]>  getTestResultsRelevance() {
        return testResultsRelevance;
    }

    /**
     *
     * @return lista de valores encontrados no teste estatístico.
     */
    public List getTestValues() {
        return testValues;
    }

    /**
     *
     * @return o nome da baseline escolhida
     */
    public String getBaseline() {
        return baseline;
    }

    /**
     *
     * @return o aplha escolhido
     */
    public String getSignificant() {
        return significant;
    }

    public List<Boolean> parser(){
        List temp =  new ArrayList();
        for(int i = 0;i < this.testResults.size();i++){
            for(Boolean item:this.testResults.get(i)) {
                temp.add(item);
            }
        }
        return temp;
    }
}
