package org.xtream.core.utilities.printers;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.utilities.Printer;

public class CompositePrinter<T extends Component> implements Printer<T>
{
	
	private List<Printer<T>> printers = new ArrayList<>();
	
	@SafeVarargs
	public CompositePrinter(Printer<T>... printers)
	{
		for (Printer<T> printer : printers)
		{
			this.printers.add(printer);
		}
	}
	
	public void add(Printer<T> printer)
	{
		printers.add(printer);
	}
	
	public void remove(Printer<T> printer)
	{
		printers.remove(printer);
	}

	@Override
	public void print(T component, State state, int timepoint)
	{
		for (Printer<T> printer : printers)
		{
			printer.print(component, state, timepoint);
		}
	}

}
