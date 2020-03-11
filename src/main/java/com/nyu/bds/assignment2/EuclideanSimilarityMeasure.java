package com.nyu.bds.assignment2;

import edu.stanford.nlp.util.ArrayUtils;

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
	double[] getAverageVector(int numFeatures, double[][] vectors) {
		double[] res = new double[numFeatures];
		for (int i = 0; i < vectors.length; i++) {
			double[] tfidf = vectors[i];
			for (int j = 0; j < tfidf.length; j++) {
				res[j] += tfidf[j];
			}
		}
		
		for (int i = 0; i < res.length; i++) {
			res[i] /= vectors.length;
		}
		
		return res;
	}

	
	

}
