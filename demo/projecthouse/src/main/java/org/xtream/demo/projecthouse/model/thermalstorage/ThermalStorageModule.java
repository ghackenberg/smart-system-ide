package org.xtream.demo.projecthouse.model.thermalstorage;

import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.room.RoomModule;
import org.xtream.demo.projecthouse.model.thermalstorage.electricheatingelement.ElectricHeatingElementModule;
import org.xtream.demo.projecthouse.model.thermalstorage.heatpump.HeatPumpModule;
import org.xtream.demo.projecthouse.model.thermalstorage.pelletheater.PelletHeaterModule;

public class ThermalStorageModule extends Module {
	
	public ElectricHeatingElementModule electricHeatingElement = new ElectricHeatingElementModule();
	public HeatPumpModule heatPump = new HeatPumpModule();
	public PelletHeaterModule pelletHeater = new PelletHeaterModule();
	
	public ThermalStorageContext context;

	public ChannelExpression<OnOffDecision> eheChannel;
	public ChannelExpression<Integer> heatPumpChannel;
	public ChannelExpression<OnOffDecision> pelletHeaterChannel;
	
	public ThermalStorageModule(RoomModule...rooms) {
		context = new ThermalStorageContext(rooms);
		eheChannel = new ChannelExpression<>(context.electricHeatingElementInput, electricHeatingElement.controller.onOffOutput);
		heatPumpChannel = new ChannelExpression<>(context.heatpumpInput, heatPump.controller.levelOutput);
		pelletHeaterChannel = new ChannelExpression<>(context.pelletHeaterInput, pelletHeater.controller.onOffOutput);
	}

}
