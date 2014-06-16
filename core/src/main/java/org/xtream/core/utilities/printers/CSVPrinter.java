package org.xtream.core.utilities.printers;

import java.io.PrintStream;
import java.text.NumberFormat;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.utilities.Printer;

public class CSVPrinter<T extends Component> implements Printer<T>
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
			out.print(port.qualifiedName);
			
			for (int i = 0; i < timepoint; i++)
			{
				Object value = port.get(i);
				
				if (value instanceof Double)
				{
					out.print(";" + NumberFormat.getInstance().format((Double) value));
				}
				else
				{
					out.print(";" + port.get(i));
				}
			}
			
			out.println();
		}
		
		// Close
		
		out.close();
	}

}