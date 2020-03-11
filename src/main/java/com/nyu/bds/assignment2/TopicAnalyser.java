package com.nyu.bds.assignment2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TopicAnalyser {
	
	TermDocumentStats termStats;
	String[][] topics = new String[3][10];
	
	public TopicAnalyser(TermDocumentStats termStats) {
		this.termStats = termStats;
	}
	
	public void processTopics(String outputFile) {
		System.out.println("\n Processing Topics");
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
		
		
		try {
	      File myObj = new File(outputFile);
	      if (myObj.exists()) {
	    	  myObj.delete();
	      }
	      myObj.createNewFile();
	      System.out.println("Topic file created: " + myObj.getName());
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		
		
		try {
			FileWriter myWriter = new FileWriter("topics.txt");
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < termStats.getAllWords().length; j++) {
					folderVector[i][j] = folderVector[i][j]/fileCounts[i];
				}
				int[] topWordsIndx = termStats.indexesOfTopElements(folderVector[i], 10);
				String[] topWords = new String[10];
				for (int j = 9; j >= 0; j--) {
					topWords[9-j] = termStats.getAllWords()[topWordsIndx[j]];
				}
				this.topics[i] = topWords.clone();
				myWriter.write("Top 10 in folder "+ (i+1) +" : " + Arrays.toString(topWords) + "\n");
				System.out.println("Top 10 in folder "+ (i+1) +":" +Arrays.toString(topWords));
			}
			myWriter.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[][] getTopics(){
		return this.topics;
	}
	
	public String[] getTopTermsOfTopics() {
		ArrayList<String> topTerms = new ArrayList<String>();
		for (String[] topic: topics) {
			topTerms.add(topic[0]);
		}
		return topTerms.toArray(new String[topics.length]);
	}
}
