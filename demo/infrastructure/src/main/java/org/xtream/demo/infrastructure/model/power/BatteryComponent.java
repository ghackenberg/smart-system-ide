package org.xtream.demo.infrastructure.model.power;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.infrastructure.model.EnergyComponent;

public class BatteryComponent extends EnergyComponent
{

    // Parameters
	
    public Double batteryEfficiency; // Constant in kwH conversion loss transmission
    public Double batteryLoss; // Constant in kwH conversion loss transmission
    public Double stateOfCharge; // in kwH
    public Double stateOfChargeSpeed; // charge/discharge speed in kwH
    public Double stateOfChargeMinimum; // in kwH
    public Double stateOfChargeMaximum; // in kwH
    
	public BatteryComponent(Double batteryEfficiency, Double batteryLoss, Double stateOfCharge, Double stateOfChargeSpeed, Double stateOfChargeMinimum, Double stateOfChargeMaximum)
	{
		this.batteryEfficiency = batteryEfficiency;
		this.batteryLoss = batteryLoss;
		this.stateOfCharge = stateOfCharge;
		this.stateOfChargeSpeed = stateOfChargeSpeed;
		this.stateOfChargeMinimum = stateOfChargeMinimum;
		this.stateOfChargeMaximum = stateOfChargeMaximum;
	}

	// Ports

	// Outputs
	
	public Port<Double> stateOfChargeOutput = new Port<>();
	public Port<Double> stateOfChargeMinimumOutput = new Port<>();
	public Port<Double> stateOfChargeMaximumOutput = new Port<>();
	public Port<Double> probabilityOutput = new Port<>();
	
	// Constraints
	
	public Constraint chargeStateConstraint = new Constraint(validOutput);
	
	// Expressions
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (probabilityOutput.get(state, timepoint) > 0.)
			{
				return stateOfChargeSpeed;
			}
			else if (probabilityOutput.get(state, timepoint) < 0.) 
			{
				return -stateOfChargeSpeed;
			}
			else 
			{
				return 0.;
			}
		}
	};
	
	public Expression<Double> stateOfChargeExpression = new Expression<Double>(stateOfChargeOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return 0.5 * stateOfCharge;
			}
			else 
			{
				if (probabilityOutput.get(state, timepoint) < 0.)
				{
					return stateOfChargeOutput.get(state, timepoint - 1) * batteryLoss + stateOfChargeSpeed * batteryEfficiency; 
				}
				else if (probabilityOutput.get(state, timepoint) == 0.)
				{
					return stateOfChargeOutput.get(state, timepoint - 1) * batteryLoss;
				}
				else if (probabilityOutput.get(state, timepoint) > 0.)
				{
					return stateOfChargeOutput.get(state, timepoint - 1) * batteryLoss - stateOfChargeSpeed;
				}
					
				throw new IllegalStateException();
			}
		}
	};
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.;
		}
	};
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return stateOfChargeOutput.get(state, timepoint) >= stateOfChargeMinimum && stateOfChargeOutput.get(state, timepoint) <= stateOfChargeMaximum;
		}
	};
	
	public Expression<Double> stateOfChargeMaximumExpression = new Expression<Double>(stateOfChargeMaximumOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return stateOfChargeMaximum;
		}
	};
	
	public Expression<Double> stateOfChargeMinimumExpression = new Expression<Double>(stateOfChargeMinimumOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return stateOfChargeMinimum;
		}
	};
	
	public Expression<Double> probabilityExpression = new ConstantNonDeterministicExpression<>(probabilityOutput, 0., -0.25, .25, -0.5, .5, -0.75, .75, -1., 1.);
	
	// Charts
	
	public Chart levelChart = new Timeline(stateOfChargeMinimumOutput, stateOfChargeOutput, stateOfChargeMaximumOutput);

}
