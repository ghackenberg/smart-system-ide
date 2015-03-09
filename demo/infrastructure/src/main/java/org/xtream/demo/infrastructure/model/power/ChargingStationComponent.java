package org.xtream.demo.infrastructure.model.power;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.model.EnergyComponent;

public class ChargingStationComponent extends EnergyComponent
{

    // Parameters
	
	public Double modelscale;
	public Graph context;
	public Edge position;
	public Double chargeRate;
    
    // Previews

	public ChargingStationComponent(Graph context, Edge position, Double chargeRate)
	{
		this.context = context;
		this.position = position;
		this.chargeRate = chargeRate;
	}

	// Ports

	public Port<Edge> positionOutput = new Port<>();
	
	public Port<Double> chargeRateOutput = new Port<>();
	public Port<Double> chargeRateInput = new Port<>();
	
	public Port<Double> efficiencyOutput = new Port<>();
	public Port<Double> carConnectionOutput = new Port<>();
	
	// Constraints
	
	public Constraint chargeStateConstraint = new Constraint(validOutput);
	
	// Objectives
	
	// Expressions
	
	public Expression<Edge> positionExpression = new Expression<Edge>(positionOutput, true)
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			return position;	
		}
		
	};
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return -chargeRateOutput.get(state, timepoint);
		}
		
	};
	
	public Expression<Double> chargeRateExpression = new Expression<Double>(chargeRateOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (chargeRateInput.get(state, timepoint) != null) 
			{
				if (chargeRateInput.get(state, timepoint) != 0.)
				{
					return chargeRateInput.get(state, timepoint);
				}
				else 
				{
					return -chargeRate;
				}
			}	
			
			return 0.;
			
			//return chargeRate*efficiencyOutput.get(state, timepoint);
		}
	};
	
	public Expression<Double> carConnectionExpression = new Expression<Double>(carConnectionOutput, true) 
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (chargeRateInput.get(state, timepoint) != null) ? 1. : 0.;
		}
	};
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
//			if (chargeRateExpression.get(state, timepoint) > 0 )
//			{
//				return 1.;
//			}
//			else 
//			{
//				return 0.;
//			}
			return 0.;
		}
	};
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return true;
		}
	};
	
	public Expression<Double> efficiencyExpression = new ConstantNonDeterministicExpression<>(efficiencyOutput, 0., 1.);
	
	// Charts
	
	public Chart levelChart = new Timeline(powerOutput);
	public Chart carConnectionChart = new Timeline(carConnectionOutput);

}
