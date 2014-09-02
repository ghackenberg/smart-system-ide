package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Irradiance;
import org.xtream.demo.projecthouse.model.room.lights.LightsModule;
import org.xtream.demo.projecthouse.model.room.window.BlindsController;
import org.xtream.demo.projecthouse.model.room.window.WindowModule;
import org.xtream.demo.projecthouse.model.room.window.WindowSpecification;
import org.xtream.demo.projecthouse.model.simple.SunComponent;

public class RoomModule extends Module {
	
	public RoomHeatingController heatingController = new RoomHeatingController();
	
	public RoomContext roomContext;
	public BlindsController[] blindsControllers;	
	
	public WindowModule[] windows;
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] blindsChannels;
	public ChannelExpression<OnOffDecision> heatingChannel;
	public ChannelExpression<OnOffDecision> lightsChannel;
	public ChannelExpression<Irradiance> sunChannel;
	public ChannelExpression<Double> temperatureChannel;
	
	@SuppressWarnings("unchecked")
	public RoomModule(double volume, double lowerTemperatureLimit, double upperTemperatureLimit, TemperatureComponent comfortTemperature, SunComponent sun, LightsModule lights, WindowSpecification...windowSpecs) {
		super();		
		int nrOfWindows = windowSpecs.length;
		windows = new WindowModule[windowSpecs.length];
		for(int i = 0; i < windowSpecs.length; i++) {
			windows[i] = new WindowModule(windowSpecs[i]);
		}
		
		roomContext = new RoomContext(volume, lowerTemperatureLimit, upperTemperatureLimit, windows);		
		heatingChannel = new ChannelExpression<>(roomContext.heatingInput, heatingController.onOffOutput);
		lightsChannel = new ChannelExpression<>(roomContext.lightsInput, lights.controller.onOffOutput);
		sunChannel = new ChannelExpression<>(roomContext.irradianceInput, sun.irradianceOutput);
		temperatureChannel = new ChannelExpression<>(roomContext.temperatureInput, comfortTemperature.temperatureOutput);
		
		blindsControllers = new BlindsController[nrOfWindows];
		blindsChannels = new ChannelExpression[nrOfWindows];
		for(int i=0; i<nrOfWindows; i++) {
			blindsChannels[i] = new ChannelExpression<Double>(roomContext.blindsInputs[i], windows[i].blindsController.blindsOutput);
		}
		
	}

}
