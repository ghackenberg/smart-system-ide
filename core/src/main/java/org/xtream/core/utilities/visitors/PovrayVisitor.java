package org.xtream.core.utilities.visitors;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.components.AmbientComponent;
import org.xtream.core.model.components.BackgroundComponent;
import org.xtream.core.model.components.PlaneComponent;
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.model.components.nodes.lights.DirectionalLightComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.model.components.nodes.shapes.ConeComponent;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.nodes.shapes.CylinderComponent;
import org.xtream.core.model.components.nodes.shapes.LineComponent;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.model.components.nodes.shapes.TorusComponent;
import org.xtream.core.utilities.Visitor;

public class PovrayVisitor extends Visitor 

{
	private FileWriter writer;
	private State state;
	private int timepoint;

	public PovrayVisitor(FileWriter writer, State state, int timepoint) 
	{
		this.writer = writer;
		this.state = state;
		this.timepoint = timepoint;
	}

	public void handle(Component component) 
	{
		traverse(component);
	}

	public void handle(AmbientComponent light) 
	{

	}

	public void handle(PointLightComponent light) 
	{
		try 
		{
			RealVector position = light.positionOutput.get(state, timepoint);
			Color diffuse = light.diffuseOutput.get(state, timepoint);
			//Color specular = light.specularOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("light_source{ " + handle(position) + " " + handle(diffuse) + " }");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void handle(DirectionalLightComponent light) 
	{
		try 
		{
			RealVector direction = light.directionOutput.get(state, timepoint);
			Color diffuse = light.diffuseOutput.get(state, timepoint);
			//Color specular = light.specularOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("light_source{ <0,0,0> " + handle(diffuse) + " parallel point_at " + handle(direction) + " }");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void handle(CameraComponent camera) 
	{
		try 
		{
			RealVector location = camera.eyeOutput.get(state, timepoint);
			RealVector look_at = camera.centerOutput.get(state, timepoint);
			
			writer.write("camera{ location " + handle(location) + " look_at " + handle(look_at) + " up <0,1,0>}");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}
	
	public void handle(BackgroundComponent background)
	{
		try 
		{
			Color color = background.colorOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("background { " + handle(color) + " }");
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	
	public void handle(PlaneComponent plane)
	{
		try 
		{
			Color color = plane.colorOutput.get(state, timepoint);
			Double height = plane.heightOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("plane{ <0,1,0>," + height + " texture{pigment{" + handle(color) +"}}}");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void handle(CubeComponent box) 
	{
		try 
		{
			RealMatrix transform = box.transformInput.get(state, timepoint);
			double size = box.sizeOutput.get(state, timepoint);
			Color color = box.colorOutput.get(state, timepoint);
			
			double left = -size/2;
			double right = size/2;
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("box { <" + left + "," + left + "," + left + ">, <" + right + "," + right + "," + right + "> texture { pigment { " + handle(color) + " } } finish { diffuse 0.9 phong 1 } " + handle(transform) + " }");
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}

	public void handle(SphereComponent sphere) 
	{
		try 
		{
			//RealVector position = sphere.positionOutput.get(state, timepoint);
			RealMatrix transform = sphere.transformInput.get(state, timepoint);
			double radius = sphere.radiusOutput.get(state, timepoint);
			Color color = sphere.colorOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("sphere { <0,0,0>, " + radius + " texture { pigment { " + handle(color) + " } } finish { diffuse 0.9 phong 1 } " + handle(transform) + " }");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void handle(ConeComponent cone) 
	{
		try 
		{
			RealMatrix transform = cone.transformInput.get(state, timepoint);
			double base = cone.baseOutput.get(state, timepoint);
			double height = cone.heightOutput.get(state, timepoint);
			Color color = cone.colorOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("cone {<0,0,0>, "+ base +" <0,0," + height + ">, 0.1  texture { pigment{ " + handle(color) + " } } finish { diffuse 0.9 phong 1 } " + handle(transform) + " }");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void handle(CylinderComponent cylinder) 
	{
		try 
		{
			RealMatrix transform = cylinder.transformInput.get(state, timepoint);
			double base = cylinder.baseOutput.get(state, timepoint);
			double height = cylinder.heightOutput.get(state, timepoint);
			Color color = cylinder.colorOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("cylinder {<0,0,0>, <0,0,"+ height +">, " + base + "  texture { pigment { " + handle(color) + " } } finish { diffuse 0.9 phong 1 } " + handle(transform) + " }");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void handle(LineComponent line) 
	{
		try 
		{
			RealMatrix transform = line.transformInput.get(state, timepoint);
			RealVector start = line.startOutput.get(state, timepoint);
			RealVector end = line.endOutput.get(state, timepoint);
			Color color = line.colorOutput.get(state, timepoint);
			
			if (start.getDistance(end) != 0)
			{
				writer.write(System.getProperty( "line.separator" ));
				writer.write("cylinder {" + handle(start) + "," + handle(end) + ", 0.1 texture { pigment { " + handle(color) + " } } finish { diffuse 0.9 phong 1 } " + handle(transform) + " }");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void handle(TorusComponent torus) 
	{
		try 
		{
			RealMatrix transform = torus.transformInput.get(state, timepoint);
			Double innerRadius = torus.innerRadiusOutput.get(state, timepoint);
			Double outerRadius = torus.outerRadiusOutput.get(state, timepoint);
			Color color = torus.colorOutput.get(state, timepoint);
			
			writer.write(System.getProperty( "line.separator" ));
			writer.write("torus {" + outerRadius + "," + innerRadius + " texture { pigment { " + handle(color) + " } } finish { diffuse 0.9 phong 1 } " + handle(transform) + " }");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private String handle(Color color)
	{
		return "color rgb<" + color.getRed() / 255f + "," + color.getGreen() / 255f + "," + color.getBlue() / 255f + ">";
	}
	
	private String handle(RealVector vector)
	{
		return "<" + vector.getEntry(0) + "," + vector.getEntry(1) + "," + vector.getEntry(2) + ">";
	}
	
	private String handle(RealMatrix matrix)
	{
		String result = "matrix <";
		for (int col = 0; col < 4; col++)
		{
			for (int row = 0; row < 3; row++)
			{
				result += (row == 0 && col == 0 ? "" : ",") + matrix.getEntry(row, col);
			}
		}
		result += ">";
		
		return result;
	}
}
