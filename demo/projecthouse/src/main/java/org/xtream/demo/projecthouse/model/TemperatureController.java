package org.xtream.demo.projecthouse.model;

import java.io.File;
import java.net.URISyntaxException;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;

public class TemperatureController extends Component {
	
	private CSVFileWithOneKey csvData;
	
	public Port<Double> temperatureOutput = new Port<>();
	
	public Chart temperature = new Timeline(temperatureOutput);
	
	public TemperatureController(String outerTemperatureFileName) {
		super();
		File file = null;
		try {
			file = new File(getClass().getResource(outerTemperatureFileName).toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + outerTemperatureFileName);
		}
		csvData = new CSVFileWithOneKey(file, 1);
	}
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return csvData.get(timepoint, 1);
		}
	};

}
