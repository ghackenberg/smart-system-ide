package org.xtream.demo.ppu.model;

import java.awt.Color;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class SensorComponent extends Component
{
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	public Port<RealVector> positionInput = new Port<>();
	public Port<Integer> typeInput = new Port<>();
	public Port<Integer> stateInput = new Port<>();
	
	public Port<Integer> typeOutput = new Port<>();
	public Port<Integer> stateOutput = new Port<>();
	
	// Components
	
	public SphereComponent sphere = new SphereComponent()
	{
		public Expression<Double> radiusExpression = new ConstantExpression<>(radiusOutput, 0.5);
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(255,128,0));
	};
	
	// Channels
	
	public Expression<RealMatrix> transformToSphere = new ChannelExpression<>(sphere.transformInput, transformInput);
	
	// Expressions
	
	public Expression<Integer> typeExpression = new Expression<Integer>(typeOutput)
	{
		@Override
		protected Integer evaluate(State state, int timepoint)
		{
			RealVector position = positionInput.get(state, timepoint);
			RealVector center = transformInput.get(state, timepoint).operate(new ArrayRealVector(new double[] {0.0,0.0,0.0,1.0}));
			
			if (position.getDistance(center) < 0.5)
			{
				return typeInput.get(state, timepoint);
			}
			else
			{
				return -1;
			}
		}
	};
	public Expression<Integer> stateExpression = new Expression<Integer>(stateOutput)
	{
		@Override
		protected Integer evaluate(State state, int timepoint)
		{
			RealVector position = positionInput.get(state, timepoint);
			RealVector center = transformInput.get(state, timepoint).operate(new ArrayRealVector(new double[] {0.0,0.0,0.0,1.0}));
			
			if (position.getDistance(center) < 0.5)
			{
				return stateInput.get(state, timepoint);
			}
			else
			{
				return -1;
			}
		}
	};

}
