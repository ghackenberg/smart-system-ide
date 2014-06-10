package org.xtream.core.workbench.printers;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.controls.LwjglCanvas;

public class LwjglAnimationPrinter<T extends Component> extends Part implements Printer<T>
{
	
	private LwjglCanvas canvas;
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
		
		try
		{
			canvas = new LwjglCanvas();
			slider = new JSlider(0, 95, 0);
			slider.addChangeListener(new ChangeListener()
				{
					@Override
					public void stateChanged(ChangeEvent event)
					{
						canvas.repaint();
					}
				}
			);
			
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(canvas, BorderLayout.CENTER);
			panel.add(slider, BorderLayout.PAGE_END);
			
			getPanel().add(panel);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void print(T component, int timepoint)
	{
		// TODO handle optimization finish
	}

}
