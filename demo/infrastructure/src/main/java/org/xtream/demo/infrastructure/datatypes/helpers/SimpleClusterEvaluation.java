package org.xtream.demo.infrastructure.datatypes.helpers;

import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.core.Dataset;

public class SimpleClusterEvaluation implements ClusterEvaluation {

	
	@Override
	public boolean compareScore(double score1, double score2) {
		
		if (score2 < score1)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	@Override
	public double score(Dataset[] clusters) 
	{
		Double sumDataSetSize = 0.;
		
		for (Dataset dataset : clusters)
		{
			sumDataSetSize += dataset.size();
		}
		
		Double mean = sumDataSetSize/clusters.length;
		
		Double result = 0.;
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		
		for (Dataset dataset : clusters)
		{
			if (dataset.size() < min)
			{
				min = (double) dataset.size();
			}
			
			if (dataset.size() > max) 
			{
				max = (double) dataset.size();
			}
			
			result += Math.pow((mean-dataset.size()),2);
		}
		
		return (result + Math.abs(mean-min) + Math.abs(mean-max));
	}

}
