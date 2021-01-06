package br.uefs.analyzIR.ui.screenView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;
 
/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class Tab extends JPanel {
  
	/**
	 * 
	 */
	private String name;
	private JLabel label;
	private JButton close;
	private static final long serialVersionUID = 1L;
 
    public Tab(String name, ActionListener listener){
    	
    	super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
    	this.name = name;
    	this.label = new JLabel(name);
    	this.close = new TabButton();
    	
        this.label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        this.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
        this.close.addActionListener(listener);
        
        this.setOpaque(false);
        super.add(label);
        super.add(close);
    }
 
    private class TabButton extends JButton  {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close this plot");
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
          
        }
 
        
        //we don't want to update UI for this button
        public void updateUI() {
        }
 
        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
 
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
 
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
    
    public String getName(){
    	return this.name;
    }
}