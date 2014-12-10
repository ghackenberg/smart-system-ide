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
import org.xtream.demo.hydro.data.PolynomLevel;
import org.xtream.demo.hydro.model.Constants;

public class VolumeComponent extends Component
{
	
	// Parameters
	
	private double levelMin;
	private double levelMax;
	
	private PolynomLevel model;
	
	// Constructors
	
	public VolumeComponent(int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order, double levelMin, double levelMax)
	{
		this.levelMin = levelMin;
		this.levelMax = levelMax;
		
		model = new PolynomLevel(staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order);
		model.fit(Constants.DATASET_TRAIN);
	}
	
	// Ports
	
	public Port<Double> inflowInput = new Port<Double>();
	public Port<Double> outflowInput = new Port<Double>();
	
	public Port<Double> levelMinOutput = new Port<Double>();
	public Port<Double> levelMaxOutput = new Port<Double>();
	
	public Port<Double> levelOutput = new Port<Double>();
	public Port<Boolean> bandOutput = new Port<Boolean>();
	
	// Constraints
	
	public Constraint bandConstraint = new Constraint(bandOutput);
	
	// Charts
	
	public Chart levelChart = new Timeline(levelMinOutput, levelOutput, levelMaxOutput);
	public Chart flowChart = new Timeline(inflowInput, outflowInput);
	
	// Expressions
	
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return Constants.DATASET_TEST.getLevel(model.getStaustufe(), Constants.START + timepoint);
			}
			else
			{
				double[] levels = new double[model.getLevelPast()];
				double[] inflows = new double[model.getInflowPast()];
				double[] outflows = new double[model.getOutflowPast()];
				
				for (int i = 0; i < model.getLevelPast(); i++)
				{
					if (i < timepoint)
					{
						levels[levels.length - 1 - i] = levelOutput.get(state, timepoint - 1 - i);
					}
					else
					{
						levels[levels.length - 1 - i] = Constants.DATASET_TEST.getLevel(model.getStaustufe(), Constants.START + timepoint - 1 - i);
					}
				}
				for (int i = 0; i < model.getInflowPast(); i++)
				{
					if (i <= timepoint)
					{
						inflows[inflows.length - 1 - i] = inflowInput.get(state, timepoint - i);
					}
					else
					{
						inflows[inflows.length - 1 - i] = Constants.DATASET_TEST.getInflow(model.getStaustufe(), Constants.START + timepoint - i);
					}
				}
				for (int i = 0; i < model.getOutflowPast(); i++)
				{
					if (i <= timepoint)
					{
						outflows[outflows.length - 1 - i] = outflowInput.get(state, timepoint - i);
					}
					else
					{
						outflows[outflows.length - 1 - i] = Constants.DATASET_TEST.getOutflowTotal(model.getStaustufe(), Constants.START + timepoint - i);
					}
				}
				
				return model.estimate(levels, inflows, outflows);
			}
		}
	};
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
				return model.getStaustufe() * 2.;
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
				return model.getStaustufe() * 2.;
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
				return model.getStaustufe() * 2.;
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
