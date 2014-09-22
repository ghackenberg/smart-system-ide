package org.xtream.core.utilities.visitors;

import org.xtream.core.model.Component;
import org.xtream.core.model.components.TransformComponent;
import org.xtream.core.model.components.nodes.BackgroundComponent;
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.model.components.nodes.LightComponent;
import org.xtream.core.model.components.nodes.ShapeComponent;
import org.xtream.core.model.components.nodes.lights.AmbientLightComponent;
import org.xtream.core.model.components.nodes.lights.DirectionalLightComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.model.components.nodes.shapes.BoxComponent;
import org.xtream.core.model.components.nodes.shapes.CylinderComponent;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.model.components.transforms.chains.RotationComponent;
import org.xtream.core.model.components.transforms.chains.ScaleComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.utilities.Visitor;
import org.xtream.core.utilities.filters.TypeFilter;

public class JoglVisitor extends Visitor
{
	
	public void handle(Component component)
	{
		traverse(component, "handleBefore", new TypeFilter(TransformComponent.class));
		traverse(component, new TypeFilter(BackgroundComponent.class));
		traverse(component, new TypeFilter(CameraComponent.class));
		traverse(component, new TypeFilter(LightComponent.class));
		traverse(component, new TypeFilter(ShapeComponent.class));
		traverse(component, new TypeFilter(Component.class));
		traverse(component, "handleAfter", true, new TypeFilter(TransformComponent.class));
	}
	
	public void handle(BackgroundComponent background)
	{
		//System.out.println("Background!");
	}
	
	public void handle(AmbientLightComponent light)
	{
		//System.out.println("Ambient light!");
	}
	
	public void handle(PointLightComponent light)
	{
		//System.out.println("Point light!");
	}
	
	public void handle(DirectionalLightComponent light)
	{
		//System.out.println("Directional light!");
	}
	
	public void handle(CameraComponent camera)
	{
		//System.out.println("Camera!");
	}
	
	public void handle(BoxComponent box)
	{
		//System.out.println("Box!");
	}
	
	public void handle(SphereComponent sphere)
	{
		//System.out.println("Sphere!");
	}
	
	public void handle(CylinderComponent cylinder)
	{
		//System.out.println("Cylinder!");
	}
	
	public void handleBefore(RotationComponent rotation)
	{
		//System.out.println("Rotation!");
	}
	
	public void handleBefore(ScaleComponent scale)
	{
		//System.out.println("Scale!");
	}
	
	public void handleBefore(TranslationComponent translation)
	{
		//System.out.println("Translation!");
	}
	
	public void handleAfter(TransformComponent transform)
	{
		//System.out.println("Transform!");
	}

}
