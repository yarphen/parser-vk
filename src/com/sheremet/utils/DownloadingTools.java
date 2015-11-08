package com.sheremet.utils;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class DownloadingTools {

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
		ps.println();
		ps.println();
		ps.println();
		ps.flush();
		String str;
		if (sc.hasNextLine())
			do{
				str = sc.nextLine();
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
}
