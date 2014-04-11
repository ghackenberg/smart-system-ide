package org.xtream.demo.hydro.model;

public class Constants
{
	
	public static double SPEICHERSEE_AREA = 3800000;
	public static double VOLUMEN1_AREA = 13500;
	public static double VOLUMEN2_AREA = 12525;
	public static double VOLUMEN3_AREA = 15000;
	public static double VOLUMEN4_AREA = 48000;
	public static double VOLUMEN5_AREA = Double.MAX_VALUE;
	
	public static double SPEICHERSEE_LEVEL_MAX = 5;
	public static double VOLUMEN1_LEVEL_MAX = 1.6;
	public static double VOLUMEN2_LEVEL_MAX = 2.1;
	public static double VOLUMEN3_LEVEL_MAX = 2.9;
	public static double VOLUMEN4_LEVEL_MAX = 3.4;
	public static double VOLUMEN5_LEVEL_MAX = Double.MAX_VALUE;
	
	public static double HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX = 30;
	public static double WEHR1_TURBINE_DISCHARGE_MAX = 24;
	public static double WEHR2_TURBINE_DISCHARGE_MAX = 24;
	public static double WEHR3_TURBINE_DISCHARGE_MAX = 24;
	public static double WEHR4_TURBINE_DISCHARGE_MAX = 7.2;
	
	public static double HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX = 5;
	public static double WEHR1_WEIR_DISCHARGE_MAX = 5;
	public static double WEHR2_WEIR_DISCHARGE_MAX = 5;
	public static double WEHR3_WEIR_DISCHARGE_MAX = 5;
	public static double WEHR4_WEIR_DISCHARGE_MAX = 5;
	
	public static double HAUPTKRAFTWERK_DISCHARGE_MAX = HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX + HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX;
	public static double WEHR1_DISCHARGE_MAX = WEHR1_TURBINE_DISCHARGE_MAX + WEHR1_WEIR_DISCHARGE_MAX;
	public static double WEHR2_DISCHARGE_MAX = WEHR2_TURBINE_DISCHARGE_MAX + WEHR2_WEIR_DISCHARGE_MAX;
	public static double WEHR3_DISCHARGE_MAX = WEHR3_TURBINE_DISCHARGE_MAX + WEHR3_WEIR_DISCHARGE_MAX;
	public static double WEHR4_DISCHARGE_MAX = WEHR4_TURBINE_DISCHARGE_MAX + WEHR4_WEIR_DISCHARGE_MAX;

}
