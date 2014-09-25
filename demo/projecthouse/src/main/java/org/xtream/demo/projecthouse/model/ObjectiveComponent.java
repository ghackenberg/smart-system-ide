package org.xtream.demo.projecthouse.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.demo.projecthouse.model.room.RoomModule;
import org.xtream.demo.projecthouse.model.simple.BreakerBoxComponent;
import org.xtream.demo.projecthouse.model.simple.NetComponent;
import org.xtream.demo.projecthouse.model.thermalstorage.pelletheater.PelletHeaterModule;

public class ObjectiveComponent extends Component {
	
	@SuppressWarnings("rawtypes")
	public Port[] roomInputs;	
	public Port<Double> netCostInput = new Port<>();
	public Port<Double> autonomyInput = new Port<>();
	public Port<Double> netBalanceInput = new Port<>();
	public Port<Double> pelletHeaterInput = new Port<>();
	public Port<Double> constancyInput = new Port<>();
	
	public Port<Double> aggregatedCostPort = new Port<>();
	public Port<Double> netBalancePort = new Port<>();
	public Port<Double> comfortPort = new Port<>();
	
	public Port<Double> objectiveOutput = new Port<>();
	
	public MinObjective objective = new MinObjective(objectiveOutput);
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] channels;
	public ChannelExpression<Double> netCostChannel;
	public ChannelExpression<Double> autonomyChannel;
	public ChannelExpression<Double> netBalanceChannel;
	public ChannelExpression<Double> pelletHeaterChannel;
	public ChannelExpression<Double> constancyChannel;
	
	public Chart objectiveChart = new Timeline(objectiveOutput, aggregatedCostPort, netBalancePort);
	public Chart aggregatedCost = new Timeline(netCostInput, pelletHeaterInput, aggregatedCostPort);
	public Chart comfort = new Timeline(comfortPort);
	
	@SuppressWarnings("unchecked")
	public ObjectiveComponent(PelletHeaterModule pelletHeater, NetComponent net, BreakerBoxComponent breakerBox, RoomModule...rooms) {
		roomInputs = new Port[rooms.length];
		channels = new ChannelExpression[rooms.length];
		for(int i = 0; i < rooms.length; i++) {
			roomInputs[i] = new Port<Double>();
			channels[i] = new ChannelExpression<>(roomInputs[i], rooms[i].context.comfortOutput);
		}
		netCostChannel = new ChannelExpression<>(netCostInput, breakerBox.costOutput);
		autonomyChannel = new ChannelExpression<>(autonomyInput, breakerBox.balanceOutput);
		netBalanceChannel = new ChannelExpression<>(netBalanceInput, net.balanceOutput);
		pelletHeaterChannel = new ChannelExpression<>(pelletHeaterInput, pelletHeater.context.costOutput);
		constancyChannel = new ChannelExpression<>(constancyInput, net.constancyOutput);
	}
	
	public Expression<Double> aggregatedCostExpression = new Expression<Double>(aggregatedCostPort) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			double cost = netCostInput.get(state, timepoint) + pelletHeaterInput.get(state, timepoint);
			if(timepoint == 0) {
				return cost;
			}
			return aggregatedCostPort.get(state, timepoint - 1) + cost;
		}
	};
	
	public Expression<Double> netBalanceExpression = new Expression<Double>(netBalancePort) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return Math.sqrt(Math.pow(netBalanceInput.get(state, timepoint), 2))/1000;
		}
	};
	
	public Expression<Double> comfortExpression = new Expression<Double>(comfortPort) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			double comfort = 0;
			for(Port<Double> roomInput : roomInputs) {
				comfort += roomInput.get(state, timepoint);
			}
			return comfort/3;
		}
	};
	
	public Expression<Double> objectiveExpression = new Expression<Double>(objectiveOutput) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			// TODO [Andreas] insert objective function
			return aggregatedCostPort.get(state, timepoint) + netBalancePort.get(state, timepoint) + comfortPort.get(state, timepoint);
		}
	};

}
