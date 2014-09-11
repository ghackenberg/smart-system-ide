package org.xtream.demo.projecthouse.model.simple;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.model.Producer;

public class PVComponent extends Component implements Producer {
	
	//Ports
	public Port<Double> irradianceInput = new Port<>();
	public Port<Double> productionOutput = new Port<>();
	
	//Expressions
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return getPower(irradianceInput.get(state, timepoint));
		}
	};

	protected double getPower(double irradiance) {
		// TODO [Andreas] Get value from file with PV data
		return 0;
	}

	@Override
	public Port<Double> production() {
		return productionOutput;
	}
	

}
