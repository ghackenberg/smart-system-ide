package org.xtream.demo.projecthouse.model.battery;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.projecthouse.enums.ChargingDecision;

public class BatteryContext extends Component {
	
	//Constants
	private static final double MAX_CAPACITY = 8000; //TODO [Andreas] Correct value

	//Input
	public Port<ChargingDecision> chargingInput = new Port<>();

	//Output
	public Port<Double> socOutput = new Port<>();
	public Port<Boolean> socInBoundsOutput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> productionOutput = new Port<>();

	//Constraints
	public Constraint socInBoundsConstraint = new Constraint(socInBoundsOutput);
	
	//Expressions
	public Expression<Double> socExpression = new Expression<Double>(socOutput,
			true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if (timepoint == 0) {
				return 0.5 * MAX_CAPACITY;
			} else {
				double oldSoC = socOutput.get(state, timepoint - 1);
				double lossRate = getLossRate(oldSoC);
				switch (chargingInput.get(state, timepoint)) {
				case IDLE:
					return oldSoC * lossRate;
				case CHARGE:
					return oldSoC * lossRate + getChargeSpeed(oldSoC);
				case DISCHARGE:
					return oldSoC * lossRate - getDisChargeSpeed(oldSoC);
				default:
					throw new IllegalStateException();
				}
			}
		}

	};
	
	public Expression<Boolean> socInBoundsExpression = new Expression<Boolean>(socInBoundsOutput) {
		
		@Override
		protected Boolean evaluate(State state, int timepoint) {
			double soc = socOutput.get(state, timepoint);
			return soc > 0 && soc < MAX_CAPACITY;
		}
	};

	public Expression<Double> productionExpression = new Expression<Double>(
			productionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			Double soc = timepoint == 0 ? 0 : socOutput.get(state, timepoint - 1);
			
			if(chargingInput.get(state, timepoint) == ChargingDecision.DISCHARGE) {
				return getDisChargeSpeed(soc);
			}
			
			return 0.;
		}

	};
	
	public Expression<Double> consumptionExpression = new Expression<Double>(
			consumptionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			Double soc = timepoint == 0 ? 0 : socOutput.get(state, timepoint - 1);
			
			if(chargingInput.get(state, timepoint) == ChargingDecision.CHARGE) {
				return getChargeSpeed(soc);
			}
			
			return 0.;
		}

	};

	
	//Local Methods
	protected double getLossRate(Double soc) {
		// TODO [Andreas] Get value from file with battery data
		return 0;
	}

	protected double getDisChargeSpeed(Double soc) {
		// TODO [Andreas] Get value from file with battery data
		return 0;
	}

	protected double getChargeSpeed(Double soc) {
		// TODO [Andreas] Get value from file with battery data
		return 0;
	}

}
