package com.sheremet.utils;
import java.util.List;


public class ConverterToCSV {
	public static String convert(List<String[]>list, char separator, char strDelimiter) {
		StringBuilder builder = new StringBuilder();
		boolean f1 = true;
		for(String[] item:list){
			if (!f1){
				builder.append("\n");
			}else
				f1=false;
			builder.append(parseLine(item, separator, strDelimiter));
		}
		return builder.toString();
	}
	private static String format(String s, char delimiter) {
		s = s.replace(delimiter+"", delimiter+""+delimiter);
		s = s.replace('\n', ' ');
		s = s.replace('\n', ' ');
		return s = s.replace('\r', ' ');
	}
	public static boolean isNumeric(String s)   {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	public static String parseLine(String[] arr, char separator, char strDelimiter)   {
		StringBuilder builder = new StringBuilder();
		boolean f2 = true;
		for (String s:arr){
			if (!f2){
				builder.append(separator);
			}else
				f2=false;
			if (s==null)
				continue;
			if (!isNumeric(s)){
				builder.append(strDelimiter);
				builder.append(format(s, strDelimiter));
				builder.append(strDelimiter);
			}else{
				builder.append(format(s, strDelimiter));
			}
		}
		return builder.toString();
	}
	
}
