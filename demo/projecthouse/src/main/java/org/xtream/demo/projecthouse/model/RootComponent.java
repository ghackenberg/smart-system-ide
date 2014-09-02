package org.xtream.demo.projecthouse.model;

import org.xtream.core.model.containers.Component;
import org.xtream.core.workbench.Workbench;
import org.xtream.demo.projecthouse.model.battery.BatteryModule;
import org.xtream.demo.projecthouse.model.net.NetModule;
import org.xtream.demo.projecthouse.model.room.RoomModule;
import org.xtream.demo.projecthouse.model.room.TemperatureComponent;
import org.xtream.demo.projecthouse.model.room.lights.LightsModule;
import org.xtream.demo.projecthouse.model.room.window.WindowSpecification;
import org.xtream.demo.projecthouse.model.simple.BreakerBoxComponent;
import org.xtream.demo.projecthouse.model.simple.LoadsComponent;
import org.xtream.demo.projecthouse.model.simple.PVComponent;
import org.xtream.demo.projecthouse.model.simple.SunComponent;
import org.xtream.demo.projecthouse.model.thermalstorage.ThermalStorageModule;

public class RootComponent extends Component {

	public static final double START_TEMPERATURE = 20.;
	public static final double BRIGHTNESS_LIMIT = 0.;
	public static final double ELECTRICITY_RATE = 0.;
	public static final double PELLET_PRICE = 0.;

	public static void main(String[] args) {
		new Workbench<>(new RootComponent(), 96, 100, 10, 0, 0);
	}
	
	public TemperatureComponent temp1 = new TemperatureComponent();
	
	//Sun
	public SunComponent sun = new SunComponent();

	// Living Room
	private WindowSpecification livingRoomWindow1 = new WindowSpecification(3.01 * .60, 115.);
	private WindowSpecification livingRoomWindow2 = new WindowSpecification(4.01 * 2.20, 205.);
	private WindowSpecification livingRoomWindow3 = new WindowSpecification(1.00 * 2.20, 295.);
	private WindowSpecification livingRoomWindow4 = new WindowSpecification(2.01 * 2.20, 205.);
	private WindowSpecification livingRoomWindow5 = new WindowSpecification(2.37 * .80, 205.);
	private WindowSpecification livingRoomWindow6 = new WindowSpecification(1.37 * .80, 295.);
	
	public LightsModule livingRoomLights = new LightsModule(40); //TODO [Andreas] Find out correct value

	public RoomModule livingRoom = new RoomModule(
			(9.54 + 16.40 + 20.75) * 2.50, 18, 24, temp1, sun, livingRoomLights, livingRoomWindow1,
			livingRoomWindow2, livingRoomWindow3, livingRoomWindow4,
			livingRoomWindow5, livingRoomWindow6);
	
	//Bedroom
	private WindowSpecification bedRoomWindow = new WindowSpecification(2.01*1.20, 115.);	
	public LightsModule bedRoomLights = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bedRoom = new RoomModule(15.59*2.50, 16, 22, temp1, sun, bedRoomLights, bedRoomWindow);
	
	//Bathroom first floor
	private WindowSpecification bathRoomWindow1 = new WindowSpecification(2.01*80, 25.);
	public LightsModule bathRoomLights1 = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bathRoom1 = new RoomModule(8.87*2.50, 18, 24, temp1, sun, bathRoomLights1, bathRoomWindow1);
	
	//Water closet
	private WindowSpecification wcWindow = new WindowSpecification(.76*.80, 295.);
	public LightsModule wcLights = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule waterCloset = new RoomModule(2.81*2.50, 18, 24, temp1, sun, wcLights, wcWindow);
	
	//Bathroom second floor
	private WindowSpecification bathRoomWindow2 = new WindowSpecification(2.01*80, 25.);
	public LightsModule bathRoomLights2 = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bathRoom2 = new RoomModule(4.42*2.50, 18, 24, temp1, sun, bathRoomLights2, bathRoomWindow2);
	
	//Thermal system
	public ThermalStorageModule thermalStorage = new ThermalStorageModule(livingRoom, bedRoom, bathRoom1, waterCloset, bathRoom2);
	
	//Net
	public NetModule net = new NetModule();
	
	//Loads
	public LoadsComponent loads = new LoadsComponent();
	
	//Battery
	public BatteryModule battery = new BatteryModule();
	
	//PV
	public PVComponent pv = new PVComponent();
	
	//Breaker Box
	private Consumer[] consumers = new Consumer[]{
			livingRoomLights,
			bedRoomLights,
			bathRoomLights1,
			bathRoomLights2,
			wcLights,
			thermalStorage.electricHeatingElement,
			thermalStorage.heatPump,
			net,
			loads,
			battery
	};
	
	private Producer[] producers = new Producer[] {
			net,
			battery,
			pv
	};
	
	public BreakerBoxComponent breakerBox = new BreakerBoxComponent(producers, consumers);
	
	//Objective
	ObjectiveComponent objective = new ObjectiveComponent(thermalStorage.pelletHeater, net, livingRoom, bedRoom, bathRoom1, bathRoom2, waterCloset);
}
