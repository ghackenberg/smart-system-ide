package org.xtream.core.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.annotations.Constant;

public abstract class Expression<T>
{
	@Constant
	public List<Field> fields = new ArrayList<>();
	
	@Constant
	public String name;
	@Constant
	public String qualifiedName;
	
	@Constant
	public Component parent;
	
	@Constant
	public Port<T> port;

	public Expression(Port<T> port)
	{
		this.port = port;
		
		port.expression = this;
	}
	
	public void load()
	{
		for (Field expressionField : getClass().getFields())
		{
			if (expressionField.getAnnotation(Constant.class) == null)
			{
				fields.add(expressionField);
			}
		}
	}
	
	public abstract T evaluate(int timepoint);
	
}
