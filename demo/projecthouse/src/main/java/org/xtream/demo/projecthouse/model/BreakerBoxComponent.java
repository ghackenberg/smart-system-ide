package org.xtream.demo.projecthouse.model;

import java.util.List;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;

public class BreakerBoxComponent extends Component {
	public Producer[] producers;
	public Consumer[] consumers;
	
	public Port<Boolean> balancedOutput = new Port<Boolean>();
	
	public Constraint balancedConstraint = new Constraint(balancedOutput);
	
	public Expression<Boolean> balancedExpression = new Expression<Boolean>(balancedOutput) {
		
		@Override
		protected Boolean evaluate(State state, int timepoint) {
			double balance = 0;
			for(Producer producer : producers) {
				balance += producer.production().get(state, timepoint);
			}
			for(Consumer consumer : consumers) {
				balance += consumer.consumption().get(state, timepoint);
			}
			return Math.round(balance) == 0;
		}
	};

	public BreakerBoxComponent(Producer[] producers,
			Consumer[] consumers) {
		super();
		this.producers = producers;
		this.consumers = consumers;
	}

}
