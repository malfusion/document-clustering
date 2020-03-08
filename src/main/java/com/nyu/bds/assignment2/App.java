package com.nyu.bds.assignment2;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
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
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
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
		
		for(String folderPath: FileOperations.readFileAsLines("/Users/coderpc/Class/BDS/ass2/data.txt")) {
			for(String filePath : FileOperations.getFilesInFolder(folderPath)) {
				System.out.println(filePath);
				String content = FileOperations.readFile(filePath);
				files_words.put(filePath, preprocessor.process(content));
			}
		}
		
		
		
		
		TermDocumentStats termStats = new TermDocumentStats(files_words);
		termStats.process();
		termStats.calculateTfIdf();
		
		HashMap<String, double[]> a = termStats.getAllTfIdf();
		double [][] folderVector = new double[3][termStats.getAllWords().length];
		int[] fileCounts = new int[3];
		for (String file: a.keySet()) {
			int i=0;
			if(file.contains("C1")) {
				i=0;
				fileCounts[i] += 1;
			} else if (file.contains("C4")) {
				i=1;
				fileCounts[i] += 1;
			} else {
				i=2;
				fileCounts[i] += 1;
			}
			for (int j = 0; j < termStats.getAllWords().length; j++) {
				folderVector[i][j] += a.get(file)[j];
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < termStats.getAllWords().length; j++) {
				folderVector[i][j] = folderVector[i][j]/fileCounts[i];
			}
			int[] topWordsIndx = termStats.indexesOfTopElements(folderVector[i], 10);
			String[] topWords = new String[10];
			for (int j = 9; j >= 0; j--) {
				topWords[9-j] = termStats.getAllWords()[topWordsIndx[j]];
			}
			System.out.println("Top 10 in folder "+ (i+1) +":" +Arrays.toString(topWords));
			
		}
		
		
//		
		KmeansClustering clustering = new KmeansClustering(3, termStats.getAllWords(), termStats.getAllTfIdf(), folderVector);
		clustering.cluster();
//		
//		Matrix tfidf = termStats.getAllTfIdfAsJama();
//		SingularValueDecomposition svd = new SingularValueDecomposition(termStats.getAllTfIdfAsJama().transpose());
//		System.out.println(svd.getU().getRowDimension());
//		System.out.println(svd.getU().getColumnDimension());
//		System.out.println(svd.getS().getRowDimension());
//		System.out.println(svd.getS().getColumnDimension());
//		System.out.println(svd.getV().getRowDimension());
//		System.out.println(svd.getV().getColumnDimension());
//		System.out.println(Arrays.toString((new EigenvalueDecomposition(tfidf.times(tfidf.transpose()))).getRealEigenvalues()));
//		System.out.println(Arrays.toString(svd.getSingularValues()));
//		Matrix concepts = svd.getS().getMatrix(0, 1, 0, 23).times(svd.getV());
//		System.out.println(concepts.getRowDimension());
//		System.out.println(concepts.getColumnDimension());
//		
//		double[][] conceptsArr = concepts.getArray();
//		for (double[] arr: conceptsArr) {
//			System.out.println(Arrays.toString(arr));
//		}
//		
		final class LinePlotTest extends JFrame {
		    public LinePlotTest(double[][] concepts, String[] files) {
		        setDefaultCloseOperation(EXIT_ON_CLOSE);
		        setSize(600, 400);

		        DataTable data1 = new DataTable(Double.class, Double.class);
		        DataTable data2 = new DataTable(Double.class, Double.class);
		        DataTable data3 = new DataTable(Double.class, Double.class);
		        
				for (int i=0; i < concepts[0].length; i++) {
					if(files[i].contains("C1")) {
						data1.add(concepts[0][i], concepts[1][i]);	
					}
					else if(files[i].contains("C4")) {
						data2.add(concepts[0][i], concepts[1][i]);	
					}
					else {
						data3.add(concepts[0][i], concepts[1][i]);	
					}
					
				}
		        XYPlot plot = new XYPlot(data1, data2, data3);
		        getContentPane().add(new InteractivePanel(plot));
		        plot.getPointRenderers(data1).get(0).setColor(new Color(0.0f, 0.3f, 1.0f));
		        plot.getPointRenderers(data2).get(0).setColor(new Color(1.0f, 0.3f, 0.0f));
		        plot.getPointRenderers(data3).get(0).setColor(new Color(0.0f, 1.0f, 0.0f));
		        
		    }

		}
//		
//		
//		LinePlotTest frame = new LinePlotTest(conceptsArr, files_words.keySet().toArray(new String[24]));
//        frame.setVisible(true);
		
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
