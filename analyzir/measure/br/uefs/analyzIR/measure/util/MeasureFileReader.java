package br.uefs.analyzIR.measure.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.uefs.analyzIR.exception.InvalidItemNameException;
import br.uefs.analyzIR.exception.ItemNotFoundException;
import br.uefs.analyzIR.measure.data.MeasureSet;

/** This class read the output file to get a value */
public class MeasureFileReader {

	public MeasureSet readSingleLine(File output) throws IOException,
			InvalidItemNameException, ItemNotFoundException {

		MeasureSet topicSet; // hold the values by topic
		BufferedReader bReader;
		String line;

		topicSet = new MeasureSet();
		bReader = new BufferedReader(new FileReader(output));
		line = bReader.readLine();

		if (line != null) {

			Pattern pattern = Pattern.compile("(.+)(\\s+)(.+)(\\s+)(.+)");
			Matcher match = pattern.matcher(line);
			match.find();
			String topic = match.group(3);
			double value = Double.parseDouble(match.group(5));
			topicSet.addTopicResult(topic);
			topicSet.addValue(topic, value);
		}
		bReader.close();
		return topicSet;
	}

	public MeasureSet readLines(File output, String[] topicNumber)
			throws IOException, NumberFormatException,
			InvalidItemNameException, ItemNotFoundException {

		MeasureSet topicSet; // hold the values by topic
		BufferedReader bReader;
		String line;

		topicSet = new MeasureSet();
		bReader = new BufferedReader(new FileReader(output));
		line = bReader.readLine();

		for (int i = 0; topicNumber != null && i < topicNumber.length; i++)
			topicSet.addTopicResult(topicNumber[i]);

		if (line != null) {

			String topic, value;
			Pattern pattern;
			Matcher match;

			topic = value = "";
			pattern = Pattern.compile("(.+)(\\s+)(.+)(\\s+)(.+)");
			match = pattern.matcher(line);

			match.find();
			value = match.group(5);
			topic = match.group(3);
			for (int i = 0; i < topicNumber.length; i++) {
				if (topic.equals(topicNumber[i])) {
					topicSet.addValue(topic, Double.parseDouble(value));
				}
			}
			while ((line = bReader.readLine()) != null) {
				match = pattern.matcher(line);
				match.find();
				value = match.group(5);
				topic = match.group(3);
				for (int i = 0; i < topicNumber.length; i++) {
					if (topic.equals(topicNumber[i])) {
						topicSet.addValue(topic, Double.parseDouble(value));
					}
				}
			}
		}
		bReader.close();
		return topicSet;
	}

	public MeasureSet readLines(File output, String[] topicNumber, String xValue)
			throws IOException, InvalidItemNameException,
			NumberFormatException, ItemNotFoundException {

		MeasureSet topicSet; // hold the values by topic
		BufferedReader bReader;
		String line;

		topicSet = new MeasureSet();
		bReader = new BufferedReader(new FileReader(output));
		line = bReader.readLine();

		for (int i = 0; topicNumber != null && i < topicNumber.length; i++)
			topicSet.addTopicResult(topicNumber[i]);

		if (line != null) {

			String topic, value;
			Pattern pattern;
			Matcher match;

			topic = value = "";
			pattern = Pattern.compile("(.+)(\\s+)(.+)(\\s+)(.+)");
			match = pattern.matcher(line);

			match.find();
			value = match.group(5);
			topic = match.group(3);
			for (int i = 0; i < topicNumber.length; i++) {
				if (topic.equals(topicNumber[i])) {
					topicSet.addValue(topic, xValue, Double.parseDouble(value));
				}
			}
			while ((line = bReader.readLine()) != null) {
				match = pattern.matcher(line);
				match.find();
				value = match.group(5);
				topic = match.group(3);
				for (int i = 0; i < topicNumber.length; i++) {
					if (topic.equals(topicNumber[i])) {
						topicSet.addValue(topic, xValue, Double.parseDouble(value));
					}
				}
			}
		}
		bReader.close();
		return topicSet;
	}

	public MeasureSet readLines(File output, String[] topicNumber,
			String[] xValues) throws NumberFormatException, IOException,
			ItemNotFoundException, InvalidItemNameException {

		MeasureSet topicSet; // hold the values by topic
		BufferedReader bReader;
		String line;

		topicSet = new MeasureSet();
		bReader = new BufferedReader(new FileReader(output));
		line = bReader.readLine();

		for (int i = 0; topicNumber != null && i < topicNumber.length; i++)
			topicSet.addTopicResult(topicNumber[i]);

		if (line != null) {

			String topic, value, oldTopic;
			int count;
			Pattern pattern;
			Matcher match;

			topic = value = oldTopic = "";
			count = 0;
			pattern = Pattern.compile("(.+)(\\s+)(.+)(\\s+)(.+)");
			match = pattern.matcher(line);

			match.find();
			value = match.group(5);
			topic = match.group(3);
			for (int i = 0; i < topicNumber.length; i++) {
				if (topic.equals(topicNumber[i])) {
					topicSet.addValue(topic, xValues[count++],
							Double.parseDouble(value));
					oldTopic = topic;
				}
			}
			while ((line = bReader.readLine()) != null) {
				match = pattern.matcher(line);
				match.find();
				value = match.group(5);
				topic = match.group(3);
				for (int i = 0; i < topicNumber.length; i++) {
					if (topic.equals(topicNumber[i])) {
						if (oldTopic.equals(topic)) {
							topicSet.addValue(topic, xValues[count++],
										Double.parseDouble(value));
						}else {
							count = 0;
							topicSet.addValue(topic, xValues[count++],
										Double.parseDouble(value));
							oldTopic = topic;
						}
					}
				}
			}
		}
		bReader.close();
		return topicSet;
	}
}
