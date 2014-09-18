package org.xtream.demo.projecthouse.model.thermalstorage;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Module;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.room.RoomModule;

public class ThermalStorageContext extends Module {
	
	protected static final double MIN_TEMP = 45.;
	@SuppressWarnings("rawtypes")
	public Port[] roomHeatingInputs;
	public Port<Double> outerTemperatureInput = new Port<>();
	
	public Port<OnOffDecision> electricHeatingElementInput = new Port<>();
	public Port<OnOffDecision>	pelletHeaterInput = new Port<>();
	public Port<Integer> heatpumpInput = new Port<>();
	
	public Port<Double> temperatureOutput = new Port<>();
	public Port<Boolean> temperatureValidOutput = new Port<>();
	
	public Constraint temperatureConstraint = new Constraint(temperatureValidOutput);
	
	public Chart temperature = new Timeline(temperatureOutput);
	
	public ThermalStorageContext(RoomModule...rooms) {
		roomHeatingInputs = new Port[rooms.length];
		for(int i=0; i<rooms.length; i++) {
			roomHeatingInputs[i] = new Port<OnOffDecision>();
			roomHeatingInputs[i] = rooms[i].heatingController.onOffOutput;
		}
	}
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if(timepoint == 0) {
				return MIN_TEMP;
			}
			double oldTemperature = temperatureOutput.get(state, timepoint - 1);
			double outerTemperature = outerTemperatureInput.get(state, timepoint);
			//TODO [Andreas]
			return oldTemperature;
		}
	};
	
	public Expression<Boolean> temperatureValidExpression = new Expression<Boolean>(temperatureValidOutput) {

		@Override
		protected Boolean evaluate(State state, int timepoint) {
			return temperatureOutput.get(state, timepoint) >= MIN_TEMP;
		}
	};
	
	
}
