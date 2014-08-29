package org.xtream.demo.projecthouse.model.net;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public class NetController extends Component {
	
	public Port<Double> powerOutput = new Port<>();
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return Math.random()*1000-500;
		}
		
	};

}
