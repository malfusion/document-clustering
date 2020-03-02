package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TermDocumentStats {
	
	HashMap<String, List<String>> folder_files = new HashMap<String, List<String>>();
	HashMap<String, HashMap<String, Integer>> files_words = new HashMap<String, HashMap<String, Integer>>();
	HashMap<String, Integer> word_present_in_files = new HashMap<String, Integer>();
	
	HashMap<String, HashMap<String, HashMap<String, Integer> >> folders_files_words_tfidf = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
	
	HashMap<String, HashMap<String, List<String>>> folders_files_words = new HashMap<String, HashMap<String, List<String>>>();
	
	Integer totalDocs = 0;
	
	public TermDocumentStats(HashMap<String, HashMap<String, List<String>>> folders_files_words) {
		this.folders_files_words = folders_files_words;
	}
	
	
	public void process() {
		for(String folderPath: folders_files_words.keySet()) {
			folder_files.put(folderPath, new ArrayList<String>());
			for(String filePath : folders_files_words.get(folderPath).keySet()) {
				folder_files.get(folderPath).add(filePath);
				files_words.put(filePath, new HashMap<String, Integer>());
				HashMap<String, Integer> current_file_words_map = files_words.get(filePath);

				String content = FileOperations.readFile(filePath);
				System.out.println(filePath);
				
				List<String> words = folders_files_words.get(folderPath).get(filePath);
				
				for(String word: words) {
					if (!current_file_words_map.containsKey(word)) {
						current_file_words_map.put(word, 0);
					}
					current_file_words_map.put(word, files_words.get(filePath).get(word) + 1);
				}
				
				// Updating the presence count for global word-document statistics
				for (String wordInFile: current_file_words_map.keySet()) {
					if (!word_present_in_files.containsKey(wordInFile)) {
						word_present_in_files.put(wordInFile, 0);
					}
					word_present_in_files.put(wordInFile, word_present_in_files.get(wordInFile)+1);
				}
				totalDocs += 1;
			}
		}
		
		for(String folderPath: folders_files_words.keySet()) {
			folders_files_words_tfidf.put(folderPath, new HashMap<String, HashMap<String, Integer>>());
			HashMap<String, HashMap<String, Integer>> files_words_tfidf = folders_files_words_tfidf.get(folderPath);
			for(String filePath : folders_files_words.get(folderPath).keySet()) {
				files_words_tfidf.put(filePath, new HashMap<String, Integer>());
				HashMap<String, Integer> words_tfidf = files_words_tfidf.get(filePath);
				
				for(String word: folders_files_words.get(folderPath).get(filePath)) {
					Double res = Math.log(totalDocs/word_present_in_files.get(word));
					res *= (1 + Math.log(files_words.get(filePath).get(word)));
				}
			}
		}
	}
	
	
	
}
