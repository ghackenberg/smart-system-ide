package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;

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
	
	public Port<Double> levelOutput = new Port<Double>();
	public Port<Double> levelMinOutput = new Port<Double>();
	public Port<Double> levelMaxOutput = new Port<Double>();
	public Port<Boolean> bandOutput = new Port<Boolean>();
	
	// Constraints
	
	public Constraint bandConstraint = new Constraint(bandOutput);
	
	// Charts
	
	public Chart levelChart = new Chart(levelMinOutput, levelOutput, levelMaxOutput);
	public Chart flowChart = new Chart(inflowInput, outflowInput);
	
	public Chart levelPreview = new Chart(levelMinOutput, levelOutput, levelMaxOutput);
	
	// Expressions
	
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return levelInitial;
			}
			else
			{
				return levelOutput.get(timepoint - 1) + inflowInput.get(timepoint) * 900 / area - outflowInput.get(timepoint) * 900 / area;
			}
		}
	};
	public Expression<Double> levelMinExpression = new Expression<Double>(levelMinOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return levelMin;
		}
	};
	public Expression<Double> levelMaxExpression = new Expression<Double>(levelMaxOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return levelMax;
		}
	};
	public Expression<Boolean> bandExpression = new Expression<Boolean>(bandOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return levelOutput.get(timepoint) >= levelMin - 0.1 && levelOutput.get(timepoint) <= levelMax + 0.1;
		}
	};

}
