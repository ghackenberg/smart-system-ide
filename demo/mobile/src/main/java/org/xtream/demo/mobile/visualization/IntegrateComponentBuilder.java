package org.xtream.demo.mobile.visualization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.model.IntegrateComponent;
import org.xtream.demo.mobile.model.root.ModulesContainer;

public class IntegrateComponentBuilder {

	public IntegrateComponent buildIntegrateComponent (Graph graph, ModulesContainer modules) {
		
		IntegrateComponent builtIntegrateComponent = null;
		
		try 
		{
			Class<IntegrateComponent> integrateClass = IntegrateComponent.class;
			Constructor<IntegrateComponent> constructor = integrateClass.getConstructor(org.xtream.demo.mobile.datatypes.Graph.class, org.xtream.demo.mobile.model.root.ModulesContainer.class);
			IntegrateComponent integrate = constructor.newInstance(graph, modules);

			builtIntegrateComponent = integrate;
		} 
		catch (NoSuchMethodException | SecurityException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException e) 
		{
			e.printStackTrace();
		}
		
		return builtIntegrateComponent;
	}
	
}
