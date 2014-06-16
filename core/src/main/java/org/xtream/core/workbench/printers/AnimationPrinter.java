package org.xtream.core.workbench.printers;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

import org.xtream.core.model.Component;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;

import com.jogamp.opengl.util.gl2.GLUT;

public class AnimationPrinter<T extends Component> extends Part<T>
{
	
	private GLJPanel canvas;

	public AnimationPrinter()
	{
		this(0, 0);
	}
	public AnimationPrinter(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public AnimationPrinter(int x, int y, int width, int height)
	{
		super("Animation printer", x, y, width, height);
		
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
						gl2.glEnable(GL2.GL_LIGHT0);
						gl2.glEnable(GL2.GL_COLOR_MATERIAL);
						
						// Clear
						gl2.glClearColor(1f, 1f, 1f, 0f);
						gl2.glClearDepth(1f);

						// Material
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] {1f, 1f, 1f, 1f}, 0);
						gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 50f);
						gl2.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
						
						// Light
						gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0.5f, 0.5f, 0.5f, 1f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {0f, 20f, 5f, 0f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] {1f, 1f, 1f, 1f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {1f, 1f, 1f, 1f}, 0);
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
						
						gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
						
						// Modelview
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						{
							gl2.glLoadIdentity();
							glu.gluLookAt(10f, 10f, 10f, 0f, 0f, 0f, 0f, 1f, 0f);
						}
						
						// Cube
						gl2.glPushMatrix();
						{
							// Transform
							gl2.glTranslatef(0f, 0f, 0f);
							// Material
							gl2.glColor3f(0f, 0f, 1f);
							// Shape
							glut.glutSolidCube(2);
						}
						gl2.glPopMatrix();
						
						// Sphere
						gl2.glPushMatrix();
						{
							// Transform
							gl2.glTranslated(-5 * Math.random(), 0, 5 * Math.random());
							// Material
							gl2.glColor3f(0f, 1f, 0f);
							// Shape
							glut.glutSolidSphere(01f, 100, 100);
						}
						gl2.glPopMatrix();
						
						// Cylinder
						gl2.glPushMatrix();
						{
							// Transform
							gl2.glTranslated(5 * Math.random(), 0, -5 * Math.random());
							// Material
							gl2.glColor3f(1f, 0f, 0f);
							// Shape
							glut.glutSolidCylinder(1f, 2f, 100, 100);
						}
						gl2.glPopMatrix();
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
			canvas.repaint();
		}
	}

}
