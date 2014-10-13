package org.xtream.core.utilities.visitors;

import org.xtream.core.model.Component;
import org.xtream.core.model.components.AmbientComponent;
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.model.components.nodes.lights.DirectionalLightComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.utilities.Visitor;

public class PovrayVisitor extends Visitor
{
	
	public void handle(Component component)
	{
		traverse(component);
	}
	
	public void handle(AmbientComponent light)
	{
		
	}
	
	public void handle(PointLightComponent light)
	{
		
	}
	
	public void handle(DirectionalLightComponent light)
	{
		
	}
	
	public void handle(CameraComponent camera)
	{
		System.out.println("Camera " + camera);
	}
	
	public void handle(CubeComponent box)
	{
		System.out.println("Cube " + box);
	}
	
	public void handle(SphereComponent sphere)
	{
		System.out.println("Sphere " + sphere);
	}

}
