package org.xtream.core.utilities.visitors;

import java.awt.Color;

import javax.media.opengl.GL2;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.shapes.ConeComponent;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.nodes.shapes.CylinderComponent;
import org.xtream.core.model.components.nodes.shapes.LineComponent;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.model.components.nodes.shapes.TorusComponent;
import org.xtream.core.utilities.Visitor;

import com.jogamp.opengl.util.gl2.GLUT;

public class JoglShapeVisitor extends Visitor
{
	
	private GL2 gl2;
	private GLUT glut;
	private State state;
	private int timepoint;
	
	public JoglShapeVisitor(GL2 gl2, GLUT glut, State state, int timepoint)
	{
		this.gl2 = gl2;
		this.glut = glut;
		this.state = state;
		this.timepoint = timepoint;
	}
	
	public void handle(Component component)
	{
		traverse(component);
	}
	
	public void handle(CubeComponent box)
	{
		gl2.glPushMatrix();
		{
			RealMatrix transform = box.transformInput.get(state, timepoint);
			Color color = box.colorOutput.get(state, timepoint);
			double size = box.sizeOutput.get(state, timepoint);
			
			// Transform
			double[] coefficients = new double[16];
			
			for (int col = 0; col < 4; col++)
			{
				for (int row = 0; row < 4; row++)
				{
					coefficients[col * 4 + row] = transform.getEntry(row, col);
				}
			}
			
			gl2.glMultMatrixd(coefficients, 0);
			
			// Material
			gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);
			// Shape
			glut.glutSolidCube((float) size);
		}
		gl2.glPopMatrix();
	}
	
	public void handle(LineComponent line)
	{
		gl2.glPushMatrix();
		{
			RealMatrix transform = line.transformInput.get(state, timepoint);
			Color color = line.colorOutput.get(state, timepoint);
			RealVector start = line.startOutput.get(state, timepoint);
			RealVector end = line.endOutput.get(state, timepoint);
			
			// Transform
			double[] coefficients = new double[16];
			
			for (int col = 0; col < 4; col++)
			{
				for (int row = 0; row < 4; row++)
				{
					coefficients[col * 4 + row] = transform.getEntry(row, col);
				}
			}
			
			gl2.glMultMatrixd(coefficients, 0);
			
			gl2.glBegin(GL2.GL_LINES);
			{
				// Material
				gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);
				// Vertices
				gl2.glVertex3f((float) start.getEntry(0), (float) start.getEntry(1), (float) start.getEntry(2));
				gl2.glVertex3f((float) end.getEntry(0), (float) end.getEntry(1), (float) end.getEntry(2));
			}
			gl2.glEnd();
		}
		gl2.glPopMatrix();
	}
	
	public void handle(SphereComponent sphere)
	{
		gl2.glPushMatrix();
		{
			RealMatrix transform = sphere.transformInput.get(state, timepoint);
			Color color = sphere.colorOutput.get(state, timepoint);
			double radius = sphere.radiusOutput.get(state, timepoint);
			
			// Transform
			double[] coefficients = new double[16];
			
			for (int col = 0; col < 4; col++)
			{
				for (int row = 0; row < 4; row++)
				{
					coefficients[col * 4 + row] = transform.getEntry(row, col);
				}
			}
			
			gl2.glMultMatrixd(coefficients, 0);
			
			// Material
			gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);
			// Shape
			glut.glutSolidSphere((float) radius, 100, 100);
		}
		gl2.glPopMatrix();
	}
	
	public void handle(TorusComponent torus)
	{
		gl2.glPushMatrix();
		{
			RealMatrix transform = torus.transformInput.get(state, timepoint);
			Color color = torus.colorOutput.get(state, timepoint);
			double innerRadius = torus.innerRadiusOutput.get(state, timepoint);
			double outerRadius = torus.outerRadiusOutput.get(state, timepoint);
			
			// Transform
			double[] coefficients = new double[16];
			
			for (int col = 0; col < 4; col++)
			{
				for (int row = 0; row < 4; row++)
				{
					coefficients[col * 4 + row] = transform.getEntry(row, col);
				}
			}
			
			gl2.glMultMatrixd(coefficients, 0);
			
			// Material
			gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);
			// Shape
			glut.glutSolidTorus(innerRadius, outerRadius, 100, 100);
		}
		gl2.glPopMatrix();
	}
	
	public void handle(ConeComponent cone)
	{
		gl2.glPushMatrix();
		{
			RealMatrix transform = cone.transformInput.get(state, timepoint);
			Color color = cone.colorOutput.get(state, timepoint);
			double base = cone.baseOutput.get(state, timepoint);
			double height = cone.heightOutput.get(state, timepoint);
			
			// Transform
			double[] coefficients = new double[16];
			
			for (int col = 0; col < 4; col++)
			{
				for (int row = 0; row < 4; row++)
				{
					coefficients[col * 4 + row] = transform.getEntry(row, col);
				}
			}
			
			gl2.glMultMatrixd(coefficients, 0);
			
			// Material
			gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);
			// Shape
			glut.glutSolidCone(base, height, 100, 100);
		}
		gl2.glPopMatrix();
	}

	public void handle(CylinderComponent cylinder)
	{
		gl2.glPushMatrix();
		{
			RealMatrix transform = cylinder.transformInput.get(state, timepoint);
			Color color = cylinder.colorOutput.get(state, timepoint);
			double radius = cylinder.baseOutput.get(state, timepoint);
			double height = cylinder.heightOutput.get(state, timepoint);
			
			// Transform
			double[] coefficients = new double[16];
			
			for (int col = 0; col < 4; col++)
			{
				for (int row = 0; row < 4; row++)
				{
					coefficients[col * 4 + row] = transform.getEntry(row, col);
				}
			}
			
			gl2.glMultMatrixd(coefficients, 0);
			
			// Material
			gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);
			// Shape
			glut.glutSolidCylinder(radius, height, 100, 100);
		}
		gl2.glPopMatrix();
	}
}
