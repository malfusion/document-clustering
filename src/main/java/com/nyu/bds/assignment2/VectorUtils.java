package com.nyu.bds.assignment2;

public class VectorUtils {
	static double getSqrtOfSquared(double[] arr) {
		double sum = 0.0;
		for (int i = 0; i < arr.length; i++) {
			sum += Math.pow(arr[i], 2);
		}
		return Math.sqrt(sum);
	}

	static double getDotProduct(double[] A, double[] B) {
		double sum = 0.0;
		for (int i = 0; i < A.length; i++) {
			sum += A[i] * B[i];
		}
		return sum;
	}
}
