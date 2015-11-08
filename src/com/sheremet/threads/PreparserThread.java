package com.sheremet.threads;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import com.sheremet.utils.FragmentingTools;
import com.sheremet.utils.JTextAreaOutputStream;
import com.sheremet.utils.Tools;

public class PreparserThread extends Thread{
	private ArrayList<String> validalbums;
	private int albumNumber;
	private int photoNumber;
	private String photoLink;
	private String firstLink;
	private String albumLink;
	private PrintWriter writer;
	private ActionListener finish;
	private JTextAreaOutputStream custom;
	private PrintWriter logWriter;
	public PreparserThread(String[] albums, ActionListener f, JTextAreaOutputStream custom) {
		finish = f;
		if (custom != null)
			this.custom=custom;
		else
			this.custom = new  JTextAreaOutputStream(null, null);
		validalbums = new ArrayList<String>();
		for (String string : albums){
			try{
				validalbums.add(FragmentingTools.findAfter(string, "vk.com/"));
			}catch(StringIndexOutOfBoundsException e){}
		}
	}
	@Override
	public void run() {
		File dir = new File("albums");
		File log = new File("log.csv");
		logWriter = null;
		try {
			FileWriter fileWriter = new FileWriter(log, true);
			logWriter = new PrintWriter(fileWriter);
		} catch (IOException e1) {e1.printStackTrace();}
		dir.mkdirs();
		logWriter.write(new Date(System.currentTimeMillis())+"\n");
		for (albumNumber=0; albumNumber<validalbums.size(); albumNumber++){
			albumLink =  validalbums.get(albumNumber);
			custom.setOpen(true);
			firstLink = /*"photo-13279251_327338838";*/ Tools.getFirstLink(albumLink, "Album "+(albumNumber+1)+ " of "+validalbums.size()+" loading");
			custom.setOpen(false);
			if (firstLink==null){
				custom.setOpen(true);
				System.err.println("Album "+(albumNumber+1)+ " of "+validalbums.size()+": failed!");
				custom.setOpen(false);
				continue;
			}
			photoLink = firstLink;
			try {
				writer = new PrintWriter("albums/"+albumLink+"-raw.csv", "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				custom.setOpen(true);
				System.err.println("Writing to album "+(albumNumber+1)+ " of "+validalbums.size()+": failed!");
				custom.setOpen(false);
				continue;
			}
			photoNumber = 1;
			do{
				String html = Tools.getHTML(photoLink, "Album "+(albumNumber+1)+ " of "+validalbums.size()+", photo "+photoNumber);
				if (html == null){
					custom.setOpen(true);
					System.err.println("Error loading html: album "+(albumNumber+1)+ " of "+validalbums.size()+" failed on photo "+photoNumber+"!");
					custom.setOpen(false);
					break;
				}
				writer.write(Tools.getCSVLine(html, photoLink, logWriter)+"\r\n");
				photoLink = FragmentingTools.findNewLink(html);
				photoNumber++;
			}while(photoLink!=null&&!photoLink.equals(firstLink));
			if (photoLink == null){
				custom.setOpen(true);
				System.err.println("Error finding link: album "+(albumNumber+1)+ " of "+validalbums.size()+" failed on photo "+photoNumber+"!");
				custom.setOpen(false);
			}
			writer.close();
			custom.setOpen(true);
			System.out.println("Successfully written to /albums/"+albumLink+"-raw.csv");
			custom.setOpen(false);
			firstLink = null;
		}
		custom.setOpen(true);
		logWriter.close();
		System.out.println("Program finished.");
		custom.setOpen(false);
		if (finish!=null)
			finish.actionPerformed(null);
	}
	public PrintWriter getWriter() {
		return writer;
	}
	public PrintWriter getLogWriter() {
		return logWriter;
	}
}
