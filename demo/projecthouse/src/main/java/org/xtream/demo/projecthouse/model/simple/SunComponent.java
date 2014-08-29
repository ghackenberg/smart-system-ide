package org.xtream.demo.projecthouse.model.simple;

import java.io.File;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.expressions.DoubleValuesFromCSVExpression;
import org.xtream.demo.projecthouse.model.Irradiance;

public class SunComponent extends Component {
	
	private File file;
	
	public Port<Irradiance> irradianceOutput = new Port<>();
	
	public Expression<Irradiance> irradianceExpression = new Expression<Irradiance>(irradianceOutput) {

		@Override
		protected Irradiance evaluate(State state, int timepoint) {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
