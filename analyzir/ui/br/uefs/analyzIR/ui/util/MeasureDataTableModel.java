package br.uefs.analyzIR.ui.util;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MeasureDataTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private List<String[]> data;
	private final int columns = 4; 
	
	
	public  MeasureDataTableModel(List<String[]> values) {
		this.data = values;
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columns;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		if(rowIndex < data.size() && rowIndex >= 0){
			String [] v = data.get(rowIndex); 
			if(columnIndex >= 0 && columnIndex < columns){
				return v[columnIndex];
			}
		}
		return null;
	}
	
	public void addValue(String [] v){
		data.add(v);
	}
	
	public void remove(int rowIndex){
		
		if(rowIndex < data.size() && rowIndex >= 0){
			data.remove(rowIndex);
		}
	}

}
