package org.xtream.demo.projecthouse.model.net;

import java.io.File;
import java.net.URISyntaxException;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.model.CSVFileWithOneKey;
import org.xtream.demo.projecthouse.model.RootComponent;

public class NetContext extends Component {
	
	CSVFileWithOneKey csvData;
	
	public Port<Double> powerInput = new Port<>();
	
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> productionOutput = new Port<>();
	public Port<Double> costOutput = new Port<>();
	public Port<Double> balanceOutput = new Port<>();
	public Port<Double> constancyOutput = new Port<>();
	
	public NetContext(String filename) {
		super();
		try {
			File file = new File(getClass().getResource(filename).toURI());
			csvData = new CSVFileWithOneKey(file, 1);
		}
		catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + filename);
		}		
	}
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			double production = powerInput.get(state, timepoint);
			return production > 0 ? production : 0;
		}
	};
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			double consumption = -powerInput.get(state, timepoint);
			return consumption > 0 ? consumption : 0;
		}
	};
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			return powerInput.get(state, timepoint)*RootComponent.ELECTRICITY_RATE;
		}
	};
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput, true) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return csvData.get(timepoint, 1);
		}
	};
	
	public Expression<Double> constancyExpression = new Expression<Double>(constancyOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if(timepoint == 0) {
				return 0.;
			}
			
			double lastBalance = balanceOutput.get(state, timepoint - 1) - powerInput.get(state, timepoint - 1);
			double balance = balanceOutput.get(state, timepoint) - powerInput.get(state, timepoint);
			return Math.pow(lastBalance - balance, 2);
		}
	};

}
