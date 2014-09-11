package org.xtream.demo.projecthouse.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import au.com.bytecode.opencsv.CSVReader;

public class CSVFileWithOneKey {
	
	private static final char CHAR_SEPARATOR = ';';
	private String filepath;
	private TreeMap<Double, Double[]> data;
	private final int nrOfColumns;

	public CSVFileWithOneKey(File file, int nrOfColumns) {
		filepath = file.getAbsolutePath();
		this.nrOfColumns = nrOfColumns;
		data = readValues(file);
	}

	private TreeMap<Double, Double[]> readValues(File file) {
		TreeMap<Double, Double[]> result = new TreeMap<>();
		try (FileReader fr = new FileReader(file); CSVReader reader = new CSVReader(fr, CHAR_SEPARATOR);) {
			List<String[]> rows = reader.readAll();
			for(String[] row : rows) {
				double key = Double.parseDouble(row[0]);
				Double[] rowValues = new Double[nrOfColumns];
				for(int i = 0; i < nrOfColumns; i++) {
					rowValues[i] = Double.parseDouble(row[i+1]);
				}
				result.put(key, rowValues);
			}
			return result;
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("File " + file.getAbsolutePath() + " not found!", fnfe);
		} catch (IOException ioe) {
			throw new RuntimeException("Problems reading file " + filepath, ioe);
		}
	}

	public Double get(int key, int column) {
		if(column > nrOfColumns) {
			throw new IllegalArgumentException("The csv data does not have " + column + " columns, only " + nrOfColumns);
		}
		Double[] rowValues = data.floorEntry((double) key).getValue();
		if(rowValues == null) {
			throw new RuntimeException("No value for key " + key  + " found in file " + filepath);
		}
		else {
			return rowValues[column-1];
		}
	}
}
