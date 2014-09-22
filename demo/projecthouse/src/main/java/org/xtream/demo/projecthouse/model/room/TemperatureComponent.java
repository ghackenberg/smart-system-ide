package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class TemperatureComponent extends Component {
	
	public Port<Double> temperatureOutput = new Port<>();
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			// TODO [Andreas]
			return 20.;
		}
	};

}
