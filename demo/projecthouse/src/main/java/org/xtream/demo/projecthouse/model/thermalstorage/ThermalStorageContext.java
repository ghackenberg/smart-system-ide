package org.xtream.demo.projecthouse.model.thermalstorage;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Module;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.room.RoomModule;

public class ThermalStorageContext extends Module {
	
	@SuppressWarnings("rawtypes")
	public Port[] roomHeatingInputs;
	public Port<Double> outerTemperatureInput = new Port<>();
	
	public ThermalStorageContext(RoomModule...rooms) {
		roomHeatingInputs = new Port[rooms.length];
		for(int i=0; i<rooms.length; i++) {
			roomHeatingInputs[i] = new Port<OnOffDecision>();
			roomHeatingInputs[i] = rooms[i].heatingController.onOffOutput;
		}
	}
	
	public Port<OnOffDecision> electricHeatingElementInput = new Port<>();
	public Port<OnOffDecision>	pelletHeaterInput = new Port<>();
	public Port<Integer> heatpumpInput = new Port<>();
	
	public Port<Double> temperatureOutput = new Port<>();
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if(timepoint == 0) {
				return 50.;
			}
			double oldTemperature = temperatureOutput.get(state, timepoint - 1);
			double outerTemperature = outerTemperatureInput.get(state, timepoint);
			//TODO [Andreas]
			return oldTemperature;
		}
	};
	
	
}
