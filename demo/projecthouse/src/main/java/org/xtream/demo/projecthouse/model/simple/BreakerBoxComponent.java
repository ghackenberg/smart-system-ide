package org.xtream.demo.projecthouse.model.simple;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.model.Consumer;
import org.xtream.demo.projecthouse.model.Producer;
import org.xtream.demo.projecthouse.model.RootComponent;

public class BreakerBoxComponent extends Component {
	
	@SuppressWarnings("rawtypes")
	public Port[] consumptionInputs;
	@SuppressWarnings("rawtypes")
	public Port[] productionInputs;
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] channels;
	
	public Port<Double> balanceOutput = new Port<>();
	public Port<Double> accCostOutput = new Port<>();
	
	public Chart cost = new Timeline(accCostOutput);
	
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
	
	public Expression<Double> accCostExpression = new Expression<Double>(accCostOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if(timepoint == 0) {
				return 0.;
			}
			return accCostOutput.get(state, timepoint - 1) - balanceOutput.get(state, timepoint)*RootComponent.ELECTRICITY_RATE;
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
