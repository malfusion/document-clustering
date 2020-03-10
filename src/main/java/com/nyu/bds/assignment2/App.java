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
		
		KmeansClustering clustering = new KmeansClustering(3, termStats.getAllWords(), filesArr, termStats.getAllTfIdf(), null);
		clustering.cluster();
		
		Matrix tfidf = termStats.getAllTfIdfAsJama();
		DimensionalityReducer dimReducer = new DimensionalityReducer(tfidf);
		
		double[][] svdConceptsArr = dimReducer.performSvd(2).getArray();		
		Visualizer svdVisualizer = new Visualizer(svdConceptsArr, new int[] {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2}, new String[] { topicAnalyser.getTopics()[0][0], topicAnalyser.getTopics()[1][0], topicAnalyser.getTopics()[2][0] } );
		
		double[][] pcaConceptsArr = dimReducer.performPca(2).getArray();
		Visualizer pcaVisualizer = new Visualizer(pcaConceptsArr, new int[] {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2}, new String[] { topicAnalyser.getTopics()[0][0], topicAnalyser.getTopics()[1][0], topicAnalyser.getTopics()[2][0] } );
		
		
		
			
    }
}
