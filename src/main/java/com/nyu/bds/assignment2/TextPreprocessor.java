package com.nyu.bds.assignment2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import intoxicant.analytics.coreNlp.StopwordAnnotator;

public class TextPreprocessor {
	private StanfordCoreNLP pipeline;
	
	public TextPreprocessor() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,stopword,pos,lemma");//,ner");
		props.setProperty("customAnnotatorClass.stopword", "intoxicant.analytics.coreNlp.StopwordAnnotator");
		props.setProperty("ner.useSUTime", "false");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	public List<String> process(String content) {
		ArrayList<String> res = new ArrayList<String>();
		
		Annotation document = new Annotation(content);
		pipeline.annotate(document);
		
		
		List<CoreLabel> labels = document.get(CoreAnnotations.TokensAnnotation.class);
		res.addAll(getNamedEntities(labels));
		
		ArrayList<Integer> sizes = new ArrayList<Integer>(Arrays.asList(1, 2));
		res.addAll(getNGrams(labels, sizes));
		
		
		return res;
	}
	
	private List<String> getNamedEntities(List<CoreLabel> labels){
		return new ArrayList<String>();
//		System.out.println(label.lemma()+ ' ' +label.ner());
	}
	
	private List<String> getNGrams(List<CoreLabel> labels, List<Integer> sizes){
		ArrayDeque<String> deque = new ArrayDeque<String>();
		ArrayList<String> ngrams = new ArrayList<String>();
		
		Integer maxSize = Collections.max(sizes);
		
		for (CoreLabel label: labels) {
			String word = label.lemma(); 
			if (word != null && !label.get(StopwordAnnotator.class).first()) {
				word = word.replaceAll("\\p{Punct}","");
				if(word.trim().length() == 0) {
					continue;
				}
				if(deque.size() == maxSize) {
					deque.removeFirst();
				}
				deque.addLast(word);
				Integer currn = 0;
				StringBuilder sb = new StringBuilder();
				Iterator<String> iterator = deque.descendingIterator();
				while (iterator.hasNext()) {
					String deqWord = iterator.next();
					
					if(sb.length() > 0) {
						sb.insert(0, deqWord + '_');
					}else {
						sb.insert(0, deqWord);
					}
					
					currn += 1;
					if(sizes.contains(currn)) {
						ngrams.add(sb.toString());
					}
				}
			}
		}
		return ngrams;
	}
}