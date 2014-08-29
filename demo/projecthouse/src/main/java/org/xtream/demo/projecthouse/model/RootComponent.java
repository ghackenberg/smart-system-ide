package org.xtream.demo.projecthouse.model;

import org.xtream.core.model.containers.Component;
import org.xtream.core.workbench.Workbench;
import org.xtream.demo.projecthouse.model.battery.BatteryModule;
import org.xtream.demo.projecthouse.model.net.NetModule;
import org.xtream.demo.projecthouse.model.room.RoomModule;
import org.xtream.demo.projecthouse.model.room.lights.LightsModule;
import org.xtream.demo.projecthouse.model.room.window.WindowModule;
import org.xtream.demo.projecthouse.model.simple.LoadsComponent;
import org.xtream.demo.projecthouse.model.simple.PVComponent;
import org.xtream.demo.projecthouse.model.simple.SunComponent;
import org.xtream.demo.projecthouse.model.thermalstorage.ThermalStorageModule;

public class RootComponent extends Component {

	public static final double START_TEMPERATURE = 20;
	public static final Double BRIGHTNESS_LIMIT = null;

	public static void main(String[] args) {
		new Workbench<>(new RootComponent(), 96, 100, 10, 0, 0);
	}
	
	//Sun
	public SunComponent sun = new SunComponent();

	// Living Room
	public WindowModule livingRoomWindow1 = new WindowModule(3.01 * .60, 115.);
	public WindowModule livingRoomWindow2 = new WindowModule(4.01 * 2.20, 205.);
	public WindowModule livingRoomWindow3 = new WindowModule(1.00 * 2.20, 295.);
	public WindowModule livingRoomWindow4 = new WindowModule(2.01 * 2.20, 205.);
	public WindowModule livingRoomWindow5 = new WindowModule(2.37 * .80, 205.);
	public WindowModule livingRoomWindow6 = new WindowModule(1.37 * .80, 295.);
	
	public LightsModule livingRoomLights = new LightsModule(40); //TODO [Andreas] Find out correct value

	public RoomModule livingRoom = new RoomModule(
			(9.54 + 16.40 + 20.75) * 2.50, 18, 24, sun, livingRoomLights, livingRoomWindow1,
			livingRoomWindow2, livingRoomWindow3, livingRoomWindow4,
			livingRoomWindow5, livingRoomWindow6);
	
	//Bedroom
	public WindowModule bedRoomWindow = new WindowModule(2.01*1.20, 115.);	
	public LightsModule bedRoomLights = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bedRoom = new RoomModule(15.59*2.50, 16, 22, sun, bedRoomLights, bedRoomWindow);
	
	//Bathroom first floor
	public WindowModule bathRoomWindow1 = new WindowModule(2.01*80, 25.);
	public LightsModule bathRoomLights1 = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bathRoom1 = new RoomModule(8.87*2.50, 18, 24, sun, bathRoomLights1, bathRoomWindow1);
	
	//Water closet
	public WindowModule wcWindow = new WindowModule(.76*.80, 295.);
	public LightsModule wcLights = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule waterCloset = new RoomModule(2.81*2.50, 18, 24, sun, wcLights, wcWindow);
	
	//Bathroom second floor
	public WindowModule bathRoomWindow2 = new WindowModule(2.01*80, 25.);
	public LightsModule bathRoomLights2 = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bathRoom2 = new RoomModule(4.42*2.50, 18, 24, sun, bathRoomLights2, bathRoomWindow2);
	
	//Thermal system
	public ThermalStorageModule thermalStorage = new ThermalStorageModule(livingRoom);
	
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
}
