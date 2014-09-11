package org.xtream.demo.projecthouse.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.demo.projecthouse.model.net.NetModule;
import org.xtream.demo.projecthouse.model.room.RoomModule;
import org.xtream.demo.projecthouse.model.thermalstorage.pelletheater.PelletHeaterModule;

public class ObjectiveComponent extends Component {
	
	@SuppressWarnings("rawtypes")
	public Port[] roomInputs;	
	public Port<Double> netCostInput = new Port<>();
	public Port<Double> netAutonomyInput = new Port<>();
	public Port<Double> netBalanceInput = new Port<>();
	public Port<Double> pelletHeaterInput = new Port<>();
	public Port<Double> constancyInput = new Port<>();
	
	public Port<Double> objectiveOutput = new Port<>();
	
	public MinObjective objective = new MinObjective(objectiveOutput);
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] channels;
	public ChannelExpression<Double> netCostChannel;
	public ChannelExpression<Double> netAutonomyChannel;
	public ChannelExpression<Double> netBalanceChannel;
	public ChannelExpression<Double> pelletHeaterChannel;
	public ChannelExpression<Double> constancyChannel;
	
	public Chart objectiveChart = new Timeline(objectiveOutput, netCostInput, netAutonomyInput, pelletHeaterInput);
	
	@SuppressWarnings("unchecked")
	public ObjectiveComponent(PelletHeaterModule pelletHeater, NetModule net, RoomModule...rooms) {
		roomInputs = new Port[rooms.length];
		channels = new ChannelExpression[rooms.length];
		for(int i = 0; i < rooms.length; i++) {
			roomInputs[i] = new Port<Double>();
			channels[i] = new ChannelExpression<>(roomInputs[i], rooms[i].roomContext.comfortOutput);
		}
		netCostChannel = new ChannelExpression<>(netCostInput, net.context.costOutput);
		netAutonomyChannel = new ChannelExpression<>(netAutonomyInput, net.controller.powerOutput);
		netBalanceChannel = new ChannelExpression<>(netBalanceInput, net.context.balanceOutput);
		pelletHeaterChannel = new ChannelExpression<>(pelletHeaterInput, pelletHeater.context.costOutput);
		constancyChannel = new ChannelExpression<>(constancyInput, net.context.constancyOutput);
	}
	
	public Expression<Double> objectiveExpression = new Expression<Double>(objectiveOutput) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			// TODO [Andreas] insert objective function
			return netCostChannel.get(state, timepoint);
		}
	};

}
