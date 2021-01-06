package br.uefs.analyzIR.controller.graph;

public class GraphMakerFactory {

	/**Identify a new graph like a bar graph.*/
	private static final int BAR_GRAPH = 1;
	
	/**Identify a new graph like a curve graph.*/
	private static final int CURVE_GRAPH = 2;
	
	/**Identify a new graph like a interactive graph.*/
	private static final int INTERACITVE_GRAPH = 3;
	
	
	private static BarGraphMaker bar = new BarGraphMaker();
	private static CurveGraphMaker curve = new CurveGraphMaker();
	private static InteractiveGraphMaker interactive = new InteractiveGraphMaker();
	
	
	
	public static GraphMaker getMaker(int type){
		
		if(type == BAR_GRAPH){
			return bar;
		}else if(type == CURVE_GRAPH){
			return curve;
		}else if(type == INTERACITVE_GRAPH){
			return interactive;
		}else{
			return null;
		}
	}
	
}
