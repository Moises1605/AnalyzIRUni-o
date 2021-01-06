package br.uefs.analyzIR.measure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import br.uefs.analyzIR.data.Context;
import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.InvalidQrelFormatException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.exception.MeasureNotFoundException;
import br.uefs.analyzIR.exception.MeasuresGroupNotFoundException;
import br.uefs.analyzIR.exception.QrelItemNotFoundException;
import br.uefs.analyzIR.exception.TopicNotFoundException;
import br.uefs.analyzIR.measure.atMeasure.IPrecAtRecall;
import br.uefs.analyzIR.measure.atMeasure.MapCutAtN;
import br.uefs.analyzIR.measure.atMeasure.NDCGCutAtN;
import br.uefs.analyzIR.measure.atMeasure.NumRelAtN;
import br.uefs.analyzIR.measure.atMeasure.PAtN;
import br.uefs.analyzIR.measure.atMeasure.RPrecMultAtN;
import br.uefs.analyzIR.measure.atMeasure.RecallAtN;
import br.uefs.analyzIR.measure.atMeasure.RelativePAtN;
import br.uefs.analyzIR.measure.atMeasure.Success_N;
import br.uefs.analyzIR.measure.curveMeasure.IPrecAtRecallCurve;
import br.uefs.analyzIR.measure.curveMeasure.MapCutCurve;
import br.uefs.analyzIR.measure.curveMeasure.NDCGCutCurve;
import br.uefs.analyzIR.measure.curveMeasure.NumRelCurve;
import br.uefs.analyzIR.measure.curveMeasure.PAtNCurve;
import br.uefs.analyzIR.measure.curveMeasure.RPrecMultCurve;
import br.uefs.analyzIR.measure.curveMeasure.RecallAtNCurve;
import br.uefs.analyzIR.measure.curveMeasure.RelativePCurve;
import br.uefs.analyzIR.measure.curveMeasure.SuccessCurve;
import br.uefs.analyzIR.measure.data.MeasureSet;
import br.uefs.analyzIR.measure.singleValueMeasure.Avg11pt;
import br.uefs.analyzIR.measure.singleValueMeasure.BPref;
import br.uefs.analyzIR.measure.singleValueMeasure.BinG;
import br.uefs.analyzIR.measure.singleValueMeasure.G;
import br.uefs.analyzIR.measure.singleValueMeasure.GMAP;
import br.uefs.analyzIR.measure.singleValueMeasure.GMBPref;
import br.uefs.analyzIR.measure.singleValueMeasure.InfAP;
import br.uefs.analyzIR.measure.singleValueMeasure.MAP;
import br.uefs.analyzIR.measure.singleValueMeasure.NDCG;
import br.uefs.analyzIR.measure.singleValueMeasure.NDCGRel;
import br.uefs.analyzIR.measure.singleValueMeasure.RNDCG;
import br.uefs.analyzIR.measure.singleValueMeasure.RPrec;
import br.uefs.analyzIR.measure.singleValueMeasure.RecipRank;
import br.uefs.analyzIR.measure.singleValueMeasure.SetF;
import br.uefs.analyzIR.measure.singleValueMeasure.SetMAP;
import br.uefs.analyzIR.measure.singleValueMeasure.SetRecall;
import br.uefs.analyzIR.measure.singleValueMeasure.SetRelativeP;
import br.uefs.analyzIR.measure.singleValueMeasure.Utility;
import br.uefs.analyzIR.measure.util.ConfigValues;

/**
 * MeasureManager control all operation on measures. The manager contains the most important operation on measure like: add a new measure, 
 * list user and tool measures, remove a specific measure and calculate a value from a measure chosen.  
 * @author lucas
 *
 */
public class MeasureManager {

	//hold an object atMeasure by group of same measure
	private HashMap<String, AtMeasure> atMeasures;
	// hold a name key and an curve measure object
	private HashMap<String, CurveMeasure> curveMeasures;
	// hold a name key and an object single value measure
	private HashMap<String, SingleValueMeasure> singleValueMeasures;
	//hold a name key and an object diversify measure
	private HashMap<String, DiversifyMeasure> diversifyMeasures;

	/**
	 * Creates a manager and all measures objects by category and inits the
	 * colletions.
	 *
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @author evaluatAll
	 */
	public MeasureManager() {
		atMeasures = new HashMap<>();
		singleValueMeasures = new HashMap<>();
		curveMeasures = new HashMap<>();
		diversifyMeasures = new HashMap<>();
	}

	/**
	 * Initialize the context values for all measures.
	 *
	 * @param context a container with main values like: run, qrels.
	 * @throws ClassNotFoundException - If a user class is not found.
	 * @throws InstantiationException - if a class doesn't have a public and default construct
	 * @throws IllegalAccessException -
	 * @throws IOException            - Some problem when use a jar file
	 */
	public void init(Context context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		initMeasures(context);
	}

	/**
	 * List all measures name ordered by alphabetic rule.
	 *
	 * @return a list with all measure name.
	 */
	public List<String> listMeasures() {

		List<String> measures = new ArrayList<String>();
		for (String measure : atMeasures.keySet()) {
			measures.add(measure);
		}
		for (String measure : singleValueMeasures.keySet()) {
			measures.add(measure);
		}
		for (String measure : curveMeasures.keySet()) {
			measures.add(measure);
		}
		Collections.sort(measures, Collator.getInstance());
		return measures;
	}

	/**
	 * List all diversify measures name ordered by alphabetic rule.
	 *
	 * @return a list with all diversify measures name.
	 */

	public List<String> listDiversifyMeasure() {
		List<String> measures = new ArrayList<>();
		for (String measure : diversifyMeasures.keySet()) {
			measures.add(measure);
		}
		Collections.sort(measures, Collator.getInstance());
		return measures;
	}

	/**
	 * List all measures name that has the same type of measures specified on measures parameter.
	 *
	 * @param measures - a list of measure of the same type.
	 * @return a list with all measures names that has the same type of measures specified on measures parameter.
	 * @throws MeasuresGroupNotFoundException - if measures parameter is a list of different measures type.
	 */
	public List<String> listMeasureGroup(List<String> measures)
			throws MeasuresGroupNotFoundException {

		List<String> temp = new ArrayList<>();

		if (atMeasures.keySet().containsAll(measures)) {
			temp.addAll(atMeasures.keySet());
			temp.removeAll(measures);
		} else if (singleValueMeasures.keySet().containsAll(measures)) {
			temp.addAll(singleValueMeasures.keySet());
			temp.removeAll(measures);
		} else if (curveMeasures.keySet().containsAll(measures)) {
			temp.addAll(curveMeasures.keySet());
			temp.removeAll(measures);
		} else {
			throw new MeasuresGroupNotFoundException(
					"The selected measures are not compatible. Please select only measures of same kind. You may combine single value and at measures. "
							+ "Alternatively, you may select only curve measures.");
		}

		Collections.sort(temp, Collator.getInstance());

		return temp;
	}

	/**
	 * Returns a MeasureSet object that encapsulates the result of whatever measure.
	 *
	 * @param measureName measure chosen.
	 * @param run         a specified result that wants to calculate the measure chosen.
	 * @param topics      which topics want to calculate on the measure chosen.
	 * @param atValue     a deep of result ranking
	 * @param xValues     a set of ats.
	 * @return a MeasureSet object that encapsulates the result of whatever measure
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws MeasureNotFoundException
	 * @throws NumberFormatException
	 * @throws InvalidItemNameException
	 * @throws ItemNotFoundException
	 * @throws InvalidQrelFormatException
	 * @throws TopicNotFoundException
	 * @throws QrelItemNotFoundException
	 */
	public MeasureSet calculateMeasure(String measureName,
									   String run, String[] topics, String atValue, String[] xValues) throws IOException,
			InterruptedException, MeasureNotFoundException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException, QrelItemNotFoundException, TopicNotFoundException, InvalidQrelFormatException {

		MeasureSet result = null;
		// if measure is diversify measure
		if (diversifyMeasures.keySet().contains(measureName)) {
			DiversifyMeasure diversify = diversifyMeasures.get(measureName);
			if (diversify instanceof AtMeasure) {
				if (atValue != null) { //does it have at value?
					if (topics != null) { //is it for many topics?
						AtMeasure at = (AtMeasure) diversify;
						result = at.getValue(run, atValue, topics);
					} else { // it is for 'avg' topic
						AtMeasure at = (AtMeasure) diversify;
						result = at.getValue(run, atValue);
					}
				}
			} else if (diversify instanceof SingleValueMeasure) {
				if (topics != null) {//is it for many topics?
					SingleValueMeasure single = (SingleValueMeasure) diversify;
					result = single.getValue(run, topics);
				} else {// it is for 'avg' topic
					SingleValueMeasure single = (SingleValueMeasure) diversify;
					result = single.getValue(run);
				}
			} else if (diversify instanceof CurveMeasure) {
				if (xValues != null) { //does it have xValues (a list of deep of ranking)?
					if (topics != null) { // is it for many topics?
						CurveMeasure curve = (CurveMeasure) diversify;
						result = curve.getValue(run, xValues, topics);
					} else {// it is for 'avg' topic
						CurveMeasure curve = (CurveMeasure) diversify;
						result = curve.getValue(run, xValues);
					}
				}
			}
		} else if (atMeasures.keySet().contains(measureName)) {
			if (atValue != null) {// does it have at value?
				if (topics != null) { // is it for many topics?
					AtMeasure at = atMeasures.get(measureName);
					result = at.getValue(run, atValue, topics);
				} else { // it is for 'avg' topic
					AtMeasure at = atMeasures.get(measureName);
					result = at.getValue(run, atValue);
				}
			}
		} else if (singleValueMeasures.keySet().contains(measureName)) {
			if (topics != null) {// is it for many topics?
				SingleValueMeasure single = singleValueMeasures.get(measureName);
				result = single.getValue(run, topics);
			} else {// it is for 'avg' topic
				SingleValueMeasure single = singleValueMeasures.get(measureName);
				result = single.getValue(run);
			}
		} else if (curveMeasures.keySet().contains(measureName)) {
			if (xValues != null) { //does it have xValues (a list of deep of ranking)?
				if (topics != null) {// is it for many topics?
					CurveMeasure curve = curveMeasures.get(measureName);
					result = curve.getValue(run, xValues, topics);
				} else {// it is only for 'avg' topic
					CurveMeasure curve = curveMeasures.get(measureName);
					result = curve.getValue(run, xValues);
				}
			}
		}

		return result;
	}

	/**
	 *
	 * @param measureName
	 * @return
	 * @throws MeasureNotFoundException
     */
	public Measure getMeasure(String measureName) throws MeasureNotFoundException {
		Measure measure = null;
		if (atMeasures.keySet().contains(measureName)) {
			AtMeasure at = atMeasures.get(measureName);
			measure = at;
		} else if (singleValueMeasures.keySet().contains(measureName)) {
			SingleValueMeasure single = singleValueMeasures.get(measureName);
			measure = single;
		} else if (curveMeasures.keySet().contains(measureName)) {
			CurveMeasure curve = curveMeasures.get(measureName);
			measure = curve;
		}
		return measure;
	}
	
	/**
	 * 
	 * @param measures
	 * @return
	 */
	public int getMeasureGroup(List<String> measures) {

		if (singleValueMeasures.keySet().containsAll(measures)) {
			return 0;
		} else if (atMeasures.keySet().containsAll(measures)) {
			return 1;
		} else if (curveMeasures.keySet().containsAll(measures)) {
			return 2;
		}else if(diversifyMeasures.keySet().containsAll(measures)){
			return 3;
		}

		return -1;
	}

	/**
	 * 
	 * @param measures
	 * @return
	 */
	public int getMeasureGraphGroup(List<String> measures) {
		if (singleValueMeasures.keySet().containsAll(measures)) {
			return 1;
		} else if (atMeasures.keySet().containsAll(measures)) {
			return 1;
		} else if (curveMeasures.keySet().containsAll(measures)) {
			return 2;
		}

		return -1;
	}
	
	public int getInteractiveMeasureGraphGroup(List<String> measures) {
		if (singleValueMeasures.keySet().containsAll(measures)) {
			return 1;
		} else if (atMeasures.keySet().containsAll(measures)) {
			return 2;
		} else if (curveMeasures.keySet().containsAll(measures)) {
			return 2;
		}

		return -1;
	}
	
	/***
	 * 
	 * @param type
	 */
	public List<String> getMeasuresByType(int type) {
		
		List<String> temp = new ArrayList<String>();
		
		switch(type){
		case 0: {
			temp.addAll(singleValueMeasures.keySet());
			return temp;
		}
		case 1:{
			temp.addAll(atMeasures.keySet());
			return temp;
		}
		case 2:{
			temp.addAll(curveMeasures.keySet());
			return temp;
		}
		default:{
			return null;
		}
		}
	}
	
	/**
	 * Adds a new measure. This method copy the jar measure file to lib folder and after that extract one and only one class inside
	 * this jar file and insert a new measure object in tool. 
	 * @param url
	 * @param nameFile
	 * @param context
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public String[] addMeasure(String url, String nameFile, Context context) throws FileNotFoundException, IOException,
											ClassNotFoundException, InstantiationException, IllegalAccessException{

		//copy to lib folder
		ConfigValues config = new ConfigValues(); 
		String path = config.getLibDirectory(); 
		path += File.separator + nameFile;
		copyFiles(url, path);
		
		//get measure Class from jar file
		Class<?> type = extractType(path);
		
		//create the measure
		Measure m = createMeasureObject(type, context);
		
		//list the value of a measure included. The pattern is {measure name, class name, type, new jar file path}
		String [] values = {m.getName(), type.getName(), "", path};
		
		if(m instanceof AtMeasure){
			values[2] = "AtMeasure";
		}else if(m instanceof CurveMeasure){
			values[2] = "CurveMeasure";
		}else if(m instanceof SingleValueMeasure){
			values[2] = "SingleValueMeasure";
		}
		
		if(m instanceof DiversifyMeasure){
			values[2] += ",DiversifyMeasure";
		}
		
		return values;
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public List<String[]> listUserMeasures() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		
		List<String[]> userMeasure = new ArrayList<>(); 
		String [] values;
		HashMap<String, Class<?>> types = getUserMeasures(); 
		
		for(String path : types.keySet()){

			Class<?> type = types.get(path);
			values = new String[4];
			Measure m = (Measure) type.newInstance(); 
			values[0] = m.getName(); 
			values[1] = type.getName(); 
			
			if(m instanceof AtMeasure){
				values[2] = "AtMeasure";
			}else if(m instanceof CurveMeasure){
				values[2] = "CurveMeasure";
			}else if(m instanceof SingleValueMeasure){
				values[2] = "SingleValueMeasure";
			}
			
			if(m instanceof DiversifyMeasure){
				values[2] += ",DiversifyMeasure";
			}
			
			values[3] = path;	
			userMeasure.add(values);
		}
		return userMeasure;
	}
	


	/**
	 * 
	 * @param url
	 */
	public void removeMeasure(String url, String name){
		
		if(atMeasures.containsKey(name)){
			atMeasures.remove(name);
		}else if(singleValueMeasures.containsKey(name)){
			singleValueMeasures.remove(name);
		}else if(curveMeasures.containsKey(name)){
			curveMeasures.remove(name);
		}
		
		File jar = new File(url);
		jar.delete();
	}
	
	
	
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 */
	private Class<?> extractType(String path) throws IOException, MalformedURLException, ClassNotFoundException {
	
		JarFile jarFile2 = new JarFile(path);
		Enumeration<JarEntry> allEntries = jarFile2.entries();
		Class<?> type = null;

		URL[] urls = { new URL("jar:file:" + path+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		
		while (allEntries.hasMoreElements()) {

			JarEntry entry = (JarEntry) allEntries.nextElement();

			String name = entry.getName();
			if(name.indexOf(".class") != -1){
				name = name.substring(0, name.length() - 6);
				name = name.replace(File.separator, ".");

				type = cl.loadClass(name);
			}
		}
		jarFile2.close();
		return type;
	}

	/**
	 * 
	 * @param type
	 * @param context
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Measure createMeasureObject(Class<?> type, Context context) throws InstantiationException, IllegalAccessException {
		
		Object o = type.newInstance(); 

		if(o instanceof DiversifyMeasure){
			DiversifyMeasure diversify = (DiversifyMeasure) o; 
			diversify.setContext(context);
			this.diversifyMeasures.put(diversify.getName(), diversify);
		}else if(o instanceof AtMeasure){
			AtMeasure at = (AtMeasure) o; 
			at.setContext(context);
			this.atMeasures.put(at.getName(), at);
		}else if(o instanceof CurveMeasure){
			CurveMeasure curve = (CurveMeasure) o;  
			curve.setContext(context);
			this.curveMeasures.put(curve.getName(), curve);
		}else if(o instanceof SingleValueMeasure){
			SingleValueMeasure single = (SingleValueMeasure) o; 
			single.setContext(context);
			this.singleValueMeasures.put(single.getName(), single);
		}
		
		return (Measure)o;
	}
	
	/**
	 * 
	 * @param urlSource
	 * @param urlDestiny
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void copyFiles(String urlSource, String urlDestiny)
			throws FileNotFoundException, IOException {

		FileInputStream source;
		FileOutputStream destiny;
		FileChannel sourceChannel;
		FileChannel destinyChannel;

		try {
			source = new FileInputStream(urlSource);
			destiny = new FileOutputStream(urlDestiny);
			sourceChannel = source.getChannel();
			destinyChannel = destiny.getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinyChannel);
			source.close();
			destiny.close();
		} catch (FileNotFoundException erro) {
			throw new FileNotFoundException(
					"There was a problem during transfer of your file. Please verify the path you entered.");
		} catch (IOException erro) {
			throw new IOException(
					"A problem happened during to transfer a file to another.");
		}
	}

	
	/**
	 * 
	 * @param context
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	private void initMeasures(Context context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

		AtMeasure iprec = new IPrecAtRecall("iprec_at_recall");
		AtMeasure pn = new PAtN("P@N");
		AtMeasure recall = new RecallAtN("recall@N");
		AtMeasure rprec = new RPrecMultAtN("Rprec_mult");
		AtMeasure ndcg = new NDCGCutAtN("ndcg_cut");
		AtMeasure num_rel = new NumRelAtN("num_rel");
		AtMeasure map_cut = new MapCutAtN("map_cut");
		AtMeasure relative_p = new RelativePAtN("relative_P");
		AtMeasure sucess = new Success_N("success");
		
		CurveMeasure iprec_curve = new IPrecAtRecallCurve("iprec_at_recall");
		CurveMeasure map_cut_curve = new MapCutCurve("map_cut");
		CurveMeasure ndcg_curve =  new NDCGCutCurve("ndcg_cut");
		CurveMeasure num_rel_curve = new NumRelCurve("num_rel");
		CurveMeasure pn_curve = new PAtNCurve("p@N");
		CurveMeasure recall_curve = new RecallAtNCurve("recall@N");
		CurveMeasure relative_p_curve = new RelativePCurve("recall@N");
		CurveMeasure rprecmulti_curve  = new RPrecMultCurve("Rprec_mult");
		CurveMeasure sucess_curve = new SuccessCurve("sucess");
	
		SingleValueMeasure binG = new BinG("binG");
		SingleValueMeasure bpref = new BPref("bpref");
		SingleValueMeasure g = new G("G");
		SingleValueMeasure gmap =  new GMAP("gm_map");
		SingleValueMeasure gm_bpref = new GMBPref("gm_bpref");
		SingleValueMeasure infAP = new InfAP("infAP");
		SingleValueMeasure map = new MAP("map");
		SingleValueMeasure ndcg_single = new NDCG("ndcg");
		SingleValueMeasure ndcg_rel = new NDCGRel("ndcg_rel");
		SingleValueMeasure s11pt_avg =  new Avg11pt("11pt_avg");
		SingleValueMeasure recip_rank = new RecipRank("recip_rank");
		SingleValueMeasure Rndcg = new RNDCG("Rndcg");
		SingleValueMeasure RPrec = new RPrec("Rprec");
		SingleValueMeasure set_F = new SetF("set_F");
		SingleValueMeasure set_map = new SetMAP("set_map");
		SingleValueMeasure set_recall =  new SetRecall("set_recall");
		SingleValueMeasure set_relative_P = new SetRelativeP("set_relative_P");
		SingleValueMeasure utility = new Utility("utility");

		iprec.setContext(context);
		pn.setContext(context);
		recall.setContext(context);
		rprec.setContext(context);
		ndcg.setContext(context);
		num_rel.setContext(context);
		map_cut.setContext(context);
		relative_p.setContext(context);
		sucess.setContext(context);
		
		iprec_curve.setContext(context);
		map_cut_curve.setContext(context);
		ndcg_curve.setContext(context);
		num_rel_curve.setContext(context);
		pn_curve.setContext(context);
		recall_curve.setContext(context);
		relative_p_curve.setContext(context);
		rprecmulti_curve.setContext(context);
		sucess_curve.setContext(context);
		
		binG.setContext(context);
		bpref.setContext(context);
		g.setContext(context);
		gmap.setContext(context);
		gm_bpref.setContext(context);
		infAP.setContext(context);
		map.setContext(context);
		ndcg_single.setContext(context);
		ndcg_rel.setContext(context);
		s11pt_avg.setContext(context);
		recip_rank.setContext(context);
		Rndcg.setContext(context);
		RPrec.setContext(context);
		set_F.setContext(context);
		set_map.setContext(context);
		set_recall.setContext(context);
		set_relative_P.setContext(context);
		utility.setContext(context);
		
		
		atMeasures.put("iprec_at_recall", iprec);
		atMeasures.put("P@N", pn);
		atMeasures.put("recall@N", recall);
		atMeasures.put("Rprec_mult", rprec);
		atMeasures.put("ndcg_cut", ndcg);
		atMeasures.put("num_rel@N", num_rel);
		atMeasures.put("map_cut", map_cut);
		atMeasures.put("relative_P", relative_p);
		atMeasures.put("success", sucess);

		
		curveMeasures.put("Precision x Recall Curve", iprec_curve);
		curveMeasures.put("map_cut Curve", map_cut_curve);
		curveMeasures.put("ndcg_cut Curve", ndcg_curve);
		curveMeasures.put("num_rel Curve", num_rel_curve);
		curveMeasures.put("p@N Curve",pn_curve);
		curveMeasures.put("recall@N Curve", recall_curve);
		curveMeasures.put("relative_P Curve", relative_p_curve);
		curveMeasures.put("RPrecMult Curve", rprecmulti_curve);
		curveMeasures.put("Sucess Curve", sucess_curve);
	
		singleValueMeasures.put("binG", binG);
		singleValueMeasures.put("bpref", bpref);
		singleValueMeasures.put("G", g);
		singleValueMeasures.put("GMAP", gmap);
		singleValueMeasures.put("GM_BPref", gm_bpref);
		singleValueMeasures.put("infAP", infAP);
		singleValueMeasures.put("map", map);
		singleValueMeasures.put("ndcg", ndcg_single);
		singleValueMeasures.put("ndcg_rel", ndcg_rel);
		singleValueMeasures.put("11pt_avg", s11pt_avg);
		singleValueMeasures.put("recip_rank", recip_rank);
		singleValueMeasures.put("Rndcg", Rndcg);
		singleValueMeasures.put("RPrec", RPrec);
		singleValueMeasures.put("set_F", set_F);
		singleValueMeasures.put("set_map", set_map);
		singleValueMeasures.put("set_recall",set_recall);
		singleValueMeasures.put("set_relative_P", set_relative_P);
		singleValueMeasures.put("utility", utility);
		
		
		List<Class<?>> types = initExtensionMeasures();
		createMeasures(types, context);
		
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private List<Class<?>> initExtensionMeasures() throws IOException, ClassNotFoundException,
	InstantiationException, IllegalAccessException{

			List<Class<?>> types = new ArrayList<Class<?>>();
			ConfigValues config = new ConfigValues(); 
			File folder = new File(config.getLibDirectory()); 

			if(folder.isDirectory()){

				String [] jars = folder.list(); 

				for(String jar : jars){

					String absolutePath = folder.getAbsolutePath() + File.separator + jar;
					JarFile jarFile2 = new JarFile(absolutePath);
					Enumeration<JarEntry> allEntries = jarFile2.entries();

					URL[] urls = { new URL("jar:file:" + absolutePath+"!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);

					while (allEntries.hasMoreElements()) {

						JarEntry entry = (JarEntry) allEntries.nextElement();

						String name = entry.getName();
						if(name.indexOf(".class") != -1){
							name = name.substring(0, name.length() - 6);
							name = name.replace(File.separator, ".");

							Class<?> measure = cl.loadClass(name);
							types.add(measure);
						}
					}
					jarFile2.close();
				}
			}
			
			return types;
	}

	/**
	 * 
	 * @param types
	 * @param context
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void createMeasures(List<Class<?>> types, Context context) 
			throws InstantiationException, IllegalAccessException{
		
		for(Class<?> type : types){
			Object o = type.newInstance(); 
			if(o instanceof AtMeasure){
				AtMeasure at = (AtMeasure) o; 
				at.setContext(context);
				this.atMeasures.put(at.getName(), at);
			}else if(o instanceof CurveMeasure){
				CurveMeasure curve = (CurveMeasure) o;  
				curve.setContext(context);
				this.curveMeasures.put(curve.getName(), curve);
			}else if(o instanceof SingleValueMeasure){
				SingleValueMeasure single = (SingleValueMeasure) o; 
				single.setContext(context);
				this.singleValueMeasures.put(single.getName(), single);
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private HashMap<String, Class<?>> getUserMeasures() throws IOException, ClassNotFoundException {
		
		HashMap<String, Class<?>> values = new HashMap<>(); 
		ConfigValues config = new ConfigValues(); 
		File folder = new File(config.getLibDirectory()); 

		if(folder.isDirectory()){

			String [] jars = folder.list(); 

			for(String jar : jars){

				String absolutePath = folder.getAbsolutePath() + File.separator + jar;
				JarFile jarFile2 = new JarFile(absolutePath);
				Enumeration<JarEntry> allEntries = jarFile2.entries();

				URL[] urls = { new URL("jar:file:" + absolutePath+"!/") };
				URLClassLoader cl = URLClassLoader.newInstance(urls);

				while (allEntries.hasMoreElements()) {

					JarEntry entry = (JarEntry) allEntries.nextElement();

					String name = entry.getName();
					if(name.indexOf(".class") != -1){
						name = name.substring(0, name.length() - 6);
						name = name.replace(File.separator, ".");

						Class<?> measure = cl.loadClass(name);
						values.put(absolutePath, measure);
					}
				}
				jarFile2.close();
			}
		}
		
		return values;
	}

}
