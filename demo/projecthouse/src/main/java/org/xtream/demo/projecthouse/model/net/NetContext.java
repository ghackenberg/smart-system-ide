package org.xtream.demo.projecthouse.model.net;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public class NetContext extends Component {
	
	//Ports
	public Port<Double> powerInput = new Port<>();
	
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> productionOutput = new Port<>();
	
	//Expressions
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			double production = powerInput.get(state, timepoint);
			return production > 0 ? production : 0;
		}
	};
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			double consumption = -powerInput.get(state, timepoint);
			return consumption > 0 ? consumption : 0;
		}
	};

}
