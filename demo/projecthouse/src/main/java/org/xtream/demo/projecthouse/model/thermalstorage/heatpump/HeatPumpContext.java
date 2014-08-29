package org.xtream.demo.projecthouse.model.thermalstorage.heatpump;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public class HeatPumpContext extends Component {
	
	public Port<Integer> levelInput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			switch (levelInput.get(state, timepoint)) {
				case 0:
					return 0.;
				case 1:
					return 3.;
				case 2:
					return 6.;
				default:
					throw new IllegalStateException();
			}
		}
		
	};

}
