package com.sheremet.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class ParserTool {

	public static String[] parse(String[] array, String descr) {
		if (descr!=null){
			String[] strs = descr.split(Pattern.quote("<br/>"));
			array[2] = descr;
			boolean b3 = true, b4 = true, b5 = true, b6 = true, b7 = true; 
			for(int i=0; i<strs.length; i++){
				if (b3&&i==0){
					b3=false;
					array[3] = strs[i];
					strs[i]=null;
				}
				if (b4&&strs[i]!=null&&(strs[i].contains("Цена:")||strs[i].contains("Цена :"))){
					try{
						if (strs[i].contains("Цена:"))
						array[4] = FragmentingTools.findBetween(strs[i], "Цена:", "грн").trim();
						if (strs[i].contains("Цена :"))
							array[4] = FragmentingTools.findBetween(strs[i], "Цена :", "грн").trim();
						
						b4=false;
						strs[i]=null;
					}catch(StringIndexOutOfBoundsException e){
						
					}
					
				}
	
				if (b6&&strs[i]!=null&&strs[i].contains("Размер")){
					b6=false;
					if (strs[i].contains("Размеры:")){
						array[6] = FragmentingTools.findAfter(strs[i], "Размеры:");
					}
					if (strs[i].contains("Размер:")){
						array[6] = FragmentingTools.findAfter(strs[i], "Размер:");
					}
					if (strs[i].contains("Размеры :")){
						array[6] = FragmentingTools.findAfter(strs[i], "Размеры :");
					}
					if (strs[i].contains("Размер :")){
						array[6] = FragmentingTools.findAfter(strs[i], "Размер :");
					}
					strs[i]=null;
				}
	
				if (b5&&(i==strs.length-2||i==strs.length-1)&&strs[i]!=null&&strs[i].matches("[[А-ЯЁ]|[\\w]]+\\d+")){
					b5=false;
					array[5] = Transliterator.transliterate(strs[i]);
					strs[i]=null;
				}
	
				if (b7&&i==strs.length-1){
					b7=false;
					array[7] = strs[i];
					strs[i]=null;
				}
			}
			String fulldescr = "<h1>"+array[3]+"</h1><br/>";
			for(String s:strs){
				if (s!=null)
					fulldescr+=format(s);
			}
			array[8]=fulldescr;
			
		}
		return array;
	}

	private static String format(String s) {
		if (s.contains(":")){
			String [] strs = s.split(":", 2);
			return "<b>"+strs[0]+":</b>"+strs[1]+"<br/>";
		}else{
			return s+ "<br/>";
		}
	}

	public static void main(String[] args) {
		String s = "Товар<br/>Есть в наличии: ( розовый ) <br/>Цена: 170 грн.<br/>Механизм кварц<br/>WA10003";
		String [] strs = new String[9];
		parse(strs, s);
		System.out.println();
	}

	public static final String [] SIZE = new String[]{"XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL", "XXXXL"};

	public static List<String> parseSize(String s) {
		ArrayList<String>list = new ArrayList<String>();
		String [] arr = null;
		if (s.contains("—")||s.contains("-")){
			if (s.contains("-")){
				arr = s.split("-", 2);
			}
			if (s.contains("—")){
				arr = s.split("—", 2);
			}
			arr[0]=arr[0].trim();
			arr[1]=arr[1].trim();
			if (ConverterToCSV.isNumeric(arr[0])&&ConverterToCSV.isNumeric(arr[0])){
				int min = Integer.parseInt(arr[0]);
				int max = Integer.parseInt(arr[1]);
				for(int i=min; i<=max; i++){
					list.add(i+"");
				}
			}else{
				if (!TesterTool.testSize(arr[0])||!TesterTool.testSize(arr[1]))return null;
				boolean started = false;
				for (String str:SIZE){
					if (str.equals(arr[0]))started=true;
					if (started){
						list.add(str);
					}
					if (str.equals(arr[1]))started=false;
				}
			}
		}else{
			StringTokenizer t = new StringTokenizer(s);
			while(t.hasMoreTokens()){
				String token = t.nextToken();
				token=token.replace(",", "");
				if (!TesterTool.testSize(token)) return null;
				list.add(token);
			}
		}
		return list;
	
	}
}
