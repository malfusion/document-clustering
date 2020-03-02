package com.nyu.bds.assignment2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
	
	public static List<String> getNGrams(List<String> words, List<Integer> sizes){
		ArrayDeque<String> deque = new ArrayDeque<String>();
		ArrayList<String> ngrams = new ArrayList<String>();
		
		Integer maxSize = Collections.max(sizes);
		
		for (String word: words) {
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
		
		return ngrams;
	}
	
    public static void main( String[] args )
    {
    	 String content = FileOperations.readFile("/Users/coderpc/Class/BDS/ass2/dataset_3/data/C1/article01.txt");
    	 Properties props = new Properties();
    	 props.setProperty("annotators", "tokenize,ssplit,stopword,pos,lemma,ner,stopword");
         props.setProperty("customAnnotatorClass.stopword", "intoxicant.analytics.coreNlp.StopwordAnnotator");
         props.setProperty("ner.useSUTime", "false");

    	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    // create a document object
        Annotation document = new Annotation(content);
	    
	    // annotate the document
	    pipeline.annotate(document);
	    System.out.println(document.keySet());
	    
	    ArrayList<String> words = new ArrayList<String>();
	    // TODO: Must add NER together here 	    
	    for (CoreLabel label: document.get(CoreAnnotations.TokensAnnotation.class)) {
//	    	System.out.println(label.lemma()+ ' ' +label.ner());
	    	words.add(label.lemma());
	    }

//    	ArrayList<String> words = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6"));
	    
    	ArrayList<Integer> sizes= new ArrayList<Integer>(Arrays.asList(1, 2, 3));
    	System.out.println(App.getNGrams(words, sizes));
	    
    }
}
