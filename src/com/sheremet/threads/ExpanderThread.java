package com.sheremet.threads;

import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ExpanderThread extends Thread{

	public ExpanderThread(String[] split, ActionListener actionListener) {
		// TODO Auto-generated constructor stub
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
	public List<String[]> createRows(HashMap<String, List<String>>sizeColourMap, HashMap<String, List<String>>pictureColourMap, String article, String name, String description, String price){
		List<String[]> list = new LinkedList<String[]>();
		List<String[]> tempList = sizeMapToList(article, sizeColourMap);
		String [] arr1 = createRow(article, "", name, description, price, asImagesListString(pictureColourMap), 0, 0, asSizeMapString(tempList));
		list.add(arr1);
		for (String [] row : tempList){
			arr1 = createRow(row[0], article, name, description, "0", asImagesListString(pictureColourMap.get(row[1].split("#", 2)[0])), 1, 0, "");
			list.add(arr1);
		}
		arr1 = createRow(article, "", name, description, "", "", 2, 1, "");
		list.add(arr1);
		return list;
		
	}
	private String asImagesListString(List<String> tempList) {
		// TODO Auto-generated method stub
		return null;
	}
	private String asSizeMapString(List<String[]> tempList) {
		// TODO Auto-generated method stub
		return null;
	}
	private String asImagesListString(
			HashMap<String, List<String>> pictureColourMap) {
		// TODO Auto-generated method stub
		return null;
	}
	private List<String[]> sizeMapToList(String article,
			HashMap<String, List<String>> sizeColourMap) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void run(){
		
	}
	public PrintStream getPS() {
		// TODO Auto-generated method stub
		return null;
	}
	public Scanner getScanner() {
		// TODO Auto-generated method stub
		return null;
	}
	public PrintStream getCurFilePS() {
		// TODO Auto-generated method stub
		return null;
	}

}
