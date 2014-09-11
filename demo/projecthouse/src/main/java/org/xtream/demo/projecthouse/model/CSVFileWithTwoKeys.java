package org.xtream.demo.projecthouse.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import au.com.bytecode.opencsv.CSVReader;

public class CSVFileWithTwoKeys {
	
	private static final char CHAR_SEPARATOR = ';';
	private String filepath;
	private TreeMap<Double, TreeMap<Double,Double[]>> data;
	private final int nrOfColumns;
	
	public CSVFileWithTwoKeys(File file, int nrOfColumns) {
		filepath = file.getAbsolutePath();
		this.nrOfColumns = nrOfColumns;
		data = readValues(file);
	}

	private TreeMap<Double, TreeMap<Double, Double[]>> readValues(File file) {
		TreeMap<Double, TreeMap<Double, Double[]>> result = new TreeMap<>();
		try (FileReader fr = new FileReader(file); CSVReader reader = new CSVReader(fr, CHAR_SEPARATOR);) {
			List<String[]> rows = reader.readAll();
			for(String[] row : rows) {
				double primaryKey = Double.parseDouble(row[0]);
				double secondaryKey = Double.parseDouble(row[1]);
				Double[] rowValues = new Double[nrOfColumns];
				for(int i = 0; i < nrOfColumns; i++) {
					rowValues[i] = Double.parseDouble(row[i+1]);
				}
				TreeMap<Double, Double[]> rowMap = result.get(primaryKey);
				if(rowMap != null) {
					rowMap.put(secondaryKey, rowValues);
				}
				else {
					rowMap = new TreeMap<>();
					rowMap.put(secondaryKey, rowValues);
					result.put(primaryKey, rowMap);
				}
			}
			return result;
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("File " + file.getAbsolutePath() + " not found!", fnfe);
		} catch (IOException ioe) {
			throw new RuntimeException("Problems reading file " + filepath, ioe);
		}
	}

	public Double get(int primaryKey, int secondaryKey, int column) {
		if(column > nrOfColumns) {
			throw new IllegalArgumentException("The csv data does not have " + column + " columns, only " + nrOfColumns);
		}
		TreeMap<Double, Double[]> valueMap = data.floorEntry((double) primaryKey).getValue();
		if(valueMap == null) {
			throw new RuntimeException("No value for primary key " + primaryKey  + " found in file " + filepath);
		}
		else {
			Double[] values = valueMap.floorEntry((double) secondaryKey).getValue();
			return values[column-1];
		}
	}

}
