package org.xtream.demo.projecthouse.model.simple;

import java.io.File;
import java.net.URISyntaxException;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.model.CSVFileWithOneKey;

public class NetComponent extends Component {
	
	CSVFileWithOneKey csvData;
	
	public Port<Double> houseInput = new Port<>();
	
	public Port<Double> balanceOutput = new Port<>();
	public Port<Double> constancyOutput = new Port<>();
	
	public Chart constancy = new Timeline(constancyOutput);
	public Chart balance = new Timeline(balanceOutput);
	
	public NetComponent(String filename) {
		super();
		try {
			File file = new File(getClass().getResource(filename).toURI());
			csvData = new CSVFileWithOneKey(file, 1);
		}
		catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + filename);
		}		
	}
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput, true) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return csvData.get(timepoint, 1) + houseInput.get(state, timepoint);
		}
	};
	
	public Expression<Double> constancyExpression = new Expression<Double>(constancyOutput) {

		@Override
		protected Double evaluate(State state, int timepoint) {
			if(timepoint == 0) {
				return 0.;
			}
			
			double lastBalance = balanceOutput.get(state, timepoint - 1) ;
			double balance = balanceOutput.get(state, timepoint);
			return Math.pow(lastBalance - balance, 2);
		}
	};

}
