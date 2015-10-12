package com.sheremet.utils;


public class TesterTool {
	public static String[] test(String[] array){
		String errorlog="";
		if (array[3]==null||array[3].contains("Цена")||array[3].contains("Размер"))
			errorlog +="Missing name;";
		if (array[1]==null||(!array[1].startsWith("https://")&&!array[1].startsWith("http://"))||!array[1].endsWith("jpg"))
			errorlog +="Image link error;";
		if (array[7]==null||(array[7].trim().isEmpty()))
			errorlog +="Missing colour;";
		if (array[5]==null||(array[5].trim().isEmpty()))
			errorlog +="Missing ID;";
		if (array[4]==null||(array[4].trim().isEmpty())||!ConverterToCSV.isNumeric(array[4]))
			errorlog +="Missing price;";
		array[9]=errorlog;
		return array;
	}
}
