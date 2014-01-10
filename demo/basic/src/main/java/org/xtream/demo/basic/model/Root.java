package org.xtream.demo.basic.model;

import org.xtream.core.model.Channel;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.optimizer.Engine;
import org.xtream.demo.basic.model.system.Integrate;
import org.xtream.demo.basic.model.system.Random;

public class Root extends Component
{

	public static void main(String[] args)
	{
		new Engine(Root.class).run(96, 100, 0.5);
	}
	
	// Ports
	
	@Constraint
	public Port<Boolean> maximum = new Port<Boolean>()
	{
		@Override
		protected Boolean evaluate(int timepoint)
		{
			return random.output.get(timepoint) < 3.;
		}

	};
	
	// Components
	
	public Random random = new Random();
	public Integrate integrate = new Integrate();
	
	// Channels
	
	public Channel<Double> channel = new Channel<Double>(random.output, integrate.input);
	
}
