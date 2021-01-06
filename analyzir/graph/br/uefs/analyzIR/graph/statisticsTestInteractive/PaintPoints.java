package br.uefs.analyzIR.graph.statisticsTestInteractive;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author moises
 */
public class PaintPoints extends XYLineAndShapeRenderer {
    private XYSeriesCollection dataset;
    private ArrayList<Color> colorsLine;
    private HashMap<Integer,boolean[]> Result_Relevant;
    private int baseline;
    private HashMap<Integer,boolean[]> Result_RelevantSense;

    public PaintPoints(XYSeriesCollection dataset, ArrayList<Color> colorsLine, HashMap<Integer,boolean[]> Result_Relevant,int baseline,HashMap<Integer,boolean[]> Result_RelevantSense){
        this.dataset = dataset;
        this.colorsLine = colorsLine;
        this.baseline = baseline;
        //this.baseline = 1;
        /*this.Result_Relevant = new HashMap<Integer,boolean[]>();
        this.Result_RelevantSense = new HashMap<Integer,boolean[]>();
        boolean[] teste = new boolean[4];
        boolean[] teste1 = new boolean[4];
        boolean[] teste2 = new boolean[4];
        boolean[] teste3 = new boolean[4];
        teste[0] = false;
        teste[1] = true;
        teste[2] = false;
        teste[3] = true;

        teste1[0] = false;
        teste1[1] = true;
        teste1[2] = true;
        teste1[3] = false;

        teste2[0] = true;
        teste2[1] = true;
        teste2[2] = false;
        teste2[3] = false;

        teste3[0] = false;
        teste3[1] = false;
        teste3[2] = true;
        teste3[3] = true;
        this.Result_Relevant.put(0,teste);
        this.Result_Relevant.put(1,teste1);
        this.Result_RelevantSense.put(0,teste2);
        this.Result_RelevantSense.put(1,teste3);*/
        this.Result_Relevant = Result_Relevant;
        this.Result_RelevantSense = Result_RelevantSense;
    }

    @Override
    public Paint getItemPaint(int row, int col) {
        Paint cpaint = getItemColor(row, col);
        if (cpaint == null) {
            cpaint = super.getItemPaint(row, col);
        }
        return cpaint;
    }

    public Color getItemColor(int row, int col) {
        //System.out.println(col + "," + dataset.getY(row, col));
        int y = (int)dataset.getXValue(row , col);
        /*if(y<=1) return Color.black;
        if(y<=2) return Color.green;;
        if(y<=3) return Color.red;;
        if(y<=4) return Color.yellow;;
        if(y<=10) return Color.orange;;*/
        boolean[] Result;
        boolean[] ResultRelevanceSense;
        if(row == Result_Relevant.size()) {
            Result = this.Result_Relevant.get(row-1);
            ResultRelevanceSense = this.Result_RelevantSense.get(row - 1);
        }
        else {
            Result = this.Result_Relevant.get(row);
            ResultRelevanceSense = this.Result_RelevantSense.get(row);

        }
        /*for(boolean item: Result){
            System.out.println(item);
        }*/
        //System.out.println(Result.length);
        //System.out.println(row);
        //System.out.println(col);
        //System.out.println(Result[(y-1)]);*/
        //System.out.println(Result.length);
        /*Result[y-1]*/
        //Defina o limite da baseline se for maior que a baseline não faz a verificação de significancia
        if(row != baseline && (Result.length > col)) {

            if (Result[(col)]){
                if(ResultRelevanceSense[col]){
                    return Color.BLUE;
                }
                else{
                    return Color.RED;
                }

            } else {
                return colorsLine.get((row));
            }
        }
        else
            return colorsLine.get(row);
        //return Color.BLUE;//colorsLine.get(control);
    }

    @Override
    protected void drawFirstPassShape(Graphics2D g2, int pass, int series,
                                      int item, Shape shape) {
        //Colors listColors =  new Colors();
        //ArrayList<Color> colorsLine = listColors.getColors();
        //super.drawFirstPassShape(g2,pass,series,item,shape);

        g2.setStroke(getItemStroke(series, item));
        //Color c1 = getItemColor(series, item);
        //Color c2 = getItemColor(series, item - 1);
        //Color Paint;
        //GradientPaint linePaint = new GradientPaint(0, 0, c1, 0, 300, c2);
        //g2.setPaint(linePaint);
        //Colocar a classe que retornar as cores das linhas aqui.

        g2.setPaint(this.colorsLine.get(series));
        g2.draw(shape);
        //this.series = series;
    }

}
