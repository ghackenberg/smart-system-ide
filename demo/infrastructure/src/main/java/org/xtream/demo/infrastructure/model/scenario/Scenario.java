package org.xtream.demo.infrastructure.model.scenario;

import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.model.power.PowerComponent;
import org.xtream.demo.infrastructure.model.scene.SceneComponent;
import org.xtream.demo.infrastructure.model.transportation.TransportationComponent;

public abstract class Scenario
{
	public static int DURATION = 60;
	public static int SAMPLES = 10;
	public static int CLUSTERS = 30;
	public static double RANDOMNESS = 0.0;
	public static double CACHING = 0;
	public static int ROUNDS = 1000;
	
	public static Double MODELSCALE = 1.0; // 60 seconds 
	public static Graph context = new Graph("InfrastructureMAP.xml");
	
	public abstract TransportationComponent createTransportationComponent(PowerComponent powerComponent);
	
	public abstract PowerComponent createPowerComponent();
	
	public abstract SceneComponent createSceneComponent(TransportationComponent transportationComponent, PowerComponent powerComponent);
}
