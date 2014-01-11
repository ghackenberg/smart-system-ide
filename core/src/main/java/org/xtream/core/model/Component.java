package org.xtream.core.model;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xtream.core.model.annotations.Show;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Dominance;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;

public abstract class Component
{

	public String name;
	public String qualifiedName;
	
	public List<Port<?>> ports = new ArrayList<>();
	public List<Field> fields = new ArrayList<>();
	public List<Component> components = new ArrayList<>();
	public List<Channel<?>> channels = new ArrayList<>();
	public List<Expression<?>> expressions = new ArrayList<>();
	public List<Port<Boolean>> constraints= new ArrayList<>();
	public List<Port<Double>> minDominances= new ArrayList<>();
	public List<Port<Double>> maxDominances= new ArrayList<>();
	public List<Port<?>> equivalences= new ArrayList<>();
	public List<Port<Double>> minObjectives= new ArrayList<>();
	public List<Port<Double>> maxObjectives= new ArrayList<>();
	
	public Map<String, List<Port<Double>>> charts = new HashMap<>();
	
	public List<Port<?>> portsRecursive = new ArrayList<>();
	public List<Field> fieldsRecursive = new ArrayList<>();
	public List<Component> componentsRecursive = new ArrayList<>();
	public List<Channel<?>> channelsRecursive = new ArrayList<>();
	public List<Expression<?>> expressionsRecursive = new ArrayList<>();
	public List<Port<Boolean>> constraintsRecursive= new ArrayList<>();
	public List<Port<Double>> minDominancesRecursive = new ArrayList<>();
	public List<Port<Double>> maxDominancesRecursive = new ArrayList<>();
	public List<Port<?>> equivalencesRecursive= new ArrayList<>();
	public List<Port<Double>> minObjectivesRecursive= new ArrayList<>();
	public List<Port<Double>> maxObjectivesRecursive= new ArrayList<>();
	
	public Map<Component, Map<String, List<Port<Double>>>> chartsRecursive = new HashMap<>();
	
	public void init()
	{
		init("root", "root", 0);
	}
	
	@SuppressWarnings("unchecked")
	public void init(String name2, String name, int number)
	{
		this.name = name2;
		this.qualifiedName = name;
		
		for (Field componentField : this.getClass().getFields())
		{
			try
			{
				if (Port.class.isAssignableFrom(componentField.getType()))
				{
					Port<?> port = (Port<?>) componentField.get(this);
					
					ports.add(port);
					
					port.name = componentField.getName();
					port.qualifiedName = name + "." + componentField.getName();
					port.number = number++;
					
					if (componentField.getAnnotation(Constraint.class) != null && (Port<Boolean>) port != null)
					{
						constraints.add((Port<Boolean>) port);
					}
					else if (componentField.getAnnotation(Dominance.class) != null && (Port<Double>) port != null)
					{
						if (componentField.getAnnotation(Dominance.class).value() ==Direction.MAX)
						{
							maxDominances.add((Port<Double>) port);
						}
						else
						{
							minDominances.add((Port<Double>) port);	
						}
					}
					else if (componentField.getAnnotation(Equivalence.class) != null)
					{
						equivalences.add((Port<?>) port);
					}
					else if (componentField.getAnnotation(Objective.class) != null && (Port<Double>) port != null)
					{
						if (componentField.getAnnotation(Objective.class).value() == Direction.MAX)
						{
							maxObjectives.add((Port<Double>) port);
						}
						else
						{
							minObjectives.add((Port<Double>) port);
						}
					}
					
					if (componentField.getAnnotation(Show.class) != null && (Port<Double>) port != null)
					{
						for (String chart : componentField.getAnnotation(Show.class).value())
						{
							List<Port<Double>> series = charts.get(chart);
							
							if (series == null)
							{
								series = new ArrayList<>();
								
								charts.put(chart, series);
							}
							
							series.add((Port<Double>) port);
						}
					}
				}
				else if (Component.class.isAssignableFrom(componentField.getType()))
				{
					Component component = (Component) componentField.get(this);
					
					components.add(component);
					
					component.init(componentField.getName(), name + "." + componentField.getName(), number);
					
					portsRecursive.addAll(component.portsRecursive);
					fieldsRecursive.addAll(component.fieldsRecursive);
					componentsRecursive.addAll(component.componentsRecursive);
					channelsRecursive.addAll(component.channelsRecursive);
					expressionsRecursive.addAll(component.expressionsRecursive);
					constraintsRecursive.addAll(component.constraintsRecursive);
					minDominancesRecursive.addAll(component.minDominancesRecursive);
					maxDominancesRecursive.addAll(component.maxDominancesRecursive);
					equivalencesRecursive.addAll(component.equivalencesRecursive);
					minObjectivesRecursive.addAll(component.minObjectivesRecursive);
					maxObjectivesRecursive.addAll(component.maxObjectivesRecursive);
					
					for (Entry<Component, Map<String, List<Port<Double>>>> entry : component.chartsRecursive.entrySet())
					{
						chartsRecursive.put(entry.getKey(), entry.getValue());
					}
					
					number += component.portsRecursive.size();
				}
				else if (Channel.class.isAssignableFrom(componentField.getType()))
				{
					Channel<?> channel = (Channel<?>) componentField.get(this);
					
					channels.add(channel);
					
					channel.name = name + "." + componentField.getName();
				}
				else if (Expression.class.isAssignableFrom(componentField.getType()))
				{
					Expression<?> expression = (Expression<?>) componentField.get(this);
					
					expressions.add(expression);
					
					expression.name = name + "." + componentField.getName();
					
					for (Field expressionField : expression.getClass().getFields())
					{
						if (expressionField.getAnnotation(Constant.class) == null)
						{
							fields.add(expressionField);
						}
					}
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
		
		portsRecursive.addAll(ports);
		fieldsRecursive.addAll(fields);
		componentsRecursive.addAll(components);
		channelsRecursive.addAll(channels);
		expressionsRecursive.addAll(expressions);
		constraintsRecursive.addAll(constraints);
		minDominancesRecursive.addAll(minDominances);
		maxDominancesRecursive.addAll(maxDominances);
		equivalencesRecursive.addAll(equivalences);
		minObjectivesRecursive.addAll(minObjectives);
		maxObjectivesRecursive.addAll(maxObjectives);
		
		chartsRecursive.put(this, charts);
	}
	
	public void dump(PrintStream out)
	{
		dump(out, 0);
	}
	
	private void dump(PrintStream out, int indent)
	{
		tabs(out, indent);
		
		out.println(qualifiedName);
		
		for (Component component : components)
		{
			component.dump(out, indent + 1);
		}
	}
	
	private void tabs(PrintStream out, int indent)
	{
		for (int i = 0; i < indent; i++)
		{
			out.print("\t");
		}
	}

}
