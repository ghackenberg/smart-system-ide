package org.xtream.demo.ppu.model;

import java.awt.Color;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.nodes.shapes.LineComponent;
import org.xtream.core.model.components.transforms.chains.rotations.XRotationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class SlideComponent extends Component
{
	
	// Cunstructors
	
	public SlideComponent(double width, double length, double height)
	{
		this.width = width;
		this.length = length;
		this.height = height;
	}
	
	// Parameters

	private double width;
	private double length;
	private double height;
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	
	// Components
	
	public LineComponent line = new LineComponent()
	{
		public Expression<RealVector> startExpression = new Expression<RealVector>(startOutput)
		{
			@Override
			protected RealVector evaluate(State state, int timepoint)
			{
				return new ArrayRealVector(new double[] {0.0,height,0.0,1.0});
			}
		};
		public Expression<RealVector> endExpression = new Expression<RealVector>(endOutput)
		{
			@Override
			protected RealVector evaluate(State state, int timepoint)
			{
				return new ArrayRealVector(new double[] {width,0.0,length,1.0});
			}
		};
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(32,32,32));
	};
	
	// Channels
	
	public Expression<RealMatrix> transformToLine = new ChannelExpression<>(line.transformInput, transformInput);

}
