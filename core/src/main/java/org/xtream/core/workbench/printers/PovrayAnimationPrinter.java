package org.xtream.core.workbench.printers;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.panels.ImagePanel;

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
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(image, BorderLayout.CENTER);
		panel.add(slider, BorderLayout.PAGE_END);
		
		getPanel().add(panel);

		try
		{
			FileWriter out = new FileWriter("Frame.pov");
			
			out.write("#include \"colors.inc\"\n");
			out.write("camera { location <10,10,10> look_at <0,0,0> }\n");
			out.write("sphere { <0,0,0> 1 texture { pigment { color Yellow } } }\n");
			out.write("light_source { <10,10,10> color White }\n");
			
			out.close();
		
			Runtime.getRuntime().exec("pvengine /EXIT /RENDER Frame.pov").waitFor();
			
			image.setImage(ImageIO.read(new File("Frame.png")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void print(T component, int timepoint)
	{
		// TODO react to optimization finish
	}

}
