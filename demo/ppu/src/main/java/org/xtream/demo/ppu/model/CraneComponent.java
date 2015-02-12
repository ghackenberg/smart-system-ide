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
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class CraneComponent extends Component
{
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	
	// Components
	
	public CylinderComponent cylinder = new CylinderComponent(1.0, 3.0, 0.5, 3.0, 0.2, 0.1);
	public ArmComponent arm = new ArmComponent();
	
	public CubeComponent cube = new CubeComponent()
	{
		public Expression<Double> sizeExpression = new ConstantExpression<>(sizeOutput, 2.0);
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(0,0,255));
	};
	
	public TranslationComponent translation_cylinder = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 0.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	};
	public TranslationComponent translation_arm = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 0.0);
		public Expression<Double> yExpression = new Expression<Double>(yOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return cylinder.positionOutput.get(state, timepoint) + 1.5;
			}
		};
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	};
	public RotationComponent rotation = new XRotationComponent()
	{
		public Expression<Double> angleExpression = new ConstantExpression<>(angleOutput, - Math.PI / 2);
	};
	
	// Channels
	
	public Expression<RealMatrix> transformToCube = new ChannelExpression<>(cube.transformInput, transformInput);
	public Expression<RealMatrix> transformToTranslationCylinder = new ChannelExpression<>(translation_cylinder.transformInput, transformInput);
	public Expression<RealMatrix> transformToTranslationArm = new ChannelExpression<>(translation_arm.transformInput, transformInput);
	
	public Expression<RealMatrix> transformTranslationCylinderToRotation = new ChannelExpression<>(rotation.transformInput, translation_cylinder.transformOutput);
	public Expression<RealMatrix> transformTranslationArmToArm= new ChannelExpression<>(arm.transformInput, translation_arm.transformOutput);
	public Expression<RealMatrix> transformRotationToCylinder = new ChannelExpression<>(cylinder.transformInput, rotation.transformOutput);
	
	// Expressions
	
	public Expression<Double> energyCylinderExpression = new Expression<Double>(cylinder.energyInput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Math.random() < 0.5 ? 0.0 : 1.0;
		}
	};
	public Expression<Double> energyArmExpression = new Expression<Double>(arm.energyInput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Math.random() - 0.5;
		}
	};
	
}
