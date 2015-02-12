package org.xtream.demo.ppu.model;

import java.awt.Color;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.shapes.CylinderComponent;
import org.xtream.core.model.components.transforms.chains.RotationComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.components.transforms.chains.rotations.XRotationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class WorkpieceComponent extends Component
{
	
	// Constructors
	
	public WorkpieceComponent(int type, int state)
	{
		this.type = type;
		this.state = state;
	}
	
	// Parameters
	
	private int type;
	private int state;
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	public Port<RealVector> positionOutput = new Port<>();
	public Port<Integer> typeOutput = new Port<>();
	public Port<Integer> stateOutput = new Port<>();
	
	// Components
	
	public CylinderComponent cylinder = new CylinderComponent()
	{
		public Expression<Double> baseExpression = new ConstantExpression<>(baseOutput, 1.0);
		public Expression<Double> heightExpression = new ConstantExpression<>(heightOutput, 2.0);
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(128,128,128));
	};
	
	public RotationComponent rotation = new XRotationComponent()
	{
		public Expression<Double> angleExpression = new ConstantExpression<>(angleOutput, - Math.PI / 2);
	};
	public TranslationComponent translation = new TranslationComponent()
	{
		public Expression<Double> xExpression = new Expression<Double>(xOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return positionOutput.get(state, timepoint).getEntry(0);
			}
		};
		public Expression<Double> yExpression = new Expression<Double>(yOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return positionOutput.get(state, timepoint).getEntry(1);
			}
		};
		public Expression<Double> zExpression = new Expression<Double>(zOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return positionOutput.get(state, timepoint).getEntry(2);
			}
		};
	};
	
	// Channels
	
	public Expression<RealMatrix> transformToTranslation = new ChannelExpression<>(translation.transformInput, transformInput);
	public Expression<RealMatrix> transformTranslationToRotation = new ChannelExpression<>(rotation.transformInput, translation.transformOutput);
	public Expression<RealMatrix> transformRotationToCylinder= new ChannelExpression<>(cylinder.transformInput, rotation.transformOutput);
	
	// Expressions
	
	public Expression<RealVector> positionExpression = new Expression<RealVector>(positionOutput)
	{
		@Override protected RealVector evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return new ArrayRealVector(new double[] {-3.0,2.0,0.0,1.0});
			}
			else
			{
				return positionOutput.get(state, timepoint - 1);
			}
		}
		
	};
	public Expression<Integer> typeExpression = new Expression<Integer>(typeOutput)
	{
		@Override protected Integer evaluate(State state, int timepoint)
		{
			return type;
		}
	};
	public Expression<Integer> stateExpression = new Expression<Integer>(stateOutput)
	{
		@Override protected Integer evaluate(State state2, int timepoint)
		{
			return state;
		}
	};

}
