package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Container;

public class ModulesContainer extends Container
{
	
	// Parameters
	
	public Graph graph;
    public GenericModuleContainer[] modules;

	private static VehicleContainer[] getModules(int size, Graph graph)
	{
		VehicleContainer[] modules = new VehicleContainer[size];
		
		// Vehicles
		
		for (int i = 0; i < size; i++) 
		{
			if (i < size / 3.0)
			{
				modules[i] = new VehicleContainer(graph, "OriginA", "DestinationA", 1., 1.);		
			}
			else if(i >= size / 3.0 && i < size * 2.0 / 3.0)
			{
				modules[i] = new VehicleContainer(graph, "OriginB", "DestinationB", 1., 1.);		
			}
			else
			{
				modules[i] = new VehicleContainer(graph, "OriginC", "DestinationC", 1., 1.);		
			}
		}
		
		return modules;
	}
	
	public ModulesContainer(int size, Graph graph)
	{
		this(graph, getModules(size, graph));
	}
	

	public ModulesContainer(Graph graph, VehicleContainer[] vehicleContainers)
	{
        modules = vehicleContainers;
	}


}
