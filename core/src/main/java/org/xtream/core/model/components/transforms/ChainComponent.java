package org.xtream.core.model.components.transforms;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.TransformComponent;

public abstract class ChainComponent extends TransformComponent
{
	
	// Ports

	public Port<RealMatrix> transformInput = new Port<>();
	
	public Port<RealMatrix> intermediateTransformOutput = new Port<>();
	
	// Expressions
	
	public Expression<RealMatrix> transformExpression = new Expression<RealMatrix>(transformOutput)
	{
		@Override protected RealMatrix evaluate(State state, int timepoint)
		{
			return transformInput.get(state, timepoint).multiply(intermediateTransformOutput.get(state, timepoint));
		}
		
	};

}
