package org.xtream.core.optimizer.printers;

import java.io.PrintStream;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.Printer;

public class CSVPrinter<T extends Component> extends Printer<T>
{
	
	private PrintStream out;
	
	public CSVPrinter()
	{
		this(System.out);
	}
	
	public CSVPrinter(PrintStream out)
	{
		this.out = out;
	}

	@Override
	public void print(T component, int timepoint)
	{
		// Header
		
		out.print("Port");
		
		for (int i = 0; i < timepoint; i++)
		{
			out.print(";" + i);
		}
		
		// Body
		
		out.println();
		
		for (Port<?> port : component.portsRecursive)
		{
			out.print(port.name);
			
			for (int i = 0; i < timepoint; i++)
			{
				out.print(";" + port.get(i));
			}
			
			out.println();
		}
		
		// Close
		
		out.close();
	}

}
