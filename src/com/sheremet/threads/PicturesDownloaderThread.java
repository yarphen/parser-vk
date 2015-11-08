package com.sheremet.threads;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import com.sheremet.utils.ConverterFromCSV;
import com.sheremet.utils.ConverterToCSV;
import com.sheremet.utils.Transliterator;

public class PicturesDownloaderThread extends Thread{
	private static Object locker = new Object();
	private static FileOutputStream os;
	private ActionListener onFinish;
	private Scanner scanner;
	private BufferedWriter writer;
	private LinkedList<File> files = new LinkedList<>();
	public PicturesDownloaderThread(String[] strings, ActionListener listener) {
		onFinish = listener;
		for(String s:strings){
			File f = new File (s);
			if (f.exists())
				files.add(f);
		}

	}
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		int counter = 0;
		while(true)
			try{
				if (counter>5)break;
				counter++;
				URL url = new URL(imageUrl);
				InputStream is = url.openStream();
				os = new FileOutputStream(destinationFile);
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
				System.out.println("failed attempt on "+imageUrl+"!");
				synchronized (locker ) {
					try {
						locker.wait((long) (3000*counter));
					} catch (InterruptedException e2) {
					}
				}
			}
	}
	@Override
	public void run(){
		File dir = new File("images");
		dir.mkdirs();
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("imagestable.csv", true),"UTF-8"));
		}catch(IOException e){
			e.printStackTrace();
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		for(File file:files){
			try {
				File mydir = new File("images/"+file.getName().split("-raw", 2)[0]);
				if (!mydir.exists())mydir.mkdirs();
				scanner = new Scanner(file, "utf-8");
				while(scanner.hasNextLine()){
					String str = scanner.nextLine();
					String [] arr = ConverterFromCSV.parseLine(str, ';', '"');
					String article = arr[5];
					if (!article.isEmpty()){
						int i;
						if (!map.containsKey(article))
							i = 1;
						else
							i = map.get(article)+1;
						map.put(article, i);
						map2.put(arr[1], article+"_"+i+".jpg");
						saveImage(arr[1], "images/"+file.getName().split("-raw", 2)[0]+"/"+Transliterator.transliterate(article)+"_"+i+".jpg");
						String[] arr2 = new String[2];
						arr2[0] = arr[1];
						arr2[1] = Transliterator.transliterate(article)+"_"+i+".jpg";
						writer.write(ConverterToCSV.unparseLine(arr2, ';', '"')+"\n");
						System.out.println(file.getName()+": "+arr[1]+" - downloaded to "+arr2[1]+"!");
					}

				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Program finished.");
		if (onFinish!=null)
			onFinish.actionPerformed(null);
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
		new PicturesDownloaderThread(new String[]{}, null).start();
		//		System.out.println();
	}
	public Scanner getScanner() {
		return scanner;
	}
	public BufferedWriter getWriter() {
		return writer;
	}
	public static FileOutputStream getOs() {
		return os;
	}
}
