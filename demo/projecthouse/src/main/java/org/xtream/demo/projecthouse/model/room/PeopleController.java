package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public class PeopleController extends Component {
	
	Port<Double> possibilityOutput = new Port<>();
	
	Expression<Double> possibilityExpression = new Expression<Double>(possibilityOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			// TODO [Andreas]
			return 1.;
		}
	};

}
