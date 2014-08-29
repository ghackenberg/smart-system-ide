package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Irradiance;
import org.xtream.demo.projecthouse.model.room.lights.LightsController;
import org.xtream.demo.projecthouse.model.room.lights.LightsModule;
import org.xtream.demo.projecthouse.model.room.window.BlindsController;
import org.xtream.demo.projecthouse.model.room.window.WindowModule;
import org.xtream.demo.projecthouse.model.simple.SunComponent;

public class RoomModule extends Module {
	
	public RoomHeatingController heatingController = new RoomHeatingController();
	
	public RoomContext roomContext;
	public BlindsController[] blindsControllers;	
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] blindsChannels;
	public ChannelExpression<OnOffDecision> heatingChannel;
	public ChannelExpression<OnOffDecision> lightsChannel;
	public ChannelExpression<Irradiance> sunChannel;
	
	@SuppressWarnings("unchecked")
	public RoomModule(double volume, double lowerTemperatureLimit, double upperTemperatureLimit, SunComponent sun, LightsModule lights, WindowModule...windows) {
		super();
		roomContext = new RoomContext(volume, lowerTemperatureLimit, upperTemperatureLimit, windows);		
		heatingChannel = new ChannelExpression<>(roomContext.heatingInput, heatingController.onOffOutput);
		lightsChannel = new ChannelExpression<>(roomContext.lightsInput, lights.controller.onOffOutput);
		sunChannel = new ChannelExpression<>(roomContext.irradianceInput, sun.irradianceOutput);
		
		int nrOfWindows = windows.length;
		blindsControllers = new BlindsController[nrOfWindows];
		blindsChannels = new ChannelExpression[nrOfWindows];
		for(int i=0; i<nrOfWindows; i++) {
			blindsChannels[i] = new ChannelExpression<Double>(roomContext.blindsInputs[i], windows[i].blindsController.blindsOutput);
		}
	}

}
