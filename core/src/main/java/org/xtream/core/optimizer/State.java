package org.xtream.core.optimizer;

import java.lang.reflect.Field;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.annotations.Preference;

public class State implements Comparable<State>
{
	public Component root;
	
	public int timepoint;
	
	public State previous;
	
	public Object[] values;
	
	public Object[] fields;
	
	public State(int portCount, int fieldCount)
	{
		this(portCount, fieldCount, -1, null);
	}
	
	public State(int portCount, int fieldCount, int timepoint, State previous)
	{
		this.timepoint = timepoint;
		this.previous = previous;
		
		if (portCount > 0)
		{
			values = new Object[portCount];
		}
		if (fieldCount > 0)
		{
			fields = new Object[fieldCount];
		}
	}
	
	public void connect(Component root)
	{
		this.root = root;
		
		for (Port<?> port : root.portsRecursive)
		{
			port.state = this;
		}
	}
	
	public void restore(Component root)
	{
		connect(root);
		
		int index = 0;
		
		for (Expression<?> expression : root.expressionsRecursive)
		{
			for (Field field : expression.getClass().getFields())
			{
				try
				{
					field.setAccessible(true);
					
					if (field.getAnnotation(Constant.class) == null)
					{
						field.set(expression, fields[index++]);
					}
				}
				catch (IllegalArgumentException e)
				{
					throw new IllegalStateException(e);
				}
				catch (IllegalAccessException e)
				{
					throw new IllegalStateException(e);
				}
			}
		}
	}
	
	public void save()
	{
		int index = 0;
		
		for (Expression<?> expression : root.expressionsRecursive)
		{
			for (Field field : expression.getClass().getFields())
			{
				try
				{
					field.setAccessible(true);
					
					if (field.getAnnotation(Constant.class) == null)
					{
						fields[index++] = field.get(expression);
					}
				}
				catch (IllegalArgumentException e)
				{
					throw new IllegalStateException(e);
				}
				catch (IllegalAccessException e)
				{
					throw new IllegalStateException(e);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Port<T> port, int timepoint)
	{
		if (this.timepoint == timepoint)
		{
			return (T) values[port.number];
		}
		else
		{
			return previous.get(port, timepoint);
		}
	}
	
	public <T> void set(Port<T> port, int timepoint, T value)
	{
		if (this.timepoint == timepoint)
		{
			values[port.number] = value;
		}
		else
		{
			previous.set(port, timepoint, value);
		}
	}
	
	public void dump()
	{
		int index = 0;

		for (Expression<?> expression : root.expressionsRecursive)
		{
			for (Field field : expression.getClass().getFields())
			{
				System.out.println(expression.qualifiedName + "." + field.getName() + " = " + fields[index++]);
			}
		}
	}
	
	public Integer compareDominanceTo(State other)
	{
		if (root.minDominancesRecursive.size() > 0 || root.maxDominancesRecursive.size() > 0)
		{
			// Check equivalence
			
			for (Equivalence equivalence : root.equivalencesRecursive)
			{
				if (!get(equivalence.port, timepoint).equals(other.get(equivalence.port, timepoint)))
				{
					return null; // Not comparable
				}
			}
		
			// Check min dominance
			
			double difference = 0;
			
			for (Preference preference : root.minDominancesRecursive)
			{
				double temp = get(preference.port, timepoint) - other.get(preference.port, timepoint);
				
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
			
			for (Preference preference : root.maxDominancesRecursive)
			{
				double temp = get(preference.port, timepoint) - other.get(preference.port, timepoint);
				
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

	@Override
	public int compareTo(State other)
	{
		if (root.minObjectivesRecursive.size() == 1 || root.maxObjectivesRecursive.size() == 1)
		{
			for (Objective objective : root.minObjectivesRecursive)
			{
				return (int) Math.signum(get(objective.port, timepoint) - other.get(objective.port, timepoint));
			}
			for (Objective objective : root.maxObjectivesRecursive)
			{
				return (int) Math.signum(get(objective.port, timepoint) - other.get(objective.port, timepoint)) * -1;
			}
			
			throw new IllegalStateException();
		}
		else
		{
			return 0;
		}
	}

}
