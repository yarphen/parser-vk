package com.sheremet.utils;

import java.util.HashMap;

public class Transliterator {

	private static final HashMap<Character, String> charMap = new HashMap<Character, String>();

	static {
	    charMap.put('À', "A");
	    charMap.put('Á', "B");
	    charMap.put('Â', "V");
	    charMap.put('Ã', "H");
	    charMap.put('¥', "G");
	    charMap.put('Ä', "D");
	    charMap.put('Å', "E");
	    charMap.put('¨', "Jo");
	    charMap.put('Æ', "Zh");
	    charMap.put('Ç', "Z");
	    charMap.put('È', "I");
	    charMap.put('É', "J");
	    charMap.put('Ê', "K");
	    charMap.put('Ë', "L");
	    charMap.put('Ì', "M");
	    charMap.put('Í', "N");
	    charMap.put('Î', "O");
	    charMap.put('Ï', "P");
	    charMap.put('Ğ', "R");
	    charMap.put('Ñ', "S");
	    charMap.put('Ò', "T");
	    charMap.put('Ó', "U");
	    charMap.put('Ô', "F");
	    charMap.put('Õ', "Kh");
	    charMap.put('Ö', "C");
	    charMap.put('×', "Ch");
	    charMap.put('Ø', "Sh");
	    charMap.put('Ù', "Sch");
	    charMap.put('Ú', "^");
	    charMap.put('Û', "Y");
	    charMap.put('Ü', "'");
	    charMap.put('İ', "E");
	    charMap.put('Ş', "U");
	    charMap.put('ß', "Ya");
	    charMap.put('à', "A");
	    charMap.put('á', "B");
	    charMap.put('â', "V");
	    charMap.put('ã', "H");
	    charMap.put('´', "G");
	    charMap.put('ä', "D");
	    charMap.put('å', "E");
	    charMap.put('¸', "Jo");
	    charMap.put('æ', "Zh");
	    charMap.put('ç', "Z");
	    charMap.put('è', "I");
	    charMap.put('é', "J");
	    charMap.put('ê', "K");
	    charMap.put('ë', "L");
	    charMap.put('ì', "M");
	    charMap.put('í', "N");
	    charMap.put('î', "O");
	    charMap.put('ï', "P");
	    charMap.put('ğ', "R");
	    charMap.put('ñ', "S");
	    charMap.put('ò', "T");
	    charMap.put('ó', "U");
	    charMap.put('ô', "F");
	    charMap.put('õ', "Kh");
	    charMap.put('ö', "C");
	    charMap.put('÷', "Ch");
	    charMap.put('ø', "Sh");
	    charMap.put('ù', "Sch");
	    charMap.put('ú', "^");
	    charMap.put('û', "Y");
	    charMap.put('ü', "'");
	    charMap.put('ı', "E");
	    charMap.put('ş', "U");
	    charMap.put('ÿ', "Ya");
	
	}

	public static String transliterate(String string) {
	    StringBuilder transliteratedString = new StringBuilder();
	    for (int i = 0; i < string.length(); i++) {
	        Character ch = string.charAt(i);
	        String charFromMap = charMap.get(ch);
	        if (charFromMap != null) {
	            transliteratedString.append(charFromMap);
	        } else {
	            transliteratedString.append(ch);
	        }
	    }
	    return transliteratedString.toString();
	}
}