package org.xtream.demo.projecthouse.model.thermalstorage.electricheatingelement;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.enums.OnOffDecision;

public class ElectricHeatingElementContext extends Component {
	
	public Port<OnOffDecision> onOffInput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			// TODO Auto-generated method stub
			return null;
		}
	};

}
