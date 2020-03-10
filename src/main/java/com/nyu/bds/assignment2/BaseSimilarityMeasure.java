package com.nyu.bds.assignment2;

public abstract class BaseSimilarityMeasure {
	
	abstract double getDistance(double[] A, double[] B);
	
	abstract double[] getAverageVector(double[][] vectors);
	
}
