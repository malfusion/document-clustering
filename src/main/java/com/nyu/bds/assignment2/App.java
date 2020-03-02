package com.nyu.bds.assignment2;

import java.util.Properties;

import com.nyu.bds.assignment2.FileOperations;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import intoxicant.analytics.coreNlp.StopwordAnnotator;
/**
 * Hello world!
 *
 */
public class App 
{
	
	
    public static void main( String[] args )
    {
    	 String content = FileOperations.readFile("/Users/coderpc/Class/BDS/ass2/dataset_3/data/C1/article01.txt");
    	 Properties props = new Properties();
    	 props.setProperty("annotators", "tokenize,ssplit,stopword,pos,lemma,ner");
         props.setProperty("customAnnotatorClass.stopword", "intoxicant.analytics.coreNlp.StopwordAnnotator");
         props.setProperty("ner.useSUTime", "false");

    	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    // create a document object
        Annotation document = new Annotation(content);
	    
	    System.out.println("ASasdasd");
	    // annotate the document
	    pipeline.annotate(document);
	    
	    for (CoreLabel label: document.get(CoreAnnotations.TokensAnnotation.class)) {
	    	System.out.println(label);
	    }
	    
    		       
	     
    }
}
