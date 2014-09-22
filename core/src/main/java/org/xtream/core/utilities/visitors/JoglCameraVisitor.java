package org.xtream.core.utilities.visitors;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.utilities.Visitor;
import org.xtream.core.utilities.filters.TypeFilter;

public class JoglCameraVisitor extends Visitor
{
	
	private GL2 gl2;
	private GLU glu;
	private State state;
	private int timepoint;
	
	public JoglCameraVisitor(GL2 gl2, GLU glu, State state, int timepoint)
	{
		this.gl2 = gl2;
		this.glu = glu;
	}
	
	public void handle(Component component)
	{
		traverse(component, new TypeFilter(CameraComponent.class));
		traverse(component, new TypeFilter(Component.class));
	}
	
	public void handle(CameraComponent camera)
	{
		RealMatrix transform = camera.transformInput.get(state, timepoint);
		
		double[] coefficients = new double[16];
		
		for (int col = 0; col < 4; col++)
		{
			for (int row = 0; row < 4; row++)
			{
				coefficients[col * 4 + row] = transform.getEntry(row, col);
			}
		}
		
		gl2.glLoadMatrixd(coefficients, 0);
		
		RealVector eye = camera.eyeOutput.get(state, timepoint);
		RealVector center = camera.centerOutput.get(state, timepoint);
		RealVector up = camera.upOutput.get(state, timepoint);
		
		glu.gluLookAt(eye.getEntry(0), eye.getEntry(1), eye.getEntry(2), center.getEntry(0), center.getEntry(1), center.getEntry(2), up.getEntry(0), up.getEntry(1), up.getEntry(2));
	}

}
