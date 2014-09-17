package org.xtream.core.utilities.monitors;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.Key;

public class CSVMonitor<T extends Component> implements Monitor<T>
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
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		NumberFormat format = NumberFormat.getInstance();
		
		out.print(timepoint + ";" + statistics.generatedStates + ";" + statistics.validStates + ";" + statistics.preferredStates + ";" + equivalenceClasses.size() + ";");
		out.print(format.format(statistics.minObjective) + ";" + format.format(statistics.avgObjective) + ";" + format.format(statistics.maxObjective) + ";");
		out.print(Memory.maxMemory() + ";" + Memory.totalMemory() + ";" + Memory.freeMemory() + ";");
		out.print(statistics.branch + ";" + statistics.norm + ";" + statistics.cluster + ";" + statistics.sort + ";" + statistics.stats + ";");
		out.print(System.currentTimeMillis() - start);
		out.println();
	}

	@Override
	public void stop()
	{	
		out.close();
	}

}
