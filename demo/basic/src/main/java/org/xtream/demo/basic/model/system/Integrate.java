package org.xtream.demo.basic.model.system;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.ports.ConnectablePort;

public class Integrate extends Component
{
	
	public ConnectablePort<Double> input = new ConnectablePort<>();
	
	public Port<Double> output = new Port<Double>()
	{
		public double previous = 0;
		
		@Override
		protected Double evaluate(int timepoint)
		{
			return previous += input.get(timepoint);
		}
	};

}
