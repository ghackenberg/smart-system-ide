package org.xtream.demo.projecthouse.model;

import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Workbench;
import org.xtream.demo.projecthouse.model.battery.BatteryModule;
import org.xtream.demo.projecthouse.model.room.RoomModule;
import org.xtream.demo.projecthouse.model.room.lights.LightsModule;
import org.xtream.demo.projecthouse.model.room.window.WindowSpecification;
import org.xtream.demo.projecthouse.model.simple.BreakerBoxComponent;
import org.xtream.demo.projecthouse.model.simple.LoadsComponent;
import org.xtream.demo.projecthouse.model.simple.NetComponent;
import org.xtream.demo.projecthouse.model.simple.PVComponent;
import org.xtream.demo.projecthouse.model.simple.SunComponent;
import org.xtream.demo.projecthouse.model.thermalstorage.ThermalStorageModule;

public class RootComponent extends Component {

	public static final double START_TEMPERATURE = 20.;
	public static final double BRIGHTNESS_LIMIT = 100.;
	public static final double ELECTRICITY_RATE = .00024;
	public static final double PELLET_PRICE = 1.;
	private static final String LIVINGROOM_PEOPLE_CSV = "livingRoomPeople.csv";
	private static final String BEDROOM_PEOPLE_CSV = "bedRoomPeople.csv";
	private static final String BATHROOM_PEOPLE_CSV = "bathRoomPeople.csv";
	private static final String BATHROOM_PEOPLE_CSV2 = "bathRoomPeople2.csv";
	private static final String WC_PEOPLE_CSV = "wcPeople.csv";
	private static final String LIVINGROOM_TEMPERATURE_CSV = "livingRoomTemperature.csv";
	private static final String BEDROOM_TEMPERATURE_CSV = "bedRoomTemperature.csv";
	private static final String BATHROOM_TEMPERATURE_CSV = "bathRoomTemperature.csv";
	private static final String BATHROOM_TEMPERATURE_CSV2 = "bathRoomTemperature2.csv";
	private static final String WC_TEMPERATURE_CSV = "wcTemperature.csv";
	private static final String OUTER_TEMPERATURE_CSV = "outerTemperature.csv";
	private static final String LOADS_CONSUMPTION_CSV = "loadsConsumption.csv";
	private static final String SOLAR_IRRADIANCE_CSV = "solarIrradiance.csv";
	private static final String NET_CSV = "net.csv";

	public static void main(String[] args) {
		new Workbench<>(new RootComponent(), 144, 100, 50, 0, 0, 50);
	}
	
	//Sun
	public SunComponent sun = new SunComponent(SOLAR_IRRADIANCE_CSV);

	// Living Room
	private WindowSpecification livingRoomWindow1 = new WindowSpecification(3.01 * .60, 115.);
	private WindowSpecification livingRoomWindow2 = new WindowSpecification(4.01 * 2.20, 205.);
	private WindowSpecification livingRoomWindow3 = new WindowSpecification(1.00 * 2.20, 295.);
	private WindowSpecification livingRoomWindow4 = new WindowSpecification(2.01 * 2.20, 205.);
	private WindowSpecification livingRoomWindow5 = new WindowSpecification(2.37 * .80, 205.);
	private WindowSpecification livingRoomWindow6 = new WindowSpecification(1.37 * .80, 295.);
	
	public LightsModule livingRoomLights = new LightsModule(40); //TODO [Andreas] Find out correct value

	public RoomModule livingRoom = new RoomModule(
			(9.54 + 16.40 + 20.75) * 2.50, 18, 24, LIVINGROOM_TEMPERATURE_CSV, sun, livingRoomLights, LIVINGROOM_PEOPLE_CSV, livingRoomWindow1,
			livingRoomWindow2, livingRoomWindow3, livingRoomWindow4,
			livingRoomWindow5, livingRoomWindow6);
	
	//Bedroom
	private WindowSpecification bedRoomWindow = new WindowSpecification(2.01*1.20, 115.);	
	public LightsModule bedRoomLights = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bedRoom = new RoomModule(15.59*2.50, 16, 22, BEDROOM_TEMPERATURE_CSV, sun, bedRoomLights, BEDROOM_PEOPLE_CSV , bedRoomWindow);
	
	//Bathroom first floor
	private WindowSpecification bathRoomWindow1 = new WindowSpecification(2.01*80, 25.);
	public LightsModule bathRoomLights1 = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bathRoom1 = new RoomModule(8.87*2.50, 18, 24, BATHROOM_TEMPERATURE_CSV, sun, bathRoomLights1, BATHROOM_PEOPLE_CSV , bathRoomWindow1);
	
	//Water closet
	private WindowSpecification wcWindow = new WindowSpecification(.76*.80, 295.);
	public LightsModule wcLights = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule waterCloset = new RoomModule(2.81*2.50, 18, 24, WC_TEMPERATURE_CSV, sun, wcLights, WC_PEOPLE_CSV, wcWindow);
	
	//Bathroom second floor
	private WindowSpecification bathRoomWindow2 = new WindowSpecification(2.01*80, 25.);
	public LightsModule bathRoomLights2 = new LightsModule(40); //TODO [Andreas] Find out correct value
	public RoomModule bathRoom2 = new RoomModule(4.42*2.50, 18, 24, BATHROOM_TEMPERATURE_CSV2, sun, bathRoomLights2, BATHROOM_PEOPLE_CSV2, bathRoomWindow2);
	
	//Outer temperature
	TemperatureController outerTemperature = new TemperatureController(OUTER_TEMPERATURE_CSV);
	
	//Thermal system
	public ThermalStorageModule thermalStorage = new ThermalStorageModule(outerTemperature, livingRoom, bedRoom, bathRoom1, waterCloset, bathRoom2);
	
	//Net
	public NetComponent net = new NetComponent(NET_CSV);
	
	//Loads
	public LoadsComponent loads = new LoadsComponent(LOADS_CONSUMPTION_CSV);
	
	//Battery
	public BatteryModule battery = new BatteryModule();
	
	//PV
	public PVComponent pv = new PVComponent();
	public ChannelExpression<Double> outTempChannel = new ChannelExpression<>(pv.outerTemperatureInput, outerTemperature.temperatureOutput);	
	public ChannelExpression<Irradiation> irradiance = new ChannelExpression<>(pv.irradiationInput, sun.irradiationOutput);
	
	//Breaker Box
	private Consumer[] consumers = new Consumer[]{
			livingRoomLights,
			bedRoomLights,
			bathRoomLights1,
			bathRoomLights2,
			wcLights,
			thermalStorage.electricHeatingElement,
			thermalStorage.heatPump,
			loads,
			battery
	};
	
	private Producer[] producers = new Producer[] {
			battery,
			pv
	};
	public BreakerBoxComponent breakerBox = new BreakerBoxComponent(producers, consumers);	
	public ChannelExpression<Double> houseNetChannel = new ChannelExpression<>(net.houseInput, breakerBox.balanceOutput);
	
	//Objective
	public ObjectiveComponent objective = new ObjectiveComponent(thermalStorage.pelletHeater, net, breakerBox, livingRoom, bedRoom, bathRoom1, bathRoom2, waterCloset);
}
