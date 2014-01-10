package org.xtream.demo.basic.model.system;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.ports.SimpleRandomPort;

public class Random extends Component
{
	
	//
	// PORTS
	//
	
	
	
	public Port<Double> output = new SimpleRandomPort<Double>()
	{
		@Override
		protected Set<Double> evaluateSet(int timepoint)
		{
			Set<Double> set = new HashSet<>();
			
			set.add(1.);
			set.add(2.);
			set.add(3.);
			
			return set;
		}
	};
	
	
	
	//
	// COMPONENTS
	//
	
	
	
	//
	// CHANNELS
	//
	
	
	
}
