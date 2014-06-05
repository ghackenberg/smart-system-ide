package org.xtream.core.workbench.printers;

import java.awt.Dimension;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
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
			{
				Box box = new Box(1,1,1);
				
				Geometry geometry = new Geometry("Box", box);
				geometry.setMaterial(blue);
				geometry.setLocalTranslation(-2f, 1f, 2f);
				geometry.setShadowMode(ShadowMode.Cast);
				
				rootNode.attachChild(geometry);
			}
			
			// Sphere
			{
				Sphere sphere = new Sphere(20, 20, 1);
				
				Geometry geometry = new Geometry("Sphere", sphere);
				geometry.setMaterial(red);
				geometry.setLocalTranslation(2f, 1f, -2f);
				geometry.setShadowMode(ShadowMode.Cast);
				
				rootNode.attachChild(geometry);
			}
			
			// Plane
			{
				Box box = new Box(10f, 0.5f, 10f);
				
				Geometry geometry = new Geometry("Box", box);
				geometry.setMaterial(white);
				geometry.setLocalTranslation(0f, -0.5f, 0f);
				geometry.setShadowMode(ShadowMode.Receive);
				
				rootNode.attachChild(geometry);
			}
		}
	}

	public AnimationPrinter()
	{
		super("Animation printer");
	}

	@Override
	public void print(T component, int timepoint)
	{
		AppSettings settings = new AppSettings(true);
		settings.setWidth(640);
		settings.setHeight(480);
		settings.setUseInput(false);
		
		Application app = new TestApplication();
		app.setSettings(settings);
		app.createCanvas();
		
		JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
		ctx.setSystemListener(app);
		ctx.getCanvas().setPreferredSize(new Dimension(640, 480));
		
		getPanel().add(ctx.getCanvas());
	}

}
