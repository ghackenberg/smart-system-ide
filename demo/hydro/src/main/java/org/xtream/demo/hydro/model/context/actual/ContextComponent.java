package org.xtream.demo.hydro.model.context.actual;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.demo.hydro.model.Constants;

public class ContextComponent extends org.xtream.demo.hydro.model.ContextComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new Expression<Double>(hauptkraftwerkTurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(0, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr1TurbineDischargeExpression = new Expression<Double>(wehr1TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(1, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr2TurbineDischargeExpression = new Expression<Double>(wehr2TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(2, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr3TurbineDischargeExpression = new Expression<Double>(wehr3TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(3, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr4TurbineDischargeExpression = new Expression<Double>(wehr4TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(4, Constants.START + timepoint);
		}
	};
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new Expression<Double>(hauptkraftwerkWeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(0, Constants.START + timepoint ) - Constants.DATASET.getOutflowTurbine(0, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr1WeirDischargeExpression = new Expression<Double>(wehr1WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(1, Constants.START + timepoint ) - Constants.DATASET.getOutflowTurbine(1, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr2WeirDischargeExpression = new Expression<Double>(wehr2WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(2, Constants.START + timepoint ) - Constants.DATASET.getOutflowTurbine(2, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr3WeirDischargeExpression = new Expression<Double>(wehr3WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(3, Constants.START + timepoint ) - Constants.DATASET.getOutflowTurbine(3, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr4WeirDischargeExpression = new Expression<Double>(wehr4WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(4, Constants.START + timepoint ) - Constants.DATASET.getOutflowTurbine(4, Constants.START + timepoint);
		}
	};
	
	public Expression<Double> speicherseeLevelExpression = new Expression<Double>(speicherseeLevelOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getLevel(0, Constants.START + timepoint);
		}
	};
	public Expression<Double> volumen1LevelExpression = new Expression<Double>(volumen1LevelOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getLevel(1, Constants.START + timepoint);
		}
	};
	public Expression<Double> volumen2LevelExpression = new Expression<Double>(volumen2LevelOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getLevel(2, Constants.START + timepoint);
		}
	};
	public Expression<Double> volumen3LevelExpression = new Expression<Double>(volumen3LevelOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getLevel(3, Constants.START + timepoint);
		}
	};
	public Expression<Double> volumen4LevelExpression = new Expression<Double>(volumen4LevelOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getLevel(4, Constants.START + timepoint);
		}
	};
	public Expression<Double> volumen5LevelExpression = new Expression<Double>(volumen5LevelOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getLevel(5, Constants.START + timepoint);
		}
	};

	public Expression<Double> netProductionExpression = new Expression<Double>(netProductionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getProduction(0, Constants.START + timepoint) + Constants.DATASET.getProduction(1, Constants.START + timepoint) + Constants.DATASET.getProduction(2, Constants.START + timepoint) + Constants.DATASET.getProduction(3, Constants.START + timepoint) + Constants.DATASET.getProduction(4, Constants.START + timepoint);
		}
	};
	
}
