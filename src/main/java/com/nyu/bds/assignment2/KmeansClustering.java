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
	BaseSimilarityMeasure similarityMeasure;
	BaseCentroidInitializer centroidInitializer;
	HashMap<String, Integer> files_centroids;

	public enum InitialCentroids {
		RANDOM, KMEANSPLUSPLUS
	};

	public enum DistanceMeasure {
		EUCLIDEAN, COSINE
	};

	public KmeansClustering(int numClusters, String[] features, String[] files,
			HashMap<String, double[]> wordTfidfByFile, InitialCentroids initCentroids, DistanceMeasure distMeasure) {
		centroids = new double[numClusters][features.length];
		this.numClusters = numClusters;
		this.wordTfidfByFile = wordTfidfByFile;
		this.features = features;
		this.files = files;

		switch (distMeasure) {
		case EUCLIDEAN:
			System.out.println("Similarity Measure: EUCLIDEAN");
			this.similarityMeasure = new EuclideanSimilarityMeasure();
			break;
		case COSINE:
			System.out.println("Similarity Measure: COSINE");
			this.similarityMeasure = new CosineSimilarityMeasure();
			break;
		}
		
		switch (initCentroids) {
		case RANDOM:
			System.out.println("Centroid Initialization Method: RANDOM");
			centroidInitializer = new RandomCentroidInitializer();
			break;
		case KMEANSPLUSPLUS:
			System.out.println("Centroid Initialization Method: KMEANS++");
			centroidInitializer = new KMeansPlusPlusCentroidInitializer();
			break;
		}
		initCentroids();
	}
	
		private void initCentroids(){
		ArrayList<double[]> vectors = new ArrayList<double[]>();
		for(double[] vector: wordTfidfByFile.values()) vectors.add(vector);
		centroids = centroidInitializer.getCentroids(numClusters, features.length, vectors.toArray(new double[numClusters][features.length]), similarityMeasure);
	}
	
		
	public void cluster() {
		HashMap<String, Integer> prev_files_centroids = null;
		files_centroids = assign();
		while ( prev_files_centroids == null || !prev_files_centroids.equals(files_centroids)) {
			prev_files_centroids = files_centroids;
			centroids = updateCentroids(files_centroids);
			files_centroids = assign();
		}
	}
	
	
	public int[] getClusters() {
		int[] clusters = new int[files.length]; 
		for(int i=0; i<files.length; i++) {
			clusters[i] = files_centroids.get(files[i]);
		}
		return clusters;
	}
	
	public int getNumClusters() {
		return numClusters;
	}
	
	
	public void printClusters() {
		for (String file: files) {
			System.out.println(file + " : " + files_centroids.get(file));	
		}
	}
	
	
	/** Private Methods **/
	
	private double[][] updateCentroids(HashMap<String, Integer> files_centroids) {
		double[][] newCentroids = new double[numClusters][features.length];
		int[] numFiles = new int[3];

		for (int i = 0; i < numClusters; i++) {
			ArrayList<double[]> vectors = new ArrayList<double[]>();
			for (String file : files_centroids.keySet()) {
				if (files_centroids.get(file) == i) {
					vectors.add(wordTfidfByFile.get(file));
				}
			}
			newCentroids[i] = similarityMeasure.getAverageVector(features.length, vectors.toArray(new double[vectors.size()][features.length]));
		}
		return newCentroids;
	}

	
	private HashMap<String, Integer> assign() {
		HashMap<String, Integer> files_centroids = new HashMap<String, Integer>();
		for (String filePath : wordTfidfByFile.keySet()) {
			double minDistance = Double.MAX_VALUE;
			for (int i = 0; i < numClusters; i++) {
				double distance = similarityMeasure.getDistance(centroids[i], wordTfidfByFile.get(filePath));
				if (distance < minDistance) {
					minDistance = distance;
					files_centroids.put(filePath, i);
				}
			}
		}
		return files_centroids;
	}

}
