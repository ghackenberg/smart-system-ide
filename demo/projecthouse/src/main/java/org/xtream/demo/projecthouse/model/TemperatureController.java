package org.xtream.demo.projecthouse.model;

import java.io.File;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public class TemperatureController extends Component {
	
	private CSVFileWithOneKey csvData;
	
	public Port<Double> temperatureOutput = new Port<>();
	
	public TemperatureController(File file) {
		super();
		csvData = new CSVFileWithOneKey(file, 1);
	}
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return csvData.get(timepoint, 1);
		}
	};

}
