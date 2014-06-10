package org.xtream.core.workbench.printers;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.controls.ImagePanel;

public class PovrayAnimationPrinter<T extends Component> extends Part implements Printer<T>
{
	
	private ImagePanel image;
	private JSlider slider;

	public PovrayAnimationPrinter()
	{
		this(0, 0);
	}
	public PovrayAnimationPrinter(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public PovrayAnimationPrinter(int x, int y, int width, int height)
	{
		super("Pov-RAY animation printer", x, y, width, height);
		
		image = new ImagePanel();
		
		slider = new JSlider(0, 95, 0);
		slider.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					shuffle();
				}
			}
		);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(image, BorderLayout.CENTER);
		panel.add(slider, BorderLayout.PAGE_END);
		
		getPanel().add(panel);
	}

	@Override
	public void print(T component, int timepoint)
	{
		// TODO react to optimization finish
	}

	public void shuffle()
	{
		try
		{
			FileWriter out = new FileWriter("Frame.pov");
			
			out.write("#include \"colors.inc\"\n");
			out.write("camera { location <10,10,10> look_at <0,0,0> }\n");
			out.write("plane { <0,1,0> 0 texture { pigment { color White } } }\n");
			out.write("sphere { <0,1,0> 2 texture { pigment { color Red } } }\n");
			out.write("box { <-5,0,5>, <-3,2,3> texture { pigment { color Blue } } }\n");
			out.write("box { <5,0,-5>, <3,2,-3> texture { pigment { color Green } } }\n");
			out.write("light_source { <0,20,10> color White }\n");
			
			out.close();
		
			Runtime.getRuntime().exec("pvengine /EXIT /RENDER Frame.pov").waitFor();
			
			image.setImage(ImageIO.read(new File("Frame.png")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
