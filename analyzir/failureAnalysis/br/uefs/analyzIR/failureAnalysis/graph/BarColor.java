package br.uefs.analyzIR.failureAnalysis.graph;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Wanderson on 07/11/16.
 */
public class BarColor {

    public BarColor(){}


    public ArrayList<Color> getRedColor(){
        ArrayList<Color> colors = new ArrayList();

        colors.add(new Color(255,160,122));
        colors.add(new Color(233,150,122));
        colors.add(new Color(255,127,80));
        colors.add(new Color(255,99,71));
        colors.add(new Color(255, 0,0));

        return colors;
    }


    public ArrayList<Color> getBlueColor(){
        ArrayList<Color> colors = new ArrayList();


        colors.add(new Color(51,153,255));
        colors.add(new Color(51,102,255));
        colors.add(new Color(51,51,225));
        colors.add(new Color(51,0,255));
        colors.add(new Color(51,0,153));


        return colors;
    }

}
