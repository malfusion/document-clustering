package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
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
//		this.centroids = new double[numClusters][features.length];
		
		
		
//		for(int i=0; i < numClusters; i++) {
//			double[] randomPoint = wordTfidfByFile.get(wordTfidfByFile.keySet().toArray()[i]);
//			for (int j = 0; j < features.length; j++) {
//				centroids[i][j] = randomPoint[j];
//			}
//		}
		
		for (int i = 0; i < numClusters; i++) {
			System.out.println("^TopCentroid");
			for (int j = 0; j < features.length; j++) {
				System.out.print(centroids[i][j] + " ");
			}
			
		}
	}
	
	public boolean isAssignmentEqual(HashMap<String, Integer> A, HashMap<String, Integer> B) {
		if (A != null && B != null) {
			if(A.keySet().equals(B.keySet())) {
				for (String key: A.keySet()) {
					if(A.get(key) != B.get(key)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
			
		}
		else {
			return false;
		}
	}
	
	
	public void updateCentroids(HashMap<String, Integer> files_centroids) {
		
		for (int i = 0; i < numClusters; i++) {
			ArrayList<String> filesBelongingToCentroid = new ArrayList<String>();
			
			// Get all files belonging to this centroid
			for (String filePath: files_centroids.keySet()) {
				if(files_centroids.get(filePath) == i) {
					filesBelongingToCentroid.add(filePath);
				}
			}
			
			if(filesBelongingToCentroid.size() == 0) {
				continue;
//				throw new Error("NO FILES BELONG TO THIS CLUSTER");
			}
			
			int numFilesBelongingToCentroid = filesBelongingToCentroid.size();
			System.out.println("Num files belogning to centroid" + numFilesBelongingToCentroid);
			// Find the new centroid
			
			for (int j = 0; j < features.length; j++) {
				Double sum = 0.0;
				for(String filePath: filesBelongingToCentroid) {
					sum += wordTfidfByFile.get(filePath)[j];	
				}
				centroids[i][j] = sum / numFilesBelongingToCentroid;
			}
		}
		for (int i = 0; i < numClusters; i++) {
			for (int j = 0; j < features.length; j++) {
				System.out.print(centroids[i][j] + " ");
			}
			System.out.println("^Centroid");
		}
	}
	
	public void cluster() {
		HashMap<String, Integer> prev_files_centroids = null;
		HashMap<String, Integer> files_centroids = assign();
		
		System.out.println(files_centroids.values());
		while(!isAssignmentEqual(prev_files_centroids, files_centroids)) {
			prev_files_centroids = files_centroids;
			updateCentroids(files_centroids);
			files_centroids = assign();
//			System.out.println(prev_files_centroids.values());
			for (Entry<String, Integer> entry : files_centroids.entrySet()) {
				System.out.println(entry);
			}
			
			System.out.println("Nextloop");
		}
		
	}
	
	public double getSqrtOfSquared(double[] arr) {
		double sum = 0.0;
		for (int i = 0; i < features.length; i++) {
			sum += arr[i] * arr[i];
		}
		return Math.sqrt(sum);
	}

	public Double getDotProduct(double[] centroidArr, double[] documentArr) {
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
	
	public HashMap<String, Integer> assign() {
//		double[] centroidsSquared = new double[numClusters];
//		
//		for (int i = 0; i < numClusters; i++) {
//			centroidsSquared[i] = getSqrtOfSquared(centroids[i]);
//		}
		
		HashMap<String, Integer> files_centroids = new HashMap<String, Integer>();
		
		for(String filePath : wordTfidfByFile.keySet()) {
			/*
			 * REMEMBER THE DIFFERENCE BETWEEN DISTANCE AND SIMILARITY
			 */
			Double minDistance = Double.MAX_VALUE;
			
			for (int i = 0; i < numClusters; i++) {
				Double distance = getEuclideanDistance(centroids[i], wordTfidfByFile.get(filePath));
				System.out.println(distance);
				/*
				 * START COSINE SIMILARITY
				 * */
//				Double ASquared = centroidsSquared.get(idx);
//				Double BSquared = getSqrtOfSquared(files_words_tfidf.get(filePath));
//				Double AB = getDotProduct(centroid, files_words_tfidf.get(filePath));
//				Double similarity = AB / (ASquared*BSquared);
				/*
				 * END COSINE SIMILARITY
				 * */
				if(distance < minDistance) {
					minDistance = distance;
					files_centroids.put(filePath, i);
				}
			}
			System.out.println("Clear");
		}
		return files_centroids;
	}
	
}
