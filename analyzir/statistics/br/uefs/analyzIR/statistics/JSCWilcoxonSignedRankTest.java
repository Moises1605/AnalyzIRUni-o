package br.uefs.analyzIR.statistics;

import br.uefs.analyzIR.measure.util.LinuxCommand;
import jsc.datastructures.PairedData;
import jsc.onesample.WilcoxonTest;

import java.io.*;
import java.util.Arrays;

/** Wilcoxon Signed Rank Test by Java Statistical Class (JSC)
 *
 */
public class JSCWilcoxonSignedRankTest {

	private double [] value1;
	private double [] value2;


	/** Data Initiation
	 *
	 * @param value1 The first set of values
	 * @param value2 The second set of values
	 */
	public JSCWilcoxonSignedRankTest(double[] value1, double[] value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	/** Calculate Wilcoxon test by JSC using two vectors
	 *
	 * @return Returns the P-value found on test
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public double runTest(){
		PairedData pairedData = new PairedData(value1,value2);
		WilcoxonTest jscWilcoxon = new WilcoxonTest(pairedData);
		double pValue = jscWilcoxon.approxSP();
		return pValue;
	}
}
