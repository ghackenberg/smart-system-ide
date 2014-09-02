package org.xtream.demo.projecthouse.model.room;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.expressions.RandomOnOffExpression;

public class RoomHeatingController extends Component {
	
	public Port<OnOffDecision> onOffOutput = new Port<>();
	
	public Expression<OnOffDecision> onOffExpression = new RandomOnOffExpression(onOffOutput);

}