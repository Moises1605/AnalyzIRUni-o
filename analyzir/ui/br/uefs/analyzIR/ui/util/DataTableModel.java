package br.uefs.analyzIR.ui.util;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel {
	
	
	private static final long serialVersionUID = 1L;
	private List<String> data;

	public DataTableModel(List<String> data) {
		this.data = data;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex);
	}

	public void add(String measure){
		if(!data.contains(measure))
			data.add(measure);
	}
	public void remove(int rowIndex, int columnIndex){
		String value = (String)getValueAt(rowIndex, 1);
		data.remove(value);
	}
	
	

}
