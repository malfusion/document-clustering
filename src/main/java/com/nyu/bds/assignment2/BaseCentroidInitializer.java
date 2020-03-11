package com.nyu.bds.assignment2;

import com.nyu.bds.assignment2.KmeansClustering.DistanceMeasure;

public abstract class BaseCentroidInitializer {
	abstract double[][] getCentroids(int numCentroids, int numFeatures, double[][] vectors, BaseSimilarityMeasure similarityMeasure);
}
