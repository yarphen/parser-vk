package com.sheremet.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class Tools {

	public static String getFirstLink(String albumLink, String msg) {
		String html = getHTML(albumLink, msg);
		if (html!=null)
		try{
			return FragmentingTools.findFirstLink(html);
		}catch(Exception e){}
		return null;
	}

	public static String getHTML(String url, String msg) {
		String html=null;
		int counter = 0;
		while(true)
			try{
				if (counter>5)break;
				html = DownloadingTools.readStream(url);
				System.out.println(msg+ ": "+ url + " - successful!");
				return html;
			}catch(IOException e){
				System.err.println(msg+ ": "+ url + " - attempt failed!");
				counter++;
				synchronized (e) {
					try {
						e.wait((long) (3000*counter));
					} catch (InterruptedException e2) {
					}
				}
			}
		if (html == null)
			System.err.println(msg+ ": "+ url + " - reading failed!");
		return html;
	}

	public static String getCSVLine(String html, String photoLink, PrintWriter logWriter) {
		String [] arr = new String[10];
		String descr = FragmentingTools.findDescriptionText(html);
		arr[0] = photoLink;
		arr[1] = FragmentingTools.findImageLink(html);
		ParserTool.parse(arr, descr);
		TesterTool.test(arr);
		String line = ConverterToCSV.unparseLine(arr, ';', '"');
		if (arr[9]!=null&&(arr[9].contains("Missing name")||arr[9].contains("Missing price")||arr[9].contains("Missing ID")||arr[9].contains("Image link error"))){
			logWriter.write(line+"\n");
		}
		return line;
	}

	public static String listWithSeparators(Iterable<String> arr, char separator){
		StringBuilder sb = new StringBuilder();
		for(String s:arr){
			sb.append(s);
			sb.append(separator);
		}
		if (sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	public static String maxRepeats(List<String[]> list2, int i) {
		HashMap<String, Integer> map = new HashMap<>();
		String maxStr=null;
		int maxCount=0;
		for(String[]arr:list2){
			if (i<arr.length&&i>=0){
				String s = arr[i];
				int count=0;
				if (map.containsKey(s))
					count = map.get(s);
				count++;
				if (count>maxCount){
					maxCount=count;
					maxStr=s;
				}
				map.put(s, count);
			}
		}
		return maxStr;
	
	}

}
