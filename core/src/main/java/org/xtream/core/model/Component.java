package org.xtream.core.model;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
	public List<Expression<?>> expressions = new ArrayList<>();
	public List<Chart> charts = new ArrayList<>();
	public List<Port<Boolean>> constraints= new ArrayList<>();
	public List<Port<Double>> minDominances= new ArrayList<>();
	public List<Port<Double>> maxDominances= new ArrayList<>();
	public List<Port<?>> equivalences= new ArrayList<>();
	public List<Port<Double>> minObjectives= new ArrayList<>();
	public List<Port<Double>> maxObjectives= new ArrayList<>();
	
	public List<Port<?>> portsRecursive = new ArrayList<>();
	public List<Field> fieldsRecursive = new ArrayList<>();
	public List<Component> componentsRecursive = new ArrayList<>();
	public List<Expression<?>> expressionsRecursive = new ArrayList<>();
	public List<Chart> chartsRecursive = new ArrayList<>();
	public List<Port<Boolean>> constraintsRecursive= new ArrayList<>();
	public List<Port<Double>> minDominancesRecursive = new ArrayList<>();
	public List<Port<Double>> maxDominancesRecursive = new ArrayList<>();
	public List<Port<?>> equivalencesRecursive= new ArrayList<>();
	public List<Port<Double>> minObjectivesRecursive= new ArrayList<>();
	public List<Port<Double>> maxObjectivesRecursive= new ArrayList<>();
	
	public void init()
	{
		init("root", "root");
		
		for (int i = 0; i < portsRecursive.size(); i++)
		{
			portsRecursive.get(i).number = i;
		}
	}
	
	public void init(String name, String qualifiedName)
	{
		this.name = name;
		this.qualifiedName = qualifiedName;
		
		for (Field componentField : this.getClass().getFields())
		{
			try
			{
				if (componentField.getType().isArray())
				{
					Object[] objects = (Object[]) componentField.get(this);
					
					for (int i = 0; i < objects.length; i++)
					{
						load(componentField, objects[i], componentField.getName() + "[" + i + "]", qualifiedName + "." + componentField.getName() + "[" + i + "]");
					}
				}
				else
				{
					load(componentField, componentField.get(this), componentField.getName(), qualifiedName + "." + componentField.getName());
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
		expressionsRecursive.addAll(expressions);
		chartsRecursive.addAll(charts);
		constraintsRecursive.addAll(constraints);
		minDominancesRecursive.addAll(minDominances);
		maxDominancesRecursive.addAll(maxDominances);
		equivalencesRecursive.addAll(equivalences);
		minObjectivesRecursive.addAll(minObjectives);
		maxObjectivesRecursive.addAll(maxObjectives);
	}
	
	private void load(Field componentField, Object object, String name, String qualifiedName)
	{
		if (object instanceof Port<?>)
		{
			Port<?> port = (Port<?>) object;
			
			load(componentField, port, name, qualifiedName);
		}
		else if (object instanceof Component)
		{
			Component component = (Component) object;
			
			load(componentField, component, name, qualifiedName);
		}
		else if (object instanceof Expression<?>)
		{
			Expression<?> expression = (Expression<?>) object;
			
			load(expression, name, qualifiedName);
		}
		else if (object instanceof Chart)
		{
			Chart chart = (Chart) object;
			
			load(chart, name, qualifiedName);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void load(Field componentField, Port<?> port, String name, String qualifiedName)
	{
		ports.add(port);
	
		port.name = name;
		port.qualifiedName = qualifiedName;
		
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
	}
	
	private void load(Field componentField, Component component, String name, String qualifiedName)
	{
		components.add(component);
	
		component.init(name, qualifiedName);
		
		portsRecursive.addAll(component.portsRecursive);
		fieldsRecursive.addAll(component.fieldsRecursive);
		componentsRecursive.addAll(component.componentsRecursive);
		expressionsRecursive.addAll(component.expressionsRecursive);
		chartsRecursive.addAll(component.chartsRecursive);
		constraintsRecursive.addAll(component.constraintsRecursive);
		minDominancesRecursive.addAll(component.minDominancesRecursive);
		maxDominancesRecursive.addAll(component.maxDominancesRecursive);
		equivalencesRecursive.addAll(component.equivalencesRecursive);
		minObjectivesRecursive.addAll(component.minObjectivesRecursive);
		maxObjectivesRecursive.addAll(component.maxObjectivesRecursive);
	}
	
	private void load(Expression<?> expression, String name, String qualifiedName)
	{
		expressions.add(expression);
		
		expression.name = name;
		expression.qualifiedName = qualifiedName;
		
		for (Field expressionField : expression.getClass().getFields())
		{
			if (expressionField.getAnnotation(Constant.class) == null)
			{
				fields.add(expressionField);
			}
		}
	}
	
	private void load(Chart chart, String name, String qualifiedName)
	{
		charts.add(chart);
		
		chart.name = name;
		chart.qualifiedName = qualifiedName;
	}
	
	public void dump(PrintStream out)
	{
		dump(out, 0);
	}
	
	private void dump(PrintStream out, int indent)
	{
		tabs(out, indent);
		
		out.println(qualifiedName);
		
		for (Port<?> port : ports)
		{
			tabs(out, indent + 1);
			
			out.println(port.qualifiedName);
		}
		
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
