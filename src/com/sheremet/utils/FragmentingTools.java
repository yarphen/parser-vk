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
	public static String findBefore(String string, String after){
		int i = string.indexOf(after);
		return string.substring(0, i);
	}
	public static String findFirstLink(String html){
		html = findAfter(html, "<a name=\"photos\">");
		html = findBefore(html, "\"><img");
		return html = findAfter(html, "href=\"/");
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
	public static void main(String[] args) {
		String s = 	"<div class=\"pv_footer bl_cont\"><div class=\"like_box bl_cont\"><div class=\"photo_msg bl_item\"></div><div class=\"mv_details\"><div class=\"mv_description\">!!!!Акц. товар!!!!<br>Есть в наличии: ( розовый ) <br>Цена: 170 грн.<br><br>Механизм кварц<br>WA10003</div><dl class=\"si_row\"><dt>Альбом:</dt><dd><a href=\"album-13279251_191063097\">Есть в наличии (бейсболки, очки, сумки, акс.)</a></dd></dl><dl class=\"si_row\"><dt>Надсилач:</dt><dd><a class=\"_g13279251\" href=\"/ismod\">Shop Mod</a></dd></dl><div class=\"vi_values\"><span class=\"item_date\">Додана 26 листопада 2014</span></div></div><ul class=\"mv_actions\"><li><a href=\"https://cs7051.vk.me/c7006/v7006380/5d754/iRFg2DPnspY.jpg\" class=\"mva_item\" target=\"_blank\">Завантажити оригінал</a></li></ul></div><div class=\"comments_wrap bl_cont\"></div></div>";
		System.out.println(findDescriptionText(s));
	}
}
