package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;

public class ConstantVolumeComponent extends Component
{
	
	// Constructors
	
	public ConstantVolumeComponent(double levelInitial)
	{
		this.levelInitial = levelInitial;
	}
	
	// Parameters
	
	protected double levelInitial;
	
	// Ports
	
	public Port<Double> levelOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> levelExpression = new ConstantExpression<Double>(levelOutput, levelInitial);

}
