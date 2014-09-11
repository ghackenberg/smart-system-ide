package org.xtream.demo.projecthouse.model.room;

import java.io.File;
import java.net.URISyntaxException;

import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Irradiation;
import org.xtream.demo.projecthouse.model.TemperatureController;
import org.xtream.demo.projecthouse.model.room.lights.LightsModule;
import org.xtream.demo.projecthouse.model.room.window.BlindsController;
import org.xtream.demo.projecthouse.model.room.window.WindowModule;
import org.xtream.demo.projecthouse.model.room.window.WindowSpecification;
import org.xtream.demo.projecthouse.model.simple.SunComponent;

public class RoomModule extends Module {
	
	public RoomHeatingController heatingController = new RoomHeatingController();
    public PeopleController peopleController;
    public TemperatureController temperatureController;
	
	public RoomContext roomContext;
	public BlindsController[] blindsControllers;	
	
	public WindowModule[] windows;
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] blindsChannels;
	public ChannelExpression<OnOffDecision> heatingChannel;
	public ChannelExpression<OnOffDecision> lightsChannel;
	public ChannelExpression<Irradiation> sunChannel;
	public ChannelExpression<Double> temperatureChannel;
	public ChannelExpression<Double> peopleChannel;
	
	@SuppressWarnings("unchecked")
	public RoomModule(double volume, double lowerTemperatureLimit, double upperTemperatureLimit, String temperatureFile, SunComponent sun, LightsModule lights, String peopleFile, WindowSpecification...windowSpecs) {
		super();		
		int nrOfWindows = windowSpecs.length;
		windows = new WindowModule[windowSpecs.length];
		for(int i = 0; i < windowSpecs.length; i++) {
			windows[i] = new WindowModule(windowSpecs[i]);
		}
		peopleController = new PeopleController(peopleFile);
		
		File file;
		try {
			file = new File(getClass().getResource(temperatureFile).toURI());
			temperatureController = new TemperatureController(file);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + temperatureFile);
		}
		
		roomContext = new RoomContext(volume, lowerTemperatureLimit, upperTemperatureLimit, windows);		
		heatingChannel = new ChannelExpression<>(roomContext.heatingInput, heatingController.onOffOutput);
		lightsChannel = new ChannelExpression<>(roomContext.lightsInput, lights.controller.onOffOutput);
		sunChannel = new ChannelExpression<>(roomContext.irradianceInput, sun.irradiationOutput);
		temperatureChannel = new ChannelExpression<>(roomContext.temperatureInput, temperatureController.temperatureOutput);
		peopleChannel = new ChannelExpression<>(roomContext.possibilityInput, peopleController.possibilityOutput);
		
		blindsControllers = new BlindsController[nrOfWindows];
		blindsChannels = new ChannelExpression[nrOfWindows];
		for(int i=0; i<nrOfWindows; i++) {
			blindsChannels[i] = new ChannelExpression<Double>(roomContext.blindsInputs[i], windows[i].blindsController.blindsOutput);
		}
		
	}

}
