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
import org.xtream.demo.projecthouse.model.Consumer;

public class LoadsComponent extends Component implements Consumer {
	
	private CSVFileWithOneKey csvData;
	
	public Port<Double> consumptionOutput = new Port<>();
	
	public Chart consumption = new Timeline(consumptionOutput);
	
	public LoadsComponent(String filename) {
		super();
		try {
			File file = new File(getClass().getResource(filename).toURI());
			csvData = new CSVFileWithOneKey(file, 1);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + filename);
		}		
	}
	
	public Expression<Double> powerExpression = new Expression<Double>(consumptionOutput, true) {

		@Override
		protected Double evaluate(State state, int timepoint) {			
			return csvData.get(timepoint, 1);
		}
	};

	@Override
	public Port<Double> consumption() {
		return consumptionOutput;
	}

}
