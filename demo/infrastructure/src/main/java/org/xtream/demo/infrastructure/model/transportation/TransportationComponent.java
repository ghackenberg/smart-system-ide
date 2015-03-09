package org.xtream.demo.infrastructure.model.transportation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Equivalence;
import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.model.EnergyComponent;

public class TransportationComponent extends EnergyComponent
{
	
	// Parameters
	
	public Graph context;
    public CarComponent[] cars;

	@SuppressWarnings("unchecked")
	public TransportationComponent(CarComponent[] cars, Graph context)
	{
		this.context = context;
		this.cars = cars;
		
		costInputs = new Port[cars.length];
		cost = new ChannelExpression[cars.length];
		shortestPathLengthIndexInputs = new Port[cars.length];
		shortestPathLengthIndex = new ChannelExpression[cars.length];
		
		positionTraversedLengthInputs = new Port[cars.length];
		positionInputs = new Port[cars.length];
		vehicleLengthInputs = new Port[cars.length];
		positionTraversedLength = new ChannelExpression[cars.length];
		position = new ChannelExpression[cars.length];
		vehicleLength = new ChannelExpression[cars.length];
		
		for (int i = 0; i < cars.length; i++)
		{
			costInputs[i] = new Port<>();
			cost[i] = new ChannelExpression<>(costInputs[i], cars[i].costOutput);
		
			positionTraversedLengthInputs[i] = new Port<>();
			positionTraversedLength[i] = new ChannelExpression<>(positionTraversedLengthInputs[i], cars[i].positionTraversedLengthOutput);
			
			positionInputs[i] = new Port<>();
			position[i] = new ChannelExpression<>(positionInputs[i], cars[i].positionOutput);
			
			vehicleLengthInputs[i] = new Port<>();
			vehicleLength[i] = new ChannelExpression<>(vehicleLengthInputs[i], cars[i].vehicleLengthOutput);
		}
		
		// Determine start/end positions for cars and according equivalences
		HashMap<String, HashSet<CarComponent>> startDestinationPairs = new HashMap<String, HashSet<CarComponent>>();
		final HashMap<CarComponent, Port<Double>> linkMap = new HashMap<CarComponent, Port<Double>>(); // stores references of cars to shortestPathLengthIndexInputs
		
		for (int i = 0; i < cars.length; i++)
		{
			shortestPathLengthIndexInputs[i] = new Port<>();
			shortestPathLengthIndex[i] = new ChannelExpression<>(shortestPathLengthIndexInputs[i], cars[i].shortestPathLengthIndexOutput);
			
			if (!startDestinationPairs.containsKey(cars[i].startPosition.toString() + cars[i].destinationPosition.toString()))
			{
				HashSet<CarComponent> set = new HashSet<CarComponent>();
				set.add(cars[i]);
				startDestinationPairs.put(cars[i].startPosition.toString() + cars[i].destinationPosition.toString(), set);
			}
			else 
			{
				startDestinationPairs.get(cars[i].startPosition.toString() + cars[i].destinationPosition.toString()).add(cars[i]);
			}
			
			linkMap.put(cars[i], shortestPathLengthIndexInputs[i]);
		}
		
		int startDestinationPairsSize = startDestinationPairs.size();
		
		// Group shortestPathLengthIndexes by car
		equivalenceAvgDistanceOutputs = new Port[startDestinationPairsSize];
		equivalenceAvgDistanceExpressions = new Expression[startDestinationPairsSize];
		equivalenceAvgDistances = new Equivalence[startDestinationPairsSize];
		
		int i = 0;
		for (final Entry<String, HashSet<CarComponent>> iterator : startDestinationPairs.entrySet())
		{
			equivalenceAvgDistanceOutputs[i] = new Port<>();
			
			equivalenceAvgDistanceExpressions[i] = new Expression<Double>(equivalenceAvgDistanceOutputs[i])
			{
				@Override protected Double evaluate(State state, int timepoint)
				{
					int size = iterator.getValue().size();
					
					double sum = 0;
					
					for (CarComponent car : iterator.getValue())
					{
						sum += linkMap.get(car).get(state, timepoint);
					}
					
					return sum/size;
				}
			};
			
			equivalenceAvgDistances[i] = new Equivalence(equivalenceAvgDistanceOutputs[i]);
			i++;
		}
		
		equivalenceVarDistanceExpressions = new Expression[startDestinationPairsSize];
		equivalenceVarDistanceOutputs = new Port[startDestinationPairsSize];
		equivalenceVarDistances = new Equivalence[startDestinationPairsSize];
		
		i = 0;
		
		for (final Entry<String, HashSet<CarComponent>> iterator : startDestinationPairs.entrySet())
		{
			final int[] value = {i};
			equivalenceVarDistanceOutputs[i] = new Port<>();
			
			equivalenceVarDistanceExpressions[i] = new Expression<Double>(equivalenceVarDistanceOutputs[i])
			{
				@Override protected Double evaluate(State state, int timepoint)
				{
					int size = iterator.getValue().size();
					
					double sum = 0;
					
					for (CarComponent car : iterator.getValue())
					{
						sum += Math.pow(linkMap.get(car).get(state, timepoint) - equivalenceAvgDistanceOutputs[value[0]].get(state, timepoint),2);
					}
					
					return sum/size;
				}
			};
			
			equivalenceVarDistances[i] = new Equivalence(equivalenceVarDistanceOutputs[i]);		
			i++;
		}
	}

	// Ports
	
	// Inputs
	
	public Port<Double>[] costInputs;
	public Port<Double>[] shortestPathLengthIndexInputs;
	public Port<Double>[] positionTraversedLengthInputs;
	public Port<Double>[] vehicleLengthInputs;
	public Port<Edge>[] positionInputs;
	
	// Outputs

	public Port<Double>[] equivalenceAvgDistanceOutputs;
	public Port<Double>[] equivalenceVarDistanceOutputs;

	
	
	// Constraints
	public Constraint validConstraint = new Constraint(validOutput);
	
	// Equivalences
	public Equivalence[] equivalenceAvgDistances;
	public Equivalence[] equivalenceVarDistances;
	
	// Channels
	public ChannelExpression<Double>[] shortestPathLengthIndex;
	public ChannelExpression<Double>[] cost;
	public ChannelExpression<Double>[] positionTraversedLength;
	public ChannelExpression<Edge>[] position;
	public ChannelExpression<Double>[] vehicleLength;

	// Expressions
	public Expression<Double>[] equivalenceAvgDistanceExpressions;
	public Expression<Double>[] equivalenceVarDistanceExpressions;
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < costInputs.length; i++)
			{
				sum += costInputs[i].get(state, timepoint);
			}
			
			return (timepoint == 0 ? 0 : sum);
		}
	};
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.;
		}
	};
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{	
		@Override public Boolean evaluate(State state, int timepoint)
		{	
			
			for (int i = 0; i < positionInputs.length; i++)
			{
				// Vehicle 1 Position
				Port<Edge> position = positionInputs[i];
	
				int maximumAllowedVehicles = (int) context.getEdgeWeight(position.get(state, timepoint).getName());
				
				for (int j = 0; j < positionInputs.length; j++)
				{	
					// Vehicle 2 Position
					Port<Edge> position2 = positionInputs[j];
					
					if (!(position.equals(position2)) && position.get(state, timepoint).equals(position2.get(state, timepoint)))
					{
						
						if (maximumAllowedVehicles <= 0)
						{
							return false;
						}
						
						if (Math.abs(positionTraversedLengthInputs[i].get(state, timepoint)-(positionTraversedLengthInputs[j].get(state, timepoint))) < (vehicleLengthInputs[i].get(state, timepoint)+vehicleLengthInputs[j].get(state, timepoint))) 
						{				
							maximumAllowedVehicles--;
						}	
					}
	
				}
				
			}

			return true;
		}
	};
}
