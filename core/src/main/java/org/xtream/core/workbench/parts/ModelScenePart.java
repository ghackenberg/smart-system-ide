package org.xtream.core.workbench.parts;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

import org.xtream.core.model.Component;
import org.xtream.core.model.components.nodes.LightComponent;
import org.xtream.core.utilities.visitors.JoglCameraVisitor;
import org.xtream.core.utilities.visitors.JoglLightVisitor;
import org.xtream.core.utilities.visitors.JoglShapeVisitor;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;

import com.jogamp.opengl.util.gl2.GLUT;

public class ModelScenePart<T extends Component> extends Part<T>
{
	
	private GLJPanel canvas;
	private int timepoint = 0;

	public ModelScenePart()
	{
		this(0, 0);
	}
	public ModelScenePart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ModelScenePart(int x, int y, int width, int height)
	{
		super("Model scene", ModelScenePart.class.getClassLoader().getResource("parts/model_scene.png"), x, y, width, height);
		
		try
		{
			GLProfile profile = GLProfile.getDefault();
			GLCapabilities capabilities = new GLCapabilities(profile);
			
			canvas = new GLJPanel(capabilities);
			canvas.addGLEventListener(new GLEventListener()
				{
					@Override
					public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
					{
						GL2 gl2 = drawable.getGL().getGL2();
						GLU glu = new GLU();
						
						// Viewport
						gl2.glViewport(x, y, width, height);

						// Projection
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						{
							gl2.glLoadIdentity();
							glu.gluPerspective(60f, (float)width/height, 1f, 1000f);
						}
					}
					@Override
					public void init(GLAutoDrawable drawable)
					{
						GL2 gl2 = drawable.getGL().getGL2();
						
						// Features
						gl2.glEnable(GL2.GL_DEPTH_TEST);
						gl2.glEnable(GL2.GL_LIGHTING);
						
						System.out.println(getRoot().getDescendantsByClass(LightComponent.class).size() + " lichter gefunden");
						
						gl2.glEnable(GL2.GL_LIGHT0);
						gl2.glEnable(GL2.GL_LIGHT1);
						gl2.glEnable(GL2.GL_LIGHT2);
						gl2.glEnable(GL2.GL_LIGHT3);
						gl2.glEnable(GL2.GL_LIGHT4);
						gl2.glEnable(GL2.GL_LIGHT5);
						gl2.glEnable(GL2.GL_LIGHT6);
						gl2.glEnable(GL2.GL_LIGHT7);
						gl2.glEnable(GL2.GL_COLOR_MATERIAL);
						
						// Material
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] {1f, 1f, 1f, 1f}, 0);
						gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 50f);
						gl2.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
					}
					@Override
					public void dispose(GLAutoDrawable drawable)
					{
						
					}
					@Override
					public void display(GLAutoDrawable drawable)
					{
						GL2 gl2 = drawable.getGL().getGL2();
						GLU glu = new GLU();
						GLUT glut = new GLUT();
						
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						{
							new JoglCameraVisitor(gl2, glu, getState(), timepoint).handle(getRoot());
							new JoglLightVisitor(gl2, glu, getState(), timepoint).handle(getRoot());
							gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
							new JoglShapeVisitor(gl2, glut, getState(), timepoint).handle(getRoot());
						}
				    }
				}
			);
			
			getPanel().add(canvas);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			timepoint = jump.getTimepoint();
			
			canvas.repaint();
		}
	}

}
