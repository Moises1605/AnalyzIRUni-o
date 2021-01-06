package br.uefs.analyzIR.graph.curves;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import br.uefs.analyzIR.RunComparator;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.exception.NoGraphPointException;
import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.statisticsTestInteractive.Colors;
import br.uefs.analyzIR.graph.statisticsTestInteractive.PaintPoints;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import br.uefs.analyzIR.graph.GraphExporter;
import br.uefs.analyzIR.graph.GraphItem;
import br.uefs.analyzIR.graph.Point;


/**
 * Displays information as a series of data points called 'markers' connected by straight line segments.
 * */
@XStreamAlias("CurveGraph")
public class CurveGraph extends Graph {


	/**
	 * Constructs a curve graph representation.
	 * @param title graph title
	 * @param labelX x axis title
	 * @param labelY y axis title
	 * @param name graph name file
	 */
	public CurveGraph(String title, String labelX, String labelY, String name) {
		super(title, labelX, labelY, name);
	}

	/**
	 * Creates a JPanel object to be used for an user interface. Overlay used for graphics with statistical tests
	 * @param Result_RelevantSense lista com os resultados para colocaração no gráfico levando em conta o valor encontrado no cálculo das medidas
	 * @param baseline nome da baseline do teste estatístico
	 * @param Result_Relevant lista com os resultados dos testes estatísticos
	 * @throws NoGraphPointException if chart is empty
	 * @return a chart visualization
	 */
	//@Override
	public JPanel makeInteractive(HashMap<Integer,boolean[]> Result_Relevant,String baseline,HashMap<Integer,boolean[]> Result_RelevantSense) throws NoGraphItemException, NoGraphPointException {

		XYSeriesCollection dataset = new XYSeriesCollection();
		initDataset(dataset);

		JFreeChart chart = ChartFactory.createXYLineChart(getTitle(),
				getLabelX(), getLabelY(), dataset, PlotOrientation.VERTICAL,
				true, true, false);

		chart.setBackgroundPaint(Color.WHITE);		
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		Colors listColors =  new Colors();
		ArrayList<Color> colorsLine = listColors.getColors();
		XYPlot plot = (XYPlot) chart.getPlot();
		//XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer renderer = new PaintPoints(dataset,colorsLine,Result_Relevant,this.returnBaseline(baseline),Result_RelevantSense);
        LegendItemCollection legends = new LegendItemCollection();

        for(int i = 0; i < dataset.getSeriesCount(); i++){
			  renderer.setSeriesLinesVisible(i, true);
		      renderer.setSeriesShapesVisible(i, true);
		      LegendItem legend = new LegendItem(this.getItems().get(i).getName());
		      legend.setFillPaint(colorsLine.get(i));
		      legends.add(legend);
		}
		plot.setFixedLegendItems(legends);
		/*for(int i = 0; i < plot.getFixedLegendItems().getItemCount();i++){
			plot.getFixedLegendItems().get(i).setFillPaint(colorsLine.get(i));
		}*/
		plot.setRenderer(renderer);
		/*if(renderer.getLegendItems() == null || renderer.getLegendItems().getItemCount() ==0){
			System.out.println("deu ruim");
		}*/
		//renderer.getLegendItems().get(0).
		/*for(int i =0;i < renderer.getLegendItems().getItemCount();i++){
			renderer.getLegendItems().get(i).setLabelPaint(colorsLine.get(i));
			renderer.getLegendItems().get(i).setFillPaint(colorsLine.get(i));
		}*/


		ChartPanel panel = new ChartPanel(chart, true);
		
		return panel;
	}

    /**
     * Creates a JPanel object to be used for an user interface.
     * @throws NoGraphPointException if chart is empty
     * @return a chart visualization
     */
    @Override
    public JPanel make() throws NoGraphItemException, NoGraphPointException {

        XYSeriesCollection dataset = new XYSeriesCollection();
        initDataset(dataset);

        JFreeChart chart = ChartFactory.createXYLineChart(getTitle(),
                getLabelX(), getLabelY(), dataset, PlotOrientation.VERTICAL,
                true, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for(int i = 0; i < dataset.getSeriesCount(); i++){
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, true);
        }
        plot.setRenderer(renderer);

        ChartPanel panel = new ChartPanel(chart, true);

        return panel;
    }

	/***
	 * Creates a JPanel object by an item to be used for an user interface.
	 * @param item item name
	 * @throws GraphItemNotFoundException if chart item is not found
	 * @throws NoGraphItemException if chart is empty
	 * @throws NoGraphPointException if an item is empty
	 */
	@Override
	public JPanel makeByChartItem(String item) throws GraphItemNotFoundException,
			NoGraphItemException, NoGraphPointException {

		XYSeriesCollection dataset = new XYSeriesCollection();
		initDataset(dataset, item); //init data set with all chart items

		//creates curve chart
		JFreeChart chart = ChartFactory.createXYLineChart(getTitle(),
				getLabelX(), getLabelY(), dataset, PlotOrientation.VERTICAL,
				true, true, false);

		chart.setBackgroundPaint(Color.WHITE);	
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		
		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		
		for(int i = 0; i < dataset.getSeriesCount(); i++){
			  renderer.setSeriesLinesVisible(i, true);
		      renderer.setSeriesShapesVisible(i, true);
		      renderer.setSeriesItemLabelsVisible(i, true);
		}
		plot.setRenderer(renderer);
		
		
		ChartPanel panel = new ChartPanel(chart, true);
		
		return panel;
	}

	/**
	 *
	 * @throws NoGraphPointException
	 * 
	 */
	@Override
	public void exportAs(String url, String name, int width, int height,
			String format) throws IOException, NoGraphItemException, NoGraphPointException {

		GraphExporter exporter = new GraphExporter(name, url, this);
		if (format.equals(".jpg")){
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			initDataset(dataset);

			JFreeChart chart = ChartFactory.createXYLineChart(getTitle(),
					getLabelX(), getLabelY(), dataset, PlotOrientation.VERTICAL,
					true, true, false);

			chart.setBackgroundPaint(Color.WHITE);		
			chart.getPlot().setBackgroundPaint(Color.WHITE);
			
			XYPlot plot = (XYPlot) chart.getPlot();
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			
			for(int i = 0; i < dataset.getSeriesCount(); i++){
				  renderer.setSeriesLinesVisible(i, true);
			      renderer.setSeriesShapesVisible(i, true);
			}

			plot.setRenderer(renderer);

			String tempURL = url;

			File file = new File(tempURL);
			if(!file.exists())
				file.createNewFile();
			OutputStream fos = new FileOutputStream(file);
			ChartUtilities.writeChartAsJPEG(fos, chart, 550, 400);
			fos.close();

		}else if (format.equals(".png")){
			exporter.exportToPNG(width, height);
		}else if(format.equals(".xls")){
			exporter.exportToXLS();
		}else{
			
		}	
			
			
		
	}


	/**
	 * Initializes a data set object with all chart items data.
	 * @param dataset FreeChart object represents a set of curves.
	 * @throws NoGraphItemException if chart is empty
	 * @throws NoGraphPointException if an item is empty
	 */
	private void initDataset(XYSeriesCollection dataset)
			throws NoGraphItemException, NoGraphPointException {

		List<GraphItem> items = this.getItems(); // gets all chart items
		//adicionei comparator para ordernar as runs
		RunComparator comparator = new RunComparator();
		Collections.sort(items, comparator);
		//repeat until items is not empty and i < size
		for (int i = 0; items != null && i < items.size(); i++) {
			GraphItem plotItem = items.get(i);
			Point[] points = plotItem.getPoints();

			//converting all chart item points in FreeChart point objects
			XYSeries serie = new XYSeries(plotItem.getName());
            System.out.println("Nome do plotItem" + plotItem.getName());

			for (int j = 0; j < points.length; j++) {
				serie.add(Double.parseDouble(points[j].getX()), Double.parseDouble(points[j].getY()));
			}

			dataset.addSeries(serie);// add all series (items) in data set.
		}
	}

	/**
	* Initializes a data set object with a specific item points.
	@param dataset FreeChart object represents a set of curves
	@param itemName chart item name
	@throws NoGraphItemException if chart is empty
	@throws NoGraphPointException if an item is empty
	* */
	private void initDataset(XYSeriesCollection dataset, String itemName)
			throws GraphItemNotFoundException, NoGraphItemException, NoGraphPointException {
		List<Object> teste = new ArrayList<Object>();

		GraphItem plotItem = getGraphItem(itemName);
		Point[] points = plotItem.getPoints();
		XYSeries serie = new XYSeries(plotItem.getName());
		for(int i = 0; i < points.length; i++){
			serie.add(Double.parseDouble(points[i].getX()),Double.parseDouble(points[i].getY()));
		}
		dataset.addSeries(serie);
	}
	
}
