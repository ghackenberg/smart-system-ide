package org.xtream.demo.infrastructure.model.scenario.factories;

import java.util.HashSet;

import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.model.scenario.factories.profiles.Car;
import org.xtream.demo.infrastructure.model.transportation.CarComponent;

public class CarFactory {

	private Graph context;
	private HashSet<Edge> chargingStationPositions;
	private HashSet<Edge> origins;
	private HashSet<Edge> destinations;
	
	public CarFactory(Graph context, HashSet<Edge> chargingStationPositions)
	{
		this.context = context;
		this.chargingStationPositions = chargingStationPositions;
		origins = new HashSet<Edge>();
		destinations = new HashSet<Edge>();
		
		for (Edge iterator : context.getEdges())
		{
			if (iterator.toString().contains("Origin"))
			{
				if (!origins.contains(iterator)) origins.add(iterator);
			}
			
			if (iterator.toString().contains("Destination"))
			{
				if (!destinations.contains(iterator)) destinations.add(iterator);
			}
		}
	}
	
	public CarComponent generateCar(Edge startPosition, Edge destinationPosition, Double stateOfChargeWeight, Double timeWeight, Double powerWeight, Double priority, Double vehicleLength, Double vehicleWeight, Double enginePerformance, Double engineEfficiency, Double stateOfCharge, Double stateOfChargeInputRate, Double stateOfChargeOutputRate, Double stateOfChargeMinimum, Double stateOfChargeMaximum, Double vehicleRangeAnxiety, Double chargingStationSelectionRandomness) 
	{
		return new CarComponent(context, startPosition, destinationPosition, chargingStationPositions, stateOfChargeWeight, timeWeight, powerWeight, priority, vehicleLength, vehicleWeight, enginePerformance, engineEfficiency, stateOfCharge, stateOfChargeInputRate, stateOfChargeOutputRate, stateOfChargeMinimum, stateOfChargeMaximum, vehicleRangeAnxiety, chargingStationSelectionRandomness);
	}
	
	public CarComponent generateCar(Edge startPosition, Edge destinationPosition, Car car) 
	{	
		return new CarComponent(context, startPosition, destinationPosition, chargingStationPositions, car.stateOfChargeWeight(), car.timeWeight(), car.powerWeight(), car.priority(), car.vehicleLength(), car.vehicleWeight(), car.enginePerformance(), car.engineEfficiency(), car.stateOfCharge(), car.stateOfChargeInputRate(), car.stateOfChargeOutputRate(), car.stateOfChargeMinimum(), car.stateOfChargeMaximum(), car.vehicleRangeAnxiety(), car.chargingStationSelectionRandomness());
	}
	
	public CarComponent generateRandomCar(Edge startPosition, Edge destinationPosition, Car car) 
	{
		return new CarComponent(context, startPosition, destinationPosition, chargingStationPositions, car.timeWeight(), 1., 1., 1., 0.002, 2., 1., 1., 85., 5., 5., 0., 85., 0.2, 0.);
	}

}
