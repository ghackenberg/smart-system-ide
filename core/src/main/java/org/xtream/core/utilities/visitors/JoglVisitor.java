package org.xtream.core.utilities.visitors;

import org.xtream.core.model.Transform;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.nodes.Background;
import org.xtream.core.model.nodes.Camera;
import org.xtream.core.model.nodes.Group;
import org.xtream.core.model.nodes.Light;
import org.xtream.core.model.nodes.Shape;
import org.xtream.core.model.nodes.lights.Ambient;
import org.xtream.core.model.nodes.lights.Directional;
import org.xtream.core.model.nodes.lights.Point;
import org.xtream.core.model.nodes.shapes.Box;
import org.xtream.core.model.nodes.shapes.Cylinder;
import org.xtream.core.model.nodes.shapes.Sphere;
import org.xtream.core.model.transforms.Rotation;
import org.xtream.core.model.transforms.Scale;
import org.xtream.core.model.transforms.Translation;
import org.xtream.core.utilities.Visitor;
import org.xtream.core.utilities.filters.TypeFilter;

public class JoglVisitor extends Visitor
{
	
	public void handle(Component component)
	{
		traverse(component, "handleBefore", new TypeFilter(Transform.class));
		traverse(component, new TypeFilter(Background.class));
		traverse(component, new TypeFilter(Camera.class));
		traverse(component, new TypeFilter(Light.class));
		traverse(component, new TypeFilter(Shape.class));
		traverse(component, new TypeFilter(Group.class));
		traverse(component, new TypeFilter(Component.class));
		traverse(component, "handleAfter", true, new TypeFilter(Transform.class));
	}
	
	public void handle(Background background)
	{
		//System.out.println("Background!");
	}
	
	public void handle(Ambient light)
	{
		//System.out.println("Ambient light!");
	}
	
	public void handle(Point light)
	{
		//System.out.println("Point light!");
	}
	
	public void handle(Directional light)
	{
		//System.out.println("Directional light!");
	}
	
	public void handle(Camera camera)
	{
		//System.out.println("Camera!");
	}
	
	public void handle(Group group)
	{
		traverse(group, "handleBefore", new TypeFilter(Transform.class));
		traverse(group, new TypeFilter(Camera.class));
		traverse(group, new TypeFilter(Light.class));
		traverse(group, new TypeFilter(Shape.class));
		traverse(group, new TypeFilter(Group.class));
		traverse(group, "handleAfter", true, new TypeFilter(Transform.class));
	}
	
	public void handle(Box box)
	{
		//System.out.println("Box!");
	}
	
	public void handle(Sphere sphere)
	{
		//System.out.println("Sphere!");
	}
	
	public void handle(Cylinder cylinder)
	{
		//System.out.println("Cylinder!");
	}
	
	public void handleBefore(Rotation rotation)
	{
		//System.out.println("Rotation!");
	}
	
	public void handleBefore(Scale scale)
	{
		//System.out.println("Scale!");
	}
	
	public void handleBefore(Translation translation)
	{
		//System.out.println("Translation!");
	}
	
	public void handleAfter(Transform transform)
	{
		//System.out.println("Transform!");
	}

}
