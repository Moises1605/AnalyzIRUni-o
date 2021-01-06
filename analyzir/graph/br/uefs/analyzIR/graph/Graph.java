package br.uefs.analyzIR.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import br.uefs.analyzIR.exception.GraphItemNotFoundException;
import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.exception.NoGraphPointException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
  * A visual representation of data or numerical values of different ways to facilitate understanding thereof.
 * The user of this abstract class has to describes the kind of graph will be created. A graph is a representation
 * of curves, bars, slices or others. A single curve, bar or other representation is defined by Item in a Graph.
 * A {@GraphItem} is a set of points. Each point is a ordered pair (x,y). The user can access an Item by a unique
 * tag, add a new Item and add new points to a specified item.
 * 
 * 
 * @since 1.0
 * @author lucas
 * */

@XStreamAlias("Chart")
public abstract class Graph {
	
	private String fileName;
	private String title;
	private String labelX;
	private String labelY;
	private List<GraphItem> items;
	private GraphInfo info;


	/**
	 * Constructs a new Graph with title, abscissa and coordinate labels, file name and create a empty Item list.
	 * 
	 * @param title
	 *            Graph title
	 * @param labelX
	 *             abscissa name
	 * @param labelY
	 *            coordinate name
	 * @param name
	 *            file name
	 * @since 1.0
	 * @author lucas
	 * */
	public Graph(String title, String labelX, String labelY, String name) {
		this.title = title;
		this.labelX = labelX;
		this.labelY = labelY;
		this.fileName = name;
		items = new ArrayList<>();
	}
	

	/**
	 * Returns a graph using all items.
	 * 
	 * @return a graph
	 * @throws NoGraphItemException
	 *             if item list is empty. 
	 * @throws NoGraphPointException
	 * 			   if point list in some GraphItem is empty
	 * @since 1.0
	 * @author lucas
	 *  
	 * */
	public abstract JPanel make() throws NoGraphItemException, NoGraphPointException;

	/**
	 * Returns a graph using all items.
	 * @param Result_Relevant lista com os resultados dos testes estatísticos
	 * @param baseline nome da baseline do teste estatístico
	 * @param Result_RelevantSense lista com os resultados para colocaração no gráfico levando em conta o valor encontrado no cálculo das medidas
	 * @return a graph
	 * @throws NoGraphItemException
	 *             if item list is empty.
	 * @throws NoGraphPointException
	 * 			   if point list in some GraphItem is empty
	 * @since 1.0
	 * @author moises
	 *
	 * */
	public abstract JPanel makeInteractive(HashMap<Integer,boolean[]> Result_Relevant,String baseline,HashMap<Integer,boolean[]> Result_RelevantSense) throws NoGraphItemException, NoGraphPointException;

	/**
	 * Returns a Graph of the specified item. Search the item on the list and create a new Graph for only this item.
	 * 
	 * @return a graph
	 * @throws GraphItemNotFoundException
	 *             if there is no graph item with the name specified. 
	 * @throws NoGraphItemException
	 * 			   if item list is empty 
	 * @throws NoGraphPointException
	 * 			   if there is no point in a Graph Item
	 * @param item
	 *            a specific item.
	 * @since 1.0
	 * @author lucas
	 * */
	public abstract JPanel makeByChartItem(String item)
			throws GraphItemNotFoundException, NoGraphPointException, NoGraphItemException;
	

	/**
	 * Appends the specified item to the end of GraphItem list. Verify before append if there is
	 * no another item with the same identifier. 
	 * 
	 * @param tagItem
	 *            name of a new graph item
	 * @throws GraphItemNotFoundException
	 * 			  if there is no graph item with the name specified.
	 * @since 1.0
	 * @author lucas
	 * */
	public void addGraphItem(String tagItem) throws GraphItemNotFoundException {
		if (!containsGraphItem(tagItem)) {
			GraphItem item = new GraphItem(tagItem);
			items.add(item);
		} else {
			throw new GraphItemNotFoundException(
					"There is no GraphItem with this name [" + tagItem
							+ "].");
		}
	}
	
	/***
	 * Returns true if the GraphItem list contains the specified item.
	 * @param tagItem
	 * 			item name
	 * @return true if this the GraphItem contains the specified item.
	 */
	public boolean containsGraphItem(String tagItem){
		GraphItem item = new GraphItem(tagItem);
		return items.contains(item);
	}

	/**
	 * Appends a new point in specified GraphItem.
	 * 
	 * @param nameItem
	 *            a name item.
	 * @param x
	 *            a value
	 * @param y
	 *            a value
	 * @throws GraphItemNotFoundException
	 *             if there is no graph item with the name specified.
	 * @since 1.0
	 * @author lucas
	 * 
	 * */
	public void addPoint(String nameItem, String x, double y)
			throws GraphItemNotFoundException {
		GraphItem item = getGraphItem(nameItem);
		item.addPoint(x, y);
	}



	/**
	 * Exports this graphs to the specified url. This method creates a image
	 * file with the specified name, width and height. The format specify a
	 * format of file, if png, jpge, a lot of formats.
	 * 
	 * Exports this graph to a specified format. Creates a new file in specified URL 
	 * with a graph of (width x height) dimension. 
	 * 
	 * @param url
	 *            the path to save this graph.
	 * @param name
	 *            name file.
	 * @param width
	 *            a value
	 * @param height
	 *            a value
	 * @param format
	 *            the file format, for example: png, jpge, and others.
	 * @throws IOException
	 *             problem to write the file.
	 * @throws NoGraphItemException
	 * 			   if item list is empty 
	 * @throws NoGraphPointException
	 * 				if point list in some item is empty
	 * @since 1.0
	 * @author lucas
	 
	 * 
	 * */
	public abstract void exportAs(String url, String name, int width,
			int height, String format) throws IOException, NoGraphItemException, NoGraphPointException;

	/**
	 * Returns Graph title.
	 * 
	 * @return a title
	 * @since 1.0
	 * @author lucas
	 * */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns abscissa label.
	 * 
	 * @return abscissa label
	 * @since 1.0
	 * @author lucas
	 * */
	public String getLabelX() {
		return this.labelX;
	}

	/**
	 * Sets abscissa label. 
	 */
	public void setLabelX(String labelX){
		this.labelX = labelX;
	}
	
	/**
	 * Returns coordinate label.
	 * 
	 * @return coordinate label.
	 * @since 1.0		
	 * @author lucas
	 * */
	
	public String getLabelY() {
		return this.labelY;
	}

	/**
	 * Sets coordinate label. 
	 * @param labelY
	 */
	public void setLabelY(String labelY){
		this.labelY = labelY;
	}
	
	/**
	 * Returns graph name file. Graph name file can be different of Graph title.
	 * 
	 * @return name file
	 */
	public String getName() {
		return fileName;
	}

	
	/**
	 * Sets GraphInfo.
	 */
	public void setGraphInfo(GraphInfo maker){
		this.info = maker;
	}
	
	/**
	 * 
	 * @return
	 */
	public GraphInfo getGraphInfo(){
		return this.info;
	}
	
	/**
	 * Returns all GraphItems.
	 * 
	 * @return all graphItems
	 * @since 1.0
	 * @author lucas
	 * */

	public List<GraphItem> getItems() {
		
		return this.items;
	}

	/**
	 * Returns a specific GraphItem.
	 * 
	 * @param name
	 *            name item
	 * @return a specific GraphItem
	 * @since 1.0
	 * @author lucas
	 * 
	 * @throws GraphItemNotFoundException
	 *            if there is no graph item with the name specified. 
	 * */

	public GraphItem getGraphItem(String name) throws GraphItemNotFoundException {
		for (GraphItem item : items) {
			if (item.getName().equals(name))
				return item;
		}
		throw new GraphItemNotFoundException("There is no GraphItem with this name ["
				+ name + "].");
	}
	//responsavel por retornar a posição da baseline do teste estatístico.
	/**
	 * @param name nome do sistema que é a baseline.
	 *
	 * @return a posição da baseline na lista de sistemas.
	 * @author Moisés
	 **/
	public int returnBaseline(String name){
	    String name1 = name.substring(0,4);
	    String temp;
        for (int i = 0;i < items.size();i++) {
            temp = items.get(i).getName().substring(0,4);
            if (temp.equalsIgnoreCase(name1))
               return i;

        }
        return 0;
    }
}
