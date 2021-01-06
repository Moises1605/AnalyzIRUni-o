package br.uefs.analyzIR.graph.bars;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import br.uefs.analyzIR.RunComparator;
import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.exception.NoGraphPointException;
import br.uefs.analyzIR.graph.GraphItem;
import br.uefs.analyzIR.graph.statisticsTestInteractive.Colors;
import br.uefs.analyzIR.graph.statisticsTestInteractive.PaintPoints;
import org.jfree.chart.*;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import br.uefs.analyzIR.graph.Graph;
import br.uefs.analyzIR.graph.GraphExporter;
import br.uefs.analyzIR.graph.Point;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Presents grouped data with rectangular bars with lengths proportional to the values that they represent. 
 * @author lucas
 *
 */
@XStreamAlias("BarGraph")
public class BarGraph extends Graph {

	private List<Color> colors;

	public BarGraph(String title, String labelX, String labelY, String name) {
		super(title, labelX, labelY, name);
		this.colors = new ArrayList<Color>();
		initColors();
	}

	/**
	 * 
	 */
	@Override
	public JPanel make() throws NoGraphItemException, NoGraphPointException {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		initDataset(dataset);

		JFreeChart chart = ChartFactory.createBarChart(getTitle(), getLabelX(),
				getLabelY(), dataset, PlotOrientation.VERTICAL, true, true,
				false);

		System.out.println("oi");
		chart.setBackgroundPaint(Color.WHITE);
		chart.getPlot().setBackgroundPaint(Color.WHITE);
	
		
		CategoryPlot category = chart.getCategoryPlot();
		BarRenderer render = (BarRenderer)category.getRenderer();
		CategoryItemRenderer item = category.getRenderer();
		
		renderColorItem(item);
		render.setMaximumBarWidth(0.15);
		
		item.setBaseItemLabelGenerator(new  StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.0000")));//por item do gráfico
		item.setBaseItemLabelsVisible(true);
		
		ChartPanel panel = new ChartPanel(chart, true);
		
		return panel;
	}
	//Método utilizado em graficos com testes estatísticos.
	/**
	 * Creates a JPanel object to be used for an user interface. Overlay used for graphics with statistical tests
	 * @param Result_RelevantSense lista com os resultados para colocaração no gráfico levando em conta o valor encontrado no cálculo das medidas
	 * @param baseline nome da baseline do teste estatístico
	 * @param Result_Relevant lista com os resultados dos testes estatísticos
	 * @throws NoGraphPointException if chart is empty
	 * @return a chart visualization
	 */
	@Override
		public JPanel makeInteractive(HashMap<Integer,boolean[]> Result_Relevant,String baseline,HashMap<Integer,boolean[]> Result_RelevantSense) throws NoGraphItemException, NoGraphPointException {

		/*Result_Relevant = new HashMap<Integer,boolean[]>();
		Result_RelevantSense = new HashMap<Integer,boolean[]>();
		boolean[] teste = new boolean[1];
		boolean[] teste1 = new boolean[1];
		boolean[] teste2 = new boolean[1];
		boolean[] teste3 = new boolean[1];
		teste[0] = true;

		teste1[0] = true;

		teste2[0] = true;

		teste3[0] = false;

		Result_Relevant.put(0,teste);
		Result_Relevant.put(1,teste1);
		Result_RelevantSense.put(0,teste2);
		Result_RelevantSense.put(1,teste3);*/

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			initDataset(dataset);

			JFreeChart chart = ChartFactory.createBarChart(getTitle(), getLabelX(),
					getLabelY(), dataset, PlotOrientation.VERTICAL, true, true,
					false);

			chart.setBackgroundPaint(Color.WHITE);
			chart.getPlot().setBackgroundPaint(Color.WHITE);

			Colors listColors =  new Colors();
			ArrayList<Color> colorsLine = listColors.getColors();
			this.colors = listColors.getColors();

			CategoryPlot category = chart.getCategoryPlot();
			BarRenderer render = (BarRenderer)category.getRenderer();

			CategoryItemRenderer item = category.getRenderer();
			renderColorItem(item);
			LegendItemCollection legends = new LegendItemCollection();
			System.out.println("Base position 2 "+ this.returnBaseline(baseline));
			for(int i = 0; i < dataset.getRowCount(); i++){
				LegendItem legend = null;
				if(i != this.returnBaseline(baseline)){
					System.out.println("Indice: " + i);
					if(Result_Relevant.get(i)[0]){
						legend = new LegendItem(this.getItems().get(i).getName() + "*");
						legend.setFillPaint(colorsLine.get(i));
						if(Result_RelevantSense.get(i)[0]){
							item.setSeriesItemLabelPaint(i,Color.BLUE);
						}else{
							item.setSeriesItemLabelPaint(i,Color.RED);
						}

					}
					else{
						legend = new LegendItem(this.getItems().get(i).getName());
						legend.setFillPaint(colorsLine.get(i));
						item.setSeriesPaint(i,colorsLine.get(i));
					}
				}else{
					legend = new LegendItem(this.getItems().get(i).getName());
					legend.setFillPaint(colorsLine.get(i));
					item.setSeriesPaint(i,colorsLine.get(i));
				}
				legends.add(legend);
			}

			category.setFixedLegendItems(legends);


			render.setMaximumBarWidth(0.15);

			item.setBaseItemLabelGenerator(new  StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.0000")));//por item do gráfico
			item.setBaseItemLabelsVisible(true);

			ChartPanel panel = new ChartPanel(chart, true);

			return panel;

		}

	/**
	 * 
	 */
	@Override
	public JPanel makeByChartItem(String nameBase) throws GraphItemNotFoundException,
			NoGraphItemException, NoGraphPointException {
	
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		initDataset(dataset, nameBase);

		JFreeChart chart = ChartFactory.createBarChart(getTitle(), getLabelX(),
				getLabelY(), dataset, PlotOrientation.VERTICAL, true, true,
				false);
	
		chart.setBackgroundPaint(Color.WHITE);
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		CategoryPlot category = chart.getCategoryPlot();
		CategoryItemRenderer item = category.getRenderer();
		
		BarRenderer render = (BarRenderer)category.getRenderer();
		render.setMaximumBarWidth(0.15);
		renderColorItem(item);
		
		ChartPanel panel = new ChartPanel(chart, true);
		return panel;
	}

	/**
	 * 
	 */
	@Override
	public void exportAs(String url, String name, int width, int height,
			String format) throws IOException, NoGraphItemException, NoGraphPointException {
		
		GraphExporter exporter = new GraphExporter(name, url, this);
		if (format.equals(".jpg")){

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			initDataset(dataset);

			JFreeChart chart = ChartFactory.createBarChart(getTitle(), getLabelX(),
					getLabelY(), dataset, PlotOrientation.VERTICAL, true, true,
					false);
			
			chart.setBackgroundPaint(Color.WHITE);
			chart.getPlot().setBackgroundPaint(Color.WHITE);
		
			
			CategoryPlot category = chart.getCategoryPlot();
			BarRenderer render = (BarRenderer)category.getRenderer();
			CategoryItemRenderer item = category.getRenderer();
			
			renderColorItem(item);
			render.setMaximumBarWidth(0.15);
			
			item.setBaseItemLabelGenerator(new  StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.0000")));//por item do gráfico
			item.setBaseItemLabelsVisible(true);
			

			String tempURL = url + File.separator + name;

			File file = new File(tempURL);
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream fos = new FileOutputStream(file);
			
			ChartUtilities.writeChartAsJPEG(fos, chart, width, height);
				
			fos.close();
		}else if (format.equals(".png")){
			exporter.exportToPNG(width, height);
		}else if(format.equals(".xls")){
			exporter.exportToXLS();
		}else{
			
		}	
	}

	private void initDataset(DefaultCategoryDataset dataset)
			throws NoGraphItemException, NoGraphPointException {
		List<GraphItem> bases = getItems();
		//adicionei comparator para ordernar as runs
		RunComparator comparator = new RunComparator();
		Collections.sort(bases, comparator);
		for (int i = 0; bases != null && i < bases.size(); i++) {
			GraphItem plotItem = bases.get(i);
			Point[] points = plotItem.getPoints();
			for (int j = 0; j < points.length; j++) {
				dataset.addValue(Double.parseDouble(points[j].getY()),
						plotItem.getName(), points[j].getX());
			}
		}
	}

	private void initDataset(DefaultCategoryDataset dataset, String nameBase)
			throws GraphItemNotFoundException, NoGraphItemException, NoGraphPointException {
		GraphItem plotItem = getGraphItem(nameBase);
		Point[] points = plotItem.getPoints();
		for (int i = 0; i < points.length; i++) {
			dataset.addValue(Double.parseDouble(points[i].getY()),
					plotItem.getName(), points[i].getX());
		}
	}

	private void initColors() {
		
		colors.add(Color.orange);
		colors.add(Color.red);
		colors.add(Color.blue);
		colors.add(Color.cyan);
		colors.add(Color.pink);
		colors.add(Color.green);
		colors.add(Color.yellow);
		colors.add(Color.lightGray);
		colors.add(Color.magenta);
		colors.add(Color.darkGray);
	
	}
	
	private void initColors(int tam){
		
		for(int i = 0; i < tam; i++){
			Color color = new Color(5*i);
			colors.add(color);
		}
	}

	private void renderColorItem(CategoryItemRenderer render) {

		List<GraphItem> items = getItems();
		initColors(items.size());
		boolean[] checked = new boolean[items.size()];
		int series = 0;
		int colorIndex = 0;

		for (GraphItem i : items) {
			if (!checked[series]) {

				int seriesLoop = 0;
				String[] values = i.getName().split(":");
				String begin = values[0];
				
				for (GraphItem j : items) {
					if (!checked[seriesLoop] && j.getName().contains(begin)) {
						render.setSeriesPaint(seriesLoop,colors.get(colorIndex));
						checked[seriesLoop] = true;
					}
					seriesLoop++;
				}
				colorIndex++;
			}
			series++;
		}
	}
}
