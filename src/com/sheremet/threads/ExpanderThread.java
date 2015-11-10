package com.sheremet.threads;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.sheremet.utils.ConverterFromCSV;
import com.sheremet.utils.ConverterToCSV;

public class ExpanderThread extends Thread{
	private File []mergeFiles; 
	private File []sizeFiles; 
	private int filecount;
	private Scanner sc;
	private PrintStream curFilePS;
	private ActionListener onFinish;
	public ExpanderThread(String[] split, ActionListener actionListener) {
		filecount=split.length;
		mergeFiles=new File[filecount];
		sizeFiles=new File[filecount];
		for(int i=0; i<filecount; i++){
			String[] arr = split[i].split(Pattern.quote("|"));
			mergeFiles[i]=new File(arr[0]);
			sizeFiles[i]=new File(arr[1]);
		}
		onFinish = actionListener;
	}
	public String[] createRow(String article, String sub_article, String name, String description, String price, String img_url, int custom_1, int custom_2, String custom_3){
		String [] arr = new String[11];
		arr[0] = "1";
		arr[1] = article;
		arr[2] = sub_article;
		arr[3] = "";
		arr[4] = name;
		arr[5] = description;
		arr[6] = price;
		arr[7] = img_url;
		switch(custom_1){
		case 0: arr[8] = "Выберите цвет и размер"; break;
		case 1: arr[8] = ""; break;
		case 2: arr[8] = "Таблица размеров"; break;		
		}
		switch(custom_2){
		case 0: arr[9] = "stockable"; break;
		case 1: arr[9] = "<p><a href=\"images/table-size/razmeri_obuvi_34-43.jpg\" target=\"_blank\" class=\"jcepopup\">Таблица размеров</a></p>"; break;		
		}
		arr[10] = custom_3;
		return arr;
	}
	public List<String[]> createRows(HashMap<String, List<String>>sizeColourMap, HashMap<String, String>pictureColourMap, String article, String name, String description, String price){
		List<String[]> list = new LinkedList<String[]>();
		List<String[]> tempList = sizeMapToList(article, sizeColourMap);
		String [] arr1 = createRow(article, "", name, description, price, asImagesListString(pictureColourMap), 0, 0, asSizeMapString(tempList));
		list.add(arr1);
		for (String [] row : tempList){
			arr1 = createRow(row[0], article, name, description, "0", pictureColourMap.get(row[1].split("#", 2)[0]), 1, 0, "");
			list.add(arr1);
		}
		arr1 = createRow(article, "", name, description, "", "", 2, 1, "");
		list.add(arr1);
		return list;

	}
	private String asImagesListString(HashMap<String, String> pictureColourMap) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(String temp:pictureColourMap.keySet())
			if (!first)
				sb.append("|"+pictureColourMap.get(temp));
			else{
				sb.append(pictureColourMap.get(temp));
				first=false;
			}
		return sb.toString();
	}
	private String asSizeMapString(List<String[]> tempList) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(String[] strs: tempList){
			if (!first){
				builder.append(";");
			}else{
				first = false;
			}
			builder.append(strs[0]+"["+strs[1]+"[");
		}
		return builder.toString();
	}
	private List<String[]> sizeMapToList(String article,
			HashMap<String, List<String>> sizeColourMap) {
		List<String[]> list = new LinkedList<>();
		for(String colour: sizeColourMap.keySet()){
			for(String size: sizeColourMap.get(colour)){
				String [] arr = new String[2];
				arr[0]=article+"-v"+list.size();
				arr[1]=colour+"#"+size;
				list.add(arr);
			}
		}
		return list;
	}
	@Override
	public void run(){
		File csvdir = new File("expand");
		if (!csvdir.exists())csvdir.mkdirs();
		for (int i=0; i<filecount; i++){
			HashMap<String, HashMap<String, List<String>>> idSizeColourMap = readSizes(sizeFiles[i]);
			String albName = mergeFiles[i].getName().split("-merge")[0];
			HashMap<String, HashMap<String,String[]>> mergeMap = readMerges(mergeFiles[i]);
			writeToFile(albName, idSizeColourMap, mergeMap);
		}
		System.out.println("Program finished.");
		if (onFinish!=null)
			onFinish.actionPerformed(null);
	}
	private void writeToFile(String albName,
			HashMap<String, HashMap<String, List<String>>> idSizeColourMap,
			HashMap<String, HashMap<String, String[]>> mergeMap) {
		try {
			curFilePS = new PrintStream(new File("expand/"+albName+"-expand.csv"), "utf-8");
			System.out.println("file "+albName+"-expand.csv opened!");
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			System.out.println("cannot open expand/"+albName+"-expand.csv!");
		}
		List<String[]>csvlist = new LinkedList<String[]>();
		for(String id: mergeMap.keySet()){
			HashMap<String, List<String>> sizeColourMap = idSizeColourMap.get(id);
			HashMap<String, String[]> rowsMap = mergeMap.get(id);
			HashMap<String, String>pictureColourMap = pictureMap(rowsMap);
			String[] row =  rowsMap.get(rowsMap.keySet().iterator().next());
			String name = row[2];
			String description = row[5];
			String price = row[4];
			csvlist.addAll(createRows(sizeColourMap, pictureColourMap, id, name, description, price));
		}
		for(String[] strs: csvlist){
			curFilePS.println(ConverterToCSV.unparseLine(strs, '^', '\''));
		}
		curFilePS.close();
		System.out.println("file "+albName+"-expand.csv successfully written!");
	}
	private HashMap<String, String> pictureMap(
			HashMap<String, String[]> rowsMap) {
		HashMap<String, String> map = new HashMap<>();
		for(String str:rowsMap.keySet()){
			map.put(str, rowsMap.get(str)[6]);
		}
		return map;
	}
	private HashMap<String, HashMap<String, String[]>> readMerges(File file) {
		HashMap<String, HashMap<String, String[]>> map = new HashMap<String, HashMap<String, String[]>>();
		try {
			sc = new Scanner(file, "utf-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNextLine()) {
			String raw =  sc.nextLine();
			String[] row = ConverterFromCSV.parseLine(raw, ';', '"');
			if (!map.containsKey(row[0])){
				map.put(row[0], new HashMap<String, String[]>());
			}
			map.get(row[0]).put(row[1], row);
		}
		sc.close();
		return map;
	}
	private HashMap<String, HashMap<String, List<String>>> readSizes(File file) {
		HashMap<String, HashMap<String, List<String>>> map = new HashMap<String, HashMap<String, List<String>>>();
		try {
			sc = new Scanner(file, "utf-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNextLine()) {
			String raw =  sc.nextLine();
			String[] row = ConverterFromCSV.parseLine(raw, ';', '"');
			if (!map.containsKey(row[0])){
				map.put(row[0], new HashMap<String, List<String>>());
			}
			HashMap<String, List<String>> map1 = map.get(row[0]);
			if (!map1.containsKey(row[1])){
				map1.put(row[1], new LinkedList<String>());
			}
			for(int i=4; i<row.length; i++){
				map1.get(row[1]).add(row[i]);
			}
		}
		sc.close();
		return map;
	}
	public Scanner getScanner() {
		return sc;
	}
	public PrintStream getCurFilePS() {
		return curFilePS;
	}

}
