package org.xtream.demo.street.model;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;

public class FridgeComponent extends Component
{
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public FridgeComponent()
	{
		temperatures = new Expression[4];
		
		temperatures[0] = new ChannelExpression<>(coldPipeInnerAir.temperatureOneInput, coldPipe.temperatureOutput);
		temperatures[1] = new ChannelExpression<>(coldPipeInnerAir.temperatureTwoInput, innerAir.temperatureOutput);

		temperatures[2] = new ChannelExpression<>(hotPipeInnerAir.temperatureOneInput, hotPipe.temperatureOutput);
		temperatures[3] = new ChannelExpression<>(hotPipeInnerAir.temperatureTwoInput, innerAir.temperatureOutput);
		
		heats = new Expression[5];

		heats[0] = new ChannelExpression<>(coldPipe.heatInputs[0], coldPipeInnerAir.heatOneOutput);
		heats[1] = new ChannelExpression<>(innerAir.heatInputs[0], coldPipeInnerAir.heatTwoOutput);

		heats[2] = new ChannelExpression<>(hotPipe.heatInputs[0], hotPipeInnerAir.heatOneOutput);
		heats[3] = new ChannelExpression<>(innerAir.heatInputs[1], hotPipeInnerAir.heatTwoOutput);
		
		heats[4] = new ChannelExpression<>(hotPipe.heatInputs[1], compressor.heatOutput);
	}
	
	// Ports
	
	public Port<Double> loadOutput = new Port<>();
	
	// Components

	public VolumeComponent innerAir = new VolumeComponent(2, 5);
	public VolumeComponent hotPipe = new VolumeComponent(2, 120);
	public VolumeComponent coldPipe = new VolumeComponent(3, -4);
	
	public CompressorComponent compressor = new CompressorComponent();
	
	public InterfaceComponent coldPipeInnerAir = new InterfaceComponent(0, 0, 0);
	public InterfaceComponent hotPipeInnerAir = new InterfaceComponent(0, 0, 0);
	
	// Expressions
	
	public Expression<Double> load = new ChannelExpression<>(loadOutput, compressor.loadOutput);
	public Expression<Double> temperatures[];
	public Expression<Double> heats[];

}
