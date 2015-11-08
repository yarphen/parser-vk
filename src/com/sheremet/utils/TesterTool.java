package com.sheremet.utils;

import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;


public class TesterTool {

	public static String[] test(String[] array){
		String errorlog="";
		if (array[3]==null||array[3].contains("÷ена")||array[3].contains("–азмер"))
			errorlog +="Missing name;";
		if (array[1]==null||(!array[1].startsWith("https://")&&!array[1].startsWith("http://"))||!array[1].endsWith("jpg"))
			errorlog +="Image link error;";
		if (array[7]==null||(array[7].trim().isEmpty()))
			errorlog +="Missing colour;";
		if (array[5]==null||(array[5].trim().isEmpty()))
			errorlog +="Missing ID;";
		if (array[4]==null||(array[4].trim().isEmpty())||!ConverterToCSV.isNumeric(array[4]))
			errorlog +="Missing price;";
		if (array[6]==null||testSizeFormat(array[6]))
			errorlog +="Missing size;";
		array[9]=errorlog;
		return array;
	}

	private static boolean testSizeFormat(String string) {
		try{
	
			String[]  arr = null;
			boolean t1 = string.contains("-");
			boolean t2 = string.contains("Ч");
			if (t1||t2){
				if (t1){
					arr = string.split("-", 2);
				}else if (t2){
					arr = string.split("Ч", 2);
				}
				if (!testSize(arr[0]))return false;
				if (!testSize(arr[1]))return false;
				if (ConverterToCSV.isNumeric(arr[0])&&!ConverterToCSV.isNumeric(arr[1]))return false;
				if (!ConverterToCSV.isNumeric(arr[0])&&ConverterToCSV.isNumeric(arr[1]))return false;
				if (ConverterToCSV.isNumeric(arr[0])&&ConverterToCSV.isNumeric(arr[1])){
					if (Integer.parseInt(arr[0])>Integer.parseInt(arr[1]))return false;
				}
				if (!ConverterToCSV.isNumeric(arr[0])&&!ConverterToCSV.isNumeric(arr[1])){
					if (findIndex(arr[0])>findIndex(arr[1]))return false;
				}
			}else{
				StringTokenizer t = new StringTokenizer(string);
				while (t.hasMoreTokens()) {
					if (!testSize(t.nextToken()))return false;
				}
			}
		}catch(Throwable t){
			return false;
		}
		return true;
	}

	public static boolean testSize(String s) {
	
		if (ConverterToCSV.isNumeric(s))return true;
		else{
			for(String size: ParserTool.SIZE){
				if (s.equals(size))return true;
			}
			return false;
		}
	
	}

	private static int findIndex(String string) {
		int i=0;
		for(String s:ParserTool.SIZE){
			if (s.equals(string))return i;
			i++;
		}
		throw new IllegalArgumentException();
	}

	public static String test2(List<String[]> list2, String[] merged) {
		HashSet<String> set = new HashSet<String>();
		for (String[] strs:list2){
			for (int i=0; i<strs.length; i++){
				if (strs[i]!=null&&!strs[i].equals(merged[i])){
					switch (i) {
					case 0:set.add("Different IDs");break;
					case 1:set.add("Different colours");break;
					case 2:set.add("Different names");break;
					case 3:set.add("Different sizes");break;
					case 4:set.add("Different prices");break;
					case 5:set.add("Different descriptions");break;
					}
				}
			}
		}
		if (merged[2].trim().isEmpty()){
			set.add("Missing name");
		}
		if (merged[4].trim().isEmpty()){
			set.add("Missing price");
		}else
			if (!ConverterToCSV.isNumeric(merged[4].trim())){
				set.add("Non numeric price");
			}
		if (merged[5].trim().isEmpty()){
			set.add("Missing description");
		}
		return Tools.listWithSeparators(set, ';');
	}
}
