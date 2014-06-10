package org.xtream.core.workbench.printers;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.nio.FloatBuffer;

import javax.swing.JPanel;
import javax.swing.JSlider;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;

public class LwjglAnimationPrinter<T extends Component> extends Part implements Printer<T>
{
	
	private Canvas canvas;
	private JSlider slider;

	public LwjglAnimationPrinter()
	{
		this(0, 0);
	}
	public LwjglAnimationPrinter(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public LwjglAnimationPrinter(int x, int y, int width, int height)
	{
		super("LWJGL animation printer", x, y, width, height);
		
		canvas = new Canvas();
		slider = new JSlider(0, 95, 0);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canvas, BorderLayout.CENTER);
		panel.add(slider, BorderLayout.PAGE_END);
		
		getPanel().add(panel);
	}

	@Override
	public void print(T component, int timepoint)
	{
		try
		{
			Display.setParent(canvas);
			Display.setResizable(true);
			Display.create();
			
			// Projection matrix
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluPerspective(45f, (float)Display.getWidth()/Display.getHeight(), 1f, 1000f);
			
			// Clear framebuffer
			
			GL11.glClearColor(1f, 1f, 1f, 0f);
			GL11.glClearDepth(1f);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// Define light

			GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
			
			GL11.glShadeModel(GL11.GL_SMOOTH);
			
			FloatBuffer materialSpecular = BufferUtils.createFloatBuffer(4);
			materialSpecular.put(1f).put(1f).put(1f).put(1f).flip();
			
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, materialSpecular);
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);
			
			FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
			lightPosition.put(1f).put(1f).put(1f).put(0f).flip();
			FloatBuffer lightSpecular = BufferUtils.createFloatBuffer(4);
			lightSpecular.put(1f).put(1f).put(1f).put(1f).flip();
			FloatBuffer lightDiffuse = BufferUtils.createFloatBuffer(4);
			lightDiffuse.put(1f).put(1f).put(1f).put(1f).flip();
			
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, lightSpecular);
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightDiffuse);
			
			FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
			ambient.put(0.5f).put(0.5f).put(0.5f).put(1f).flip();
			
			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambient);
			
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_LIGHT0);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
			
			// Modelview matrix
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GLU.gluLookAt(10f, 10f, 10f, 0f, 0f, 0f, 0f, 1f, 0f);
			
			// Draw sphere
			
			GL11.glColor3f(0.5f,0.5f,1.0f);
			new Sphere().draw(1f, 20, 20);
			
			// Update display
			
			Display.update(false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
