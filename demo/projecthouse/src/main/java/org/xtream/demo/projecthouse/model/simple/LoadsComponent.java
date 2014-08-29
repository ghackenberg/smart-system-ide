package org.xtream.demo.projecthouse.model.simple;

import java.io.File;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.model.Consumer;

public class LoadsComponent extends Component implements Consumer {
	
	private File file;
	
	public Port<Double> consumptionOutput = new Port<>();
	
	public Expression<Double> powerExpression = new Expression<Double>(consumptionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			// TODO [Andreas]
			return null;
		}
	};

	@Override
	public Port<Double> consumption() {
		return consumptionOutput;
	}

}
