package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.stanford.nlp.util.ArrayUtils;

public class AccuracyMeasures {

	int[] predictedClusters;
	int[] realClusters;
	int numClusters;
	
	int[][] confusionMatrix;
	double totalRecall;
	double totalPrecision;
	double fMeasure;
	
	public AccuracyMeasures(KmeansClustering clustering, int[] actualResults) {
		predictedClusters = clustering.getClusters();
		realClusters = actualResults;
		numClusters = clustering.getNumClusters();
		confusionMatrix = new int[numClusters][numClusters];
		remapPredictions();
		calcAccuracy();
	}
	
	private void remapPredictions(){
		HashSet<Integer> candidates = new HashSet<Integer>();
		for(int i=0; i<numClusters; i++) candidates.add(i);
		
		HashMap<Integer, Integer> mapping = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < numClusters; i++) {
			int[] counts = new int[numClusters];
			int max = Integer.MIN_VALUE;
			int idx = -1;
			for (int j = 0; j < predictedClusters.length; j++) {
				if(candidates.contains(realClusters[j])) {
					if(predictedClusters[j] == i) {
						counts[realClusters[j]] += 1;
					}
					if(counts[realClusters[j]] > max) {
						max = counts[realClusters[j]];
						idx = j;
					}
				}
			}
			mapping.put(i, realClusters[idx]);
			candidates.remove(realClusters[idx]);
		}
		System.out.println("\nCluster Prediction Re-Mapping:");
		for (Integer key: mapping.keySet()){
            System.out.println(key+ " : " + mapping.get(key));  
		}
		System.out.println("Before Remapping: " + Arrays.toString(predictedClusters));
		for (int i = 0; i < predictedClusters.length; i++) {
			predictedClusters[i] = mapping.get(predictedClusters[i]);
		}
		System.out.println("After Remapping: " + Arrays.toString(predictedClusters));
	}
	
	private void calcAccuracy() {
		for(int i=0; i < predictedClusters.length; i++) {
			confusionMatrix[predictedClusters[i]][realClusters[i]] += 1;
		}
		
		double[] recall = new double[numClusters];
        double[] precision = new double[numClusters];
        for(int i = 0; i < numClusters; i++)
        {
                int rsum = 0;
                int csum = 0;
                for(int j=0; j < numClusters; j++) {
                        rsum += confusionMatrix[i][j];
                        csum += confusionMatrix[j][i];
                }
                if(csum>0) {
                	recall[i] = 1.0*confusionMatrix[i][i]/csum;	
                }
                if(rsum > 0) {
                	precision[i] = 1.0*confusionMatrix[i][i]/rsum;
                }
        }
        
        totalRecall = 0.0;
        totalPrecision = 0.0;
        for(int i=0 ; i<numClusters; i++) {
                totalRecall += recall[i];
                totalPrecision += precision[i];                 
        }

        totalRecall = totalRecall/numClusters;
        totalPrecision = totalPrecision/numClusters;
        fMeasure = 2 * ((totalRecall*totalPrecision)/(totalRecall+totalPrecision));
	}
	
	public int[] getRemappedClusters() {
		return predictedClusters;
	}
	
	public int[][] getConfusionMatrix(){
		return confusionMatrix;
	}
	
	public double getFMeasure(){
		return fMeasure;
	}
}




