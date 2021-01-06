package br.uefs.analyzIR.failureAnalysis.graph;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Wanderson on 29/09/16.
 */
public class FRelStackedBarChart extends JFrame {

	/**
	 * Creates a new demo.
	 *
	 * @param title
	 *            the frame title.
	 */
	private ArrayList allFrelList;
	private List<String> runsName;

	public FRelStackedBarChart(final String title, ArrayList frelList, List<String> runsName) {
		super(title);
		this.allFrelList = frelList;
		this.runsName = runsName;
		final CategoryDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(590, 350));
		setContentPane(chartPanel);
	}

	public static void show(ArrayList<ArrayList> frelList, List<String> runsName) {
		final FRelStackedBarChart frame = new FRelStackedBarChart("F_Rel", frelList, runsName);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}

	/**
	 * Creates a sample dataset.
	 *
	 * @return A sample dataset.
	 */
	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		for (int j = 0; j < allFrelList.size(); j++) {
			ArrayList frel = (ArrayList) allFrelList.get(j);
			for (int i = 0; i < frel.size(); i++) {
				result.addValue(1, "celula" + i + " " + j, runsName.get(j));
			}
		}

		return result;
	}

	/**
	 * Creates a sample chart.
	 *
	 * @return A sample chart.
	 */
	private JFreeChart createChart(CategoryDataset dataset) {

		final JFreeChart chart = ChartFactory.createStackedBarChart("F_REL Graph", // chart
																					// title
				"", // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // the plot orientation
				true, // legend
				true, // tooltips
				false // urls
		);

		StackedBarRenderer renderer = frelRender();

		renderer.setMaximumBarWidth(.1);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.getRangeAxis().setInverted(true);
		plot.getRangeAxis().setLabel("Rank depth");
		plot.setRenderer(renderer);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		plot.setFixedLegendItems(createLegendItems());

		return chart;
	}

	/**
     * Creates the legend items for the chart.  In this case, we set them manually because we
     * only want legend items for a subset of the data series.
     *
     * @return The legend items.
     */
    private StackedBarRenderer frelRender() {

        StackedBarRenderer renderer = new GroupedStackedBarRenderer();

        ArrayList<Integer> relevanceValues = new ArrayList<>();
        BarColor barColor = new BarColor();


        for (int j = 0; j < allFrelList.size(); j++) {
            ArrayList frel = (ArrayList) allFrelList.get(j);

            for (int a = 0; a < frel.size(); a++) {
                if (!relevanceValues.contains(frel.get(a)))
                    relevanceValues.add((Integer) frel.get(a));
            }

            Collections.sort(relevanceValues);
            int step = relevanceValues.size()/5;
            int resto = relevanceValues.size() % 5;
            int size = relevanceValues.size() -1;
         

            //paint the bars
            for (int i = 0; i < frel.size(); i++) {

                
            	switch (relevanceValues.size()) {
            	//binary relevance
            	case 2: 
                    if ((Integer) frel.get(i) <= 0)
                        renderer.setSeriesPaint(i + (j * frel.size()), Color.darkGray);
                    else if (frel.get(i) == relevanceValues.get(1))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(4));
                    else
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(0));
                    break;

                case 3:
                    if ((Integer) frel.get(i) <= 0)
                        renderer.setSeriesPaint(i + (j * frel.size()), Color.darkGray);
                    else if (frel.get(i) == relevanceValues.get(0))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(0));
                    else if (frel.get(i) == relevanceValues.get(1))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(2));
                    else if (frel.get(i) == relevanceValues.get(2))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(4));
                    break;
                    
                 case 4: 
                    if ((Integer) frel.get(i) <= 0)
                        renderer.setSeriesPaint(i + (j * frel.size()), Color.darkGray);
                    else if (frel.get(i) == relevanceValues.get(0))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(0));
                    else if (frel.get(i) == relevanceValues.get(1))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(1));
                    else if (frel.get(i) == relevanceValues.get(2))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(2));
                    else if (frel.get(i) == relevanceValues.get(3))
                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(3));
                    break;
                    
                  default :
                  
                    if ((Integer) frel.get(i) <= 0)
                        renderer.setSeriesPaint(i + (j * frel.size()), Color.darkGray);
                    	
                    else if ((Integer) frel.get(i) <= relevanceValues.get(size) &&
                            (Integer) frel.get(i) > relevanceValues.get(size-step))

                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(4));

                    else if ((Integer) frel.get(i) <= relevanceValues.get(size - step) &&
                            (Integer) frel.get(i) > relevanceValues.get(size - (step*2)))

                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(3));

                    else if ((Integer) frel.get(i) <= relevanceValues.get(size - (step*2)) &&
                            (Integer) frel.get(i) > relevanceValues.get(size-(step*3)))

                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(2));

                    else if ((Integer) frel.get(i) <= relevanceValues.get(size - (step*3)) &&
                            (Integer) frel.get(i) > relevanceValues.get(size -(step*4)))

                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(1));

                    else if ((Integer) frel.get(i) <= relevanceValues.get(size- (step*4)))

                        renderer.setSeriesPaint(i + (j * frel.size()), barColor.getBlueColor().get(0));
                   break;
                	
            	}
            	
                renderer.setSeriesItemLabelGenerator(i + (j * frel.size()), new StandardCategoryItemLabelGenerator(frel.get(i).toString(), new DecimalFormat()));
                renderer.setSeriesItemLabelsVisible(i + (j * frel.size()), true);
            	
            }   
        }
        return renderer;
    }

	private LegendItemCollection createLegendItems() {
		LegendItemCollection result = new LegendItemCollection();

		LegendItem item1 = new LegendItem("RELEVANT", new Color(51, 0, 153));
		LegendItem item2 = new LegendItem("A LITTLE RELEVANT", new Color(51, 153, 255));
		LegendItem item3 = new LegendItem("NO RELEVANT", Color.darkGray);

		result.add(item1);
		result.add(item2);
		result.add(item3);
		return result;
	}
}
