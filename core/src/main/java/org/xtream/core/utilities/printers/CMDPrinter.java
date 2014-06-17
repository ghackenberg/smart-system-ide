package org.xtream.core.utilities.printers;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;
import org.xtream.core.utilities.Printer;

public class CMDPrinter<T extends Component> implements Printer<T>
{

	@Override
	public void print(T component, State state, int timepoint)
	{
		for (int i = 0; i < timepoint; i++)
		{
			System.out.println();
			System.out.println("Timepoint " + i);
			System.out.println();
			
			for (Port<?> port : component.getDescendantsByClass(Port.class))
			{
				System.out.println(port.getQualifiedName() + " = " + port.get(state, i));
			}
		}
	}

}
