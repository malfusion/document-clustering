package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import edu.stanford.nlp.util.ArrayUtils;

import java.util.Set;

public class KmeansClustering {
	
	double[][] centroids;
	String[] features;
	int numClusters;
	HashMap<String, double[]> wordTfidfByFile;
	
	public KmeansClustering(int numClusters, String[] features, HashMap<String, double[]> wordTfidfByFile, double[][] initialCentroids) {
		this.centroids = initialCentroids;
		this.numClusters = numClusters;
		this.wordTfidfByFile = wordTfidfByFile;
		this.features = features;
		if(this.centroids == null) {
			initRandomCentroids();
		}
	}
	
	public boolean isAssignmentEqual(HashMap<String, Integer> A, HashMap<String, Integer> B) {
		if (A != null && B != null) {
			Integer[] a = new Integer[24];
			a = A.values().toArray(a);
			
			Integer[] b = new Integer[24];
			b = B.values().toArray(b);
					
			for (int i = 0; i < 24; i++) {
				if(a[i] != b[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
		
//		if (A != null && B != null) {
//			if(A.keySet().equals(B.keySet())) {
//				for (String key: A.keySet()) {
//					if(A.get(key) != B.get(key)) {
//						return false;
//					}
//				}
//				return true;
//			}
//			else {
//				return false;
//			}
//			
//		}
//		else {
//			return false;
//		}
	}
	
	
	public void printCurrentCentroids() {
		for (int i = 0; i < numClusters; i++) {
			for (int j = 0; j < features.length; j++) {
				System.out.print(centroids[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	
	public void initRandomCentroids() {
		centroids = new double[numClusters][features.length];
		for(int i=0; i < numClusters; i++) {
			double[] randomPoint = wordTfidfByFile.get(wordTfidfByFile.keySet().toArray()[i]);
			for (int j = 0; j < features.length; j++) {
				centroids[i][j] = randomPoint[j];
			}
		}
	}
	
	
	
	public void cluster() {
		HashMap<String, Integer> prev_files_centroids = null;
		HashMap<String, Integer> files_centroids = assign();
		while(!isAssignmentEqual(prev_files_centroids, files_centroids)) {
			prev_files_centroids = files_centroids;
			centroids = updateCentroids(files_centroids);
			files_centroids = assign();
		}
	}
	
	public double[][] updateCentroids(HashMap<String, Integer> files_centroids) {
		double[][] newCentroids = new double[numClusters][features.length];
		int[] numFiles = new int[3];
		
		for (String filePath: files_centroids.keySet()) {
			int centroid = files_centroids.get(filePath);
			numFiles[centroid] += 1;
			
			double[] tfidf = wordTfidfByFile.get(filePath);
			for (int j = 0; j < features.length; j++) {
				newCentroids[centroid][j] += tfidf[j];
			}
		}
		
		for (int i = 0; i < numClusters; i++) {
			for (int j = 0; j < features.length; j++) {
				newCentroids[i][j] /= numFiles[i];	
			}
		}	
		return newCentroids;
	}
	
	
	public HashMap<String, Integer> assign() {
		HashMap<String, Integer> files_centroids = new HashMap<String, Integer>();
		for(String filePath : wordTfidfByFile.keySet()) {
			double minDistance = Double.MAX_VALUE;
			for (int i = 0; i < numClusters; i++) {
				// double distance = getEuclideanDistance(centroids[i], wordTfidfByFile.get(filePath));				
				double distance = getCosineDistance(centroids[i], wordTfidfByFile.get(filePath));
//				System.out.println(distance);
				if(distance < minDistance) {
					minDistance = distance;
					files_centroids.put(filePath, i);
				}
			}
		}
		return files_centroids;
	}
	
	
	
	
	public double getCosineDistance(double[] A, double[] B){
		Double ASquared = getSqrtOfSquared(A);
		Double BSquared = getSqrtOfSquared(B);
		Double AB = getDotProduct(A, B);
		Double similarity = AB / (ASquared*BSquared);
		double distance = 1 - similarity;
		return distance;
	}
	
	
	public double getSqrtOfSquared(double[] arr) {
		double sum = 0.0;
		for (int i = 0; i < features.length; i++) {
			sum += Math.pow(arr[i], 2);
		}
		return Math.sqrt(sum);
	}

	public double getDotProduct(double[] centroidArr, double[] documentArr) {
		double sum = 0.0;
		for (int i = 0; i < features.length; i++) {
			sum += documentArr[i] * centroidArr[i];
		}
		return sum;
	}
	
	public double getEuclideanDistance(double[] centroidArr, double[] documentArr) {
		double sum = 0.0;
		for (int i = 0; i < features.length; i++) {
			sum += Math.pow((centroidArr[i] - documentArr[i]), 2);
		}
		return Math.sqrt(sum);
	}
	
}
