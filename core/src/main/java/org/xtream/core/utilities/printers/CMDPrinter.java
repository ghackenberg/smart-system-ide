package org.xtream.core.utilities.printers;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.utilities.Printer;

public class CMDPrinter<T extends Component> implements Printer<T>
{

	@Override
	public void print(T component, int timepoint)
	{
		for (int i = 0; i < timepoint; i++)
		{
			System.out.println();
			System.out.println("Timepoint " + i);
			System.out.println();
			
			for (Port<?> port : component.portsRecursive)
			{
				System.out.println(port.qualifiedName + " = " + port.get(i));
			}
		}
	}

}
