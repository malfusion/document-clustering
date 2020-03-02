package com.nyu.bds.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileOperations
{
	public static String readFile(String filepath) {
		String res = "";
		File file = new File(filepath); 
	    Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				res += sc.nextLine();
		    }
			return res;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
