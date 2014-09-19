package org.xtream.core.model;

//import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.Preference;
import org.xtream.core.model.markers.objectives.MaxObjective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.core.model.markers.preferences.MaxPreference;
import org.xtream.core.model.markers.preferences.MinPreference;

public class State
{
	
	private Component root;
	
	private int timepoint;
	
	private State previous;
	
	private Object[] values;
	
	public State(Component root)
	{
		this(root, -1, null);
	}
	
	public State(Component root, int timepoint, State previous)
	{
		this.root = root;
		this.timepoint = timepoint;
		this.previous = previous;
		
		if (root.getCachingExpressions().size() > 0)
		{
			values = new Object[root.getCachingExpressions().size()];
		}
	}
	
	public int getTimepoint()
	{
		return timepoint;
	}
	
	@Reference
	public State getPrevious()
	{
		return previous;
	}
	
	@SuppressWarnings("unchecked")
	public <S> S getValue(Expression<S> port, int timepoint)
	{
		if (timepoint >= 0)
		{
			if (timepoint <= this.timepoint)
			{
				if (this.timepoint == timepoint)
				{
					return (S) values[port.getNumber()];
				}
				else
				{
					return previous.getValue(port, timepoint);
				}	
			}
			else
			{
				throw new IllegalStateException(port.getQualifiedName() + ": Timepoint must not be in the future (" + timepoint + " vs " + this.timepoint + ")!");
			}
		}
		else
		{
			throw new IllegalStateException(port.getQualifiedName() + ": Timepoint must not be negative (" + timepoint + ")!");
		}
	}
	
	public <S> void setValue(Expression<S> port, int timepoint, S value)
	{
		if (timepoint >= 0)
		{
			if (timepoint <= this.timepoint)
			{
				if (this.timepoint == timepoint)
				{
					values[port.getNumber()] = value;
				}
				else
				{
					previous.setValue(port, timepoint, value);
				}
			}
			else
			{
				throw new IllegalStateException(port.getQualifiedName() + ": Timepoint must not be in the future (" + timepoint + ")!");
			}
		}
		else
		{
			throw new IllegalStateException(port.getQualifiedName() + ": Timepoint must not be negative (" + timepoint + ")!");
		}
	}
	
	public Integer comparePreferencesTo(State other)
	{
		if (root.getDescendantsByClass(MinPreference.class).size() > 0 || root.getDescendantsByClass(MaxPreference.class).size() > 0)
		{
			// Check equivalence
			
			// TODO Use clusters for comparing equivalence
			/*
			for (Equivalence equivalence : root.equivalencesRecursive)
			{
				if (!get(equivalence.port, timepoint).equals(other.get(equivalence.port, timepoint)))
				{
					return null; // Not comparable
				}
			}
			*/
		
			// Check min dominance
			
			double difference = 0;
			
			for (Preference preference : root.getDescendantsByClass(MinPreference.class))
			{
				double temp = preference.getPort().get(this, timepoint) - preference.getPort().get(other, timepoint);
				
				if (difference != 0 && Math.signum(difference) != Math.signum(temp))
				{
					return null; // Not comparable
				}
				else
				{
					difference = temp;
				}
			}
			
			// Check max dominance
			
			difference *= -1;
			
			for (Preference preference : root.getDescendantsByClass(MaxPreference.class))
			{
				double temp = preference.getPort().get(this, timepoint) - preference.getPort().get(other, timepoint);
				
				if (difference != 0 && Math.signum(difference) != Math.signum(temp))
				{
					return null; // Not comparable
				}
				else
				{
					difference = temp;
				}
			}
			
			// Return result
			
			return (int) Math.signum(difference);
		}
		else
		{
			return null; // Not comparable
		}
	}
	
	public int compareObjectiveTo(State other)
	{
		if (root.getDescendantsByClass(MinObjective.class).size() == 1 || root.getDescendantsByClass(MaxObjective.class).size() == 1)
		{
			for (Objective objective : root.getDescendantsByClass(MinObjective.class))
			{
				return (int) Math.signum(objective.getPort().get(this, timepoint) - objective.getPort().get(other,timepoint));
			}
			for (Objective objective : root.getDescendantsByClass(MaxObjective.class))
			{
				return (int) Math.signum(objective.getPort().get(this, timepoint) - objective.getPort().get(other, timepoint)) * -1;
			}
			
			throw new IllegalStateException();
		}
		else
		{
			return 0;
		}
	}

}
