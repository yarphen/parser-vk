package com.sheremet.utils;
import java.io.IOException;
import java.util.ArrayList;


public class AlbumDownloader {
	public static ArrayList<String[]> downloadAlbum(String albumURL) throws IOException{
		String firstLink = FragmentingTools.findFirstLink(DownloadingTools.readStream(albumURL));
		String curLink = firstLink;
		ArrayList<String[]> list = new ArrayList<String[]>();
		int counter = 0 ;
		do{
			try{
				String html = DownloadingTools.readStream(curLink);
				System.out.println(counter+ ": "+ curLink + " - successful!");
				
				String [] arr = new String[10];
				String descr = FragmentingTools.findDescriptionText(html);
				arr[0] = curLink;
				arr[1] = FragmentingTools.findImageLink(html);
				ParserTool.parse(arr, descr);
				TesterTool.test(arr);
				list.add(arr);
//				if (counter==5)
//					break;
				counter ++;
				curLink = FragmentingTools.findNewLink(html);
			}catch(IOException e){
				System.err.println(counter+ ": "+ curLink + " - failed!");
				synchronized (e) {
					try {
						e.wait((long) (10000*Math.random()));
					} catch (InterruptedException e2) {
					}
				}
			}
		}while(!firstLink.equals(curLink));
		return list;
	}
	public static void main(String[] args) throws IOException {
		for (String s1:args){
			ArrayList<String[]> list = downloadAlbum(FragmentingTools.findAfter(s1, "vk.com/"));
			String s = ConverterToCSV.convert(list, '^', '\'');
			System.out.println(s);//https://vk.com/photo-13279251_326200869
		}
	}
}
