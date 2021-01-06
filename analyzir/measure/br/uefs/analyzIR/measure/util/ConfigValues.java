package br.uefs.analyzIR.measure.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ConfigValues {

	private String directory;
	private String libDirectory;
	private String outputFile; 
	
	public ConfigValues(){
		
		this.directory = File.separator + getApplicationPath() + File.separator + "trec_eval" + File.separator;
		this.libDirectory = File.separator + getApplicationPath() + File.separator + "lib" + File.separator;
		
		String path = new File("").getAbsolutePath(); //used to generate the .jar file.
		this.directory = path+"/trec_eval";
		this.libDirectory = path+"/trec_eval/trec_eval";

		this.directory = this.directory.replace(" ", "\\ ");
		this.libDirectory = this.libDirectory.replace(" ", "\\ ");

		this.outputFile = File.separator + directory + File.separator + "output.txt";
	}
	
	public String getDirectory(){
		return this.directory;
	}
	
	public String getOutputFileURL(){
		return this.outputFile;
	}
	
	public String getLibDirectory(){
		return this.libDirectory;
	}

	private String getApplicationPath() {

	        String url = getClass().getResource(getClass().getSimpleName() + ".class").getPath();  
	        File dir = new File(url).getParentFile();  
	        String path = null;  
	        
	        if (dir.getPath().contains("analyzIR.jar")) { 
	            path = findJarParentPath(dir);  
	        }else  {
	            path = dir.getPath();  
	        }
	        
	        try {  
	            return URLDecoder.decode(path, "UTF-8");  
	        }  
	        catch (UnsupportedEncodingException e) {                  
	            return path.replace("%20", " ");  
	        }  
  }

	 private String findJarParentPath(File jarFile) {  
	        while (jarFile.getPath().contains(".jar"))  
	            jarFile = jarFile.getParentFile();           
	        return jarFile.getPath().substring(6);  
	 }   
}
