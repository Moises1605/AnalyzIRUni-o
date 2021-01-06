import java.io.*;
import java.util.*;

/**
 * WilcoxonSignedRank - a Java utility to perform a Wilcoxon Signed-Rank test on a table of data
 * <p>
 * 
 * The Wilcoxon Signed-Rank test is a non-parametric statistical test used to assess the difference
 * between two correlated samples, which is to say, between two conditions assigned within-subjects.
 * The relationship with other non-parametric tests is illustrated below:
 * <p>
 * 
 * <blockquote><img src="WilcoxonSignedRank-0.jpg"></blockquote>
 * <p>
 * 
 * The test is also called the Wilcoxon Matched-Pairs Signed-Rank Test. Without "Matched-Pairs" the
 * test assumes one of the samples is a default or assumed score against which variations in the
 * other sample are compared. Either way, the computations are identical.
 * <p>
 * 
 * The procedure involves first building an array of difference scores (the difference between pairs
 * of values for each participant). Differences equal to zero are ignored (i.e., no difference). An
 * array of ranks is then built from the array of difference scores. The ranks are tested to
 * determine if there is a significant difference between the two conditions. Or, more formally, the
 * hypothesis evaluated is "whether or not in the underlying populations represented by the
 * samples/experimental conditions, the median of the difference scores equals zero. If a
 * significant difference is obtained it indicates there is a high likelihood the two
 * samples/conditions represent two different populations" (D. J. Sheskin, <i>Handbook of Parametric
 * and Non-parametric Statistical Procedures</i>, 5th ed., CRC Press, 2011, p. 809).
 * <p>
 * 
 * Invocation (usage message if invoked with no arguments):
 * <p>
 * 
 * <blockquote><a href="WilcoxonSignedRank-2.jpg"><img src="WilcoxonSignedRank-2.jpg"
 * width="600"></a></blockquote>
 * <p>
 * 
 * <b>Example.</b> Consider an experiment seeking to evaluate the "cool appeal" for two new designs
 * of media players. Young tech-savvy individuals are of particular interest. Ten young tech
 * enthusiasts are recruited and then given a demo of, and allowed to play with, each media player.
 * At the end, they are asked to rate the media players on a 10-point linear scale (1 = not cool at
 * all, 10 = really cool) with the assessments stored in a text file as a table, one row per
 * participant, one column per media player (see <a
 * href="wilcoxonsignedrank-ex1.txt">wilcoxonsignedrank-ex1.txt</a>). The assessments are then
 * tested for a significant difference between the media players using the Wilcoxon Signed-Rank
 * test. For example,
 * 
 * <blockquote><a href="WilcoxonSignedRank-3.jpg"><img src="WilcoxonSignedRank-3.jpg"
 * width="600"></a></blockquote>
 * <p>
 * 
 * Note above that the column means for the assessments were 6.4 and 3.7 (not shown), thus revealing
 * which media player was considered more cool by the participants.
 * <p>
 * 
 * The test statistic is a <i>z</i>-score. Since the customary threshold for statistical
 * significance is exceeded (<i>p</i> < .05), we conclude there is indeed a difference between the
 * two media players in their "cool appeal".
 * <p>
 * 
 * The test also provides <i>z'</i> which is corrected for ties. The difference is minor. NOTE:
 * Correcting for ties is with reference to the difference scores, not to the raw scores. For
 * example, the 3rd and 5th rows have difference scores of 1 (4 - 3 = 1, 6 - 5 = 1), thus generating
 * a tie in the ranks.
 * <p>
 * 
 * StatView provides the following output using the above data set:
 * <p>
 * 
 * <blockquote><img src="WilcoxonSignedRank-1.jpg"> </blockquote>
 * <p>
 * 
 * @author Scott MacKenzie, 2012-2014
 */
public class WilcoxonSignedRank
{
	public static void main(String[] args) throws IOException
	{
		boolean vOption = false;
		double z1, z2, p1, p2;

		if (args.length < 1)
		{
			System.out.println("------------------------------------------------------------------------\n"
					+ "Usage: java WilcoxonSignedRank file [-v]\n" + "\n"
					+ "  where \'file\' contains a data table (comma or space delimited)\n"
					+ "        [-v]   = verbose (debugging and other information)\n"
					+ "------------------------------------------------------------------------\n");
			System.exit(0);
		}

		String file = args[0];
		if (args.length == 2 && args[1].equals("-v"))
			vOption = true;

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e)
		{
			System.out.println("File not found: " + file);
			System.exit(0);
		}

		String s = null;
		Vector<String> v = new Vector<String>();
		double[][] data;
		StringTokenizer st;
		int numberOfColumns = -1;

		// read the data
		try
		{
			while ((s = br.readLine()) != null)
				v.add(s);
		} catch (IOException e)
		{
			System.out.println("IOException: " + e);
			System.exit(0);
		}

		// determine number of rows and columns and, hence, N (the number of sample observations)
		int nRows = v.size();
		String tmp = v.elementAt(0);
		int nCols = (new StringTokenizer(tmp)).countTokens();

		if (nCols != 2)
		{
			System.out.println("Data format error.  Two columns required.");
			System.exit(0);
		}

		// create data matrix
		data = new double[nRows][nCols];

		// fill data matrix
		for (int i = 0; i < data.length; ++i)
		{
			st = new StringTokenizer(v.elementAt(i));
			if (numberOfColumns == -1)
				numberOfColumns = st.countTokens();
			else if (numberOfColumns != 2)
			{
				System.out.println("Error: Data format error.  Columns = " + st.countTokens());
				System.exit(0);
			}

			int j = 0;
			while (st.hasMoreTokens())
				data[i][j++] = Double.parseDouble(st.nextToken());
		}

		if (vOption)
		{
			System.out.println("Raw data...");
			for (int i = 0; i < data.length; ++i)
			{
				for (int j = 0; j < data[0].length; ++j)
					System.out.print(data[i][j] + ", ");
				System.out.println();
			} // data matrix loaded!
			System.out.println();
		}

		WilcoxonSignedRank wsr = new WilcoxonSignedRank(data, vOption);

		z1 = wsr.getZ1();
		z2 = wsr.getZ2();
		p1 = wsr.getP1();
		p2 = wsr.getP2();

		System.out.printf("%.4f\n",p1);
		System.out.printf("%.4f\n", p2);
	}

	public WilcoxonSignedRank(double[][] dataArg, boolean vOption)
	{
		double[][] data = new double[dataArg.length][dataArg[0].length];
		for (int i = 0; i < data.length; ++i)
			for (int j = 0; j < data[0].length; ++j)
				data[i][j] = dataArg[i][j];

		int nRows = data.length;
		int nCols = data[0].length;
		int ns = nRows * nCols;

		double[] rowMeans = new double[data.length];
		for (int i = 0; i < data.length; ++i)
			rowMeans[i] = mean(data[i]);

		double[] colMeans = new double[data[0].length];
		for (int i = 0; i < data[0].length; ++i)
		{
			for (int j = 0; j < data.length; ++j)
				colMeans[i] += data[j][i];
			colMeans[i] /= data.length;
		}

		if (vOption)
		{
			System.out.printf("rows = %d\n", nRows);
			System.out.printf("columns = %d\n", nCols);
			System.out.printf("N = %d (number of sample observations)\n", ns);
			System.out.print("row means = ");
			for (int i = 0; i < rowMeans.length; ++i)
				System.out.print(rowMeans[i] + ", ");
			System.out.println();
			System.out.print("column means = ");
			for (int i = 0; i < colMeans.length; ++i)
				System.out.print(colMeans[i] + ", ");
			System.out.println("\n");
		}

		double[] aMinusB = new double[nRows];
		double[] absAMinusB = new double[nRows];
		for (int i = 0; i < aMinusB.length; ++i)
		{
			aMinusB[i] = data[i][0] - data[i][1];
			absAMinusB[i] = Math.abs(aMinusB[i]);
		}

		if (vOption)
		{
			printArray(aMinusB, "Ai - Bi = ");
			printArray(absAMinusB, "abs(Ai - Bi) = ");
			System.out.println();
		}

		int zeroCount = 0;
		for (int i = 0; i < absAMinusB.length; ++i)
			if (absAMinusB[i] == 0)
				++zeroCount;

		double[] temp = new double[absAMinusB.length - zeroCount];
		double[] sign = new double[absAMinusB.length - zeroCount];
		for (int i = 0, j = 0; i < absAMinusB.length; ++i)
		{
			if (absAMinusB[i] != 0)
			{
				temp[j] = absAMinusB[i];
				sign[j] = absAMinusB[i] / aMinusB[i];
				++j;
			}
		}

		double[] rank = new double[temp.length];
		rank = getRanks(temp);

		double[] signedRank = new double[rank.length];
		for (int i = 0; i < signedRank.length; ++i)
			signedRank[i] = sign[i] * rank[i];

		if (vOption)
		{
			printArray(rank, "ranks = ");
			printArray(signedRank, "signed ranks = ");
			System.out.println();
		}

		double sp = 0.0; // sum of positive ranks
		double sn = 0.0; // sum of negative ranks
		for (int i = 0; i < signedRank.length; ++i)
			if (signedRank[i] > 0)
				sp += signedRank[i];
			else
				sn += signedRank[i];

		double t = Math.min(sp, Math.abs(sn));
		double n = signedRank.length;
		double c = correctionForTies(rank);

		if (vOption)
		{
			System.out.printf("t = %f (sum[Ti^3-Ti])\n", t);
			System.out.printf("n = %f (number of ranks)\n", n);
			System.out.printf("c = %f (correction for ties)\n\n", c);
		}

		z1 = (t - n * (n + 1) / 4.0) / Math.sqrt((n * (n + 1) * (2 * n + 1)) / 24);
		p1 = 2.0 * Statistics.normalProbability(z1);

		z2 = (t - n * (n + 1) / 4.0) / Math.sqrt((n * (n + 1) * (2 * n + 1)) / 24 - c);
		p2 = 2.0 * Statistics.normalProbability(z2);

		double r1 = Math.abs(z1) / Math.sqrt(ns); // effect size
		double r2 = Math.abs(z2) / Math.sqrt(ns); // effect size
		if (vOption)
		{
			System.out.printf("r = %.3f (effect size: r = abs(z) / sqrt(N))\n", r1);
			System.out.printf("r' = %.3f (effect size: r' = abs(z') / sqrt(N))\n\n", r2);
		}
	}

	private double z1, z2, p1, p2;

	/**
	 * Return the <i>z</i> statistic for this <code>WilcoxonSignedRank</code> object.
	 * 
	 * @return the <i>z</i> statistic
	 */
	public double getZ1()
	{
		return z1;
	}

	/**
	 * Return the <i>z</i> statistic for this <code>WilcoxonSignedRank</code> object. The statistic
	 * is corrected for ties.
	 * 
	 * @return the <i>z</i> statistic (correct for ties).
	 */
	public double getZ2()
	{
		return z2;
	}

	/**
	 * Return the <i>p</i> statistic (probability) for this <code>WilcoxonSignedRank</code> object.
	 * 
	 * @return the <i>p</i> statistic
	 */
	public double getP1()
	{
		return p1;
	}

	/**
	 * Return the <i>p</i> statistic (probability) for this <code>WilcoxonSignedRank</code> object.
	 * The statistic is corrected for ties.
	 * 
	 * @return the <i>p</i> statistic (corrected for ties)
	 */
	public double getP2()
	{
		return p2;
	}

	/**
	 * Given an array of data, return an array of ranks (handles ties). The order of the original
	 * data is preserved. As an example, if the passed array contains
	 * 
	 * <pre>
	 *      25, 75, 80, 81, 75
	 * </pre>
	 * 
	 * the returned array contains
	 * 
	 * <pre>
	 *      1.0, 2.5, 4.0, 5.0, 2.5
	 * </pre>
	 * 
	 * @param d
	 *            a double array
	 * @return an array of ranks
	 */
	public static double[] getRanks(double[] d)
	{
		// put raw ranks in 'r' array
		double[] r = new double[d.length];

		// create a copy of 'd' array
		double[] a = new double[d.length];
		for (int i = 0; i < a.length; ++i)
			a[i] = d[i];
		Arrays.sort(a); // sort, but leave values intact

		for (int i = 0; i < a.length; ++i)
		{
			double rawScore = a[i];
			int rawRank = i + 1; // raw rank
			int count = 1;
			int j = i + 1;
			while (j < a.length) // look for ties (potentially to end of array)
			{
				if (rawScore == a[j]) // found a tie
				{
					rawRank += j + 1;
					++count;
					++j;
				} else
					// no tie, skip to next position in array
					break;
			}
			double rank = (double)rawRank / count;
			r[i] = rank;
			while (--count > 0) // continue to fill slots with ties
			{
				++i;
				r[i] = rank;
			}
		}

		// use a (raw scores, sorted), r (ranks, sorted), and d (raw scores, unsorted) to build rr
		double[] rr = new double[d.length];

		for (int i = 0; i < d.length; ++i)
		{
			for (int j = 0; j < d.length; ++j)
			{
				if (d[i] == a[j])
				{
					rr[i] = r[j];
					break;
				}
			}
		}
		return rr;
	}

	// compute correction factor (used in the formula for z2; see above)
	private static double correctionForTies(double[] rArg)
	{
		double c = 0.0; // correction for ties
		double factor = 0.0;

		double[] r = new double[rArg.length];
		for (int i = 0; i < r.length; ++i)
			r[i] = rArg[i];
		Arrays.sort(r);

		for (int i = 0; i < r.length; ++i)
		{
			double rank = r[i];
			int j = i + 1;
			int countRanks = 1;
			while (j < r.length)
			{
				if (rank == r[j])
				{
					// System.out.println("FOUND TIE: i=" + i + ", j=" + j + ", k=" + k + ", rank="
					// + rank);
					++countRanks;
					++i;
					++j;
				} else
					break;
			}
			if (countRanks > 1)
				factor += countRanks * countRanks * countRanks - countRanks;
		}

		c = factor / 48.0;
		return c;
	}

	private static void printArray(double[] d, String s)
	{
		System.out.print(s);
		for (int i = 0; i < d.length; ++i)
			System.out.print(d[i] + ", ");
		System.out.println();
	}

	/**
	 * Calculate the mean of the values in a double array.
	 * 
	 * @param n
	 *            a double array
	 * @return a double equal to the mean of the values in the array.
	 */
	public static double mean(double n[])
	{
		double mean = 0.0;
		for (int j = 0; j < n.length; j++)
			mean += n[j];
		return mean / n.length;
	}
}
