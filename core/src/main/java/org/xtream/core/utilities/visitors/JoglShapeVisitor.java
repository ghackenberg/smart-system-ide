package org.xtream.core.utilities.visitors;

import java.awt.Color;

import javax.media.opengl.GL2;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.ShapeComponent;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.utilities.Visitor;
import org.xtream.core.utilities.filters.TypeFilter;

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
		traverse(component, new TypeFilter(ShapeComponent.class));
		traverse(component, new TypeFilter(Component.class));
	}
	
	public void handle(CubeComponent box)
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
		
		gl2.glLoadMatrixd(coefficients, 0);
		
		// Material
		gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);
		// Shape
		glut.glutSolidCube((float) size);
	}
	
	public void handle(SphereComponent sphere)
	{
		throw new IllegalStateException("not implemented yet!");
	}

}
