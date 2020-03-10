package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Jama.Matrix;



public class TermDocumentStats {
	
	HashMap<String, int[]> wordFreqByFile = new HashMap<String, int[]>();
	HashMap<String, double[]> wordTfidfByFile = new HashMap<String, double[]>();
	HashMap<String, List<String>> wordListByFile = new HashMap<String, List<String>>();
	HashMap<String, Integer> wordIdLookup = new HashMap<String, Integer>();
	
	String[] allWords;
	String[] allFiles;
	int[] globalWordOccurence;
	
	public TermDocumentStats(HashMap<String, List<String>> files_words, List<String> files) {
		wordListByFile = files_words;
		allFiles = new String[files.size()];
		allFiles = files.toArray(allFiles);
		generateWordLookupTable();
		globalWordOccurence = new int[allWords.length];
	}
	
	
	public Integer lookupWordId(String word) {
		return wordIdLookup.get(word);
	}
	
	public void generateWordLookupTable() {
		Integer ctr = 0;
		List<String> words = new ArrayList<String>();
		for (String filePath: allFiles) {
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
		int[] wordCounts = new int[allWords.length];
		for(String word: wordListByFile.get(filePath)) {
			wordCounts[lookupWordId(word)] += 1;
		}
		return wordCounts;
	}
	
	public void updateGlobalWordOccurence(int[] wordFreq) {
		for (int i = 0; i < allWords.length; i++) {
			if(wordFreq[i] > 0) {
				globalWordOccurence[i] += 1;
			}
		}
	}
	
	
	public void process() {
		for(String filePath : allFiles) {
			// Get Word Frequencies for file
			int[] wordFreq= getWordFreqForFile(filePath);
			// Add to file-word frequency map
			wordFreqByFile.put(filePath, wordFreq);
			// Updating the presence count for global word-document statistics
			updateGlobalWordOccurence(wordFreq);
		}
		
		
		
	}
	
	public void calculateTfIdf() {
		for(String filePath : allFiles) {
			double[] wordTfIdf= new double[allWords.length];
			int numWordsInFile = 0;
			/// Count total words in file
		    for (int count : wordFreqByFile.get(filePath)) {
		        numWordsInFile += count;
		    }
			for (int i = 0; i < allWords.length; i++) {
				double res = Math.log(allFiles.length/globalWordOccurence[i]);
				res *= (wordFreqByFile.get(filePath)[i]*1.0) / numWordsInFile;
//				 Remove all the super low frequency word
//				if(wordFreqByFile.get(filePath)[i] < 1){
////					res = 0.0;
//				}
				wordTfIdf[i] = res;	
			}
			wordTfidfByFile.put(filePath, wordTfIdf);
		}
	}
	
	public int[] indexesOfTopElements(double[] orig, int nummax) {
		double[] copy = Arrays.copyOf(orig,orig.length);
        Arrays.sort(copy);
        double[] honey = Arrays.copyOfRange(copy,copy.length - nummax, copy.length);
        Set<Integer> result = new HashSet<Integer>();
        int[] arr = new int[nummax];
        int resultPos = 0;
        for(int i = 0; i < honey.length; i++) {
        	for(int j = 0; j < orig.length; j++) {
        		if(orig[j] == honey[i] && !result.contains(j)) {
        			result.add(j);
        			arr[resultPos++] = j;
        			break;
        		}
        	}
        }
        
        return arr;
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
	
	public double getWordFreq(String filePath, String word) {
		if(!wordFreqByFile.containsKey(filePath)) {
			return 0.0;
		}
		if(lookupWordId(word) == null) {
			return 0.0;
		}
		return wordFreqByFile.get(filePath)[lookupWordId(word)]*1.0;
	}
	
	
	
	public HashMap<String, double[]> getAllTfIdf(){
		return wordTfidfByFile;
	}
	
	public Matrix getAllTfIdfAsJama(){
		double[][] res = new double[allFiles.length][allWords.length];
		int i = 0;
		for (String filePath : allFiles) {
			res[i++] = wordTfidfByFile.get(filePath);
		}
		return new Matrix(res);
	}
		
}
