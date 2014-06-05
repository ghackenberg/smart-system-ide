package org.xtream.core.optimizer;

public class Memory
{
	
	public static long totalMemory()
	{
		return Runtime.getRuntime().totalMemory() / 1024 / 1024;
	}
	
	public static long freeMemory()
	{
		return Runtime.getRuntime().freeMemory() / 1024 / 1024;
	}
	
	public static long maxMemory()
	{
		return Runtime.getRuntime().maxMemory() / 1024 / 1024;
	}
	
	public static long usedMemory()
	{
		return totalMemory() - freeMemory();
	}
	

}
