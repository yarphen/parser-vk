package com.sheremet.utils;
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
				if (b4&&strs[i]!=null&&(strs[i].contains("����:")||strs[i].contains("���� :"))){
					try{
						if (strs[i].contains("����:"))
						array[4] = FragmentingTools.findBetween(strs[i], "����:", "���").trim();
						if (strs[i].contains("���� :"))
							array[4] = FragmentingTools.findBetween(strs[i], "���� :", "���").trim();
						
						b4=false;
						strs[i]=null;
					}catch(StringIndexOutOfBoundsException e){
						
					}
					
				}

				if (b6&&strs[i]!=null&&strs[i].contains("������")){
					b6=false;
					if (strs[i].contains("�������:")){
						array[6] = FragmentingTools.findAfter(strs[i], "�������:");
					}
					if (strs[i].contains("������:")){
						array[6] = FragmentingTools.findAfter(strs[i], "������:");
					}
					if (strs[i].contains("������� :")){
						array[6] = FragmentingTools.findAfter(strs[i], "������� :");
					}
					if (strs[i].contains("������ :")){
						array[6] = FragmentingTools.findAfter(strs[i], "������ :");
					}
					strs[i]=null;
				}

				if (b5&&(i==strs.length-2||i==strs.length-1)&&strs[i]!=null&&strs[i].matches("[[�-ߨ]|[\\w]]+\\d+")){
					b5=false;
					array[5] = strs[i];
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
		String s = "�����<br/>���� � �������: ( ������� ) <br/>����: 170 ���.<br/>�������� �����<br/>WA10003";
		String [] strs = new String[9];
		parse(strs, s);
		System.out.println();
	}
}
