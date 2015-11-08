package com.sheremet.utils;


public class FragmentingTools {

	public static String findBetween(String string, String before, String after){
	
		int l = before.length();
		int i = string.indexOf(before);
		if (i==-1)return null;
		string = string.substring(i+l);
		i = string.indexOf(after);
		if (i==-1)i=Integer.MAX_VALUE;
		return string.substring(0, i);
	}

	public static String findAfter(String string, String before){
		int l = before.length();
		int i = string.indexOf(before);
		return string = string.substring(i+l);
	}

	public static String findFirstLink(String html){
		html = findAfter(html, "<a name=\"photos\">");
		html = findBefore(html, "\"><img");
		return html = findAfter(html, "href=\"/");
	}

	public static String findBefore(String string, String after){
		int i = string.indexOf(after);
		return string.substring(0, i);
	}

	public static String findNewLink(String html){
		return findBetween(html, "<a class=\"thumb_item\" href=\"/", "?_ni=1\"");
	}

	public static String findImageLink(String html){
		try{
			html =  findBetween(html, "<div class=\"pv_photo_wrap\"", "class=\"ph_img\"");
			return html =  findBetween(html, "<img src=\"", "\"");
		}catch(Exception e){return null;}
	}

	public static String findDescriptionText(String html){
		return findBetween(html, "<div class=\"mv_description\">", "</div>");
	}
}
