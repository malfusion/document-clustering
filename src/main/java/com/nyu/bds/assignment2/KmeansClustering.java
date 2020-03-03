package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KmeansClustering {
	
	ArrayList<HashMap<String, Double>> centroids;
	String[] features;
	HashMap<String, HashMap<String, Double>> files_words_tfidf;
	
	public KmeansClustering(Integer k, String[] features,HashMap<String, HashMap<String, HashMap<String, Double>>> folders_files_words_tfidf) {
		centroids = new ArrayList<HashMap<String, Double>>();
		for(Integer i=0; i < k; i++) {
			HashMap<String, Double> centroid = new HashMap<String, Double>();
			for (String word: features) {
				centroid.put(word, Math.random());
			}
			centroids.add(centroid);
		}
		this.files_words_tfidf = new HashMap<String, HashMap<String,Double>>();
		this.features = features;
		for(String folderPath: folders_files_words_tfidf.keySet()) {
			for(String filePath: folders_files_words_tfidf.get(folderPath).keySet()) {
				files_words_tfidf.put(filePath, folders_files_words_tfidf.get(folderPath).get(filePath));
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
		ArrayList<HashMap<String, Double>> newCentroids = new ArrayList<HashMap<String,Double>>();
		
		for (int i = 0; i < centroids.size(); i++) {
			ArrayList<String> filesBelongingToCentroid = new ArrayList<String>();
			
			// Get all files belonging to this centroid
			for (String filePath: files_centroids.keySet()) {
				if(files_centroids.get(filePath) == i) {
					filesBelongingToCentroid.add(filePath);
				}
			}
			
			// Get all words intersecting in these files
			Set<String> wordsInvolved = new HashSet<String>();
			for (String filePath: filesBelongingToCentroid) {
				for (String word: files_words_tfidf.get(filePath).keySet()) {
					wordsInvolved.add(word);
				}
			}
			
			// Find the new centroid
			HashMap<String, Double> newCentroid = new HashMap<String, Double>();
			for (String word: wordsInvolved) {
				Double sum = 0.0;
				for(String filePath: filesBelongingToCentroid) {
					sum += files_words_tfidf.get(filePath).getOrDefault(word, 0.0);
				}
				newCentroid.put(word, sum / filesBelongingToCentroid.size());
			}
			newCentroids.add(newCentroid);
		}
		centroids = newCentroids;
	}
	
	public void cluster() {
		HashMap<String, Integer> prev_files_centroids = null;
		HashMap<String, Integer> files_centroids = assign();
		while(!isAssignmentEqual(prev_files_centroids, files_centroids)) {
			System.out.println("notsame");
			System.out.println(centroids.get(0));
			prev_files_centroids = files_centroids;
			updateCentroids(files_centroids);
			System.out.println(centroids.get(0));
			files_centroids = assign();
			
			System.out.println("Nextloop");
			
	
		}
		
	}
	
	public Double getSqrtOfSquared(HashMap<String, Double> sparseMatrix) {
		double sum = 0.0;
		for (String word: sparseMatrix.keySet()) {
			sum += sparseMatrix.get(word) * sparseMatrix.get(word);
		}
		return Math.sqrt(sum);
	}

	public Double getDotProduct(HashMap<String, Double> centroidSparseMatrix, HashMap<String, Double> documentSparseMatrix) {
		double sum = 0.0;
		for (String word: documentSparseMatrix.keySet()) {
			if(centroidSparseMatrix.containsKey(word)) {
				sum += documentSparseMatrix.get(word) * centroidSparseMatrix.get(word);
			}
		}
		return sum;
	}
	
	public HashMap<String, Integer> assign() {
		ArrayList<Double> centroidsSquared = new ArrayList<Double>();
		
		for (HashMap<String, Double> centroid: centroids) {
			centroidsSquared.add(getSqrtOfSquared(centroid));
		}
		
		HashMap<String, Integer> files_centroids = new HashMap<String, Integer>();
		
		
		for(String filePath : files_words_tfidf.keySet()) {
			Double minSimilarity = Double.MAX_VALUE;
			Integer idx = 0;
			for (HashMap<String, Double> centroid: centroids) {
				Double ASquared = centroidsSquared.get(idx);
				Double BSquared = getSqrtOfSquared(files_words_tfidf.get(filePath));
				Double AB = getDotProduct(centroid, files_words_tfidf.get(filePath));
				Double similarity = AB / (ASquared*BSquared);
				if(similarity < minSimilarity) {
					minSimilarity = similarity;
					files_centroids.put(filePath, idx);
				}
				idx += 1;
			}
			
		}

		return files_centroids;
			
		
	}
	
}
