package org.xtream.core.model.components.transforms.chains;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.transforms.ChainComponent;


public abstract class TranslationComponent extends ChainComponent
{
	
	// Ports
	
	public Port<Double> xOutput = new Port<>();
	public Port<Double> yOutput = new Port<>();
	public Port<Double> zOutput = new Port<>();
	
	// Expressions
	
	public Expression<RealMatrix> intermediateTransformExpression = new Expression<RealMatrix>(intermediateTransformOutput)
	{
		@Override protected RealMatrix evaluate(State state, int timepoint)
		{
			return new BlockRealMatrix(new double[][] {{1,0,0,xOutput.get(state, timepoint)}, {0,1,0,yOutput.get(state, timepoint)}, {0,0,1,zOutput.get(state, timepoint)}, {0,0,0,1}});
		}
		
	};

}
