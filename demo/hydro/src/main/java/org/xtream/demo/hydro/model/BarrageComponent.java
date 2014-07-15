package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;

public class BarrageComponent extends Component
{
	
	// Constructors
	
	public BarrageComponent(double headLevelOffset, double tailLevelOffset, double efficiency)
	{
		this.headLevelOffset = headLevelOffset;
		this.tailLevelOffset = tailLevelOffset;
		this.efficiency = efficiency;
	}
	
	// Parameters
	
	protected double roh = 1;
	protected double gravity = 9.81;
	protected double efficiency;
	protected double headLevelOffset;
	protected double tailLevelOffset;
	
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
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return turbineDischargeInput.get(state, timepoint) * ((headLevelOffset + headLevelInput.get(state, timepoint)) - (tailLevelOffset + tailLevelInput.get(state, timepoint))) * 0.25 * roh * gravity * efficiency;
		}
	};

}
