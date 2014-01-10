package org.xtream.demo.basic.model.system;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.model.ports.ConnectablePort;

public class Integrate extends Component
{
	
	///////////
	// PORTS //
	///////////
	
	// CONNECTABLE PORTS
	
	public ConnectablePort<Double> input = new ConnectablePort<>();
	
	// NON-CONNECTABLE PORTS
	
	@Objective(Direction.MIN) public Port<Double> output = new Port<Double>()
	{
		public double previous = 0;
		
		@Override protected Double evaluate(int timepoint)
		{
			return previous += input.get(timepoint);
		}
	};
	
	////////////////
	// COMPONENTS //
	////////////////
	
	/* none */
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
}
