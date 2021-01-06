package br.uefs.analyzIR.ui.screenView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.uefs.analyzIR.exception.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import br.uefs.analyzIR.controller.Facade;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.ui.common.ProjectView;
import br.uefs.analyzIR.ui.structure.DataInfo;
import br.uefs.analyzIR.ui.structure.ScreenView;

public class SideBar implements ScreenView {

	private JButton btnOpen;
	private JButton btnRemove;
	private JButton btnOpenAll;
	private JLabel lblProjectName;
	private JLabel lblNumberQueries;
	private JComboBox<String> comboPlots;
	private Facade facade;
	
	public SideBar(Facade facade) {
		this.facade = facade;
	}
	
	@Override
	public void putDataInfo(DataInfo dataInfo) {}

	@Override
	public void show() {}

	@Override
	public JPanel make() {
		return getSideBar();
	}

	@Override
	public DataInfo getDataInfo() {return null;}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean isItem(String name) {	
		for (int i = 0; i < comboPlots.getItemCount(); i++) {
			if (comboPlots.getItemAt(i).equals(name)) {
				System.err.println("econtrou item");
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 */
	public void addItem(String name) {
		if(!isItem(name)) {
			this.comboPlots.addItem(name);
			System.err.println("Adicionou: " + name);
		}
	}

	/**
	 * 
	 * @param name
	 */
	public void removeItem(String name) {
		System.err.println("Tentando remover " + name);
		if(isItem(name)){
			this.comboPlots.removeItem(name);
			System.err.println("Removeu: " + name);
		}
	}
	
	private JPanel getSideBar() {
		initComponents();
		initFunctions();
		initValues(facade.listGraphs());
		return initLayout();
	}

	private void initComponents() {

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

		// create plot buttons actions.
		btnRemove = new JButton("Remove");
		btnOpen = new JButton("Open");
		btnOpenAll = new JButton("Open all");

		lblProjectName = new JLabel();
		lblNumberQueries = new JLabel();

		// create combo to plots list.
		comboPlots = new JComboBox<String>();

		// set font
		btnOpen.setFont(font);
		btnOpenAll.setFont(font);
		btnRemove.setFont(font);
		comboPlots.setFont(font);
		lblProjectName.setFont(font);
	}

	private JPanel initLayout() {

		String columns = "5dlu, left:80dlu, 5dlu, left:60dlu, 5dlu";
		String rows = "5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, 10dlu";

		FormLayout form = new FormLayout(columns, rows);
		PanelBuilder builder = new PanelBuilder(form);

		builder.addLabel("Project Name:", CC.xy(2, 2));
		builder.add(lblProjectName, CC.xy(4, 2));
		builder.addLabel("Number of Queries: ", CC.xy(2, 4));
		builder.add(lblNumberQueries, CC.xy(4, 4));
		builder.addLabel("Graphs:", CC.xy(2, 6));
		builder.add(comboPlots, CC.xywh(2, 8, 3, 1));
		builder.add(btnRemove, CC.xy(2, 10));
		builder.add(btnOpen, CC.xy(4, 10));
		builder.add(btnOpenAll, CC.xy(4, 12));

		return builder.getPanel();
	}



	private void initValues(List<String> graphs)  {
		
		try {
			
			lblProjectName.setText(facade.getResume()[0]);
			lblNumberQueries.setText(facade.getResume()[1]);
			comboPlots.addItem("-- Select a Graph --");
			for (String g : graphs) {
				comboPlots.addItem(g);
			}
			
		} catch (InvalidQrelFormatException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (QrelItemNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (TopicNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void open(String name) {

		List<String> values = new ArrayList<String>();
		values.add("TITLE");
		values.add("MEASURES");
		values.add("XVALUES");
		values.add("TOPICS");
		values.add("ATVALUE");
		values.add("RUNS");
		//nilson add here in the values list the info that you wanna get.

		try {

			HashMap<String, List<String>> datas = facade.getPlotValues(values,
					name);

			List<String> measures = datas.get("MEASURES");

			String title = datas.get("TITLE").get(0);

			List<String> topics = datas.get("TOPICS");
			List<String> runs = datas.get("RUNS");

			String[] xValues = new String[datas.get("XVALUES").size()];
			xValues = datas.get("XVALUES").toArray(xValues);
			
			String atValue = datas.get("ATVALUE").get(0);

			DataInfo dataInfo = new DataInfo();
			dataInfo.putData("measures", measures);
			dataInfo.putData("runs", runs);
			dataInfo.putData("topics", topics);
			dataInfo.putData("name", name);
			dataInfo.putData("xvalues", xValues);
			dataInfo.putData("atValue", atValue);
			dataInfo.putData("title", title);
			
			JPanel panel = facade.getGraph(name);
			
			GraphScreenView view = new GraphScreenView(panel, facade);
			view.putDataInfo(dataInfo);

			ProjectView.getInstance(null).addGraph(view);

		} catch (GraphNotFoundException e1) {

			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (NoGraphItemException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (NoGraphPointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initFunctions() {

		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String plot = comboPlots.getSelectedItem().toString();
				open(plot);
			}

		});
		
		btnOpenAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < comboPlots.getItemCount(); i++) {
					String plot = comboPlots.getItemAt(i).toString();
					if(!plot.equals("-- Select a Graph --"))
						open(plot);
				}
			}
		});

		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String value = comboPlots.getSelectedItem().toString();

				if(!value.equals("-- Select a Graph --")) {
					comboPlots.removeItem(value);
					ProjectView.getInstance(null).removeGraph(value);
				}else{
					if(comboPlots.getItemCount() <= 1)
						JOptionPane.showMessageDialog(null, "Chart list is empty. You could not remove a graph.", "Error graph list", JOptionPane.WARNING_MESSAGE);
					else
						JOptionPane.showMessageDialog(null, "Select a graph to remove", "Error graph list", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	public void removeAll() {
		comboPlots.removeAll();
	}

	
}
