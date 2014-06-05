package org.xtream.core.workbench.printers;

import java.awt.Dimension;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class AnimationPrinter<T extends Component> extends Part implements Printer<T>
{
	
	private class TestApplication extends SimpleApplication
	{
		@Override
		public void simpleInitApp()
		{
			
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
		
		Application app = new TestApplication();
		app.setSettings(settings);
		app.createCanvas();
		
		JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
		ctx.setSystemListener(app);
		ctx.getCanvas().setPreferredSize(new Dimension(640, 480));
		
		getPanel().add(ctx.getCanvas());
	}

}
