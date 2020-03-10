package com.nyu.bds.assignment2;

public class CosineSimilarityMeasure extends BaseSimilarityMeasure {

	@Override
	double getDistance(double[] A, double[] B) {
		Double ASquared = VectorUtils.getSqrtOfSquared(A);
		Double BSquared = VectorUtils.getSqrtOfSquared(B);
		Double AB = VectorUtils.getDotProduct(A, B);
		Double similarity = AB / (ASquared*BSquared);
		double distance = 1 - similarity;
		return distance;
	}

	@Override
	double getAverage(double[][] vectors) {
		// TODO Auto-generated method stub
		return 0;
	}

}
