package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;

import com.nyu.bds.assignment2.FileOperations;
import com.nyu.bds.assignment2.KmeansClustering.DistanceMeasure;
import com.nyu.bds.assignment2.KmeansClustering.InitialCentroids;

import Jama.Matrix;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {

		TextPreprocessor preprocessor = new TextPreprocessor();

		HashMap<String, HashMap<String, List<String>>> folder_files_words = new HashMap<String, HashMap<String, List<String>>>();
		HashMap<String, List<String>> files_words = new HashMap<String, List<String>>();

		List<String> files = new ArrayList<String>();

		for (String folderPath : FileOperations.readFileAsLines("data.txt")) {
			for (String filePath : FileOperations.getFilesInFolder(folderPath)) {
				String content = FileOperations.readFile(filePath);
				files_words.put(filePath, preprocessor.process(content));
				files.add(filePath);
			}
		}
		String[] filesArr = files.toArray(new String[files.size()]);

		TermDocumentStats termStats = new TermDocumentStats(files_words, files);
		termStats.process();
		termStats.calculateTfIdf();

		TopicAnalyser topicAnalyser = new TopicAnalyser(termStats);
		topicAnalyser.processTopics("topics.txt");
		
		System.out.println("\n Clustering with k=3");
		KmeansClustering clustering = new KmeansClustering(3, termStats.getAllWords(), filesArr, termStats.getAllTfIdf(), InitialCentroids.KMEANSPLUSPLUS, DistanceMeasure.COSINE);
		clustering.cluster();
		clustering.printClusters();

		int[] originalClusters = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2 };

		AccuracyMeasures accMeasures = new AccuracyMeasures(clustering, originalClusters);
		System.out.println("\nConfusion Matrix: ");
		for(int[] row: accMeasures.getConfusionMatrix()) {
			System.out.println(Arrays.toString(row));
		}
		System.out.println("FMeasure: " + accMeasures.getFMeasure());

		Matrix tfidf = termStats.getAllTfIdfAsJama();
		DimensionalityReducer dimReducer = new DimensionalityReducer(tfidf);

	
		double[][] svdConceptsArr = dimReducer.performSvd(2).getArray();
		Visualizer svdVisualizer = new Visualizer(svdConceptsArr, originalClusters,
				topicAnalyser.getTopTermsOfTopics(), "SVD Clustering - Original");
		Visualizer svdVisualizer2 = new Visualizer(svdConceptsArr, accMeasures.getRemappedClusters(),
				topicAnalyser.getTopTermsOfTopics(), "SVD Clustering - Predicted");

		double[][] pcaConceptsArr = dimReducer.performPca(2).getArray();
		Visualizer pcaVisualizer = new Visualizer(pcaConceptsArr, originalClusters,
				topicAnalyser.getTopTermsOfTopics(), "PCA Clustering - Original");
		Visualizer pcaVisualizer2 = new Visualizer(pcaConceptsArr, accMeasures.getRemappedClusters(),
				topicAnalyser.getTopTermsOfTopics(), "PCA Clustering - Predicted");
		
		

	}
}
