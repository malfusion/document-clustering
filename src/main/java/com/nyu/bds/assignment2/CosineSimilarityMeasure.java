package com.nyu.bds.assignment2;

import edu.stanford.nlp.util.ArrayUtils;

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
	double[] getAverageVector(int numFeatures, double[][] vectors) {
		double[] res = new double[numFeatures];
		for (int i = 0; i < vectors.length; i++) {
			double[] normTfidf = ArrayUtils.normalize(vectors[i]);
			for (int j = 0; j < normTfidf.length; j++) {
				res[j] += normTfidf[j];
			}
		}
		res = ArrayUtils.normalize(res);
		return res;
	}

}
