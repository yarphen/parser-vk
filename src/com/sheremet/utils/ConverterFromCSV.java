package com.sheremet.utils;

import java.util.LinkedList;

public class ConverterFromCSV {

	public static String[] parseLine(String raw, char separator, char strDelimiter) {
		String [] arr = customSplit(raw,strDelimiter+"");
		LinkedList<String> list = new LinkedList<>();
		StringBuilder sb = new StringBuilder();
		boolean used = true;
		for(int i=0; i<arr.length; i++){
			if (i%2==0){
				if (arr[i].isEmpty()&&i!=arr.length-1&&i!=0){
					sb.append(strDelimiter);
					used=false;
				}else{
					if (i!=0){
						list.add(sb.toString());
						used = true;
						sb = new StringBuilder();
					}
					String[] arr2 = customSplit(arr[i],separator+"");
					int k1, k2;
					if (i==0)k1=0;else k1=1;
					if (i==arr.length-1)k2=0;else k2=1;
					for (int j=k1; j<arr2.length-k2; j++){
						list.add(arr2[j]);
						used = true;
					}
				}
			}else{
				sb.append(arr[i]);
				used=false;
			}
		}
		if (!used)list.add(sb.toString());
		String [] arr3 = new String [list.size()];
		int counter=0;
		for(String s:list){
			arr3[counter]=s;
			counter++;
		}
		return arr3;
	}

	private static String[] customSplit(String str, String splitter) {
		if (!str.contains(splitter))return new String[]{str};
		String temp = str;
		LinkedList<String> list = new LinkedList<>();
		do{
			String [] arr = my2split(temp, splitter);
			list.add(arr[0]);
			temp = arr[1];
		}while(temp.contains(splitter));
		list.add(temp);
		String [] arr = new String [list.size()];
		int counter=0;
		for(String s:list){
			arr[counter]=s;
			counter++;
		}
		return arr;
	}

	private static String[] my2split(String temp, String splitter) throws IllegalArgumentException {
		if (!temp.contains(splitter))throw new  IllegalArgumentException();
		else{
			String [] arr = new String [2];
			arr[0] = FragmentingTools.findBefore(temp, splitter);
			arr[1] = FragmentingTools.findAfter(temp, splitter);
			return arr;
		}
			
	}
}
