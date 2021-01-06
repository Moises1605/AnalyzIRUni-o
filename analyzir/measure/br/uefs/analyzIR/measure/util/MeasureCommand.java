package br.uefs.analyzIR.measure.util;

import java.io.File;
import java.io.IOException;

import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;

/**
 * 
 * @author lucas
 *
 */
public class MeasureCommand {

	private ConfigValues config;
	private MeasureFileReader mReader; 
	private static MeasureCommand command; 

	private MeasureCommand() {
		mReader = new MeasureFileReader();
		config = new ConfigValues();
	}
	
	public static MeasureCommand getInstance(){
		
		if(command == null)
			command = new MeasureCommand();
		return command;
	}

	public MeasureSet executeCommand(String command)
			throws IOException, InterruptedException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException {
		
		File outFile = new File(config.getOutputFileURL());
		LinuxCommand shell = new LinuxCommand();
		shell.executeCommand(command, config.getDirectory(),
				config.getOutputFileURL());
		return mReader.readSingleLine(outFile);
	}
	
	public MeasureSet executeCommand(String command, String [] topcis)
			throws IOException, InterruptedException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException {

		File outFile = new File(config.getOutputFileURL());
		LinuxCommand shell = new LinuxCommand();
		shell.executeCommand(command, config.getDirectory(),
				config.getOutputFileURL());
		
		return mReader.readLines(outFile, topcis);
	}
	
	public MeasureSet executeCommand(String command, String[] topic,
			String atValue) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException {

		File outFile = new File(config.getOutputFileURL());
		LinuxCommand shell = new LinuxCommand();
		shell.executeCommand(command, config.getDirectory(),
				config.getOutputFileURL());

		return mReader.readLines(outFile, topic, atValue);
	}

	public MeasureSet executeCommand(String command, String[] topic,
			String[] xValues) throws IOException, InterruptedException,
			NumberFormatException, InvalidItemNameException,
			ItemNotFoundException {

		File outFile = new File(config.getOutputFileURL());
		LinuxCommand shell = new LinuxCommand();
		shell.executeCommand(command, config.getDirectory(),
				config.getOutputFileURL());

		return mReader.readLines(outFile, topic, xValues);
	}
}
