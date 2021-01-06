package br.uefs.analyzIR.measure.util;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author lucas
 *
 */
public class LinuxCommand {
	
	public void executeCommand(String command, String directory, String output)
			throws IOException, InterruptedException {
		File dir = new File(directory);
		File out = new File(output);
		
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
		
		builder.directory(dir);
		builder.redirectOutput(out);
		Process process = builder.start();
		process.waitFor();
	}
}
