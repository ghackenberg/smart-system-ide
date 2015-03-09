package org.xtream.demo.infrastructure.model.scenario;

import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.model.power.PowerComponent;
import org.xtream.demo.infrastructure.model.scene.SceneComponent;
import org.xtream.demo.infrastructure.model.transportation.TransportationComponent;

public abstract class Scenario
{
	public static int DURATION = 60;
	public static int SAMPLES = 75;
	public static int CLUSTERS = 75;
	public static int BRANCH_ROUNDS = 2;
	public static long BRANCH_DURATION = 5;
	public static int KMEANS_ROUNDS = 1000;
	public static long KMEANS_DURATION = 100;
	
	public static Double MODELSCALE = 1.0; // 60 seconds 
	public static Graph context = new Graph("InfrastructureMAP.xml");
	
	public abstract TransportationComponent createTransportationComponent(PowerComponent powerComponent);
	
	public abstract PowerComponent createPowerComponent();
	
	public abstract SceneComponent createSceneComponent(TransportationComponent transportationComponent, PowerComponent powerComponent);
}
