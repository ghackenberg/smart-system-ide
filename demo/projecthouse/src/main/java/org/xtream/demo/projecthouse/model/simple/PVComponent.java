package org.xtream.demo.projecthouse.model.simple;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.demo.projecthouse.model.Irradiation;
import org.xtream.demo.projecthouse.model.Producer;

public class PVComponent extends Component implements Producer {
	
	public Port<Irradiation> irradiationInput = new Port<>();
	public Port<Double> outerTemperatureInput = new Port<>();
	
	public Port<Double> productionOutput = new Port<>();
	
	public Chart production = new Timeline(productionOutput);
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return getPower(irradiationInput.get(state, timepoint).irradiance, outerTemperatureInput.get(state, timepoint));
		}
	};

	protected double getPower(double irradiance, Double temperature) {
		// TODO [Andreas] Get value from file with PV data
		return irradiance*1000;
	}

	@Override
	public Port<Double> production() {
		return productionOutput;
	}
	

}
