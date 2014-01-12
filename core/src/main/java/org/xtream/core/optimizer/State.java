package org.xtream.core.optimizer;

import java.lang.reflect.Field;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

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
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
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
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
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
			
			for (Port<?> port : root.equivalencesRecursive)
			{
				if (!get(port, timepoint).equals(other.get(port, timepoint)))
				{
					return null; // Not comparable
				}
			}
		
			// Check min dominance
			
			double difference = 0;
			
			for (Port<Double> port : root.minDominancesRecursive)
			{
				double temp = get(port, timepoint) - other.get(port, timepoint);
				
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
			
			for (Port<Double> port : root.maxDominancesRecursive)
			{
				double temp = get(port, timepoint) - other.get(port, timepoint);
				
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
			for (Port<Double> port : root.minObjectivesRecursive)
			{
				return (int) Math.signum(get(port, timepoint) - other.get(port, timepoint));
			}
			for (Port<Double> port : root.maxObjectivesRecursive)
			{
				return (int) Math.signum(get(port, timepoint) - other.get(port, timepoint)) * -1;
			}
			
			throw new IllegalStateException();
		}
		else
		{
			return 0;
		}
	}

}
