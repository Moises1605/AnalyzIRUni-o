package br.uefs.analyzIR.statistics;

import br.uefs.analyzIR.measure.util.LinuxCommand;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import java.io.*;
import java.util.Arrays;

/** Wilcoxon Signed Rank Test by Apache Commons Math
 *
 */
public class ApacheCommonsWilcoxonSignedRankTest {

	private double [] value1;
	private double [] value2;

	/** Data Initiation
	 *
	 * @param value1 The first set of values
	 * @param value2 The second set of values
	 */
	public ApacheCommonsWilcoxonSignedRankTest(double[] value1, double[] value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	/** Calculate Wilcoxon test by Apache Commons using two vectors
	 *
	 * @return Returns the P-value found on test
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public double runTest() throws IOException, InterruptedException {
		WilcoxonSignedRankTest acmWilcox = new WilcoxonSignedRankTest();
		double pValue = acmWilcox.wilcoxonSignedRankTest(value1,value2,false);
		return pValue;
	}
}
