package org.xtream.demo.mobile.model.root;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.Equivalence;

public class EquivalenceComponent extends Component
{
	@SuppressWarnings("unchecked")
	public EquivalenceComponent(ModulesContainer modules)
	{
		int modulesLength = modules.modules.length;
		chargeStateRelativeInputs = new Port[modulesLength];
		chargeStateRelative = new ChannelExpression[modulesLength];
		
		for (int i = 0; i < modulesLength; i++)
		{
			chargeStateRelativeInputs[i] = new Port<>();
			VehicleContainer vehicleModule = (VehicleContainer) modules.modules[i];
			chargeStateRelative[i] = new ChannelExpression<>(chargeStateRelativeInputs[i], vehicleModule.context.shortestPathLengthIndexOutput);
		}
	}
	
	// Inputs

	public Port<Double>[] chargeStateRelativeInputs;
	
	// Outputs
	
	public Port<Double> equivalenceAvgDistance1Output = new Port<>();
	public Port<Double> equivalenceAvgDistance2Output = new Port<>();
	public Port<Double> equivalenceAvgDistance3Output = new Port<>();
	
	public Port<Double> equivalenceVarDistance1Output = new Port<>();
	public Port<Double> equivalenceVarDistance2Output = new Port<>();
	public Port<Double> equivalenceVarDistance3Output = new Port<>();
	
	// Channels
	
	public ChannelExpression<Double>[] chargeStateRelative;
	
	// Equivalences
	
	public Equivalence equivalenceAvgDistance1 = new Equivalence(equivalenceAvgDistance1Output);
	public Equivalence equivalenceAvgDistance2 = new Equivalence(equivalenceAvgDistance2Output);
	public Equivalence equivalenceAvgDistance3 = new Equivalence(equivalenceAvgDistance3Output);
	
	public Equivalence equivalenceVarDistance1 = new Equivalence(equivalenceVarDistance1Output);
	public Equivalence equivalenceVarDistance2 = new Equivalence(equivalenceVarDistance2Output);
	public Equivalence equivalenceVarDistance3 = new Equivalence(equivalenceVarDistance3Output);
	
	// Expressions
	
	public Expression<Double> equivalenceAvgDistance1Expression = new Expression<Double>(equivalenceAvgDistance1Output)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			int size = chargeStateRelativeInputs.length/3;
			
			double sum = 0;
			
			for (int i = 0; i < 20; i++)
			{
				sum += chargeStateRelativeInputs[i].get(state, timepoint);
			}
			
			return sum/size;
		}
	};
	
	public Expression<Double> equivalenceAvgDistance2Expression = new Expression<Double>(equivalenceAvgDistance2Output)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			int size = chargeStateRelativeInputs.length/3;
			
			double sum = 0;
			
			for (int i = 20; i < 40; i++)
			{
				sum += chargeStateRelativeInputs[i].get(state, timepoint);
			}
			
			return sum/size;
		}
	};
	
	public Expression<Double> equivalenceAvgDistance3Expression = new Expression<Double>(equivalenceAvgDistance3Output)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			int size = chargeStateRelativeInputs.length/3;
			
			double sum = 0;
			
			for (int i = 40; i < 60; i++)
			{
				sum += chargeStateRelativeInputs[i].get(state, timepoint);
			}
			
			return sum/size;
		}
	};
	
	public Expression<Double> equivalenceVarDistance1Expression = new Expression<Double>(equivalenceVarDistance1Output)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			
			int size = chargeStateRelativeInputs.length/3;
			
			double sum = 0;
			
			for (int i = 0; i < 20; i++)
			{
				sum += Math.pow(chargeStateRelativeInputs[i].get(state, timepoint) - equivalenceAvgDistance1Output.get(state, timepoint),2);
			}
			
			return sum/size;
		}
	};
	
	public Expression<Double> equivalenceVarDistance2Expression = new Expression<Double>(equivalenceVarDistance2Output)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			
			int size = chargeStateRelativeInputs.length/3;
			
			double sum = 0;
			
			for (int i = 20; i < 40; i++)
			{
				sum += Math.pow(chargeStateRelativeInputs[i].get(state, timepoint) - equivalenceAvgDistance2Output.get(state, timepoint),2);
			}
			
			return sum/size;
		}
	};
	
	public Expression<Double> equivalenceVarDistance3Expression = new Expression<Double>(equivalenceVarDistance3Output)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			
			int size = chargeStateRelativeInputs.length/3;
			
			double sum = 0;
			
			for (int i = 40; i < 60; i++)
			{
				sum += Math.pow(chargeStateRelativeInputs[i].get(state, timepoint) - equivalenceAvgDistance3Output.get(state, timepoint),2);
			}
			
			return sum/size;
		}
	};
	
}
