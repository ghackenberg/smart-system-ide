package org.xtream.core.workbench.printers;

import java.awt.BorderLayout;
import java.util.concurrent.Callable;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class AnimationPrinter<T extends Component> extends Part implements Printer<T>
{
	
	private class TestApplication extends SimpleApplication
	{
		private static final int SHADOWMAP_SIZE = 2048;
		
		private Geometry boxGeometry;
		private Geometry sphereGeometry;
		private Geometry planeGeometry;
		
		public TestApplication()
		{
			setDisplayFps(false);
			setDisplayStatView(false);
		}
		
		@Override
		public void simpleInitApp()
		{
			// Background
			
			viewPort.setBackgroundColor(ColorRGBA.White);
			
			// Camera
			
			cam.setLocation(new Vector3f(15f, 15f, 15f));
			cam.lookAt(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f));
			
			// Light
			
			DirectionalLight directional = new DirectionalLight();
			directional.setColor(ColorRGBA.White);
			directional.setDirection(new Vector3f(-10f, -20f, -2.5f));
			
			rootNode.addLight(directional);
			
			// Shadow
			
			DirectionalLightShadowRenderer renderer = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
			renderer.setLight(directional);
			
			viewPort.addProcessor(renderer);
			
			DirectionalLightShadowFilter filter = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
			filter.setLight(directional);
			filter.setEnabled(true);
			
			FilterPostProcessor processor = new FilterPostProcessor(assetManager);
			processor.addFilter(filter);
			
			viewPort.addProcessor(processor);
			
			// Material
			
			Material blue = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			blue.setBoolean("UseMaterialColors",true);
			blue.setColor("Diffuse",ColorRGBA.Blue);
			blue.setColor("Specular",ColorRGBA.Blue);
			
			Material red = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			red.setBoolean("UseMaterialColors",true);
			red.setColor("Diffuse",ColorRGBA.Red);
			red.setColor("Specular",ColorRGBA.Red);
			
			Material green = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			green.setBoolean("UseMaterialColors",true);
			green.setColor("Diffuse",ColorRGBA.Green);
			green.setColor("Specular",ColorRGBA.Green);
			
			Material white = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			white.setBoolean("UseMaterialColors",true);
			white.setColor("Diffuse",ColorRGBA.White);
			white.setColor("Specular",ColorRGBA.White);
			
			Material black = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			black.setBoolean("UseMaterialColors",true);
			black.setColor("Diffuse",ColorRGBA.Black);
			black.setColor("Specular",ColorRGBA.Black);
			
			// Box
			Box box = new Box(1,1,1);
			
			boxGeometry = new Geometry("Box", box);
			boxGeometry.setMaterial(blue);
			boxGeometry.setLocalTranslation(-2f, 1f, 2f);
			boxGeometry.setShadowMode(ShadowMode.Cast);
			
			rootNode.attachChild(boxGeometry);
			
			// Sphere
			Sphere sphere = new Sphere(20, 20, 1);
			
			sphereGeometry = new Geometry("Sphere", sphere);
			sphereGeometry.setMaterial(red);
			sphereGeometry.setLocalTranslation(2f, 1f, -2f);
			sphereGeometry.setShadowMode(ShadowMode.Cast);
			
			rootNode.attachChild(sphereGeometry);
			
			// Plane
			Box plane = new Box(10f, 0.5f, 10f);
			
			planeGeometry = new Geometry("Box", plane);
			planeGeometry.setMaterial(white);
			planeGeometry.setLocalTranslation(0f, -0.5f, 0f);
			planeGeometry.setShadowMode(ShadowMode.Receive);
			
			rootNode.attachChild(planeGeometry);
		}
		
		public void shuffle()
		{
			planeGeometry.setLocalTranslation((float) Math.random(), (float) Math.random(), (float) Math.random());
		}
	}

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
		
		AppSettings settings = new AppSettings(true);
		settings.setUseInput(false);
		
		final TestApplication app = new TestApplication();
		app.setPauseOnLostFocus(false);
		app.setSettings(settings);
		app.createCanvas();
		app.startCanvas();
		
		JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
		ctx.setSystemListener(app);
		
		JSlider slider = new JSlider(0, 95, 0);
		slider.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					app.enqueue(
						new Callable<Spatial>()
						{
							@Override
							public Spatial call() throws Exception
							{
								app.shuffle();
								return null;
							}
						}
					);
				}
			}
		);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(ctx.getCanvas(), BorderLayout.CENTER);
		panel.add(slider, BorderLayout.PAGE_END);
		
		getPanel().add(panel);
	}

	@Override
	public void print(T component, int timepoint)
	{
		
	}

}
