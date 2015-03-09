package org.xtream.demo.infrastructure.model.scenario.factories.profiles;

public enum Car {
 
		CAR("startPosition", "destinationPosition", 1., 1., 1., 1., 0.002, 2., 1., 1., 85., 5., 5., 0., 85., 0.15, 0.), 
		BUS("startPosition", "destinationPosition", 1., 1., 1., 1., 0.011, 8., 1., 1., 200., 5., 5., 0., 200., 0.2, 0.),
		
		LOWENERGYCAR("startPosition", "destinationPosition", 1., 15., 1., 1., 0.002, 2., 1., 1., 25., 5., 5., 0., 85., 0.15, 0.), 
		LOWENERGYBUS("startPosition", "destinationPosition", 1., 1., 1., 1., 0.011, 2., 1., 1., 80., 5., 5., 0., 85., 0.2, 0.), 
		
		RANDOMLOWENERGYCAR("startPosition", "destinationPosition", 1., 15., 1., 1., 0.002, 2., 1., 1., 25., 5., 5., 0., 85., 0.15, 1.); 
		
		private final String startPosition; 
		private final String destinationPosition; 
		private final Double stateOfChargeWeight; 
		private final Double timeWeight; 
		private final Double powerWeight; 
		private final Double priority; 
		private final Double vehicleLength; 
		private final Double vehicleWeight; 
		private final Double enginePerformance; 
		private final Double engineEfficiency; 
		private final Double stateOfCharge; 
		private final Double stateOfChargeInputRate; 
		private final Double stateOfChargeOutputRate; 
		private final Double stateOfChargeMinimum; 
		private final Double stateOfChargeMaximum;
		private final Double vehicleRangeAnxiety;
		private final Double chargingStationSelectionRandomness;
		
		public String startPosition() { return startPosition; }
		public String destinationPosition() { return destinationPosition; }
		public Double stateOfChargeWeight() { return stateOfChargeWeight; }
		public Double timeWeight() { return timeWeight; }
		public Double powerWeight() { return powerWeight; }
		public Double priority() { return priority; }
		public Double vehicleLength() { return vehicleLength; }
		public Double vehicleWeight() { return vehicleWeight; }
		public Double enginePerformance() { return enginePerformance; }
		public Double engineEfficiency() { return engineEfficiency; }
		public Double stateOfCharge() { return stateOfCharge; }
		public Double stateOfChargeInputRate() { return stateOfChargeInputRate; }
		public Double stateOfChargeOutputRate() { return stateOfChargeOutputRate; }
		public Double stateOfChargeMinimum() { return stateOfChargeMinimum; }
		public Double stateOfChargeMaximum() { return stateOfChargeMaximum; }
		public Double vehicleRangeAnxiety() { return vehicleRangeAnxiety; }
		public Double chargingStationSelectionRandomness() { return chargingStationSelectionRandomness; }
		
		Car (String startPosition, String destinationPosition, Double stateOfChargeWeight, Double timeWeight, Double powerWeight, Double priority, Double vehicleLength, Double vehicleWeight, Double enginePerformance, Double engineEfficiency, Double stateOfCharge, Double stateOfChargeInputRate, Double stateOfChargeOutputRate, Double stateOfChargeMinimum, Double stateOfChargeMaximum, Double vehicleRangeAnxiety, Double chargingStationSelectionRandomness)
		{
			this.startPosition = startPosition;
			this.destinationPosition = destinationPosition;
			this.stateOfChargeWeight = stateOfChargeWeight; 
			this.timeWeight = timeWeight;
			this.powerWeight = powerWeight;
			this.priority = priority;
			this.vehicleLength = vehicleLength;
			this.vehicleWeight = vehicleWeight;
			this.enginePerformance = enginePerformance;
			this.engineEfficiency = engineEfficiency;
			this.stateOfCharge = stateOfCharge;
			this.stateOfChargeInputRate = stateOfChargeInputRate; 
			this.stateOfChargeOutputRate = stateOfChargeOutputRate;
			this.stateOfChargeMinimum = stateOfChargeMinimum;
			this.stateOfChargeMaximum = stateOfChargeMaximum;
			this.vehicleRangeAnxiety = vehicleRangeAnxiety;
			this.chargingStationSelectionRandomness = chargingStationSelectionRandomness;
		}
}
