package br.uefs.analyzIR.statistics;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import br.uefs.analyzIR.RunComparator;
import br.uefs.analyzIR.controller.Facade;
/**
*Clase responsável por organizar os dados para que seja possivél utilizar os métodos relacionados ao calculo de significancia estatistica
* da classe Facade. Calculando para cada interação de uma ou mais sistemas.
*
*@author Moisés
**/

public class StatisticsInteractive extends Statistics{

    private StatisticsIteractiveInfo info;

	public void calcular(int tamanho){

	}

	//retorna o indice do baseline a medida da baseline
	//Obs:Criar um try cath para a situação onde não existe o baseline escolhido.
    /**
    * @param baseline system que irá ser usado como base para calcular a relevancia dos outros sistemas em relação a ele.
    * @param runs lista de sistemas do projeto
    * @return a posição do baseline na lista de sistemas do projeto.
     **/
	public int baseline(String baseline,List<String> runs){
		String run;
        String auxiliar2;
        List<String> auxiliar = new ArrayList<String>();
        String baseline_test = baseline.substring(0,4);
        Collections.sort(runs, new RunComparator());
        //retira as runs repetidas que representam as iterações.
        for(String run_:runs){
            auxiliar2 = run_.substring(0,4);
            if(!auxiliar.contains(auxiliar2)) {
                auxiliar.add(auxiliar2);
            }
        }
        //Compara a baseline com as outras runs para descobrir a sua posição.
		for(int i =0;i<auxiliar.size();i++){
			run = auxiliar.get(i);
            //System.out.println(run);
			if(run.equalsIgnoreCase(baseline_test)){
				return auxiliar.indexOf(run);
			}
		}
		return 0;
	}

	//retorna as medidas das outras runs sem a baseline.
	/*public List results(){
		return null;
	}*/

	/**
	* Classe responsavel por calcular o resultado estatistico em relação a testes de significancia estátistica
	* @param results lista de resultados para a medida solicitada, para cada interação de uma lista de sistemas.
	* @param baseline lista de resultados das medidas do sistema que foi escolhido como base.
	* @param wilcoxonBib biblioteca que irá ser utilizada como base para o calculo estatistico.
	* @param facade o objeto da classe facade que está relacionado com o projeto
	* @param alphaD nível de significância
	* @return um objeto da classe StatisticsIteractiveInfo, que é responsável por guardar os dados do teste estatístico
     * escolhido.
	**/

	public StatisticsIteractiveInfo FilterwilcoxonTest(List<List> results,List<double[]> baseline,String wilcoxonBib,Facade facade,double alphaD, String baselineName,List<String> runs,HashMap<String, Integer> runsIteration) throws IOException, InterruptedException {

	    boolean[] values;
	    boolean[] valuesRelevance;
        HashMap<Integer,boolean[]> mp = new HashMap<Integer, boolean[]>();
        HashMap<Integer,boolean[]> relevance = new HashMap<Integer, boolean[]>();
        //Adicionando a baseline, será adicionando como null, pois a baseline não é comparada com ela mesma
        //int baselinePosition = this.baseline(baselineName,runs);
        //mp.put(baselinePosition,null);
        //relevance.put(baselinePosition,null);
	    double[] value1;
	    double[] value2;
	    double p_value;
	    int Control_Result = 0;
	    this.info = new StatisticsIteractiveInfo("wilcoxon",""+alphaD);
        /*boolean[] teste = new boolean[4];
        teste[0] = false;
        teste[1] = false;
        teste[2] = false;
        teste[3] = false;
        mp.put(0,teste);*/
	    //entra na lista de sistemas do projeto
		for(List result: results){
            values = new boolean[baseline.size()];
            valuesRelevance = new boolean[baseline.size()];
            int sizeAux = 0;
            if(baseline.size() >= result.size()){
                sizeAux = result.size();
            }
            else{
                sizeAux = baseline.size();
            }
            for(int i=0;i < sizeAux/*result.size()*/;i++){
			    //pega a interação desejada no baseline
                value1 = (double[])baseline.get(i);
                //pega a mesma interação de outro sistema
                value2 = (double[])result.get(i);

				/*double sum1, sum2, avg1, avg2;
				sum1 = sum2 = 0;

				for(double v : value1){
					sum1 += v;
				}

				for(double v : value2){
					sum2 += v;
				}

				avg1 = sum1 / value1.length;
				avg2 = sum2 / value2.length;*/

                p_value = facade.wilcoxonTest(value1, value2, wilcoxonBib);
                //System.out.println("P_Value: " + p_value);
                info.addValues(p_value);
                //caso não tenha significancia
                if(p_value == 0 || p_value < alphaD){
                    values[i] = false;
                }else{
                    values[i] = true;
                    valuesRelevance[i] = this.relevance(value1,value2);
                }

			}
			//guarda o vetor de boolean em um map, cada sistema terá um vetor de booelano indicando em qual interação houe significancia.
			mp.put(Control_Result,values);
            relevance.put(Control_Result,valuesRelevance);
            Control_Result++;
		}
		info.setTestResults(mp);
		info.setTestResultsRelevance(relevance);

		return info;
	}

    /**
     * Classe responsavel por calcular o resultado estatistico em relação ao teste T-Student
     * @param results lista de resultados para a medida solicitada, para cada interação de uma lista de sistemas.
     * @param baseline lista de resultados das medidas do sistema que foi escolhido como base.
     * @param TStudentBib biblioteca que irá ser utilizada como base para o calculo estatistico.
     * @param facade o objeto da classe facade que está relacionado com o projeto
     * @param alphaD nível de significância
     * @return um objeto da classe StatisticsIteractiveInfo, que é responsável por guardar os dados do teste estatístico
     * escolhido.
     **/

    public StatisticsIteractiveInfo FilterTStudentTest(List<List> results,List<double[]> baseline,String TStudentBib,Facade facade,double alphaD) throws IOException, InterruptedException {

        boolean[] values;
        boolean[] valuesRelevance;
        HashMap<Integer,boolean[]> mp=new HashMap<Integer, boolean[]>();
        HashMap<Integer,boolean[]> relevance = new HashMap<Integer, boolean[]>();
        double[] value1;
        double[] value2;
        double p_value;
        double statistical_value;
        double T_value;
        //Mudar isso, recebe direto o valor direto do alphaD
        double alpha = (1 - alphaD);
        int Control_Result = 0;
        this.info = new StatisticsIteractiveInfo("TStudentTest",""+alphaD);
        /*
        * Como para indicar se há siginificancia pelo teste T de Student é preciso comparar com uma tabela, foi necessario lê a mesma e obter o valor
        * que iria ser comparado com o valor encontrado pelo teste.
        *
        * */
        //Leitura da tabela T-Student para obter o valor que irá ser comparado com o encontrado.
        //FileReader read = new FileReader("TStudentTable.txt");
        //BufferedReader buffer = new BufferedReader(read);
        String[] line;
        int alpha_Colunn;

        // de acordo com o nivel de significancia escolhido é encontrado a coluna na tabela
        //Terá que ser feita modificações por causa da escolha do Alpha ser com 0.5 para representar 95.
        if(alpha == 0.90){
            alpha_Colunn = 1;
        }
        else if(alpha == 0.95){
            alpha_Colunn = 2;
        }

        else if(alpha == 0.975){
            alpha_Colunn = 3;
        }

        else if(alpha == 0.99){
            alpha_Colunn =  4;
        }

        else if(alpha == 0.995){
            alpha_Colunn = 5;
        }

        else{
            alpha_Colunn = 6;
        }
        for(List result: results){

            values = new boolean[baseline.size()];
            valuesRelevance = new boolean[baseline.size()];
            for(int i=0;i < result.size();i++){
                FileReader read = new FileReader("TStudentTable.txt");
                BufferedReader buffer = new BufferedReader(read);
                value1 = (double[])baseline.get(i);
                value2 = (double[])result.get(i);

                //p_value = facade.tStudentTestSP(value1, value2,TStudentBib);
                statistical_value = facade.tStudentTestStatistic(value1,value2,TStudentBib);

                //para encontrar a linha é feita uma leitura do aquivo até chega uma linha antes da escolhida pelo tamanho da amostra
                for(int j = 1; j < (value2.length-1);j++){
                    buffer.readLine();
                }

                //lê a linha toda para o tamanho da amostra.
                line = buffer.readLine().split(" ");
                //depois de dividida, utilizando o valor do nivel de significancia  é escolhido o a informação correta
                T_value = Double.parseDouble(line[alpha_Colunn]);
                System.out.println("Statistical_value: " + statistical_value);
                System.out.println("T_Value: " + T_value);
                info.addValues(T_value);
               if(statistical_value < T_value){
                    values[i] = true;
                   valuesRelevance[i] = this.relevance(value1,value2);
                }
                else{
                    values[i] = false;
                }


            }
            mp.put(Control_Result,values);
            relevance.put(Control_Result,valuesRelevance);
            Control_Result++;
        }
        info.setTestResults(mp);
        info.setTestResultsRelevance(relevance);
        return info;
	}



    /**
     * Classe responsavel por calcular o resultado estatistico em relação a testes de significancia estátistica: Mann Whitney U
     * @param results lista de resultados para a medida solicitada, para cada interação de uma lista de sistemas.
     * @param baseline lista de resultados das medidas do sistema que foi escolhido como base.
     * @param facade o objeto da classe facade que está relacionado com o projeto
     * @param alphaD nível de significância
     * @return um objeto da classe StatisticsIteractiveInfo, que é responsável por guardar os dados do teste estatístico
     * escolhido.
     */

    public StatisticsIteractiveInfo FilterMannWhitneyUTest(List<List> results,List<double[]> baseline,Facade facade,double alphaD) throws IOException, InterruptedException {

        boolean[] valuesRelevance;
        boolean[] values;
        HashMap<Integer,boolean[]> mp=new HashMap<Integer, boolean[]>();
        HashMap<Integer,boolean[]> relevance = new HashMap<Integer, boolean[]>();
        double[] value1;
        double[] value2;
        double p_value;
        int Control_Result = 0;
        this.info = new StatisticsIteractiveInfo("MannWhitneyU",""+alphaD);
        for(List result: results){
            values = new boolean[baseline.size()];
            valuesRelevance = new boolean[baseline.size()];
            int sizeAux = 0;
            if(baseline.size() >= result.size()){
                sizeAux = result.size();
            }
            else{
                sizeAux = baseline.size();
            }
            for(int i=0;i < sizeAux/*result.size()*/;i++){
                value1 = (double[])baseline.get(i);
                value2 = (double[])result.get(i);

                p_value = facade.mannWhitneyTest(value1,value2);
                System.out.println("P_Value: " + p_value);
                info.addValues(p_value);
                if(p_value == 0 || p_value <= alphaD){
                    values[i] = false;

                }else{

                    values[i] = true;
                    valuesRelevance[i] = this.relevance(value1,value2);
                }

            }
            mp.put(Control_Result,values);
            relevance.put(Control_Result,valuesRelevance);
            Control_Result++;
        }
        info.setTestResults(mp);
        info.setTestResultsRelevance(relevance);
        return info;
    }

    /**
     * Classe responsavel por verificar se o valor baseline é maior do que o sistema que está sendo comparada.
     * @param baselineValue Valor da baseline.
     * @param run Valor do sistema comparado.
     *
     * @return se o valor da run é maior do que a do baseline.
     **/

    public boolean relevance(double[] baselineValue,double[] run){

        double averageBaseline = 0;
        double averageRun = 0;
        for(double temp: baselineValue){
            averageBaseline+=temp;
        }
        for(double temp2: run){
            averageRun+=temp2;
        }

        return (averageRun > averageBaseline);
    }
}