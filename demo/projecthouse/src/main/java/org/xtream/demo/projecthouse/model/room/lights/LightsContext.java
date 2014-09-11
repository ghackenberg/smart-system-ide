package org.xtream.demo.projecthouse.model.room.lights;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.enums.OnOffDecision;

public class LightsContext extends Component {
	
	private double consumption;
	
	Port<OnOffDecision> onOffInput = new Port<>();
	
	Port<Double> consumptionOutput = new Port<>();
	
	public LightsContext(double consumption) {
		super();
		this.consumption = consumption;
	}
	
	Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return onOffInput.get(state, timepoint) == OnOffDecision.ON ? consumption : 0.;
		}		
	};

}
