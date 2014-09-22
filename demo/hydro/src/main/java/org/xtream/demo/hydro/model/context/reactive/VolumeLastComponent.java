package org.xtream.demo.hydro.model.context.reactive;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.demo.hydro.data.PolynomLevelLast;
import org.xtream.demo.hydro.model.Constants;

public class VolumeLastComponent extends Component
{
	
	private PolynomLevelLast model;
	
	// Constructors
	
	public VolumeLastComponent(int staustufe, int level_past, int level_order, int inflow_past, int inflow_order)
	{
		model = new PolynomLevelLast(staustufe, level_past, level_order, inflow_past, inflow_order);
		model.fit(Constants.DATASET);
	}
	
	// Ports
	
	public Port<Double> inflowInput = new Port<Double>();
	
	public Port<Double> levelOutput = new Port<Double>();
	
	// Charts
	
	public Chart levelChart = new Timeline(levelOutput);
	public Chart flowChart = new Timeline(inflowInput);
	
	// Expressions
	
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return Constants.DATASET.getLevel(model.getStaustufe(), Constants.START + timepoint);
			}
			else
			{
				double[] levels = new double[model.getLevelPast()];
				double[] inflows = new double[model.getInflowPast()];
				
				for (int i = 0; i < model.getLevelPast(); i++)
				{
					if (i < timepoint)
					{
						levels[levels.length - 1 - i] = levelOutput.get(state, timepoint - 1 - i);
					}
					else
					{
						levels[levels.length - 1 - i] = Constants.DATASET.getLevel(model.getStaustufe(), Constants.START + timepoint - 1 - i);
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
						inflows[inflows.length - 1 - i] = Constants.DATASET.getInflow(model.getStaustufe(), Constants.START + timepoint - i);
					}
				}
				
				return model.estimate(levels, inflows);
			}
		}
	};

}
