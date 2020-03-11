package com.nyu.bds.assignment2;

import com.nyu.bds.assignment2.KmeansClustering.DistanceMeasure;

public class RandomCentroidInitializer extends BaseCentroidInitializer {

	@Override
	double[][] getCentroids(int numCentroids, int numFeatures, double[][] vectors, BaseSimilarityMeasure similarityMeasure) {
		double[][] centroids = new double[numCentroids][numFeatures];
		for (int i = 0; i < numCentroids; i++) {
			
			int randomIdx = (int) Math.floor(Math.random() * vectors.length);
			double[] randomPoint = vectors[randomIdx];
			
			for (int j = 0; j < numFeatures; j++) {
				centroids[i][j] = randomPoint[j];
			}
		}
		return centroids;
	}

}
