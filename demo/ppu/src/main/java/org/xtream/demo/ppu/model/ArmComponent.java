package org.xtream.demo.ppu.model;

import java.awt.Color;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.transforms.chains.RotationComponent;
import org.xtream.core.model.components.transforms.chains.ScaleComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.components.transforms.chains.rotations.XRotationComponent;
import org.xtream.core.model.components.transforms.chains.rotations.YRotationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class ArmComponent extends Component
{
	
	// Ports

	public Port<RealMatrix> transformInput = new Port<>();
	public Port<Double> energyInput = new Port<>();
	
	public Port<Double> positionOutput = new Port<>();
	
	// Components
	
	public SuckerComponent sucker = new SuckerComponent();
	
	public CubeComponent cube = new CubeComponent()
	{
		public Expression<Double> sizeExpression = new ConstantExpression<>(sizeOutput, 1.0);
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(255,0,0));
	};
	
	public ScaleComponent scale = new ScaleComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 5.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 1.0);
	};
	public TranslationComponent translation_cube = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, -1.5);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 0.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	};
	public TranslationComponent translation_sucker = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, -3.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, -0.5);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	};
	public RotationComponent rotation_cube = new YRotationComponent()
	{
		public Expression<Double> angleExpression = new Expression<Double>(angleOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return positionOutput.get(state, timepoint);
			}
		};
	};
	public RotationComponent rotation_sucker = new XRotationComponent()
	{
		public Expression<Double> angleExpression = new ConstantExpression<>(angleOutput, Math.PI / 2);
	};
	
	// Channels
	
	public Expression<RealMatrix> transformToRotationCube = new ChannelExpression<>(rotation_cube.transformInput, transformInput);
	public Expression<RealMatrix> transformRotationCubeToTranslationCube = new ChannelExpression<>(translation_cube.transformInput, rotation_cube.transformOutput);
	public Expression<RealMatrix> transformRotationCubeToTranslationSucker = new ChannelExpression<>(translation_sucker.transformInput, rotation_cube.transformOutput);
	public Expression<RealMatrix> transformTranslationCubeToScale = new ChannelExpression<>(scale.transformInput, translation_cube.transformOutput);
	public Expression<RealMatrix> transformTranslationSuckerToRotationSucker = new ChannelExpression<>(rotation_sucker.transformInput, translation_sucker.transformOutput);
	public Expression<RealMatrix> transformScaleToCube = new ChannelExpression<>(cube.transformInput, scale.transformOutput);
	public Expression<RealMatrix> transformRotationSuckerToSucker = new ChannelExpression<>(sucker.transformInput, rotation_sucker.transformOutput);
	
	// Expressions
	
	public Expression<Double> positionExpression = new Expression<Double>(positionOutput, true)
	{
		@Override
		protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return 0.0;
			}
			else
			{
				return positionOutput.get(state, timepoint - 1) + energyInput.get(state, timepoint);
			}
		}
	};

}
