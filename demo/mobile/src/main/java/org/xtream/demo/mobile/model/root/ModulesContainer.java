package org.xtream.demo.mobile.model.root;

import org.xtream.core.model.Container;
import org.xtream.demo.mobile.datatypes.Graph;

public class ModulesContainer extends Container
{
	
	// Parameters
	
	public Graph graph;
    public GenericModuleContainer[] modules;

	private static VehicleContainer[] getModules(int size, Graph graph)
	{
		VehicleContainer[] modules = new VehicleContainer[size];
		
		// Vehicles
		/* Parameters VehicleContainer
		 * Graph graph (specifies Graph), 
		 * String startPosition (specifies start position on Graph), 
		 * String destinationPosition (specifies destination position on Graph), 
		 * Double timeResolution (specifies time resolution in min), 
		 * Double timeWeight,
		 * Double powerWeight, 
		 * Double chargeState (specifies charge state capacity in kWh, 
		 * Double chargeRate (specifies charging rate in kWh), 
		 * Double vMax (specifies maximum velocity in km/h), 
		 * Double mileage (specifies mileage in kWh), 
		 * Double vehicleLength (specifies vehicle length in km), 
		 * Double vehicleWidth (specifies vehicle length in km)
		 */
		
		for (int i = 0; i < size; i++) 
		{
			if (i < size / 3.0)
			{
				modules[i] = new VehicleContainer(graph, "OriginA", "DestinationA", 60., 1., 1., 85., 5.67, 200., 0.2353, 0.003, 0.002);		
			}
			else if(i >= size / 3.0 && i < size * 2.0 / 3.0)
			{
				modules[i] = new VehicleContainer(graph, "OriginB", "DestinationB", 60., 1., 1., 85., 5.67, 200., 0.2353, 0.003, 0.002);			
			}
			else
			{
				modules[i] = new VehicleContainer(graph, "OriginC", "DestinationC", 60., 1., 1., 85., 5.67, 200., 0.2353, 0.003, 0.002);			
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
