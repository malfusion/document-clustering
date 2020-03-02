package com.nyu.bds.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
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
		
		for(String folderPath: FileOperations.readFileAsLines("/Users/coderpc/Class/BDS/ass2/data.txt")) {
			for(String filePath : FileOperations.getFilesInFolder(folderPath)) {
				String content = FileOperations.readFile(filePath);
				System.out.println(filePath);
				System.out.println(preprocessor.process(content).size());
			}
		}

    }
}
