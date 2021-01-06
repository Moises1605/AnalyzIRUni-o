package br.uefs.analyzIR.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import br.uefs.analyzIR.measure.util.LinuxCommand;

public class MacKenzieWilcoxonSignedRankTest {

	private double [] value1;
	private double [] value2;
	private ConfigValues config;

	public MacKenzieWilcoxonSignedRankTest (double[] value1, double[] value2) {
		this.value1 = value1;
		this.value2 = value2;
		this.config = new ConfigValues();
	}

	public void createFile() throws IOException{

		String directory = this.config.getDirectory();
		String result = directory + File.separator + "value.txt";

		System.out.println("Result "+ result);

		File f1 = new File(result);
		FileWriter fw = new FileWriter(f1);

		for(int i = 0; i < value1.length; i++){
			String line = ""+value1[i]+"\t"+value2[i]+"\n";
			fw.write(line);
		}

		fw.close();
	}

	public double runTest() throws IOException, InterruptedException {

		if(Arrays.equals(value1, value2)){
			return 0;

		}else{

			LinuxCommand command = new LinuxCommand();
			String directory = this.config.getDirectory();
			String outputFile = directory + File.separator + "result.txt";
			String enter = directory + File.separator + "value.txt";
			System.out.println(enter);
			command.executeCommand("java WilcoxonSignedRank " + enter, directory, outputFile);

			File result = new File(outputFile);
			BufferedReader bReader = new BufferedReader(new FileReader(result));
			String line = bReader.readLine().replace(',', '.');

			double pValue = Double.parseDouble(line);

			bReader.close();

			return pValue;
		}
	}
}