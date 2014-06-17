package org.xtream.demo.hydro.model;

import java.awt.Color;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Node;
import org.xtream.core.model.Port;
import org.xtream.core.model.Transform;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.CachingExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.nodes.shapes.Box;
import org.xtream.core.model.transforms.Translation;
import org.xtream.core.optimizer.State;

public class VolumeComponent extends Component
{
	
	// Constructors
	
	public VolumeComponent(double levelInitial, double levelMin, double levelMax, double area)
	{
		this.levelInitial = levelInitial;
		this.levelMin = levelMin;
		this.levelMax = levelMax;
		this.area = area;
	}
	
	// Parameters
	
	protected double levelInitial;
	protected double levelMin;
	protected double levelMax;
	protected double area;
	
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
	
	// Transforms
	
	public Transform translate = new Translation()
	{
		
	};
	
	// Nodes
	
	public Node box = new Box()
	{	
		@Override
		public Color getColor(int timepoint)
		{
			return null;
		}
	};
	
	// Expressions
	
	public Expression<Double> levelExpression = new CachingExpression<Double>(levelOutput)
	{
		@Override protected Double evaluateInternal(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return levelInitial;
			}
			else
			{
				return levelOutput.get(state, timepoint - 1) + inflowInput.get(state, timepoint) * 900 / area - outflowInput.get(state, timepoint) * 900 / area;
			}
		}
	};
	public Expression<Double> levelMinExpression = new Expression<Double>(levelMinOutput)
	{
		@Override public Double evaluate(State state, int timepoint)
		{
			return levelMin;
		}
	};
	public Expression<Double> levelMaxExpression = new Expression<Double>(levelMaxOutput)
	{
		@Override public Double evaluate(State state, int timepoint)
		{
			return levelMax;
		}
	};
	public Expression<Boolean> bandExpression = new Expression<Boolean>(bandOutput)
	{
		@Override public Boolean evaluate(State state, int timepoint)
		{
			return levelOutput.get(state, timepoint) >= levelMin - 0.01 && levelOutput.get(state, timepoint) <= levelMax + 0.01;
		}
	};

}
