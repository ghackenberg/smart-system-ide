package org.xtream.demo.projecthouse.model.simple;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.projecthouse.model.Consumer;
import org.xtream.demo.projecthouse.model.Producer;

public class BreakerBoxComponent extends Component {
	
	@SuppressWarnings("rawtypes")
	public Port[] consumptionInputs;
	@SuppressWarnings("rawtypes")
	public Port[] productionInputs;
	
	@SuppressWarnings("rawtypes")
	public ChannelExpression[] channels;
	
	public Port<Boolean> balancedOutput = new Port<Boolean>();
	
	public Constraint balancedConstraint = new Constraint(balancedOutput);
	
	public Expression<Boolean> balancedExpression = new Expression<Boolean>(balancedOutput) {
		
		
		
		@SuppressWarnings("unchecked")
		@Override
		protected Boolean evaluate(State state, int timepoint) {
			double balance = 0;
			for(Port<Double> producer : productionInputs) {
				balance += producer.get(state, timepoint);
			}
			for(Port<Double> consumer : consumptionInputs) {
				balance += consumer.get(state, timepoint);
			}
			return Math.round(balance) == 0;
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
