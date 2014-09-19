package org.xtream.demo.hydro.model.context.reactive;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.hydro.data.PolynomProduction;
import org.xtream.demo.hydro.model.Constants;

public class BarrageComponent extends Component
{
	
	// Parameters
	
	private PolynomProduction model;
	
	// Constructors
	
	public BarrageComponent(int staustufe, int turbine_outflow_past, int turbine_outflow_order, int upper_level_past, int upper_level_order, int lower_level_past, int lower_level_order)
	{
		model = new PolynomProduction(staustufe, turbine_outflow_past, turbine_outflow_order, upper_level_past, upper_level_order, lower_level_past, lower_level_order);
		model.fit(Constants.DATASET);
	}
	
	// Ports
	
	public Port<Double> turbineDischargeInput = new Port<>();
	public Port<Double> weirDischargeInput = new Port<>();
	public Port<Double> headLevelInput = new Port<>();
	public Port<Double> tailLevelInput = new Port<>();
	
	public Port<Double> dischargeOutput = new Port<>();
	public Port<Double> productionOutput = new Port<>();
	
	// Charts
	
	public Chart dischargeChart = new Timeline(turbineDischargeInput, weirDischargeInput);
	public Chart levelChart = new Timeline(headLevelInput, tailLevelInput);
	public Chart productionChart = new Timeline(productionOutput);
	
	// Expressions
	
	public Expression<Double> dischargeExpression = new Expression<Double>(dischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return turbineDischargeInput.get(state, timepoint) + weirDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double[] turbine_outflow_past = new double[model.getTurbineOutflowPast()];
			double[] upper_level_past = new double[model.getUpperLevelPast()];
			double[] lower_level_past = new double[model.getLowerLevelPast()];
			
			for (int i = 0; i < model.getTurbineOutflowPast(); i++)
			{
				if (i <= timepoint)
				{
					turbine_outflow_past[turbine_outflow_past.length - 1 - i] = turbineDischargeInput.get(state, timepoint - i);
				}
				else
				{
					turbine_outflow_past[turbine_outflow_past.length - 1 - i] = Constants.DATASET.getOutflowTurbine(model.getStaustufe(), Constants.START + timepoint - i);
				}
			}
			for (int i = 0; i < model.getUpperLevelPast(); i++)
			{
				if (i <= timepoint)
				{
					upper_level_past[upper_level_past.length - 1 - i] = headLevelInput.get(state, timepoint - i);
				}
				else
				{
					upper_level_past[upper_level_past.length - 1 - i] = Constants.DATASET.getLevel(model.getStaustufe(), Constants.START + timepoint - i);
				}
			}
			for (int i = 0; i < model.getLowerLevelPast(); i++)
			{
				if (i <= timepoint)
				{
					lower_level_past[lower_level_past.length - 1 - i] = tailLevelInput.get(state, timepoint - i);
				}
				else
				{
					lower_level_past[lower_level_past.length - 1 - i] = Constants.DATASET.getLevel(model.getStaustufe() + 1, Constants.START + timepoint - i);
				}
			}
			
			return model.estimate(turbine_outflow_past, upper_level_past, lower_level_past);
		}
	};

}
