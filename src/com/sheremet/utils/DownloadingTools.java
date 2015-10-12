package com.sheremet.utils;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


public class DownloadingTools {

	public void downloadImage(String url, String previosUrl, String diskpath){

	}
	public String downloadHtml(String url, String previosUrl){
		return null;
	}
	public static String readStream(String url) throws IOException{
		StringBuilder sb = new StringBuilder();
		Socket s = new Socket("m.vk.com", 80);
		s.getOutputStream();
		PrintStream ps = new PrintStream(s.getOutputStream());
		Scanner sc = new Scanner(s.getInputStream(), "utf-8");
		ps.println("GET /"+url+" HTTP/1.1");
		ps.println("Host: m.vk.com");
		ps.println("Accept: */*");
		ps.println("User-Agent: Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19");
		//	ps.println("Cookie: "+mapToString(cookie));
		ps.println();
		ps.println();
		ps.println();
		ps.flush();
		String str;
		if (sc.hasNextLine())
			do{
				str = sc.nextLine();
//				System.out.println(str);
				sb.append(str+"\r\n");
				ps.flush();
			}while(!str.contains("</body>"));
		ps.close();
		sc.close();
		synchronized (sc) {
			try {
				sc.wait((long) (1000*Math.random()+500));
			} catch (InterruptedException e) {
			}
		}
		s.close();
		return sb.toString();
	}
	public static void main(String[] args)  {
		try {
			readStream("album-13279251_161253522");
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true){
			int c = 0;
			c++;
		}
	}
}
/*
Host: vk.com
User-Agent: Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19
Accept: text/html
Accept-Encoding: utf-8
Referer: ***
Cookie: remixlang=0; remixflash=19.0.0; remixscreen_depth=24; remixdt=0
Connection: keep-alive
If-Modified-Since: Mon, 14 Sep 2015 21:56:55 GMT
Cache-Control: max-age=0
 */