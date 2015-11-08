package com.sheremet.threads;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.sheremet.utils.ConverterFromCSV;
import com.sheremet.utils.ConverterToCSV;
import com.sheremet.utils.ParserTool;
import com.sheremet.utils.Transliterator;

public class SizeMatcherThread extends Thread{
	private Scanner sc;
	private PrintStream curFilePS;
	private ActionListener onFinish;
	private List<File> files = new LinkedList<File>();
	private PrintStream ps;

	public SizeMatcherThread(String[] split, ActionListener actionListener) {
		onFinish = actionListener;
		for(String s:split){
			File f = new File (s);
			if (f.exists())
				files.add(f);
		}
	}

	@Override
	public void run(){
		File csvdir = new File("size");
		try {
			ps=new PrintStream(new File("size-log.csv"));
			System.out.println("merging log file created!");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		if (!csvdir.exists())csvdir.mkdirs();
		for (File f:files){
			try {
				curFilePS = new PrintStream(new File("size/"+f.getName().split("-merge", 2)[0]+"-size.csv"));
				System.out.println("file "+f.getName()+" opened!");
			} catch (FileNotFoundException e1) {
				System.out.println("cannot open size/"+f.getName()+"!");
			}
			try {
				sc = new Scanner(f, "utf-8");
				System.out.println("file "+f.getName()+" loaded for reading!");
			} catch (FileNotFoundException e) {
				System.out.println("cannot load for reading "+f.getName()+"!");
				e.printStackTrace();
			}
			while(sc.hasNextLine()){
				String [] arr = ConverterFromCSV.parseLine(sc.nextLine(), ';', '"');
				if (!arr[1].isEmpty()){
					System.out.println(f.getName()+": line "+Transliterator.transliterate(arr[0]+"-"+arr[1])+" loaded!");
					String [] row = createRow(arr[0], arr[1], ParserTool.parseSize(arr[3]), f);
					String rowline = ConverterToCSV.unparseLine(row, ';', '"');
					if (!row[2].isEmpty())
						ps.println(rowline);
					curFilePS.println(rowline);
				}
			}
			curFilePS.close();
			sc.close();
			System.out.println("Successfully written to "+f.getName().split("-raw", 2)[0]+"-merge.csv");
		}
		ps.close();
		System.out.println("Program finished");
		if (onFinish!=null)
			onFinish.actionPerformed(null);
	}

	private String[] createRow(String string, String string2, List<String> list2, File f) {
		int count = 0;
		String errorlog = "";
		try{
			count = list2.size();
		}catch(NullPointerException e){
			errorlog = "Size error";
		}
		String [] row = new String[count+4];
		row[0]=string;
		row[1]=string2;
		for (int i=4; i<row.length; i++)
			row[i]=list2.get(i-4);
		row[2] = errorlog;
		row[3] = f.getName();
		return row;
	}

	public Scanner getScanner() {
		return sc;
	}
	public PrintStream getCurFilePS() {
		return curFilePS;
	}

	public PrintStream getPs() {
		return ps;
	}

}
