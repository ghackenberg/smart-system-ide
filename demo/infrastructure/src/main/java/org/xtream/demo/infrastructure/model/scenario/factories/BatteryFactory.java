package org.xtream.demo.infrastructure.model.scenario.factories;

import org.xtream.demo.infrastructure.model.power.BatteryComponent;

public class BatteryFactory {
	
	public BatteryComponent generateBattery(Double batteryEfficiency, Double batteryLoss, Double stateOfCharge, Double stateOfChargeSpeed, Double stateOfChargeMinimum, Double stateOfChargeMaximum) 
	{
		return new BatteryComponent(batteryEfficiency, batteryLoss, stateOfCharge, stateOfChargeSpeed, stateOfChargeMinimum, stateOfChargeMaximum);
	}
	
	public BatteryComponent generateRandomBattery(Double maximumBatteryEfficiency, Double maximumBatteryEfficiencyRandomness, Double maximumBatteryLoss, Double maximumBatteryLossRandomness, Double maximumStateOfCharge, Double maximumStateOfChargeRandomness, Double maximumStateOfChargeSpeed, Double maximumStateOfChargeSpeedRandomness, Double maximumStateOfChargeMinimum, Double maximumStateOfChargeMinimumRandomness, Double maximumStateOfChargeMaximum, Double maximumStateOfChargeMaximumRandomness) 
	{
		 Double randomizedMaximumStateOfCharge = maximumStateOfCharge-(Math.random()*maximumStateOfChargeRandomness);
		 Double randomizedMaximumStateOfChargeMaximum = maximumStateOfChargeMaximum-(Math.random()*maximumStateOfChargeMaximum);
		
		 if (randomizedMaximumStateOfCharge > randomizedMaximumStateOfChargeMaximum)
		 {
			 randomizedMaximumStateOfChargeMaximum = randomizedMaximumStateOfCharge;
		 }
		 
		return new BatteryComponent(maximumBatteryEfficiency-(Math.random()*maximumBatteryEfficiencyRandomness), maximumBatteryLoss-(Math.random()*maximumBatteryLossRandomness), randomizedMaximumStateOfCharge, maximumStateOfChargeSpeed-(Math.random()*maximumStateOfChargeSpeedRandomness), Math.abs(maximumStateOfChargeMinimum-(Math.random()*maximumStateOfChargeMinimumRandomness)), randomizedMaximumStateOfChargeMaximum);
	}

}
