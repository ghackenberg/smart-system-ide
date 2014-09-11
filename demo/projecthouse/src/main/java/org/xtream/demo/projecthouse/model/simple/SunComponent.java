package org.xtream.demo.projecthouse.model.simple;

import java.io.File;
import java.net.URISyntaxException;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.model.CSVFileWithOneKey;
import org.xtream.demo.projecthouse.model.Irradiance;

public class SunComponent extends Component {
	
	private CSVFileWithOneKey csvData;
	
	public Port<Irradiance> irradianceOutput = new Port<>();
	
	public SunComponent(String filename) {
		super();
		try {
			File file = new File(getClass().getResource(filename).toURI());
			csvData = new CSVFileWithOneKey(file, 2);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + filename);
		}		
	}

	
	public Expression<Irradiance> irradianceExpression = new Expression<Irradiance>(irradianceOutput, true) {

		@Override
		protected Irradiance evaluate(State state, int timepoint) {
			return new Irradiance(csvData.get(timepoint, 1), csvData.get(timepoint, 2));
		}
	};
}
