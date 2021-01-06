package br.uefs.analyzIR.graph;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.exception.NoGraphPointException;
import br.uefs.analyzIR.statistics.StatisticsIteractiveInfo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/*
* Exports graph info values or graph visualization to a specific format (CSV, XLS, PNG, JPEG).
*
* */
public class GraphExporter {

	private String nameFile; // exported file name
	private String fullPathFile; // exported file full path
	private Graph graph; // graph exported

	/**
	 * Constructs a graph exporter for a specific graph.
	 * @param nameFile exported file name
	 * @param fullPathFile exported file full path
	 * @param graph exported graph
	 */
	public GraphExporter(String nameFile, String fullPathFile, Graph graph){
		this.nameFile = nameFile;
		this.fullPathFile = fullPathFile;
		this.graph = graph;
	}

	/**
	 * Exports the graph info to CSV file.
	 * @throws NoGraphPointException if graph is empty
	 * @throws IOException if had had a writing problem
	 */
	public void exportToCSV() throws NoGraphPointException, IOException{
		
		List<GraphItem> items = graph.getItems(); // gets all graph items
		
		fullPathFile = fullPathFile + ".csv"; // creates full path to exported file
		File file = new File(fullPathFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		String line = "";
		
		for(GraphItem item : items){
			
			String nameItem = item.getName();
			line = nameItem + ",";

			//if item represents a curve
			if(item.getPoints().length > 1){
				
				line += item.getPoints()[0].getX() + "," + item.getPoints()[0].getY();
				bw.write(line);
				bw.newLine();
				
				for(int i = 1; i < item.getPoints().length; i++){
					
					line = nameItem + "," + item.getPoints()[i].getX()+","+item.getPoints()[i].getY();
					bw.write(line);
					bw.newLine();
				}
				
			}else{ // if item represents a bar
				
				line += item.getPoints()[0].getX() + "," + item.getPoints()[0].getY();
				bw.write(line);
				bw.newLine();
			}
			
		}

		bw.flush();
		fw.close();	
		bw.close();
	}

	/**
	 * Exports the graph info to xls file.
	 * @throws NoGraphPointException if an item is empty
	 * @throws IOException if had had a writing problem
	 */
	public void exportToXLS() throws NoGraphPointException, IOException{
		
		List<GraphItem> items = graph.getItems();
		GraphInfo info = graph.getGraphInfo();
		Boolean test = info.getStatistical();
		StatisticsIteractiveInfo iteractiveInfo = null;
		List<Boolean> gg = null;
		List<Double>  jj = null;
		if(test){
			iteractiveInfo = info.getStatisticalTest();
			gg = iteractiveInfo.parser();
			jj = iteractiveInfo.getTestValues();
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet firstSheet = workbook.createSheet(nameFile);
		
		fullPathFile = fullPathFile + ".xls";
		File file = new File(fullPathFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		
		int line = 0;

		//creating xls header
		HSSFRow row = firstSheet.createRow((short)line);
		row.createCell(0).setCellValue("ITEM");
		row.createCell(1).setCellValue("X-VALUE");
		row.createCell(2).setCellValue("Y-VALUE");
		if(test){
			row.getCell(0).setCellValue("Run");
			row.getCell(1).setCellValue("Iteration");
			row.getCell(2).setCellValue(info.getMeasures().get(0));
			row.createCell(3).setCellValue("p_value-" + iteractiveInfo.getTest());
            row.createCell(4).setCellValue("Significant-"+ iteractiveInfo.getSignificant());
			row.createCell(5).setCellValue("Baseline");
        }
		line++;
		
		for(GraphItem item : items){
			
			row = firstSheet.createRow((short)line);// add a new data line
			row.createCell(0).setCellValue(item.getName()); // put name item (or name base) in first cell

			// if item represents a curve
			if(item.getPoints().length > 1){
				row.createCell(1).setCellValue(item.getPoints()[0].getX());
				row.createCell(2).setCellValue(item.getPoints()[0].getY());
				if(test){
                    //row.createCell(3).setCellValue(info.getStatisticalTest().get(0).toString());
					row.createCell(3).setCellValue(jj.get(0).toString());
					if(gg.get(0)){
						row.createCell(4).setCellValue("significant");
					}
					else{
						row.createCell(4).setCellValue("-");
					}
					row.createCell(5).setCellValue(iteractiveInfo.getBaseline());
                }
				
				line++;
				
				for(int i = 1; i < item.getPoints().length; i++){
					row = firstSheet.createRow((short)line);
					row.createCell(0).setCellValue(item.getName());
					row.createCell(1).setCellValue(item.getPoints()[i].getX());
					row.createCell(2).setCellValue(item.getPoints()[i].getY());
					if(test){
						String significant;
						if(gg.get(i)){
							significant = "significant";
						}
						else{
							significant = "-";
						}
						row.createCell(3).setCellValue(jj.get(i));
                        row.createCell(4).setCellValue(significant);
						row.createCell(5).setCellValue(iteractiveInfo.getBaseline());
                    }
					line++;
				}
				
			}else{ // if represents a bar
				row.createCell(1).setCellValue(item.getPoints()[0].getX());
				row.createCell(2).setCellValue(item.getPoints()[0].getY());
				if(test){
					row.getCell(1).setCellValue("1");
					//row.createCell(3).setCellValue(info.getStatisticalTest().get(0).toString());
					row.createCell(3).setCellValue(jj.get(0).toString());
					if(gg.get(0)){
						row.createCell(4).setCellValue("significant");
					}
					else{
						row.createCell(4).setCellValue("-");
					}
					row.createCell(5).setCellValue(iteractiveInfo.getBaseline());
				}
				line++;
			}
			
		}
		/*if(info.getStatistical()){
			int line1 = 0;
			row  = firstSheet.createRow((short)line1);
			row.createCell(3).setCellValue("meaningfulness");
			line1++;
			for(Boolean item:info.getStatisticalTest()){
                row = firstSheet.createRow((short)line1);
				row.createCell(3).setCellValue(item.toString());
				line1++;
			}
		}*/
		
		workbook.write(fos);
		fos.flush();
		fos.close();	
		workbook.close();
	}

	/**
	 *Exports the graph to png file.
	 * @param width image width
	 * @param height image height
	 * @throws NoGraphItemException if graph is empty
	 * @throws NoGraphPointException if item is empty
	 * @throws IOException if had had writing problem
	 */
	public void exportToPNG(int width, int height) throws NoGraphItemException, NoGraphPointException, IOException{
		
		JPanel graphic = graph.make(); // creates a new visual graph
		graphic.setSize(width, height);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		graphic.printAll(g);
		g.dispose();
		
		ImageIO.write(image, "png", new File(this.fullPathFile)); 
	}

	/**
	 * Exports the cart to jpg file.
	 * @param width image width
	 * @param height image height
	 * @throws NoGraphItemException if graph is empty
	 * @throws NoGraphPointException if item is empty
	 * @throws IOException if had had writing problem
	 */
	public void exportToJPG(int width, int height) throws NoGraphItemException, NoGraphPointException, IOException{
		
		JPanel graphic = graph.make();// creates a new visual graph
		graphic.setSize(width, height);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		graphic.printAll(g);
		g.dispose();
		
		ImageIO.write(image, "jpg", new File(this.fullPathFile)); 
	}

	/**
	 * Sets the graph will be exported.
	 * @param graph graph object
	 */
	public void setGraph(Graph graph){
		if(graph != null)
			this.graph = graph;
	}

	public void exportToXLSTest() throws NoGraphPointException, IOException{

		List<GraphItem> items = graph.getItems();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet firstSheet = workbook.createSheet(nameFile);

		fullPathFile = fullPathFile + ".xls";
		File file = new File(fullPathFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);

		int line = 0;

		//creating xls header
		HSSFRow row = firstSheet.createRow((short)line);
		row.createCell(0).setCellValue("ITEM");
		row.createCell(1).setCellValue("X-VALUE");
		row.createCell(2).setCellValue("Y-VALUE");
		row.createCell(3).setCellValue("meaningfulness");
		line++;

		for(GraphItem item : items){

			row = firstSheet.createRow((short)line);// add a new data line
			row.createCell(0).setCellValue(item.getName()); // put name item (or name base) in first cell

			// if item represents a curve
			if(item.getPoints().length > 1){

				row.createCell(1).setCellValue(item.getPoints()[0].getX());
				row.createCell(2).setCellValue(item.getPoints()[0].getY());

				line++;

				for(int i = 1; i < item.getPoints().length; i++){
					row = firstSheet.createRow((short)line);
					row.createCell(0).setCellValue(item.getName());
					row.createCell(1).setCellValue(item.getPoints()[i].getX());
					row.createCell(2).setCellValue(item.getPoints()[i].getY());
					line++;
				}

			}else{ // if represents a bar
				row.createCell(1).setCellValue(item.getPoints()[0].getX());
				row.createCell(2).setCellValue(item.getPoints()[0].getY());
				line++;
			}

		}
		//outro for para criar as linhas dos valores obtidos dos testes e se ouve sigificancia ou não.

		workbook.write(fos);
		fos.flush();
		fos.close();
		workbook.close();
	}
}
