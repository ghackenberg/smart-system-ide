package org.xtream.demo.hydro.model.context.reactive.volume;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.demo.hydro.data.PolynomLevelDirect;
import org.xtream.demo.hydro.model.Constants;
import org.xtream.demo.hydro.model.context.reactive.VolumeComponent;

public class VolumeDirectComponent extends VolumeComponent
{
	
	// Parameters
	
	private PolynomLevelDirect model;
	
	// Constructors
	
	public VolumeDirectComponent(int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order, double levelMin, double levelMax)
	{
		super(staustufe, inflow_past, levelMin, levelMax);
		
		model = new PolynomLevelDirect(staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order);
		model.fit(Constants.DATASET_TRAIN);
	}
	
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

}
