package br.uefs.analyzIR.controller;

import br.uefs.analyzIR.RunComparator;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphExporter;
import br.uefs.analyzIR.graph.GraphInfo;
import br.uefs.analyzIR.controller.graph.GraphController;
import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.exception.*;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.failureAnalysis.FailureAnalysis;
import br.uefs.analyzIR.graph.statisticsTestInteractive.Colors;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.data.MeasureValue;
import br.uefs.analyzIR.measure.data.TopicResult;
import br.uefs.analyzIR.statistics.*;
import br.uefs.analyzIR.ui.common.statistics.JaccardTest;
import jsc.distributions.StudentsT;
import jsc.independentsamples.TwoSampleTtest;
import org.apache.commons.math3.stat.inference.TTest;

import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Collator;
import java.util.*;
import java.util.List;

public class Facade {

	private ProjectController projectController;
	private MeasureController measureController;
	private GraphController graphController;
    private FailureAnalysis FailureAnalysis;
	private Integer graphType;
	private Statistics statistics;

    public Facade() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        this.projectController = new ProjectController();
		this.graphController = new GraphController();
		this.measureController = new MeasureController();
        this.FailureAnalysis = new FailureAnalysis();
        this.statistics = new Statistics();
    }

	/**************************
	 * Basic operation in project 
	 * @param cqrelsPath TODO***********
	 * 
	 * @throws InvalidProjectTypeException
	 * @throws TopicNotFoundException
	 * @throws DiferentsSystemsException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws InvalidRunNumberException 
	 ********************/

	
	public void createProject(String[] runFile, String qrelsFile,
			String nameProject, String urlDirectory, boolean intersection, String cqrelsPath, int type, int protocol)

	throws FileNotFoundException, IOException, RunItemNotFoundException,
			QrelItemNotFoundException, InvalidQrelFormatException,
			InvalidURLException, CreateDirectoryException, NoGraphItemException,
			InvalidProjectTypeException, DiferentsSystemsException,
			TopicNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidRunNumberException {

		Context context = this.projectController.create(nameProject, urlDirectory, qrelsFile,
				runFile, intersection, cqrelsPath, type, protocol);

        this.FailureAnalysis.setContext(context);
        this.measureController.setContext(context);
        this.measureController.initMeasures(context);
        this.statistics.setContext(context);
	}

	public void openProject(String directory, int type) throws ClassNotFoundException,
			FileNotFoundException, IOException, QrelItemNotFoundException,
			InvalidQrelFormatException, RunItemNotFoundException,
			NoGraphItemException, InstantiationException, IllegalAccessException {

		Context context = this.projectController.open(directory, type);
        this.FailureAnalysis.setContext(context);
        this.measureController.initMeasures(context);
        this.measureController.setContext(context);
		this.statistics.setContext(context);
    }

	public void save() throws FileNotFoundException, IOException,
			NoGraphItemException {
		this.projectController.save();
	}

	public int getType() {
		return this.projectController.getType();
	}

	/********************** Others operation in Project ***********************/

	public String[] getResume() throws TopicNotFoundException,
			QrelItemNotFoundException, InvalidQrelFormatException {
		return this.projectController.getResume();
	}

	public List<String> listTopics() {
		return this.projectController.listTopics();
	}

	public List<String> listRuns() {
		return this.projectController.listRuns();
	}

	public List<String> listInterations(){
		return this.projectController.getInterations();
	}

    public FailureAnalysis getFailureAnalysis() {
        return this.FailureAnalysis;
    }

	

	/****************** Measure Operations *********************/

	public List<String> listMeasures() {
		
		List<String> measures = null; 
		if(projectController.getType() == 2){
			measures = this.measureController.getMeasures();
			measures.addAll(this.measureController.getDiversifyMeasures());
			Collections.sort(measures, Collator.getInstance());
			return measures;
		}else{
			measures = this.measureController.getMeasures();
			Collections.sort(measures);
			return measures;
		}
	}

	public int getMeasureType(List<String> measures) {
		return this.measureController.getMeasureType(measures);
	}
	
	public int getInteractiveMeasureType(List<String> measures) {
		return this.measureController.getInteractiveMeasureType(measures);
	}

	public List<String> getMeasureGroup(List<String> measures)
			throws MeasuresGroupNotFoundException {
		List<String> m = this.measureController.getMeasureGroup(measures);
		return m;
	}

	public List<String> getMeasuresByType(int type) {
		return measureController.getMeasuresByType(type);
	}
	
	public String [] add(String url, String nameFile) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		return this.measureController.addMeasure(url, nameFile);
	}
	
	public void remove(String url, String name){
		this.measureController.removeMeasure(url, name);
	}
	
	public List<String[]> listUserMesaures() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		return this.measureController.listUserMeasures();
	}

	public String getMeasureDescription(String measureName) throws MeasureNotFoundException {
		return this.measureController.getMeasureDescription(measureName);
	}

	

	// *****************************Graph operation ****************************

	public JPanel createGraph(int type, String title, List<String> measures,
			List<String> runs, List<String> topics, String[] xValues,
			String name, String atValue, String x_axis, String y_axis, List<String> iterations, int graphType)

	throws IOException, InterruptedException, MeasureNotFoundException,
			InvalidGraphTypeException, InvalidDataFormatException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException, InvalidGraphTypeException,
			EmptyTopicMeasureValueException, NoGraphItemException,
			InvalidGraphNameException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException, GraphItemNotFoundException, NoGraphPointException {



		int typeMeasure = this.measureController.getMeasureType(measures);
		HashMap<String, List<MeasureSet>> result = this.measureController.calculateMeasures(measures, runs, topics, atValue, xValues, typeMeasure);
		Graph graph = this.graphController.create(type, result, measures.size(), title, name, x_axis, y_axis, graphType);
		GraphInfo info = new GraphInfo(measures, runs, topics, atValue, xValues);

		/*Set<String> keys2 =  result.keySet();
        List<String> keysAux = new ArrayList<String>();
        for(String key:keys2){
            keysAux.add(key);
        }
        Collections.sort(keysAux,new RunComparator());
        List<MeasureSet>  resultAux3;
        TopicResult[] topicAux3;
        TopicResult topicAux4 = new TopicResult("A");
        //HashMap<String, Double> teste;
        //double average = 0;
        for(String key:keysAux){
            resultAux3 = result.get(key);
            //List<MeasureSet>  resultAux2 = new ArrayList<>();
            System.out.println("Sistema " + key);
            for(MeasureSet set: resultAux3){
                System.out.println("MeasureName " + set.getMeasureName());
                topicAux3 = set.getTopicResults();
                int i = 0;
                for(TopicResult topic: topicAux3){
                    //average += topic.getValues().get(0).getValue();
                    System.out.println("Confirmação:" + topic.getValues().size());
                    System.out.println("Topico: " + topic.getTopicName() +" NomeValor: " + topic.getValues().get(0).getX() +" Valor: "+topic.getValues().get(0).getValue());
                    i++;
                }
            }
            //resultAux2.get(0).addTopicResult("A");
            //resultAux2.get(0).addValue("A",average);
            //resultAverage.put(key,resultAux2);
        }*/

		graph.setGraphInfo(info);
		this.projectController.addGraph(graph);
		
		return graph.make();
	}
	
	

	public JPanel createGraph(int type, String title, List<String> measures, String run,
			String[] xValues, String topic, String nameFile, String atValue, String orderBy)
			throws IOException, InterruptedException, MeasureNotFoundException,
			InvalidGraphTypeException, InvalidDataFormatException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException, InvalidGraphTypeException,
			EmptyTopicMeasureValueException, NoGraphItemException,
			InvalidGraphNameException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException, GraphItemNotFoundException, NoGraphPointException {
		
		
		List<String> runs = Arrays.asList(run);
		List<String> topics = Arrays.asList(topic);
		
		
		HashMap<String, List<MeasureSet>> result = this.measureController.calculateMeasures(measures, runs, topics, atValue, xValues, type);

		graphType = (Integer) null;
		Graph graph = this.graphController.create(type, result, measures.size(), title, nameFile, "", "", graphType);


		GraphInfo info = new GraphInfo(measures, runs, topics, atValue, xValues);
		graph.setGraphInfo(info);
		this.projectController.addGraph(graph);
		
		return graph.make();
	
	}

	public JPanel replaceGraph(String oldPlot, String title, List<String> measures,
			List<String> runs,  List<String> topics, String[] xValues,
			String name, String atValue, String x_axis, String y_axis, int graphType)

	throws IOException, InterruptedException, MeasureNotFoundException,
			InvalidGraphTypeException, InvalidDataFormatException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException, InvalidGraphTypeException,
			EmptyTopicMeasureValueException, NoGraphItemException,
			InvalidGraphNameException, GraphNotFoundException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException, GraphItemNotFoundException, NoGraphPointException {


		int typeMeasure = getMeasureType(measures);		
		HashMap<String, List<MeasureSet>> result = this.measureController.calculateMeasures(measures, runs, topics, atValue, xValues, typeMeasure);
		Graph graph = this.graphController.create(typeMeasure, result, measures.size(), title, name, x_axis, y_axis, graphType);

		GraphInfo info = new GraphInfo(measures, runs, topics, atValue, xValues);
		graph.setGraphInfo(info);
		//this.projectController.addGraph(graph);		
		this.projectController.replaceGraph(graph, oldPlot);
		
		return graph.make();

	}

	public JPanel replaceGraph(String title, List<String> measures,
			List<String> runs,  List<String> topics, String[] xValues,
			String name, String atValue, String x_axis, String y_axis, int graphType, List<String> iterations)

	throws IOException, InterruptedException, MeasureNotFoundException,
			InvalidGraphTypeException, InvalidDataFormatException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException, InvalidGraphTypeException,
			EmptyTopicMeasureValueException, NoGraphItemException,
			InvalidGraphNameException, GraphNotFoundException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException, GraphItemNotFoundException, NoGraphPointException {

		int typeMeasure = getMeasureType(measures);		
		HashMap<String, List<MeasureSet>> result = this.measureController.calculateMeasures(measures, runs, topics, atValue, xValues, typeMeasure);
		Graph graph = this.graphController.create(typeMeasure, result, measures.size(), title, name, x_axis, y_axis, graphType);
		GraphInfo info = new GraphInfo(measures, runs, topics, atValue, xValues, iterations);
		graph.setGraphInfo(info);
		//this.projectController.addGraph(graph);
		this.projectController.replaceGraph(graph, graph.getName());
		
		return graph.make();

	}
	
	public boolean isGraphNameAvailable(String name) {
		return this.projectController.isGraphNameAvailable(name);
	}
	
	/*public JPanel replaceGraphed(String title, List<String> measures,
			List<String> runs,  List<String> topics, String[] xValues,
			String name, String atValue, String x_axis, String y_axis,  int graphType, List<String> iterations)

	throws IOException, InterruptedException, MeasureNotFoundException,
			InvalidGraphTypeException, InvalidDataFormatException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException, InvalidGraphTypeException,
			EmptyTopicMeasureValueException, NoGraphItemException,
			InvalidGraphNameException, GraphNotFoundException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException, GraphItemNotFoundException, NoGraphPointException {

		int typeMeasure = getMeasureType(measures);		
		HashMap<String, List<MeasureSet>> result = this.measureController.calculateMeasures(measures, runs, topics, atValue, xValues, typeMeasure);
		Graph graph = this.plotController.create(typeMeasure, result, measures.size(), title, name, x_axis, y_axis, graphType);
		GraphInfo info = new GraphInfo(measures, runs, topics, atValue, xValues, iterations);
		graph.setGraphInfo(info);
		//this.projectController.addGraph(graph);
		this.projectController.editGraph(graph, graph.getName());
		
		return graph.make();

	}*/

	public void removeGraph(String nameFilePlot) {
		this.projectController.removeGraph(nameFilePlot);
	}

	public void GraphSaveAs(String path, String name, int width, int height,
			String format) throws IOException, NoGraphItemException,
			GraphNotFoundException, NoGraphPointException {
		Graph p = this.projectController.searchGraph(name);
		p.exportAs(path, name, width, height, format);
	}

	public List<String> listGraphs() {
		return this.projectController.listPlots();
	}

	public JPanel getPlotImage(String name) throws NoGraphItemException,
			GraphNotFoundException, NoGraphPointException {
		return this.projectController.searchGraph(name).make();
	}

	public HashMap<String, List<String>> getPlotValues(List<String> keys,
			String namePlot) throws GraphNotFoundException {

		return this.projectController.getGraphValues(keys, namePlot);
	}

	/**
	 *
	 * @param name
	 * @param url
	 * @param extensao
	 * @throws GraphNotFoundException
	 * @throws IOException
	 * @throws NoGraphItemException
	 * @throws NoGraphPointException
	 */
	public void exportPlot(String name, String url, String extensao)
			throws GraphNotFoundException, IOException, NoGraphItemException, NoGraphPointException {

		Graph graph = projectController.searchGraph(name);
		GraphExporter exporter = new GraphExporter(name, url, graph);
		if(extensao.equals(".jpg"))
			exporter.exportToJPG(1024, 960);
		else if (extensao.equals(".png"))
			exporter.exportToPNG(1024,960);
		else if(extensao.equals(".csv"))
			exporter.exportToCSV();
		else if (extensao.equals(".xls"))
			exporter.exportToXLS();
	}

	public JPanel getGraph(String name) throws NoGraphItemException, GraphNotFoundException, NoGraphPointException {
	
		return this.projectController.searchGraph(name).make();
	}

	public void closeProject(boolean save) throws FileNotFoundException, IOException, NoGraphItemException {
		if(save){
			this.projectController.save();
			this.projectController.close();
		}else{
			this.projectController.close();
		}
	}

	public double[] calculeValue(String system, List<String> topics,
			List<String> measures, String atValue, String[] xValues) throws NumberFormatException, IOException,
			InterruptedException, MeasureNotFoundException, InvalidItemNameException,
			ItemNotFoundException, EmptyTopicMeasureValueException, NoGraphItemException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException {
		
		String [] t = null;// topics
		double [] values = null;//values Y
		if(topics != null && !topics.isEmpty()){
			t = new String [topics.size()];
			t = topics.toArray(t);
		}



	    List<MeasureSet> result = this.measureController.calculateMeasures(measures, system, t, atValue, xValues);

		TopicResult [] tmv = result.get(0).getTopicResults();
		
		int index, length;
		index = length = 0; 
		length = tmv.length * tmv[0].getValues().size();
		
		values = new double[length];
		
		for(TopicResult tm : tmv){	
			
			List<MeasureValue>  mValues = tm.getValues();
			
			for(MeasureValue v : mValues){
				values[index] = v.getValue();
				index++;
			}
		}
		return values;
	}


	public double wilcoxonTest(double[] value1, double[] value2, String typeSelected) throws IOException, InterruptedException {
		//This names, like "acm" and "jsc", can change after.
        return statistics.wilcoxonTest(value1,value2,typeSelected);
	}

	public double mannWhitneyTest(double[] vector1, double[] vector2){
		return statistics.mannWhitneyTest(vector1,vector2);
	}

	public double jaccardTest(String system1, String system2, String atValue, String topic) throws TopicNotFoundException {
		int atValueAux = Integer.parseInt(atValue);
		String[] vector1 = statistics.getRankByTopic(system1,topic,atValueAux);
		String[] vector2 = statistics.getRankByTopic(system2,topic,atValueAux);
		return statistics.jaccardTest(vector1, vector2);
	}

	public double pearsonTest(double[] vector1, double[] vector2){
		return statistics.pearsonTest(vector1,vector2);
	}

	public String[][][] correlationTest(List<String> systems, String atValue, List<String> topics, String correlationTestSelected) throws TopicNotFoundException {
		int atValueAux = Integer.parseInt(atValue);
		return statistics.multipleCorrelationTest(systems, topics, atValueAux, correlationTestSelected);
	}

	public double tStudentTestSP(double[] value1, double[] value2, String typeSelected) throws IOException, InterruptedException {
		double pValue = 0;
		switch (typeSelected) {
			case "Apache Commons":
				TTest tTest = new TTest();
				pValue = tTest.pairedTTest(value1, value2);
				return pValue;
			case "JSC":
				TwoSampleTtest jsctTest = new TwoSampleTtest(value1, value2);
				pValue = jsctTest.getSP();
				return pValue;
			default:
				return pValue;
		}
	}

	public double tStudentTestStatistic(double[] value1, double[] value2, String typeSelected) throws IOException, InterruptedException {
		double statisticalValue = 0;
		switch (typeSelected) {
			case "Apache Commons":
				TTest tTest = new TTest();
				statisticalValue = tTest.pairedT(value1, value2);
				return statisticalValue;
			case "JSC":
				TwoSampleTtest jsctTest = new TwoSampleTtest(value1, value2);
				statisticalValue = jsctTest.getStatistic();
				return statisticalValue;
			default:
				return statisticalValue;
		}
	}

    //*************************************testesEstatisticos************************************************


    //sobreposição para os testes estatisticos interativos
	//responsavel por criar os graficos relacionados com os testes estatisticos.
	/*
	 * @param type tipo de grafico(barra ou curva).
	 * @param title título do gráfico.
	 * @param measures lista de medidas.
	 * @param runs lista de runs.
	 * @param topics lista de topicos.
	 * @param xvalues.
	 * @param name nome do gráfico.
	 * @param atValue profundidade que irá ser considerada para o calculo da medida.
	 * @param x_axis nome da lagenda que irá aparecer no eixo x do gráfico.
	 * @param y_axis nome da lagenda que irá aparecer no eixo y do gráfico.
	 * @param iterations lista de iterações.
	 * @param graphType tipo da confuguração do gráfico(número de tópicos,sistemas,medidas e iterações).
	 * @param test nome do teste escolhido.
	 * @param lib mome da biblioteca escolhida.
	 * @param baseline nome do sistema escolhido como baseline dos testes.
	 * @param alphaD valor do alpha escolhido para o teste.
	 * @return um Panel contendo o grafico dos resultados das medidas.
	 */
    public JPanel createGraph(int type, String title, List<String> measures,
                              List<String> runs, List<String> topics, String[] xValues,
                              String name, String atValue, String x_axis, String y_axis, List<String> iterations, int graphType, String test,String lib,String baseline,double alphaD,HashMap<String, Integer> runsIteration)

            throws IOException, InterruptedException, MeasureNotFoundException,
            InvalidGraphTypeException, InvalidDataFormatException,
            NumberFormatException, InvalidItemNameException,
            ItemNotFoundException, InvalidGraphTypeException,
            EmptyTopicMeasureValueException, NoGraphItemException,
            InvalidGraphNameException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException, GraphItemNotFoundException, NoGraphPointException {



			int typeMeasure = this.measureController.getMeasureType(measures);
			HashMap<String, List<MeasureSet>> result = this.measureController.calculateMeasures(measures, runs, topics, atValue, xValues, typeMeasure);
			//fazendo a média das medidas
			HashMap<String, List<MeasureSet>> resultAverage = new HashMap();

			Set<String> keys =  result.keySet();
			List<MeasureSet>  resultAux;
			TopicResult[] topicAux;
			TopicResult topicAux2 = new TopicResult("A");
			HashMap<String, Double> teste;
			double average = 0;
			int sizeTopic = 0;
			for(String key:keys){
				resultAux = result.get(key);
				List<MeasureSet>  resultAux2 = new ArrayList<>();
				MeasureSet m = new MeasureSet(measures.get(0));
				resultAux2.add(m);
				//.out.println("Sistema " + key);
				for(MeasureSet set: resultAux){
					topicAux = set.getTopicResults();
					sizeTopic = set.getTopicResults().length;
                    System.out.println("Tamanho:" + sizeTopic);
					for(TopicResult topic: topicAux){
					    if(topic.getValues().size() == 0){
					        average += 0;
                        }
					    else{
						    average += topic.getValues().get(0).getValue();
                        }
						//System.out.println("Topico" + topic.getTopicName() + "Valor:"+topic.getValues().get(0).getValue());
					}
				}
				if(sizeTopic == 0){
					System.out.println("Não deu certo");
				}
                average = Math.floor(average * 10000)/10000;
                double aux = average/sizeTopic;
				resultAux2.get(0).addTopicResult("M");
				resultAux2.get(0).addValue("M",atValue,aux);
				resultAverage.put(key,resultAux2);
				average = 0;
			}

			List values = statistcsTestInteractive(result,runs);
			StatisticsIteractiveInfo interactiveTestInfo = valueTetsInteractive(values,test,lib,baseline,runs,alphaD,runsIteration);
			interactiveTestInfo.setBaseline(baseline);
			HashMap<Integer, boolean[]> Result_Relevant = interactiveTestInfo.getTestResults();
			StatisticsInteractive statis = new StatisticsInteractive();

			/*Set<String> keys2 =  result.keySet();
            List<String> keysAux = new ArrayList<String>();
            for(String key:keys2){
                keysAux.add(key);
            }
        Collections.sort(keysAux,new RunComparator());
			List<MeasureSet>  resultAux3;
			TopicResult[] topicAux3;
			TopicResult topicAux4 = new TopicResult("A");
			//HashMap<String, Double> teste;
			//double average = 0;
			for(String key:keysAux){
				resultAux3 = result.get(key);
				//List<MeasureSet>  resultAux2 = new ArrayList<>();
				System.out.println("Sistema " + key);
				for(MeasureSet set: resultAux3){
					System.out.println("MeasureName " + set.getMeasureName());
					topicAux3 = set.getTopicResults();
					int i = 0;
					for(TopicResult topic: topicAux3){
						//average += topic.getValues().get(0).getValue();
						System.out.println("Confirmação:" + topic.getValues().size());
						System.out.println("Topico: " + topic.getTopicName() +" NomeValor: " + topic.getValues().get(0).getX() +" Valor: "+topic.getValues().get(0).getValue());
						i++;
					}
				}
				//resultAux2.get(0).addTopicResult("A");
				//resultAux2.get(0).addValue("A",average);
				//resultAverage.put(key,resultAux2);
			}*/
			System.out.println("++++++++++++++++++++Conferindo a média++++++++++++++++++++++++++++");
		/*Set<String> keys3 =  resultAverage.keySet();
		List<String> keysAux = new ArrayList<String>();
		for(String key:keys3){
		    keysAux.add(key);
        }
		Collections.sort(keysAux,new RunComparator());
		List<MeasureSet>  resultAux4;
		TopicResult[] topicAux5;
		//TopicResult topicAux4 = new TopicResult("A");
		//HashMap<String, Double> teste;
		//double average = 0;
        /*System.out.println("Sem ordenar");
        for(String key:keys3){
            System.out.println(key);
        }
        System.out.println("Ordenado");
        for(String key:keysAux){
            System.out.println(key);
        }
		for(String key:keysAux){
			resultAux4 = resultAverage.get(key);
			//List<MeasureSet>  resultAux2 = new ArrayList<>();
			System.out.println("Sistema " + key);
			for(MeasureSet set: resultAux4){
				System.out.println("MeasureName " + set.getMeasureName());
				topicAux5 = set.getTopicResults();
				int i = 0;
				for(TopicResult topic: topicAux5){
					//average += topic.getValues().get(0).getValue();
					System.out.println("Confirmação:" + topic.getValues().size());
					System.out.println("Topico: " + topic.getTopicName() +" NomeValor: " + topic.getValues().get(0).getX() +" Valor: "+topic.getValues().get(0).getValue());
					i++;
				}
			}
			//resultAux2.get(0).addTopicResult("A");
			//resultAux2.get(0).addValue("A",average);
			//resultAverage.put(key,resultAux2);
		}*/

			//List<Boolean> results = parser(Result_Relevant);

			/*for(int i =0;i<Result_Relevant.size();i++){
				System.out.println("----"+ i +"----");
				for(boolean item: Result_Relevant.get(i)){
					System.out.println(item);
				}
			}*/

			//System.out.println(result.size());
			/*for(int i = 0;i < result.size();i++){
				System.out.println("indice" + i);
				for(Object item:result.get(i)) {
					System.out.println(item);
				}
			}
			System.out.println("---testando---");
			for(Object item:temp){
				System.out.println(item);
			}*/
			//Criar uma sobreposição para esse método
			Graph graph = this.graphController.create(type, resultAverage, measures.size(), title, name, x_axis, y_axis, graphType);

			GraphInfo info = new GraphInfo(measures, runs, topics, atValue, xValues,interactiveTestInfo,true);

        /*HashMap<Integer,boolean[]>	Result_Relevant2 = new HashMap<Integer,boolean[]>();
        //Result_RelevantSense = new HashMap<Integer,boolean[]>();
        boolean[] test0 = new boolean[4];
        boolean[] test1 = new boolean[4];
        boolean[] test2 = new boolean[4];
        boolean[] test3 = new boolean[4];
        test0[0] = false;
        test0[1] = true;
        test0[2] = false;
        test0[3] = true;

        test1[0] = false;
        test1[1] = true;
        test1[2] = true;
        test1[3] = false;

        test2[0] = true;
        test2[1] = true;
        test2[2] = false;
        test2[3] = false;

        /*teste3[0] = false;
        teste3[1] = false;
        teste3[2] = true;
        teste3[3] = true;
        Result_Relevant2.put(0,test0);
        Result_Relevant2.put(1,test1);
        Result_Relevant2.put(2,test2);
        Result_Relevant.put(3,teste3);*/

			graph.setGraphInfo(info);
			this.projectController.addGraph(graph);
			//int base = statis.baseline(baseline,runs);
			return graph.makeInteractive(Result_Relevant,baseline,interactiveTestInfo.getTestResultsRelevance());

    }

    //responsavel por chamar a classe principal dos testes estatisticos.
    /*
    * @param result resultados das medidas por iteração de cada sistema.
    * @param runs1 lista de sistemas do projeto
    *
    * @return os resultados das iterações divididos por sistema.
     */
    public List statistcsTestInteractive(HashMap<String, List<MeasureSet>> result, List<String> runs1) throws IOException, InvalidItemNameException, ItemNotFoundException, NoGraphItemException, EmptyTopicMeasureValueException, InterruptedException, MeasureNotFoundException, TopicNotFoundException, QrelItemNotFoundException, InvalidQrelFormatException {

        String old_Run = null;
        String old_RunAux = null;
        double[] auxiliar;
        int position = 0;
        String temp;
        int contPosition = 0;
        List<List<double[]>> results = new ArrayList<List<double[]>>();
        List<String> runsAux = new ArrayList<String>();
        //runsAux.addAll(runs);

        /*Set<String> keys = result.keySet();
        for(String key:keys){
            System.out.println(key);
        }*/
        List<String> runsAux1 = new ArrayList<String>();
        /*for(String run: runs1){
            String runName = run.substring(0,4);
            String iteration = run.substring(run.lastIndexOf("_"));
            String aux = runName + iteration;
            System.out.println(aux);
            runsAux1.add(aux);
        }*/
        Collections.sort(runsAux1, new RunComparator());
        /*System.out.println("Ordenado");
        for(String run: runsAux1){
            System.out.println(run);
        }*/
        List<List<String>> tempo = new ArrayList<>();
        Collections.sort(runs1, new RunComparator());

        //Entra na lista de runs separadas por interações
        for(String run:runs1){
            String aux = run.substring(0,4);
            //Confere se as interações pertencem a mesma run.
            if(aux.equalsIgnoreCase(old_Run)) {
                if(!old_Run.equalsIgnoreCase(old_RunAux) && old_RunAux != null){
                    position++;
                }
                else if(old_RunAux == null){
                    position = 0;
                }
                else{

                }
                //auxiliar = calculeValue(run,topics,measure,atValue,xvalues);
				auxiliar = filterTopics(result.get(run));
                //System.out.println("Run: " + run + "Valor: " + auxiliar[0]);
                //position = Integer.parseInt(aux.substring(aux.length()-1));
                results.get(position).add(auxiliar);
                tempo.get(position).add(run);
                old_RunAux = old_Run;
            }
            //Caso não pertença, cria outra celula para a nova run, e a antiga run passa a ser essa.
            else{
                int test;
                List<double[]> temp2 = new ArrayList<double[]>();
                List<String> temp3 = new ArrayList<String>();
                //calcula o valor para a interação
                //auxiliar = calculeValue(run,topics,measure,atValue,xvalues);
				auxiliar = filterTopics(result.get(run));
                //verifica qual é a interação para coloca-lá na sua devida posição, na lista de interações de um sistema.
                //position = Integer.parseInt(aux.substring(aux.length()-1));
                //System.out.println("Run: " + run + "Valor: " + auxiliar[0]);
                temp2.add(auxiliar);
                temp3.add(run);
                results.add(contPosition,temp2);
                tempo.add(contPosition,temp3);
                old_Run = aux;
                contPosition++;
            }
        }

        /*for(List<double[]> item:results){
            for(double[] indice: item){
                System.out.println("++++");
                for(int i =0;i<indice.length;i++){
                    System.out.println("indice: " + i + " = " + indice[i]);
                }
            }
            System.out.println("-------------------------------");
        }


        for(List<String> run_:tempo){
            for(String run:run_){
                System.out.println(run);
            }
            System.out.println("-----------");
        }*/

        return results;

    }


    //responsavel por calcular a significancia estatistica por interação sendo comparado com o baseline.
    /*
    * Classe responsável por distinguir qual teste será feito, de acordo com a escolha do usuário;
    * @param results lista de resultados para a medida solicitada, para cada interação de uma lista de sistemas.
    * @param test o tipo de teste estatistico que irá ser feito.
    * @param typeSelected a biblioteca que irá ser utilizada como base para o calculo estatistico.
    * @param baseline system que irá ser usado como base para calcular a relevancia dos outros sistemas em relação a ele.
    * @param runs lista de sistemas do projeto
    * @param alphaD nível de significância
    * @return um map que determina quais interações tem relevancia em relação as interações do baseline, sendo para cada sistema existe um array de boolean
    * onde as posições estão relacionadas com as interações;
     */
    public StatisticsIteractiveInfo valueTetsInteractive(List<List> results,String test, String typeSelected, String baseline,List<String> runs,double aplhaD,HashMap<String, Integer> runsIteration) throws IOException, InterruptedException {

        StatisticsInteractive statistics = new StatisticsInteractive();
        StatisticsIteractiveInfo info = null;
        //Tem que separar por iteração.
        /*List<String> auxiliar = new ArrayList<String>();
        String auxiliar2;
        String baseline_test = baseline.substring(0,4);
        for(String run_:runs){
            auxiliar2 = run_.substring(0,4);
            if(!auxiliar.contains(auxiliar2)) {
                auxiliar.add(auxiliar2);
            }
        }*/
		System.out.println("Base position 1 " + statistics.baseline(baseline,runs));
        List value1 = (List) results.get(statistics.baseline(baseline,runs));

        List value2 = new ArrayList();
        HashMap<Integer,boolean[]> values = null;
		value2 = results;

        //Tirando o baseline do resto das runs
        /*for(List result:results){
            if(result != value1){
                value2.add(result);
            }
        }*/
        if(test.equalsIgnoreCase("Wilcoxon")) {
            info = statistics.FilterwilcoxonTest(value2, value1, typeSelected, this, aplhaD,baseline,runs,runsIteration);
        }
        else if(test.equalsIgnoreCase("Mann Whitney U")){
            info = statistics.FilterMannWhitneyUTest(value2,value1,this,aplhaD);
        }
        else{
            info = statistics.FilterTStudentTest(value2, value1, typeSelected, this, aplhaD);
        }

        return info;
    }

	/*
	 * @param result resultado de um sistema.
	 *
	 * @return
	 */
    public double[] filterTopics(List<MeasureSet> result) throws EmptyTopicMeasureValueException {

		double [] values = null;//values Y

		TopicResult [] tmv = result.get(0).getTopicResults();

		int index, length;
		index = length = 0;
		length = tmv.length * tmv[0].getValues().size();

		values = new double[length];

		for(TopicResult tm : tmv){

			List<MeasureValue>  mValues = tm.getValues();

			for(MeasureValue v : mValues){
			    values[index] = v.getValue();
				index++;
			}
		}
		return values;

	}

	public List<Boolean> parser(HashMap<Integer, boolean[]> results){
        List temp =  new ArrayList();
        for(int i = 0;i < results.size();i++){
            for(Boolean item:results.get(i)) {
                temp.add(item);
            }
        }
        return temp;
    }

    public ProjectController getProjectController() {
        return projectController;
    }
}