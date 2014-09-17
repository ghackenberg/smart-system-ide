package org.xtream.demo.hydro.model.physics;

public class Polynom
{
	
	private int staustufe;
	
	private int level_past;
	private int level_order;
	
	private int inflow_past;
	private int inflow_order;
	
	private int outflow_past;
	private int outflow_order;
	
	private double[] beta;
	
	public Polynom(int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order)
	{
		this.staustufe = staustufe;
		
		this.level_past = level_past;
		this.level_order = level_order;

		this.inflow_past = inflow_past;
		this.inflow_order = inflow_order;

		this.outflow_past = outflow_past;
		this.outflow_order = outflow_order;
	}
	
	public double[] train(Dataset dataset)
	{
		beta = Trainer.trainRegressionModel(dataset, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order);
		
		return beta;
	}

}
