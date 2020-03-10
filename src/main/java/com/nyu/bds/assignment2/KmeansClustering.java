package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.nlp.util.ArrayUtils;

import java.util.Set;

public class KmeansClustering {
	
	double[][] centroids;
	String[] features;
	String[] files;
	int numClusters;
	HashMap<String, double[]> wordTfidfByFile;
	
	public KmeansClustering(int numClusters, String[] features, String[] files, HashMap<String, double[]> wordTfidfByFile, double[][] initialCentroids) {
		centroids = new double[numClusters][features.length];
		this.numClusters = numClusters;
		this.wordTfidfByFile = wordTfidfByFile;
		this.features = features;
		this.files = files;
		if(initialCentroids == null) {
			initKMeanPlusCentroids();
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
	
	public void initKMeanPlusCentroids() {
		ArrayList<double[]> initCentroids = new ArrayList<double[]>();
		ArrayList<double[]> pointsLeft = new ArrayList<double[]>(wordTfidfByFile.values());
		//Add First Centroid
		initCentroids.add(pointsLeft.remove(0));
	
		for(int i=1; i < numClusters; i++) {
			double[] farthestPoint = null;
			double maxDistance = Double.MIN_VALUE;
			for (double[] point: pointsLeft) {
				double minCentroidDistance = Double.MAX_VALUE;
				for(double[] centroid: initCentroids) {
					double centroidDist = getCosineDistance(point, centroid);
					if(centroidDist < minCentroidDistance) {
						minCentroidDistance = centroidDist;
					}
				}
				// At this point, we will know the closest centroid to the current point
				if(minCentroidDistance > maxDistance) {
					farthestPoint = point;
					maxDistance = minCentroidDistance;
				}
			}
			// At this point, we will know the farthest point which can become the new centroid.
			initCentroids.add(pointsLeft.remove(pointsLeft.indexOf(farthestPoint)));
		}
		centroids = initCentroids.toArray(centroids);
		
	}
	
	
	
	public void cluster() {
		System.out.println("\nClustering");
		HashMap<String, Integer> prev_files_centroids = null;
		HashMap<String, Integer> files_centroids = assign();
		while(!isAssignmentEqual(prev_files_centroids, files_centroids)) {
			for (String file: files) {
				System.out.println(file + " : " + files_centroids.get(file));	
			}
			
			prev_files_centroids = files_centroids;
			centroids = updateCentroidsUsingCosineAverage(files_centroids);
			files_centroids = assign();
		}
	}
	
	public double[][] updateCentroidsUsingEuclideanAverage(HashMap<String, Integer> files_centroids) {
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
	
	
	public double[][] updateCentroidsUsingCosineAverage(HashMap<String, Integer> files_centroids) {
		double[][] newCentroids = new double[numClusters][features.length];
		int[] numFiles = new int[3];
		
		for (String filePath: files_centroids.keySet()) {
			int centroid = files_centroids.get(filePath);
			numFiles[centroid] += 1;
			
			double[] normTfidf = ArrayUtils.normalize(wordTfidfByFile.get(filePath));
			for (int j = 0; j < features.length; j++) {
				newCentroids[centroid][j] += normTfidf[j];
			}
		}
		
		for (int i = 0; i < numClusters; i++) {
			newCentroids[i] = ArrayUtils.normalize(newCentroids[i]);
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
