package org.xtream.core.model;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.annotations.Preference;

public abstract class Component
{

	public String name;
	public String qualifiedName;
	
	public List<Port<?>> ports = new ArrayList<>();
	public List<Field> fields = new ArrayList<>();
	public List<Component> components = new ArrayList<>();
	public List<Expression<?>> expressions = new ArrayList<>();
	public List<Constraint> constraints= new ArrayList<>();
	public List<Equivalence> equivalences= new ArrayList<>();
	public List<Preference> minDominances= new ArrayList<>();
	public List<Preference> maxDominances= new ArrayList<>();
	public List<Objective> minObjectives= new ArrayList<>();
	public List<Objective> maxObjectives= new ArrayList<>();
	public List<Chart> charts = new ArrayList<>();
	public List<Chart> previews = new ArrayList<>();
	
	public List<Port<?>> portsRecursive = new ArrayList<>();
	public List<Field> fieldsRecursive = new ArrayList<>();
	public List<Component> componentsRecursive = new ArrayList<>();
	public List<Expression<?>> expressionsRecursive = new ArrayList<>();
	public List<Constraint> constraintsRecursive= new ArrayList<>();
	public List<Equivalence> equivalencesRecursive= new ArrayList<>();
	public List<Preference> minDominancesRecursive = new ArrayList<>();
	public List<Preference> maxDominancesRecursive = new ArrayList<>();
	public List<Objective> minObjectivesRecursive= new ArrayList<>();
	public List<Objective> maxObjectivesRecursive= new ArrayList<>();
	public List<Chart> chartsRecursive = new ArrayList<>();
	public List<Chart> previewsRecursive = new ArrayList<>();
	
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
		constraintsRecursive.addAll(constraints);
		equivalencesRecursive.addAll(equivalences);
		minDominancesRecursive.addAll(minDominances);
		maxDominancesRecursive.addAll(maxDominances);
		minObjectivesRecursive.addAll(minObjectives);
		maxObjectivesRecursive.addAll(maxObjectives);
		chartsRecursive.addAll(charts);
		previewsRecursive.addAll(previews);
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
		else if (object instanceof Constraint)
		{
			Constraint constraint = (Constraint) object;
			
			load(constraint, name, qualifiedName);
		}
		else if (object instanceof Equivalence)
		{
			Equivalence equivalence = (Equivalence) object;
			
			load(equivalence, name, qualifiedName);
		}
		else if (object instanceof Preference)
		{
			Preference preference = (Preference) object;
			
			load(preference, name, qualifiedName);
		}
		else if (object instanceof Objective)
		{
			Objective objective = (Objective) object;
			
			load(objective, name, qualifiedName);
		}
		else if (object instanceof Chart)
		{
			Chart chart = (Chart) object;
			
			load(chart, name, qualifiedName);
		}
	}
	
	private void load(Field componentField, Port<?> port, String name, String qualifiedName)
	{
		ports.add(port);
	
		port.name = name;
		port.qualifiedName = qualifiedName;
	}
	
	private void load(Field componentField, Component component, String name, String qualifiedName)
	{
		components.add(component);
	
		component.init(name, qualifiedName);
		
		portsRecursive.addAll(component.portsRecursive);
		fieldsRecursive.addAll(component.fieldsRecursive);
		componentsRecursive.addAll(component.componentsRecursive);
		expressionsRecursive.addAll(component.expressionsRecursive);
		constraintsRecursive.addAll(component.constraintsRecursive);
		equivalencesRecursive.addAll(component.equivalencesRecursive);
		minDominancesRecursive.addAll(component.minDominancesRecursive);
		maxDominancesRecursive.addAll(component.maxDominancesRecursive);
		minObjectivesRecursive.addAll(component.minObjectivesRecursive);
		maxObjectivesRecursive.addAll(component.maxObjectivesRecursive);
		chartsRecursive.addAll(component.chartsRecursive);
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
	
	private void load(Constraint constraint, String name, String qualifiedName)
	{
		constraints.add(constraint);
		
		constraint.name = name;
		constraint.qualifiedName = qualifiedName;
	}
	
	private void load(Equivalence equivalence, String name, String qualifiedName)
	{
		equivalences.add(equivalence);
		
		equivalence.name = name;
		equivalence.qualifiedName = qualifiedName;
	}
	
	private void load(Preference preference, String name, String qualifiedName)
	{
		switch (preference.direction)
		{
		case MIN:
			minDominances.add(preference);
			break;
		case MAX:
			maxDominances.add(preference);
			break;
		default:
			throw new IllegalStateException();
		}
		
		preference.name = name;
		preference.qualifiedName = qualifiedName;
	}
	
	private void load(Objective objective, String name, String qualifiedName)
	{
		switch (objective.direction)
		{
		case MIN:
			minObjectives.add(objective);
			break;
		case MAX:
			maxObjectives.add(objective);
			break;
		default:
			throw new IllegalStateException();
		}
		
		objective.name = name;
		objective.qualifiedName = qualifiedName;
	}
	
	private void load(Chart chart, String name, String qualifiedName)
	{
		if (name.endsWith("Chart"))
		{
			charts.add(chart);
		}
		else if (name.endsWith("Preview"))
		{
			previews.add(chart);
		}
		else
		{
			throw new IllegalStateException();
		}
		
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
