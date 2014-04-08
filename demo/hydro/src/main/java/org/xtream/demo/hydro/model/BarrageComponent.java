package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public class BarrageComponent extends Component
{
	
	// Constructors
	
	public BarrageComponent(double headLevelOffset, double tailLevelOffset)
	{
		this.headLevelOffset = headLevelOffset;
		this.tailLevelOffset = tailLevelOffset;
	}
	
	// Parameters
	
	protected double roh = 1;
	protected double gravity = 9.81;
	protected double efficiency = 0.9;
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
	
	public Chart dischargeChart = new Chart(turbineDischargeInput, weirDischargeInput);
	public Chart levelChart = new Chart(headLevelInput, tailLevelInput);
	public Chart productionChart = new Chart(productionOutput);
	
	public Chart productionPreview = new Chart(productionOutput);
	
	// Expressions
	
	public Expression<Double> dischargeExpression = new Expression<Double>(dischargeOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return turbineDischargeInput.get(timepoint) + weirDischargeInput.get(timepoint);
		}
	};
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return turbineDischargeInput.get(timepoint) * ((headLevelOffset + headLevelInput.get(timepoint)) - (tailLevelOffset + tailLevelInput.get(timepoint))) * 0.25 * roh * gravity * efficiency;
		}
	};

}
