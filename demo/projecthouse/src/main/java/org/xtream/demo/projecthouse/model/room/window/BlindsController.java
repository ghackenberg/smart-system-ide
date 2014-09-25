package org.xtream.demo.projecthouse.model.room.window;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;

public class BlindsController extends Component {
	
	private boolean changedInPreviousTimestep = false;
	
	public Port<Double> blindsOutput = new Port<Double>();
	
	public Expression<Double> blindsExpression = new Expression<Double>(blindsOutput, true) {
		@Override
		protected Double evaluate(State state, int timepoint) {
			if(changedInPreviousTimestep && timepoint > 0) {
				changedInPreviousTimestep = false;
				return blindsExpression.get(state, timepoint - 1);
			}
			else {
				changedInPreviousTimestep = true;
				return Math.random();
			}
		}
	}; //TODO [Andreas] Maybe also use sun?
	
	public Chart blindsChart = new Timeline(blindsOutput);

}
