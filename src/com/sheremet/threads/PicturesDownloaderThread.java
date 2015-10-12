package com.sheremet.threads;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import com.sheremet.utils.ConverterFromCSV;

public class PicturesDownloaderThread extends Thread{
	private static Object locker = new Object();
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		int counter = 0;
		while(true)
			try{
				if (counter>5)break;
				URL url = new URL(imageUrl);
				InputStream is = url.openStream();
				OutputStream os = new FileOutputStream(destinationFile);
				byte[] b = new byte[2048];
				int length;
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
				is.close();
				os.close();
				synchronized (locker ) {
					try {
						locker.wait((long) (500*Math.random()+200));
					} catch (InterruptedException e2) {
					}
				}
				break;
			}catch(Exception e){
				synchronized (locker ) {
					try {
						locker.wait((long) (3000*counter));
					} catch (InterruptedException e2) {
					}
				}
			}
	}
	@Override
	public void run() {
		File csvdir = new File("albums");
		File [] files = csvdir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().endsWith(".csv")&&!arg0.isDirectory();
			}
		});
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		for(File file:files){
			Scanner s;
			try {
				s = new Scanner(file);
				while(s.hasNextLine()){
					String str = s.nextLine();
					String [] arr = ConverterFromCSV.createLine(str, ';', '"');
					String article = arr[5];
					if (!map.containsKey(article)){
						map.put(article, 1);
						map2.put(arr[1], article+"_1.jpg");
						saveImage(arr[1], "images/"+article+"_1.jpg");
						System.out.println(arr[1]+" - downloaded to "+article+"_1.jpg"+"!");
					}

					else{
						int i = map.get(article)+1;
						map.put(article, i);
						map2.put(arr[1], article+"_"+i+".jpg");
						saveImage(arr[1], "images/"+article+"_"+i+".jpg");
						System.out.println(arr[1]+" - downloaded to "+article+"_"+i+".jpg"+"!");
					}

				}
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		//		File imgsdir = new File("C:/Users/Ì/work/workspace1/VkAlbumParser2/albums");
		//		File [] files = imgsdir.listFiles(new FileFilter() {
		//			
		//			@Override
		//			public boolean accept(File arg0) {
		//				return arg0.getName().endsWith("csv")&&!arg0.isDirectory();
		//			}
		//		});
//		try {
//			saveImage("http://cs9785.vk.me/u17510826/103661830/x_26c4c194.jpg", "img01.jpg");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		new PicturesDownloaderThread().start();
//		System.out.println();
	}
}
