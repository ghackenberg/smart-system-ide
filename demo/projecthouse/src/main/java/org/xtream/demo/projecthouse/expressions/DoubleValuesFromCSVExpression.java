package org.xtream.demo.projecthouse.expressions;

import java.io.File;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class DoubleValuesFromCSVExpression extends Expression<Double> {
	
	private File file;

	public DoubleValuesFromCSVExpression(Port<Double> port, File file) {
		super(port);
		this.file = file;
	}

	@Override
	protected Double evaluate(State state, int timepoint) {
		// TODO [Andreas] Get value for timepoint from file with opencsv
		return null;
	}

}
