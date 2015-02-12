package org.xtream.demo.ppu.model;

import java.awt.Color;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.nodes.shapes.CylinderComponent;
import org.xtream.core.model.components.transforms.chains.RotationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class SuckerComponent extends Component
{
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	
	// Components
	
	public CylinderComponent cylinder = new CylinderComponent()
	{
		public Expression<Double> baseExpression = new ConstantExpression<>(baseOutput, 0.5);
		public Expression<Double> heightExpression = new ConstantExpression<>(heightOutput, 1.0);
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(0,255,0));
	};
	
	// Channels
	
	public Expression<RealMatrix> transformToCylinder = new ChannelExpression<>(cylinder.transformInput, transformInput);

}
