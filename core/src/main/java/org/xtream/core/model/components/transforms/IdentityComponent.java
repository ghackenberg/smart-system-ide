package org.xtream.core.model.components.transforms;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.core.model.components.TransformComponent;

public abstract class IdentityComponent extends TransformComponent
{
	
	// Expressions
	
	public Expression<RealMatrix> transformExpression = new Expression<RealMatrix>(transformOutput)
	{
		@Override protected RealMatrix evaluate(State state, int timepoint)
		{
			// TODO Fill matrix with correct values!
			
			return new BlockRealMatrix(4, 4);
		}
		
	};

}
