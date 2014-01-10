package org.xtream.core.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xtream.core.model.enumerations.Direction;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Objective
{
	
	public Direction value();
	
}
