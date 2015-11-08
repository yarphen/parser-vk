package com.sheremet.threads;


/* package whatever; // don't place package name! */

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.sheremet.utils.ConverterFromCSV;
import com.sheremet.utils.ConverterToCSV;
import com.sheremet.utils.TesterTool;
import com.sheremet.utils.Tools;
import com.sheremet.utils.Transliterator;

/* Name of the class has to be "Main" only if the class is public. */
public class MergerThread extends Thread{

	private HashMap<String, String>imgmap = new HashMap<String, String>();
	private PrintStream curFilePS;
	private Scanner scanner;
	private PrintStream ps;
	private LinkedList<File> files = new LinkedList<File>();
	private ActionListener onFinish;
	public MergerThread(String[] split, ActionListener actionListener) {
		onFinish = actionListener;
		for(String s:split){
			File f = new File (s);
			if (f.exists())
				files.add(f);
		}
		loadImgsTable();
		File f = new File("merge");
		if(!f.exists())f.mkdirs();
		
	}

	private void loadImgsTable() {
		File f = new File("imagestable.csv");
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(s!=null&&s.hasNextLine()){
			String[] arr = ConverterFromCSV.parseLine(s.nextLine(), ';', '"');
			if (arr.length==2)
				imgmap.put(arr[0], arr[1]) ;
		}

	}

	@Override
	public void run(){
		try {
			ps = new PrintStream(new File("merging-log.csv"));
			System.out.println("merging log file created!");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		for (File f:files){
			try {
				curFilePS = new PrintStream(new File("merge/"+f.getName().split("-raw", 2)[0]+"-merge.csv"), "utf-8");
				System.out.println("file "+f.getName()+" opened!");
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				System.out.println("cannot open merge/"+f.getName()+"!");
			}
			try {
				scanner = new Scanner(f, "utf-8");
				System.out.println("file "+f.getName()+" loaded for reading!");
			} catch (FileNotFoundException e) {
				System.out.println("cannot load for reading "+f.getName()+"!");
				e.printStackTrace();
			}
			HashMap<String,  List<String[]>> map = new HashMap<>();
			int counter=1;
			while(scanner.hasNextLine()){
				addToMap(map , scanner.nextLine());
				System.out.println(f.getName()+": line "+counter+" loaded!");
				counter++;
			}
			List<String> strs = mapToList(map, f.getName());
			for(String s : strs)
				curFilePS.println(s);
			curFilePS.close();
			scanner.close();
			System.out.println("Successfully written to "+f.getName().split("-raw", 2)[0]+"-merge.csv");
		}
		ps.close();
		System.out.println("Program finished");
		if (onFinish!=null)
			onFinish.actionPerformed(null);
	}

	private void addToMap(HashMap<String, List<String[]>> map, String nextLine) {
		String [] row = ConverterFromCSV.parseLine(nextLine, ';', '"');
		if (!row[5].isEmpty()){
			String key = row[5]+"-"+row[7];
			String [] row2 = new String[8];
			row2[0]=row[5];
			row2[1]=row[7];
			row2[2]=row[3];
			row2[3]=row[6];
			row2[4]=row[4];
			row2[5]=row[8];
			row2[6]=imgmap.get(row[1]);
			if (!map.containsKey(key)){
				map.put(key, new LinkedList<String[]>());
			}
			List<String[]> l = map.get(key);
			l.add(row2);
		}
	}

	private List<String> mapToList(HashMap<String, List<String[]>> map, String filename) {
		List<String> list = new LinkedList<String>();
		Set<String> set = map.keySet();
		for (String key:set){
			System.out.println("key "+Transliterator.transliterate(key)+" started!");
			List<String[]>list2 = map.get(key);
			String[] merged = merge(list2, filename);
			String line = ConverterToCSV.unparseLine(merged, ';', '"');
			if (!merged[7].isEmpty()){
				ps.println(line);
				System.out.println("Lines of "+Transliterator.transliterate(key)+" key has errors!");
			}
			list.add(line);
		}
		return list;
	}


	private String[] merge(List<String[]> list2, String filename) {
		String [] merged = new String [9]; 
		String name = Tools.maxRepeats(list2, 2);
		String size = Tools.maxRepeats(list2, 3);
		String price = Tools.maxRepeats(list2, 4);
		String descr = Tools.maxRepeats(list2, 5);
		List<String> imgList = new LinkedList<String>();
		for(String[] strs:list2){
			if (strs.length>=7 &&strs[6]!=null&&!strs[6].trim().isEmpty()){
				imgList.add(strs[6]);
			}
		}
		String imgs = Tools.listWithSeparators(imgList, '|');
		merged[0]=list2.get(0)[0];
		merged[1]=list2.get(0)[1];
		merged[2]=name;
		merged[3]=size;
		merged[4]=price;
		merged[5]=descr;
		merged[6]=imgs;
		merged[8]=filename;
		String errorlog = TesterTool.test2(list2, merged);
		merged[7]=errorlog;
		return merged; 

	}

	public Scanner getScanner() {
		return scanner;
	}

	public PrintStream getCurFilePS() {
		return curFilePS;
	}

	public PrintStream getPS() {
		return ps;
	}
}