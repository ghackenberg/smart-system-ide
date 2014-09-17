package org.xtream.demo.projecthouse.model.simple;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.projecthouse.model.Consumer;
import org.xtream.demo.projecthouse.model.Producer;
import org.xtream.demo.projecthouse.model.RootComponent;

public class BreakerBoxComponent extends Component {
	
	private static final int MAXIMUM_POWER_HOUSE = 30000;
	@SuppressWarnings("rawtypes")
	public Port[] consumptionInputs;
	@SuppressWarnings("rawtypes")
	public Port[] productionInputs;
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] channels;
	
	public Port<Double> balanceOutput = new Port<>();
	public Port<Double> costOutput = new Port<>();
	
	public Port<Boolean> balanceValidPort = new Port<>();
	public Constraint balanceConstraint = new Constraint(balanceValidPort);
	
	public Chart cost = new Timeline(costOutput);
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput) {		
		
		@SuppressWarnings("unchecked")
		@Override
		protected Double evaluate(State state, int timepoint) {
			double balance = 0;
			for(Port<Double> producer : productionInputs) {
				balance += producer.get(state, timepoint);
			}
			for(Port<Double> consumer : consumptionInputs) {
				balance -= consumer.get(state, timepoint);
			}
			return balance;
		}
	};
	
	public Expression<Boolean> balanceValidExpression = new Expression<Boolean>(balanceValidPort) {
		
		@Override
		protected Boolean evaluate(State state, int timepoint) {
			if(balanceOutput.get(state, timepoint) < MAXIMUM_POWER_HOUSE) {
				return true;
			}
			return false;
		}
	};
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return -balanceOutput.get(state, timepoint)*RootComponent.ELECTRICITY_RATE;
		}
	};

	@SuppressWarnings("unchecked")
	public BreakerBoxComponent(Producer[] producers,
			Consumer[] consumers) {
		super();
		int nrOfConsumers = consumers.length;
		int nrOfProducers = producers.length;
		consumptionInputs = new Port[nrOfConsumers];
		productionInputs = new Port[nrOfProducers];
		channels = new ChannelExpression[nrOfConsumers + nrOfProducers];
		for(int i = 0; i < nrOfConsumers; i++) {
			consumptionInputs[i] = new Port<Double>();
			channels[i] = new ChannelExpression<>(consumptionInputs[i], consumers[i].consumption());
		}
		for(int i = 0; i < nrOfProducers; i++) {
			productionInputs[i] = new Port<Double>();
			channels[nrOfConsumers + i] = new ChannelExpression<>(productionInputs[i], producers[i].production());
		}
	}

}
