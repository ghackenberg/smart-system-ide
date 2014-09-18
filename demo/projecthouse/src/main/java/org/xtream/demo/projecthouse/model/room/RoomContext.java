package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Irradiation;
import org.xtream.demo.projecthouse.model.RootComponent;
import org.xtream.demo.projecthouse.model.room.window.WindowModule;

public class RoomContext extends Component {
	public Port<Irradiation> irradiationInput = new Port<>();
	public Port<OnOffDecision> heatingInput = new Port<>();
	public Port<OnOffDecision> lightsInput = new Port<>();
	public Port<Double> port = new Port<>();
	public Port<Double> temperatureInput = new Port<>();
	public Port<Double> possibilityInput = new Port<>();
	@SuppressWarnings("rawtypes")
	public Port[] blindsInputs;

	public Port<Double> temperatureOutput = new Port<>();
	public Port<Double> brightnessOutput = new Port<>();
	public Port<Double> comfortOutput = new Port<>();

	public Port<Boolean> temperatureValidOutput = new Port<>();
	public Port<Boolean> brightnessValidOutput = new Port<>();

	public Constraint temperatureConstraint = new Constraint(
			temperatureValidOutput);
	public Constraint brightnessConstraint = new Constraint(
			brightnessValidOutput);

	private double volume;
	private Double lowerTemperatureLimit;
	private Double upperTemperatureLimit;
	private WindowModule[] windows;

	public RoomContext(double volume, double lowerTemperatureLimit,
			double upperTemperatureLimit, WindowModule... windows) {
		super();
		this.volume = volume;
		this.lowerTemperatureLimit = lowerTemperatureLimit;
		this.upperTemperatureLimit = upperTemperatureLimit;

		this.windows = windows;
		blindsInputs = new Port[windows.length];
		for (int i = 0; i < windows.length; i++) {
			blindsInputs[i] = new Port<OnOffDecision>();
		}
	}

	public Expression<Double> temperatureExpression = new Expression<Double>(
			temperatureOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if (timepoint == 0) {
				return RootComponent.START_TEMPERATURE;
			}
			double temperature = temperatureOutput.get(state, timepoint - 1);
			// TODO [Andreas] Implement effects of heating
			return temperature;
		}
	};

	public Expression<Boolean> temperatureValidExpression = new Expression<Boolean>(
			temperatureValidOutput) {

		@Override
		protected Boolean evaluate(State state, int timepoint) {
			double temperature = temperatureOutput.get(state, timepoint);
			return temperature < upperTemperatureLimit
					&& temperature > lowerTemperatureLimit;
		}
	};

	public Expression<Double> brightnessExpression = new Expression<Double>(
			brightnessOutput, true) {
		@Override
		protected Double evaluate(State state, int timepoint) {
			double brightness = 0.;
			// TODO [Andreas] Implement effects of lights and sunlight
			if(lightsInput.get(state, timepoint) == OnOffDecision.ON) {
				return RootComponent.BRIGHTNESS_LIMIT;
			}
			for(int i = 0; i<blindsInputs.length; i++) {
				if(Math.abs(windows[i].getOrientation() - irradiationInput.get(state, timepoint).orientation) < 75) {
					brightness += ((Port<Double>)blindsInputs[i]).get(state, timepoint)*RootComponent.BRIGHTNESS_LIMIT*3;
				}
			}
			return brightness;
		}
	};

	public Expression<Boolean> brightnessValidExpression = new Expression<Boolean>(
			brightnessValidOutput) {

		@Override
		protected Boolean evaluate(State state, int timepoint) {
			//TODO [Andreas] Only needed when people in the room
			return brightnessOutput.get(state, timepoint) >= RootComponent.BRIGHTNESS_LIMIT*possibilityInput.get(state, timepoint);
		}
	};

	public Expression<Double> comfortExpression = new Expression<Double>(
			comfortOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return Math.pow(temperatureOutput.get(state, timepoint)
					- temperatureInput.get(state, timepoint), 2)
					* possibilityInput.get(state, timepoint);
		}
	};

}
