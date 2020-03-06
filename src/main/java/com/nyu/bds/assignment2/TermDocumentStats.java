package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TermDocumentStats {
	
	HashMap<String, int[]> wordFreqByFile = new HashMap<String, int[]>();
	HashMap<String, double[]> wordTfidfByFile = new HashMap<String, double[]>();
	HashMap<String, List<String>> wordListByFile = new HashMap<String, List<String>>();
	
	HashMap<String, Integer> wordIdLookup = new HashMap<String, Integer>();
	
	String[] allWords;
	int[] globalWordOccurence;
	Integer totalDocs = 0;
	Integer totalUniqueWords = 0;
	
	public TermDocumentStats(HashMap<String, List<String>> files_words) {
		wordListByFile = files_words;
		generateWordLookupTable();
		totalUniqueWords = wordIdLookup.size();
		totalDocs = files_words.keySet().size();
		globalWordOccurence = new int[totalUniqueWords];
	}
	
	
	public Integer lookupWordId(String word) {
		return wordIdLookup.get(word);
	}
	
	public void generateWordLookupTable() {
		Integer ctr = 0;
		List<String> words = new ArrayList<String>();
		for (String filePath: wordListByFile.keySet()) {
			for (String word: wordListByFile.get(filePath)) {
				if(!wordIdLookup.containsKey(word)) {
					wordIdLookup.put(word, ctr);
					words.add(word);
					ctr += 1;
				}
			}
		}
		allWords = new String[ctr];
		allWords = words.toArray(allWords);
	}
	
	public int[] getWordFreqForFile(String filePath) {
		int[] wordCounts = new int[totalUniqueWords];
		for(String word: wordListByFile.get(filePath)) {
			wordCounts[lookupWordId(word)] += 1;
		}
		return wordCounts;
	}
	
	public void updateGlobalWordOccurence(int[] wordFreq) {
		for (int i = 0; i < totalUniqueWords; i++) {
			if(wordFreq[i] > 0) {
				globalWordOccurence[i] += 1;
			}
		}
	}
	
	
	public void process() {
		for(String filePath : wordListByFile.keySet()) {
			// Get Word Frequencies for file
			int[] wordFreq= getWordFreqForFile(filePath);
			// Add to file-word frequency map
			wordFreqByFile.put(filePath, wordFreq);
			// Updating the presence count for global word-document statistics
			updateGlobalWordOccurence(wordFreq);
		}
		
		
		
	}
	
	public void calculateTfIdf() {
		for(String filePath : wordListByFile.keySet()) {
			double[] wordTfIdf= new double[totalUniqueWords];
			int numWordsInFile = 0;
			/// Count total words in file
		    for (int count : wordFreqByFile.get(filePath)) {
		        numWordsInFile += count;
		    }
			for (int i = 0; i < totalUniqueWords; i++) {
				double res = Math.log(totalDocs/globalWordOccurence[i]);
				res *= (wordFreqByFile.get(filePath)[i]*1.0) / numWordsInFile; 	
				wordTfIdf[i] = res;
			}
			wordTfidfByFile.put(filePath, wordTfIdf);
			
		}
	}
	
	public String[] getAllWords(){
		return allWords;
	}

	public double getTfIdf(String filePath, String word) {
		if(!wordTfidfByFile.containsKey(filePath)) {
			return 0.0;
		}
		if(lookupWordId(word) == null) {
			return 0.0;
		}
		return wordTfidfByFile.get(filePath)[lookupWordId(word)];
	}
	
	public HashMap<String, double[]> getAllTfIdf(){
		return wordTfidfByFile;
	}
	
	
	
	
	
	
}
