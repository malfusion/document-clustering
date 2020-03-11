package com.nyu.bds.assignment2;

import java.util.ArrayList;

import com.nyu.bds.assignment2.KmeansClustering.DistanceMeasure;

public class KMeansPlusPlusCentroidInitializer extends BaseCentroidInitializer {
	
	@Override
	double[][] getCentroids(int numCentroids, int numFeatures, double[][] vectors, BaseSimilarityMeasure similarityMeasure) {
		ArrayList<double[]> initCentroids = new ArrayList<double[]>();
		ArrayList<double[]> pointsLeft = new ArrayList<double[]>();
		for(double[] vector: vectors) pointsLeft.add(vector);

		// Add First Centroid
		initCentroids.add(pointsLeft.remove(0));

		for (int i = 1; i < numCentroids; i++) {
			double[] farthestPoint = null;
			double maxDistance = Double.MIN_VALUE;
			for (double[] point : pointsLeft) {
				double minCentroidDistance = Double.MAX_VALUE;
				for (double[] centroid : initCentroids) {
					double centroidDist = similarityMeasure.getDistance(point, centroid);
					if (centroidDist < minCentroidDistance) {
						minCentroidDistance = centroidDist;
					}
				}
				// At this point, we will know the closest centroid to the current point
				if (minCentroidDistance > maxDistance) {
					farthestPoint = point;
					maxDistance = minCentroidDistance;
				}
			}
			// At this point, we will know the farthest point which can become the new centroid.
			initCentroids.add(pointsLeft.remove(pointsLeft.indexOf(farthestPoint)));
		}
		return initCentroids.toArray(new double[numCentroids][vectors[0].length]);
	}

}
