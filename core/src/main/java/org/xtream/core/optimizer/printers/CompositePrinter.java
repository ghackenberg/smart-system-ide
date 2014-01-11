package org.xtream.core.optimizer.printers;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;

public class CompositePrinter<T extends Component> extends Printer<T>
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

	@Override
	public void print(T component, int timepoint)
	{
		for (Printer<T> printer : printers)
		{
			printer.print(component, timepoint);
		}
	}

}
