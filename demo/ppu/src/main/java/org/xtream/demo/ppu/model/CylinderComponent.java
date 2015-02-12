package org.xtream.demo.ppu.model;

import java.awt.Color;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.transforms.IdentityComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class CylinderComponent extends Component
{
	
	// Constructors
	
	public CylinderComponent(double lower_base, double lower_height, double upper_base, double upper_height, double delta_plus, double delta_minus)
	{
		this.lower_base = lower_base;
		this.lower_height = lower_height;
		this.upper_base = upper_base;
		this.upper_height = upper_height;
		this.delta_plus = delta_plus;
		this.delta_minus = delta_minus;
	}
	
	// Parameters

	private double lower_base;
	private double lower_height;
	private double upper_base;
	private double upper_height;
	private double delta_plus;
	private double delta_minus;
	
	// Ports
	
	public Port<Double> energyInput = new Port<>();
	public Port<RealMatrix> transformInput = new Port<>();
	
	public Port<Double> positionOutput = new Port<>();
	
	// Components

	public org.xtream.core.model.components.nodes.shapes.CylinderComponent lower = new org.xtream.core.model.components.nodes.shapes.CylinderComponent()
	{
		public Expression<Double> baseExpression = new Expression<Double>(baseOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return lower_base;
			}
		};
		public Expression<Double> heightExpression = new Expression<Double>(heightOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return lower_height;
			}
		};
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(255,0,0));
	};
	public org.xtream.core.model.components.nodes.shapes.CylinderComponent upper = new org.xtream.core.model.components.nodes.shapes.CylinderComponent()
	{
		public Expression<Double> baseExpression = new Expression<Double>(baseOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return upper_base;
			}
		};
		public Expression<Double> heightExpression = new Expression<Double>(heightOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return positionOutput.get(state, timepoint);
			}
		};
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(0,255,0));
	};
	
	// Expressions
	
	public Expression<RealMatrix> transformToLower = new ChannelExpression<>(lower.transformInput, transformInput);
	public Expression<RealMatrix> transformToUpper = new ChannelExpression<>(upper.transformInput, transformInput);
	
	public Expression<Double> positionExpression = new Expression<Double>(positionOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return lower_height;
			}
			else if (energyInput.get(state, timepoint) > 0.0)
			{
				return Math.min(lower_height + upper_height, positionOutput.get(state, timepoint - 1) + delta_plus * energyInput.get(state, timepoint));
			}
			else if (energyInput.get(state, timepoint) == 0.0)
			{
				return Math.max(lower_height, positionOutput.get(state, timepoint - 1) - delta_minus);
			}
			else
			{
				throw new IllegalStateException();
			}
		}
	};

}
