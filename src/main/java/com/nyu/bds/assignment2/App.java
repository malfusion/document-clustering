package com.nyu.bds.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
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

import com.nyu.bds.assignment2.FileOperations;

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
//		
//		HashMap<String, HashMap<String, List<String>>> folder_files_words = new HashMap<String, HashMap<String, List<String>>>();
//		
//		for(String folderPath: FileOperations.readFileAsLines("/Users/coderpc/Class/BDS/ass2/data.txt")) {
//			folder_files_words.put(folderPath, new HashMap<String, List<String>>());
//			for(String filePath : FileOperations.getFilesInFolder(folderPath)) {
//				HashMap<String, List<String>> file_words = folder_files_words.get(folderPath);
//				file_words.put(filePath, new ArrayList<String>());
//
//				System.out.println(filePath);
//				String content = FileOperations.readFile(filePath);
//
//				file_words.put(filePath, preprocessor.process(content));
//			}
//		}
//		TermDocumentStats termStats = new TermDocumentStats(folder_files_words);
//		termStats.process();
//		
//		KmeansClustering clustering = new KmeansClustering(3, termStats.getAllWords(), termStats.getAllTfIdf());
//		clustering.cluster();
    	
    	System.out.println(preprocessor.process("Hello the i am is a random stranger"));
    	
//    	ArrayList<String> words = new ArrayList<String>();
//    	words.add("Hello");
//    	words.add("Welcome");
//    	words.add("Bye");
//    	String[] wordsstrs = new String[3];
//    	HashMap<String, HashMap<String, HashMap<String, Double> >> folders_files_words_tfidf = new HashMap<String, HashMap<String, HashMap<String, Double>>>();
//    	
//    	HashMap<String, Double> words1 = new HashMap<String, Double>();
//    	words1.put("Hello", 2.0);
//    	words1.put("Welcome", 2.1);
//    	HashMap<String, HashMap<String, Double>> files_words1 = new HashMap<String, HashMap<String,Double>>();    	
//    	files_words1.put("filea", words1);
//    	folders_files_words_tfidf.put("a", files_words1);
//    	
//    	HashMap<String, Double> words2 = new HashMap<String, Double>();
//    	words2.put("Bye", 2.0);
//    	HashMap<String, HashMap<String, Double>> files_words2 = new HashMap<String, HashMap<String,Double>>();    	
//    	files_words2.put("fileb", words2);
//    	folders_files_words_tfidf.put("b", files_words2);
//		KmeansClustering clustering = new KmeansClustering(2, words.toArray(wordsstrs), folders_files_words_tfidf);
//		clustering.cluster();
			
    }
}
