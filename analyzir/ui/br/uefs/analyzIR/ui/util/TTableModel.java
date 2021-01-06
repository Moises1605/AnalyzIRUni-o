package br.uefs.analyzIR.ui.util;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TTableModel extends AbstractTableModel {


	private static final long serialVersionUID = 1L;
	private String[][] data;
	private int x, y;

	public TTableModel(String[][] data , int x, int y) {
		this.data = data;
		this.x = x;
		this.y = y;
	}

	@Override
	public int getRowCount() {
		return x;
	}

	@Override
	public int getColumnCount() {
		return y;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	public void add(String value, int x, int y){
		data[x][y] = value;
	}

}
