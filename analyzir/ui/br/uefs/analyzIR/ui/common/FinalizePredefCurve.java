package br.uefs.analyzIR.ui.common;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculate the cutoff point of the curve to create the Curve PreDef
 * @version 2.0
 * @author wanderson
 *
 */
public class FinalizePredefCurve {

	/**
	 * Calculate the cutoff point of the curve to create the curve PreDef
	 * @return xValues
	 */
	public String[] calculatexValues() {

        //String input = JOptionPane.showInputDialog("Ranking depth");

        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage("Rank Depth");
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setWantsInput(true);
        JDialog dialog = optionPane.createDialog("The last cutoff of the curve");

        dialog.setVisible(true);
        dialog.dispose();

        String input = (String) optionPane.getInputValue();

        double to = Double.parseDouble(input);
		double begin = 5.0;
		double step = 5.0;

		if (to <= begin) {
			JOptionPane.showMessageDialog(null,
					"You have to choose diferents values");
		} else {

			List<String> values = new ArrayList<String>();
			DecimalFormat format = new DecimalFormat("0.00");
			String[] xValues = null;

			for (double i = begin; i < to; i += step) {
				String value = format.format(i).replace(',', '.').trim();
				values.add(value);
			}

			String end = format.format(to).replace(',', '.').trim();
			if (!values.contains(end))
				values.add(format.format(to).replace(',', '.').trim());

			xValues = new String[values.size()];
			xValues = values.toArray(xValues);
			return xValues;

		}
		return null;

	}

}
