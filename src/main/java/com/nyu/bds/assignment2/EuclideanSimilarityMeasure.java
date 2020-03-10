package com.nyu.bds.assignment2;

public class EuclideanSimilarityMeasure extends BaseSimilarityMeasure{

	@Override
	double getDistance(double[] A, double[] B) {
		double sum = 0.0;
		for (int i = 0; i < A.length; i++) {
			sum += Math.pow((A[i] - B[i]), 2);
		}
		return Math.sqrt(sum);
	}

	@Override
	double[] getAverageVector(double[][] vectors) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
