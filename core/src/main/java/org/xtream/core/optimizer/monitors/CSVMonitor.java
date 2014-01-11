package org.xtream.core.optimizer.monitors;

import java.io.PrintStream;

import org.xtream.core.optimizer.Monitor;

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
		out.println("Timepoint;Generated states;Valid states;Dominant states;Equivalence classes");
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, int equivalenceClasses)
	{
		out.println(timepoint + ";" + generatedStates + ";" + validStates + ";" + dominantStates + ";" + equivalenceClasses);
	}

	@Override
	public void stop()
	{
		out.close();
	}

}
