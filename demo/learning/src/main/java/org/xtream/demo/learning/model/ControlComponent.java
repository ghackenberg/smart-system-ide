package org.xtream.demo.learning.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Histogram;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class ControlComponent extends Component
{
	
	// Ports
	
	public Port<Boolean> decisionOutput = new Port<>();
	
	// Charts
	
	public Chart decisionChart = new Histogram<>(decisionOutput);
	
	// Expressions
	
	public Expression<Boolean> decisionExpression = new ConstantNonDeterministicExpression<>(decisionOutput, true, false);

}
