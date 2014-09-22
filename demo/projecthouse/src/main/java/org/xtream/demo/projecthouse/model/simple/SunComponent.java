package org.xtream.demo.projecthouse.model.simple;

import java.io.File;
import java.net.URISyntaxException;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.demo.projecthouse.model.CSVFileWithOneKey;
import org.xtream.demo.projecthouse.model.Irradiation;

public class SunComponent extends Component {
	
	private CSVFileWithOneKey csvData;
	
	public Port<Double> irradiancePort = new Port<>();
	public Port<Double> anglePort = new Port<>();
	
	public Port<Irradiation> irradiationOutput = new Port<>();
	
	public Chart irradianceChart = new Timeline(irradiancePort);
	public Chart angleChart = new Timeline(anglePort);
	
	public SunComponent(String filename) {
		super();
		try {
			File file = new File(getClass().getResource(filename).toURI());
			csvData = new CSVFileWithOneKey(file, 2);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + filename);
		}		
	}
	
	public Expression<Irradiation> irradiationExpression = new Expression<Irradiation>(irradiationOutput, true) {

		@Override
		protected Irradiation evaluate(State state, int timepoint) {
			return new Irradiation(irradiancePort.get(state, timepoint), anglePort.get(state, timepoint));
		}
	};
	
	public Expression<Double> irradianceExpression = new Expression<Double>(irradiancePort) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return csvData.get(timepoint, 1);
		}
	};
	
	public Expression<Double> angleExpression = new Expression<Double>(anglePort) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return csvData.get(timepoint, 2);
		}
	};
}
