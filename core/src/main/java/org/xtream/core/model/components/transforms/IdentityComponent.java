package org.xtream.core.model.components.transforms;

import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.core.model.components.TransformComponent;

public class IdentityComponent extends TransformComponent
{
	
	// Expressions
	
	public Expression<RealMatrix> transformExpression = new Expression<RealMatrix>(transformOutput)
	{
		@Override protected RealMatrix evaluate(State state, int timepoint)
		{
			return new DiagonalMatrix(new double[] {1, 1, 1, 1});
		}
		
	};

}
