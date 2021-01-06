package br.uefs.analyzIR.graph.bars;

import javax.swing.JPanel;

import br.uefs.analyzIR.exception.NoGraphItemException;
import br.uefs.analyzIR.exception.NoGraphPointException;

public class FRelStackedBarGraph extends BarGraph {

	public FRelStackedBarGraph(String title, String labelX, String labelY, String name) {
		super(title, labelX, labelY, name);
	}

	@Override
	public JPanel make() throws NoGraphItemException, NoGraphPointException {
		return super.make();
	}
}
