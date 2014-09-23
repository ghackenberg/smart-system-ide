package org.xtream.demo.hydro.model;

import org.xtream.demo.hydro.data.Dataset;
import org.xtream.demo.hydro.data.DatasetParser;

public class Constants
{

	public static Dataset DATASET_2011 = DatasetParser.parseDataSilent("csv/Regression_2011.csv");
	public static Dataset DATASET_2012 = DatasetParser.parseDataSilent("csv/Regression_2012.csv");
	public static Dataset DATASET_2013 = DatasetParser.parseDataSilent("csv/Regression_2013.csv");
	
	public static int HOUR = 4;
	public static int DAY = HOUR * 24;
	public static int WEEK = DAY * 7;
	
	public static int DURATION = WEEK * 1;
	public static boolean PRUNE = true;
	public static int SAMPLES = PRUNE ? 100 : 1;
	public static int CLUSTERS = PRUNE ? 200 : 1;
	public static double RANDOM = 0;
	public static double CACHING = PRUNE ? 0 : 1;
	public static int ROUNDS = PRUNE ? 1000 : 1;
	
	public static Dataset DATASET = DATASET_2012;
	public static int START = WEEK * 13;
	
	public static double SPEICHERSEE_LEVEL_MAX = 5;
	public static double VOLUMEN1_LEVEL_MAX = 1.6;
	public static double VOLUMEN2_LEVEL_MAX = 2.1;
	public static double VOLUMEN3_LEVEL_MAX = 2.9;
	public static double VOLUMEN4_LEVEL_MAX = 3.4;
	
	public static double HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX = 30;
	public static double WEHR1_TURBINE_DISCHARGE_MAX = 24;
	public static double WEHR2_TURBINE_DISCHARGE_MAX = 24;
	public static double WEHR3_TURBINE_DISCHARGE_MAX = 24;
	public static double WEHR4_TURBINE_DISCHARGE_MAX = 7.2;
	
	public static double HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX = 66;//43;0
	public static double WEHR1_WEIR_DISCHARGE_MAX = 91.8;//69.1;44.2
	public static double WEHR2_WEIR_DISCHARGE_MAX = 89;//73.2;6.2
	public static double WEHR3_WEIR_DISCHARGE_MAX = 106.6;//86.2;40
	public static double WEHR4_WEIR_DISCHARGE_MAX = 102.8;//71.8;32.2
	
	public static double HAUPTKRAFTWERK_DISCHARGE_MAX = HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX + HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX;
	public static double WEHR1_DISCHARGE_MAX = WEHR1_TURBINE_DISCHARGE_MAX + WEHR1_WEIR_DISCHARGE_MAX;
	public static double WEHR2_DISCHARGE_MAX = WEHR2_TURBINE_DISCHARGE_MAX + WEHR2_WEIR_DISCHARGE_MAX;
	public static double WEHR3_DISCHARGE_MAX = WEHR3_TURBINE_DISCHARGE_MAX + WEHR3_WEIR_DISCHARGE_MAX;
	public static double WEHR4_DISCHARGE_MAX = WEHR4_TURBINE_DISCHARGE_MAX + WEHR4_WEIR_DISCHARGE_MAX;
	
	public static int HAUPTKRAFTWERK_TURBINE_STEPS = 2;
	public static int WEHR1_TURBINE_STEPS = 2;
	public static int WEHR2_TURBINE_STEPS = 2;
	public static int WEHR3_TURBINE_STEPS = 2;
	public static int WEHR4_TURBINE_STEPS = 20;

	public static int HAUPTKRAFTWERK_WEIR_STEPS = 2;
	public static int WEHR1_WEIR_STEPS = 2;
	public static int WEHR2_WEIR_STEPS = 2;
	public static int WEHR3_WEIR_STEPS = 2;
	public static int WEHR4_WEIR_STEPS = 20;
	
	public static int WEHR4_STEPS = 10;

}
