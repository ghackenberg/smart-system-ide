package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Module;
import org.xtream.core.model.charts.Timeline;
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

	public RoomContext context;
	public BlindsController[] blindsControllers;

	public WindowModule[] windows;

	@SuppressWarnings("rawtypes")
	public ChannelExpression[] blindsChannels;
	public ChannelExpression<OnOffDecision> heatingChannel;
	public ChannelExpression<OnOffDecision> lightsChannel;
	public ChannelExpression<Irradiation> sunChannel;
	public ChannelExpression<Double> favoriteTemperatureContextChannel;
	public ChannelExpression<Double> favoriteTemperatureControllerChannel;
	public ChannelExpression<Double> roomTemperatureChannel;
	public ChannelExpression<Double> peopleChannel;

	public Chart people;
	public Chart temperature;
	public Chart brightness;
	public Chart comfort;

	@SuppressWarnings("unchecked")
	public RoomModule(double volume, double lowerTemperatureLimit,
			double upperTemperatureLimit, String temperatureFile,
			SunComponent sun, LightsModule lights, String peopleFile,
			WindowSpecification... windowSpecs) {
		super();
		int nrOfWindows = windowSpecs.length;
		windows = new WindowModule[windowSpecs.length];
		for (int i = 0; i < windowSpecs.length; i++) {
			windows[i] = new WindowModule(windowSpecs[i]);
		}
		peopleController = new PeopleController(peopleFile);
		temperatureController = new TemperatureController(temperatureFile);

		context = new RoomContext(volume, lowerTemperatureLimit,
				upperTemperatureLimit, windows);
		heatingChannel = new ChannelExpression<>(context.heatingInput,
				heatingController.onOffOutput);
		lightsChannel = new ChannelExpression<>(context.lightsInput,
				lights.controller.onOffOutput);
		sunChannel = new ChannelExpression<>(context.irradiationInput,
				sun.irradiationOutput);
		favoriteTemperatureContextChannel = new ChannelExpression<>(
				context.favoriteTemperatureInput,
				temperatureController.temperatureOutput);
		peopleChannel = new ChannelExpression<>(context.probabilityInput,
				peopleController.probabilityOutput);
		roomTemperatureChannel = new ChannelExpression<>(
				heatingController.roomTemperatureInput,
				context.temperatureOutput);
		favoriteTemperatureControllerChannel = new ChannelExpression<>(
				heatingController.favoriteTemperatureInput,
				temperatureController.temperatureOutput);

		blindsControllers = new BlindsController[nrOfWindows];
		blindsChannels = new ChannelExpression[nrOfWindows];
		for (int i = 0; i < nrOfWindows; i++) {
			blindsChannels[i] = new ChannelExpression<Double>(
					context.blindsInputs[i],
					windows[i].blindsController.blindsOutput);
		}

		people = new Timeline(peopleController.probabilityOutput);
		temperature = new Timeline(context.temperatureOutput);
		brightness = new Timeline(context.brightnessOutput);
		comfort = new Timeline(context.comfortOutput);
	}

}
