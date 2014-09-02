package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Irradiance;
import org.xtream.demo.projecthouse.model.RootComponent;
import org.xtream.demo.projecthouse.model.room.window.WindowModule;

public class RoomContext extends Component {	
	public Port<Irradiance> irradianceInput = new Port<>();
	public Port<OnOffDecision> heatingInput = new Port<>();
	public Port<OnOffDecision> lightsInput = new Port<>();
	public Port<Double> possibilityInput = new Port<>();
	public Port<Double> temperatureInput = new Port<>();
	@SuppressWarnings("rawtypes")
	public Port[] blindsInputs;
	
	public Port<Double> temperatureOutput = new Port<>();
	public Port<Double> brightnessOutput = new Port<>();
	public Port<Double>	comfortOutput = new Port<>();
	
	public Port<Boolean> temperatureValidOutput = new Port<>();
	public Port<Boolean> brightnessValidOutput = new Port<>();
	
	public Constraint temperatureConstraint = new Constraint(temperatureValidOutput);
	public Constraint brightnessConstraint = new Constraint(brightnessValidOutput);

	private double volume;
	private Double lowerTemperatureLimit;
	private Double upperTemperatureLimit;
	private WindowModule[] windows;
	
	public RoomContext(double volume, double lowerTemperatureLimit, double upperTemperatureLimit, WindowModule...windows) {
		super();
		this.volume = volume;
		this.lowerTemperatureLimit = lowerTemperatureLimit;
		this.upperTemperatureLimit = upperTemperatureLimit;
		
		this.windows = windows;
		blindsInputs = new Port[windows.length];
		for(int i = 0; i < windows.length; i++) {
			blindsInputs[i] = new Port<OnOffDecision>();
		}
	}
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if(timepoint == 0) {
				return RootComponent.START_TEMPERATURE;
			}
			double temperature = temperatureOutput.get(state, timepoint - 1);
			//TODO [Andreas] Implement effects of heating
			return temperature;
		}
	};
	
	public Expression<Boolean> temperatureValidExpression = new Expression<Boolean>(temperatureValidOutput) {

		@Override
		protected Boolean evaluate(State state, int timepoint) {
			double temperature = temperatureOutput.get(state, timepoint);
			return temperature < upperTemperatureLimit && temperature > lowerTemperatureLimit;
		}
	};
	
	public Expression<Double> brightnessExpression = new Expression<Double>(brightnessOutput) {
		@Override
		protected Double evaluate(State state, int timepoint) {
			double brightness = RootComponent.BRIGHTNESS_LIMIT;
			//TODO [Andreas] Implement effects of lights and sunlight
			return brightness;
		}
	};
	
	public Expression<Boolean> brightnessValidExpression = new Expression<Boolean>(brightnessValidOutput) {
		
		@Override
		protected Boolean evaluate(State state, int timepoint) {
			return brightnessOutput.get(state, timepoint) > RootComponent.BRIGHTNESS_LIMIT;
		}
	};
	
	public Expression<Double> comfortExpression = new Expression<Double>(comfortOutput) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return Math.pow(temperatureOutput.get(state, timepoint) - temperatureInput.get(state, timepoint), 2);
		}
	};

}
