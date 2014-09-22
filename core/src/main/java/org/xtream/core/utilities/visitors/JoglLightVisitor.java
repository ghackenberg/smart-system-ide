package org.xtream.core.utilities.visitors;

import java.awt.Color;

import javax.media.opengl.GL2;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.components.AmbientComponent;
import org.xtream.core.model.components.BackgroundComponent;
import org.xtream.core.model.components.nodes.LightComponent;
import org.xtream.core.model.components.nodes.lights.DirectionalLightComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.utilities.Visitor;
import org.xtream.core.utilities.filters.TypeFilter;

public class JoglLightVisitor extends Visitor
{
	
	private GL2 gl2;
	private State state;
	private int timepoint;
	
	public JoglLightVisitor(GL2 gl2, State state, int timepoint)
	{
		this.gl2 = gl2;
		this.state = state;
		this.timepoint = timepoint;
	}
	
	public void handle(Component component)
	{
		traverse(component, new TypeFilter(BackgroundComponent.class));
		traverse(component, new TypeFilter(AmbientComponent.class));
		traverse(component, new TypeFilter(LightComponent.class));
		traverse(component, new TypeFilter(Component.class));
	}
	
	public void handle(BackgroundComponent background)
	{
		Color color = background.colorOutput.get(state, timepoint);
		
		// Clear
		gl2.glClearColor(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f, 0f);
		gl2.glClearDepth(1f);
	}
	
	public void handle(AmbientComponent ambient)
	{
		Color color = ambient.colorOutput.get(state, timepoint);
		
		// Light
		gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f, 1f}, 0);
	}
	
	public void handle(PointLightComponent light)
	{
		gl2.glPushMatrix();
		{
			RealMatrix transform = light.transformInput.get(state, timepoint);
			
			double[] coefficients = new double[16];
			
			for (int col = 0; col < 4; col++)
			{
				for (int row = 0; row < 4; row++)
				{
					coefficients[col * 4 + row] = transform.getEntry(row, col);
				}
			}
			
			gl2.glMultMatrixd(coefficients, 0);
			
			RealVector position = light.positionOutput.get(state, timepoint);
			Color specular = light.specularOutput.get(state, timepoint);
			Color diffuse = light.diffuseOutput.get(state, timepoint);
			
			gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {(float) position.getEntry(0), (float) position.getEntry(1), (float) position.getEntry(2), (float) position.getEntry(3)}, 0);
			gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] {specular.getRed() / 255.f, specular.getGreen() / 255.f, specular.getBlue() / 255.f, 1f}, 0);
			gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {diffuse.getRed() / 255.f, diffuse.getGreen() / 255.f, diffuse.getBlue() / 255.f, 1f}, 0);
		}
		gl2.glPopMatrix();
	}
	
	public void handle(DirectionalLightComponent light)
	{
		throw new IllegalStateException("not implemented yet!");
	}

}
