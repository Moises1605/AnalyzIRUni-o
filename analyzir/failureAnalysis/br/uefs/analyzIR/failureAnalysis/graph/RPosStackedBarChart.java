package br.uefs.analyzIR.failureAnalysis.graph;

/**
 * Created by Wanderson on 02/05/16.
 */
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * A simple demonstration application showing how to create a stacked bar chart
 * using data from a {@link CategoryDataset}.
 */
public class RPosStackedBarChart extends JFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    private ArrayList allRposList;
    private List<String> runsName;

    public RPosStackedBarChart(final String title, ArrayList rposList, List<String> runsName) {
        super(title);
        this.allRposList = rposList;
        this.runsName = runsName;
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(590, 350));
        setContentPane(chartPanel);
    }

    public static void show(ArrayList<ArrayList> rposList, List<String> runsName) {
        final RPosStackedBarChart frame = new RPosStackedBarChart("R_POS", rposList, runsName);
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

        for (int j = 0; j < allRposList.size(); j++) {
            ArrayList rpos = (ArrayList) allRposList.get(j);
            for (int i = 0; i < rpos.size(); i++) {
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

        final JFreeChart chart = ChartFactory.createStackedBarChart(
                "R_POS Graph",  // chart title
                "",                  // domain axis label
                "Value",                     // range axis label
                dataset,                     // data
                PlotOrientation.VERTICAL,    // the plot orientation
                true,                        // legend
                true,                        // tooltips
                false                        // urls
        );

        StackedBarRenderer renderer = new GroupedStackedBarRenderer();


        for (int j = 0; j < allRposList.size(); j++) {
            ArrayList rpos = (ArrayList) allRposList.get(j);

            for (int i = 0; i < rpos.size(); i++) {

                if ((double) rpos.get(i) == 0) {
                    renderer.setSeriesPaint(i + (j * rpos.size()), Color.green);

                } else if ((double) rpos.get(i) < 0) {
                    renderer.setSeriesPaint(i + (j * rpos.size()), Color.blue);
                    renderer.setSeriesItemLabelGenerator(i + (j * rpos.size()), new StandardCategoryItemLabelGenerator(rpos.get(i).toString(),new DecimalFormat()));
                    renderer.setSeriesItemLabelsVisible(i + (j * rpos.size()), true);

                } else {
                    renderer.setSeriesPaint(i + (j * rpos.size()), Color.red);
                    renderer.setSeriesItemLabelGenerator(i + (j * rpos.size()), new StandardCategoryItemLabelGenerator(rpos.get(i).toString(),new DecimalFormat()));
                    renderer.setSeriesItemLabelsVisible(i + (j * rpos.size()), true);
                }
            }
        }
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
    private LegendItemCollection createLegendItems() {
        LegendItemCollection result = new LegendItemCollection();

        LegendItem item1 = new LegendItem("OK", Color.GREEN);
        LegendItem item2 = new LegendItem("Below", Color.BLUE);
        LegendItem item3 = new LegendItem("Above", Color.RED);

        result.add(item1);
        result.add(item2);
        result.add(item3);
        return result;
    }
}