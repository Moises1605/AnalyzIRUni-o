package br.uefs.analyzIR.ui.common;

/**
 * Created by Wanderson on 15/10/16.
 */
import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.MeasureNotFoundException;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class MeasureDescriptionView {

    private JPanel panel;
    private JComboBox<String> comboBox;
    private JButton button;
    private JEditorPane text;
    private JScrollPane scroll;
    private Font font;
    private JDialog frame;
    private List<String> measures;
    private Facade facade;

    public MeasureDescriptionView(Facade facade){this.facade = facade;}

    public void show() {
        loadInfo();
        initComponents();
        initLayout();
        initFunction();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    private void initComponents() {

        frame = new JDialog();
        frame.setTitle("Measure Description");
        this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

        this.button = new JButton("Ok");

        this.text = new JEditorPane();
        text.setBackground(Color.BLACK);
        text.setContentType("text/html");
        text.setEditable(false);
        text.setAutoscrolls(true);



        this.scroll = new JScrollPane(text);
        scroll.setViewportView(text);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.comboBox = new JComboBox<>();
        comboBox.addItem("-- Select an measure --");
        for(String m: measures){
            comboBox.addItem(m);
        }

        button.setFont(font);
        comboBox.setFont(font);
        text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

    }

    private void initLayout(){

        String rows = "10dlu, 20dlu, 300dlu, 10dlu";
        String columns = "10dlu, 50dlu, 30dlu, 100dlu, 30dlu, 40dlu, 50dlu, 10dlu";

        FormLayout form = new FormLayout(columns, rows);
        PanelBuilder builder = new PanelBuilder(form);

        builder.add(comboBox,CC.xy(4,2));
        builder.add(button,CC.xy(5,2));
        builder.add(text,CC.xywh(2,3,6,2));
       // builder.add(scroll, CC.xywh(8,2,1,3 ));
        frame.setContentPane(builder.getPanel());

    }

    public void loadInfo(){
        this.measures = facade.listMeasures();
    }


    public void initFunction(){
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String measureName = ((String)comboBox.getSelectedItem());
                try {
                    text.setText(facade.getMeasureDescription(measureName));
                } catch (MeasureNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        text.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    System.out.println(hle.getURL());
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(hle.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}