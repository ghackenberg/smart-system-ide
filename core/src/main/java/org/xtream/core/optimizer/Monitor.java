package org.xtream.core.optimizer;

import java.util.List;
import java.util.Map;

import org.xtream.core.model.Component;

public interface Monitor<T extends Component>
{
	
	public void start(T root);
	
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best);
	
	public void stop(T root, int timepoint);

}
