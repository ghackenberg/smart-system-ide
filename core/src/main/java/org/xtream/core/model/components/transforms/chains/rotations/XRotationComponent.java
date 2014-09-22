package org.xtream.core.model.components.transforms.chains.rotations;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.core.model.components.transforms.chains.RotationComponent;

public class XRotationComponent extends RotationComponent
{
	
	// Expressions
	
	public Expression<RealMatrix> intermediateTransformExpression = new Expression<RealMatrix>(intermediateTransformOutput)
	{
		@Override protected RealMatrix evaluate(State state, int timepoint)
		{
			double a = angleOutput.get(state, timepoint);
			
			return new BlockRealMatrix(new double[][] {{1,0,0,0}, {0,Math.cos(a),-Math.sin(a),0}, {0,Math.sin(a),Math.cos(a),0}, {0,0,0,1}});
		}
	};

}
