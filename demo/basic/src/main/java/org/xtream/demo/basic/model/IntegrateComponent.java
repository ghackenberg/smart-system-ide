package org.xtream.demo.basic.model;

import java.awt.Color;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.components.AmbientComponent;
import org.xtream.core.model.components.BackgroundComponent;
import org.xtream.core.model.components.TransformComponent;
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.nodes.shapes.LineComponent;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.model.components.transforms.IdentityComponent;
import org.xtream.core.model.components.transforms.chains.RotationComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.components.transforms.chains.rotations.YRotationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MinObjective;

public class IntegrateComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	public Port<Double> input = new Port<>();
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> output = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////

	public TransformComponent identity = new IdentityComponent()
	{
		// empty
	};
	public BackgroundComponent background = new BackgroundComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(255, 255, 255));
	};
	public AmbientComponent ambient = new AmbientComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(128, 128, 128));
	};
	public CameraComponent camera = new CameraComponent()
	{
		@SuppressWarnings("unused")
		public Expression<RealVector> eyeExpression = new ConstantExpression<RealVector>(eyeOutput, new ArrayRealVector(new double[] {10,10,10,1}));
		@SuppressWarnings("unused")
		public Expression<RealVector> centerExpression = new ConstantExpression<RealVector>(centerOutput, new ArrayRealVector(new double[] {0,0,0,1}));
		@SuppressWarnings("unused")
		public Expression<RealVector> upExpression = new ConstantExpression<RealVector>(upOutput, new ArrayRealVector(new double[] {0,1,0,1}));
	};
	public PointLightComponent light = new PointLightComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> specularExpression = new ConstantExpression<Color>(specularOutput, new Color(255,255,255));
		@SuppressWarnings("unused")
		public Expression<Color> diffuseExpression = new ConstantExpression<Color>(diffuseOutput, new Color(255,255,255));
		@SuppressWarnings("unused")
		public Expression<RealVector> positionExpression = new ConstantExpression<RealVector>(positionOutput, new ArrayRealVector(new double[] {0,10,10,0}));
	};
	public RotationComponent rotation = new YRotationComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> angleExpression = new Expression<Double>(angleOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return timepoint / 10.;
			}
		};
	};
	public TranslationComponent translation = new TranslationComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> xExpression = new Expression<Double>(xOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return output.get(state, timepoint) / 10.;
			}
		};
		@SuppressWarnings("unused")
		public Expression<Double> yExpression = new ConstantExpression<Double>(yOutput, 0.);
		@SuppressWarnings("unused")
		public Expression<Double> zExpression = new ConstantExpression<Double>(zOutput, 0.);
	};
	public CubeComponent cube = new CubeComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new Expression<Color>(colorOutput)
		{
			@Override protected Color evaluate(State state, int timepoint)
			{
				return new Color(255, timepoint, 0);
			}
		};
		@SuppressWarnings("unused")
		public Expression<Double> sizeExpression = new ConstantExpression<Double>(sizeOutput, 1.);
	};
	public SphereComponent sphere = new SphereComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(0,255,0));
		@SuppressWarnings("unused")
		public Expression<Double> radiusExpression = new Expression<Double>(radiusOutput)
		{
			@Override protected Double evaluate(State state, int timepoint)
			{
				return timepoint / 100.0;
			}
		};
	};
	public LineComponent line = new LineComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(64,64,64));
		@SuppressWarnings("unused")
		public Expression<RealVector> startExpression = new ConstantExpression<RealVector>(startOutput, new ArrayRealVector(new double[] {0, 0, 0, 1}));
		@SuppressWarnings("unused")
		public Expression<RealVector> endExpression = new Expression<RealVector>(endOutput)
		{
			@Override protected RealVector evaluate(State state, int timepoint)
			{
				return new ArrayRealVector(new double[] {timepoint / 10., 0, 0, 1});
			}
		};
	};
	
	//////////////
	// CHANNELS //
	//////////////

	public ChannelExpression<RealMatrix> identityToCamera = new ChannelExpression<RealMatrix>(camera.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToLight = new ChannelExpression<RealMatrix>(light.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToRotation = new ChannelExpression<RealMatrix>(rotation.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToSphere = new ChannelExpression<RealMatrix>(sphere.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> rotationToTranslation = new ChannelExpression<RealMatrix>(translation.transformInput, rotation.transformOutput);
	public ChannelExpression<RealMatrix> rotationToLine = new ChannelExpression<RealMatrix>(line.transformInput, rotation.transformOutput);
	public ChannelExpression<RealMatrix> translationToCube = new ChannelExpression<RealMatrix>(cube.transformInput, translation.transformOutput);
	
	/////////////////
	// EXPRESSIONS //
	/////////////////

	public Expression<Double> outputExpression = new Expression<Double>(output, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : output.get(state, timepoint - 1)) + input.get(state, timepoint);
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	/* none */
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	public Objective outputObjective = new MinObjective(output);
	
	////////////
	// CHARTS //
	////////////
	
	public Chart outputChart = new Timeline(output); 
	
}
