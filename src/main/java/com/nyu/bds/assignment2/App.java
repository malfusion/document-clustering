package com.nyu.bds.assignment2;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JFrame;

import com.nyu.bds.assignment2.FileOperations;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.legends.ValueLegend;
import de.erichseifert.gral.ui.InteractivePanel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import intoxicant.analytics.coreNlp.StopwordAnnotator;
/**
 * Hello world!
 *
 */
public class App 
{
	
	
	
    public static void main( String[] args ) {
    	    	
		TextPreprocessor preprocessor = new TextPreprocessor();		
		
		HashMap<String, HashMap<String, List<String>>> folder_files_words = new HashMap<String, HashMap<String, List<String>>>();
		HashMap<String, List<String>> files_words = new HashMap<String, List<String>>();
		
		List<String> files = new ArrayList<String>();
		
		for(String folderPath: FileOperations.readFileAsLines("data.txt")) {
			for(String filePath : FileOperations.getFilesInFolder(folderPath)) {
//				System.out.println(filePath);
				String content = FileOperations.readFile(filePath);
				files_words.put(filePath, preprocessor.process(content));
				files.add(filePath);
			}
		}
		
		
		TermDocumentStats termStats = new TermDocumentStats(files_words, files);
		termStats.process();
		termStats.calculateTfIdf();
		
		TopicAnalyser topicAnalyser = new TopicAnalyser(termStats);
		topicAnalyser.processTopics("topics.txt");
		
		
		KmeansClustering clustering = new KmeansClustering(3, termStats.getAllWords(), termStats.getAllTfIdf(), null);
		clustering.cluster();
//		
		Matrix tfidf = termStats.getAllTfIdfAsJama();
		DimensionalityReducer dimReducer = new DimensionalityReducer(tfidf);
		
		double[][] svdConceptsArr = dimReducer.performSvd(2).getArray();		
		Visualizer svdVisualizer = new Visualizer(svdConceptsArr, new int[] {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2}, new String[] { topicAnalyser.getTopics()[0][0], topicAnalyser.getTopics()[1][0], topicAnalyser.getTopics()[2][0] } );
		
		double[][] pcaConceptsArr = dimReducer.performPca(2).getArray();
		Visualizer pcaVisualizer = new Visualizer(pcaConceptsArr, new int[] {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2}, new String[] { topicAnalyser.getTopics()[0][0], topicAnalyser.getTopics()[1][0], topicAnalyser.getTopics()[2][0] } );
		
//		for (double[] arr: conceptsArr) {
//			System.out.println(Arrays.toString(arr));
//		}
////		
		
		
//		System.out.println((new SingularValueDecomposition(termStats.getAllTfIdfAsJama())).rank());
    	
//    	System.out.println(preprocessor.process("Hello the i am is a random stranger"));
    	
//    	ArrayList<String> words = new ArrayList<String>();
//    	words.add("Hello");
//    	words.add("Welcome");
//    	words.add("Bye");
//    	String[] wordsstrs = new String[3];
//    	HashMap<String, double[]> files_words_tfidf = new HashMap<String, double[]>();
//    	
//    	double[] words1 = new double[3];
//    	words1[0] = 2.0;
//    	words1[1] = 2.1;
//    	words1[2] = 0.0;
//    	files_words_tfidf.put("a", words1);
//    	
//    	double[] words2 = new double[3];
//    	words2[0] = 0.0;
//    	words2[1] = 0.0;
//    	words2[2] = 1.0;
//    	files_words_tfidf.put("b", words2);
//    	
//    	double[] words3 = new double[3];
//    	words3[0] = 2.0;
//    	words3[1] = 2.1;
//    	words3[2] = 0.1;
//    	files_words_tfidf.put("c", words3);
//		KmeansClustering clustering2 = new KmeansClustering(2, words.toArray(wordsstrs), files_words_tfidf);
//		clustering2.cluster();
			
    }
}
