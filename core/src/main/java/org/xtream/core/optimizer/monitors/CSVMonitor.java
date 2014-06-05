package org.xtream.core.optimizer.monitors;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;

public class CSVMonitor implements Monitor
{
	
	private PrintStream out;
	private long start;
	
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
		start = System.currentTimeMillis();
		
		out.println("Timepoint;Generated states;Valid states;Dominant states;Equivalence classes;Min objective;Avg objective;Max objective;Maximum memory (in MB);Total memory (in MB);Free memory (in MB);Branch time (in ms);Norm time (in ms);Cluster time (in ms);Sort time (in ms);Stats time (in ms);Time (in ms)");
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, long branch, long norm, long cluster, long sort, long stats, Map<Key, List<State>> equivalenceClasses)
	{
		NumberFormat format = NumberFormat.getInstance();
		
		out.print(timepoint + ";" + generatedStates + ";" + validStates + ";" + dominantStates + ";" + equivalenceClasses.size() + ";");
		out.print(format.format(minObjective) + ";" + format.format(avgObjective) + ";" + format.format(maxObjective) + ";");
		out.print(Memory.maxMemory() + ";" + Memory.totalMemory() + ";" + Memory.freeMemory() + ";");
		out.print(branch + ";" + norm + ";" + cluster + ";" + sort + ";" + stats + ";");
		out.print(System.currentTimeMillis() - start);
		out.println();
	}

	@Override
	public void stop()
	{	
		out.close();
	}

}
