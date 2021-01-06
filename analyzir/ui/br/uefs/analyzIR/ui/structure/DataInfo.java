package br.uefs.analyzIR.ui.structure;

import java.util.HashMap;

/**
 * DataInfo is a local memory used by ScreenView.
 * @author lucas
 *
 */
public class DataInfo {
	
	private HashMap<String, Object> data; 
	
	/**
	 * 
	 * @param oldData
	 */
	public DataInfo(DataInfo oldData){
		this.data = oldData.data;
	}
	
	/**
	 * 
	 */
	public DataInfo(){
		this.data = new HashMap<>();
	}
	
	/**
	 * 
	 * @param key
	 * @param data
	 */
	public void putData(String key, Object data){
		if(data != null){
			if(!this.data.containsKey(key)){
				this.data.put(key, data);
			}else {
				this.data.replace(key, data);
			}
		}
	}
	

	public int getDataInfoSize(){
		return this.data.size();
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getData(String key){
		if(data.containsKey(key)){
			return data.get(key);
		}
		return null;
	}
	

}
