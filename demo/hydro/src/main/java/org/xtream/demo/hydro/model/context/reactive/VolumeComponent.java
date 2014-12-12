package org.xtream.demo.hydro.model.context.reactive;

import java.awt.Color;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.transforms.IdentityComponent;
import org.xtream.core.model.components.transforms.chains.ScaleComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Equivalence;
import org.xtream.demo.hydro.model.Constants;

public abstract class VolumeComponent extends Component
{
	
	// Parameters
	
	private int staustufe;
	
	private int inflowPast;
	
	private double levelMin;
	private double levelMax;
	
	// Constructors
	
	public VolumeComponent(int staustufe, int inflowPast, double levelMin, double levelMax)
	{
		this.staustufe = staustufe;
		
		this.inflowPast = inflowPast;
		
		this.levelMin = levelMin;
		this.levelMax = levelMax;
		
		if (staustufe == 4 || Constants.STRATEGY != 1) // Do not use for grid strategy
		{
			inflowFutureEquivalence = new Equivalence(inflowFutureOutput, staustufe == 4 ? 50 : 1);
		}
	}
	
	// Ports
	
	public Port<Double> inflowInput = new Port<Double>();
	public Port<Double> outflowInput = new Port<Double>();
	
	public Port<Double> levelMinOutput = new Port<Double>();
	public Port<Double> levelMaxOutput = new Port<Double>();
	
	public Port<Double> levelOutput = new Port<Double>();
	public Port<Boolean> bandOutput = new Port<Boolean>();
	
	public Port<Double> inflowFutureOutput = new Port<Double>();
	
	// Constraints
	
	public Constraint bandConstraint = new Constraint(bandOutput);
	
	// Equivalences
	
	public Equivalence inflowFutureEquivalence;
	
	// Charts
	
	public Chart levelChart = new Timeline(levelMinOutput, levelOutput, levelMaxOutput);
	public Chart flowChart = new Timeline(inflowInput, outflowInput);
	
	// Expressions
	
	public Expression<Double> levelMinExpression = new Expression<Double>(levelMinOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return levelMin;
		}
	};
	public Expression<Double> levelMaxExpression = new Expression<Double>(levelMaxOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return levelMax;
		}
	};
	public Expression<Boolean> bandExpression = new Expression<Boolean>(bandOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return levelOutput.get(state, timepoint) >= levelMin - 0.01 && levelOutput.get(state, timepoint) <= levelMax + 0.01;
		}
	};
	public Expression<Double> inflowFutureExpression = new Expression<Double>(inflowFutureOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double result = 0;
			
			for (int i = 0; i < inflowPast - 1; i++)
			{
				if (timepoint - i >= 0)
				{
					result += inflowInput.get(state, timepoint - i);
				}
				else
				{
					break;
				}
			}
			
			return result;
		}
	};
	
	//Scene Components
	
	public IdentityComponent identity = new IdentityComponent()
	{
		
	};
	
	public TranslationComponent translationMax = new TranslationComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> xExpressionMax = new ConstantExpression<Double>(xOutput, 0.0);
		@SuppressWarnings("unused")
		public Expression<Double> yExpressionMax = new Expression<Double>(yOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return (((levelOutput.get(state, timepoint) - 265.0) / 10.)+((levelMinOutput.get(state, timepoint)-265.0)/10.)+ (levelMaxOutput.get(state, timepoint)-265.)/10./2.);
			}	
		};
		@SuppressWarnings("unused")
		public Expression<Double> zExpressionMax = new Expression<Double>(zOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return staustufe * 2.;
			}
		};
	};
	
	public TranslationComponent translation = new TranslationComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> xExpression = new ConstantExpression<Double>(xOutput, 0.0);
		@SuppressWarnings("unused")
		public Expression<Double> yExpression = new Expression<Double>(yOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return (((levelOutput.get(state, timepoint) - 265.0) / 10. /2.)+(levelMinOutput.get(state, timepoint)-265.0)/10.);
			}	
		};
		@SuppressWarnings("unused")
		public Expression<Double> zExpression = new Expression<Double>(zOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return staustufe * 2.;
			}
		};
	};
	
	public TranslationComponent translationMin = new TranslationComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> xExpressionMin = new ConstantExpression<Double>(xOutput, 0.0);
		@SuppressWarnings("unused")
		public Expression<Double> yExpressionMin = new Expression<Double>(yOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return ((levelMinOutput.get(state, timepoint) - 265.0) / 10.)/2.;
			}	
		};
		@SuppressWarnings("unused")
		public Expression<Double> zExpression = new Expression<Double>(zOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return staustufe * 2.;
			}
		};
	};
	
	public ScaleComponent scaleMax = new ScaleComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> xExpressionMax = new ConstantExpression<Double>(xOutput, 1.);
		@SuppressWarnings("unused")
		public Expression<Double> yExpressionMax = new Expression<Double>(yOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return (levelMaxOutput.get(state, timepoint) - 265.0) / 10.;
			}
		};
		@SuppressWarnings("unused")
		public Expression<Double> zExpressionMax = new ConstantExpression<Double>(zOutput, 1.);
	};
	
	public ScaleComponent scale = new ScaleComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> xExpression = new ConstantExpression<Double>(xOutput, 1.);
		@SuppressWarnings("unused")
		public Expression<Double> yExpression = new Expression<Double>(yOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return (levelOutput.get(state, timepoint) - 265.0) / 10.;
			}
		};
		@SuppressWarnings("unused")
		public Expression<Double> zExpression = new ConstantExpression<Double>(zOutput, 1.);
	};
	
	public ScaleComponent scaleMin = new ScaleComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> xExpressionMin = new ConstantExpression<Double>(xOutput, 1.);
		@SuppressWarnings("unused")
		public Expression<Double> yExpressionMin = new Expression<Double>(yOutput)
		{
			@Override
			protected Double evaluate(State state, int timepoint)
			{
				return ((levelMinOutput.get(state, timepoint) - 265.0) / 10.);
			}
		};
		@SuppressWarnings("unused")
		public Expression<Double> zExpressionMin = new ConstantExpression<Double>(zOutput, 1.);
	};
	
	public CubeComponent cubeMax = new CubeComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> color = new ConstantExpression<Color>(colorOutput, new Color (0, 191, 255));
		@SuppressWarnings("unused")
		public Expression<Double> size = new ConstantExpression<Double>(sizeOutput, 1.);		
	};
	
	public CubeComponent cube = new CubeComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> color = new ConstantExpression<Color>(colorOutput, new Color (0, 0, 255));
		@SuppressWarnings("unused")
		public Expression<Double> size = new ConstantExpression<Double>(sizeOutput, 1.);		
	};
	
	public CubeComponent cubeMin = new CubeComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> color = new ConstantExpression<Color>(colorOutput, new Color (170, 170, 170));
		@SuppressWarnings("unused")
		public Expression<Double> size = new ConstantExpression<Double>(sizeOutput, 1.);		
	};
	
	public ChannelExpression<RealMatrix> identityMaxToTranslationMax = new ChannelExpression<RealMatrix>(translationMax.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> translationMaxToScaleMax = new ChannelExpression<RealMatrix>(scaleMax.transformInput, translationMax.transformOutput);
	public ChannelExpression<RealMatrix> scaleMaxToCubeMax = new ChannelExpression<RealMatrix>(cubeMax.transformInput, scaleMax.transformOutput);
	public ChannelExpression<RealMatrix> identityToTranslation = new ChannelExpression<RealMatrix>(translation.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> translationToScale = new ChannelExpression<RealMatrix>(scale.transformInput, translation.transformOutput);
	public ChannelExpression<RealMatrix> scaleToCube = new ChannelExpression<RealMatrix>(cube.transformInput, scale.transformOutput);
	public ChannelExpression<RealMatrix> identityToTranslationMin = new ChannelExpression<RealMatrix>(translationMin.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> translationMinToScaleMin = new ChannelExpression<RealMatrix>(scaleMin.transformInput, translationMin.transformOutput);
	public ChannelExpression<RealMatrix> scaleMinToCubeMin = new ChannelExpression<RealMatrix>(cubeMin.transformInput, scaleMin.transformOutput);

}
