package org.xtream.demo.projecthouse.model.thermalstorage.pelletheater;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.RootComponent;

public class PelletHeaterContext extends Component {
	
	public Port<OnOffDecision> onOffInput = new Port<>();
	
	public Port<Double> costOutput = new Port<>();
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return onOffInput.get(state, timepoint) == OnOffDecision.ON ? RootComponent.PELLET_PRICE : 0;
		}
	};

}
