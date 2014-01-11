package org.xtream.core.optimizer.monitors;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;

public class CSVMonitor extends Monitor
{
	
	private PrintStream out;
	
	public CSVMonitor()
	{
		this(System.out);
	}
	
	public CSVMonitor(PrintStream out)
	{
		this.out = out;
	}

	@Override
	public void start()
	{
		out.println("Timepoint;Generated states;Valid states;Dominant states;Equivalence classes;Min objective;Avg objective;Max objective;Maximum memory (in MB);Total memory (in MB);Free memory (in MB)");
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, Map<Key, List<State>> equivalenceClasses)
	{
		NumberFormat format = NumberFormat.getInstance();
		
		out.print(timepoint + ";" + generatedStates + ";" + validStates + ";" + dominantStates + ";" + equivalenceClasses.size() + ";");
		out.print(format.format(minObjective) + ";" + format.format(avgObjective) + ";" + format.format(maxObjective) + ";");
		out.print(maxMemory() + ";" + totalMemory() + ";" + freeMemory());
		out.println();
	}

	@Override
	public void stop()
	{
		out.close();
	}

}
