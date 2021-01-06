package Resources.heatMap;
import javax.swing.*;
import java.awt.*;

class HeatMapFrame extends JFrame
{
    HeatMap panel;

    public HeatMapFrame() throws Exception
    {
        super("Heat Map Frame");
        double[][] data = HeatMap.generateSinCosData(100);
        
        // you can use a pre-defined gradient:
        panel = new HeatMap(data, HeatMap.GRADIENT_BLUE_TO_RED);
        
        // or you can also make a custom gradient:
        Color[] gradientColors = new Color[]{Color.blue, Color.green, Color.yellow};
        Color[] customGradient = HeatMap.createMultiGradient(gradientColors, 500);
        panel.updateGradient(customGradient);
        
        // set miscelaneous settings
        panel.setDrawLegend(true);

        panel.setTitle("Height (m)");
        panel.setDrawTitle(true);

        panel.setXAxisTitle("X-Distance (m)");
        panel.setDrawXAxisTitle(true);

        panel.setYAxisTitle("Y-Distance (m)");
        panel.setDrawYAxisTitle(true);

        panel.setCoordinateBounds(0, 6.28, 0, 6.28);
        panel.setDrawXTicks(true);
        panel.setDrawYTicks(true);

        panel.setColorForeground(Color.black);
        panel.setColorBackground(Color.white);

        this.getContentPane().add(panel);
    }
    
    // this function will be run from the EDT
    private static void createAndShowGUI() throws Exception
    {
        HeatMapFrame hmf = new HeatMapFrame();
        hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hmf.setSize(500,500);
        hmf.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    createAndShowGUI();
                }
                catch (Exception e)
                {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }
}
