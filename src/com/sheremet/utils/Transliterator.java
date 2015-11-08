package com.sheremet.utils;

import java.util.HashMap;

public class Transliterator {

	private static final HashMap<Character, String> charMap = new HashMap<Character, String>();

	static {
	    charMap.put('�', "A");
	    charMap.put('�', "B");
	    charMap.put('�', "V");
	    charMap.put('�', "H");
	    charMap.put('�', "G");
	    charMap.put('�', "D");
	    charMap.put('�', "E");
	    charMap.put('�', "Jo");
	    charMap.put('�', "Zh");
	    charMap.put('�', "Z");
	    charMap.put('�', "I");
	    charMap.put('�', "J");
	    charMap.put('�', "K");
	    charMap.put('�', "L");
	    charMap.put('�', "M");
	    charMap.put('�', "N");
	    charMap.put('�', "O");
	    charMap.put('�', "P");
	    charMap.put('�', "R");
	    charMap.put('�', "S");
	    charMap.put('�', "T");
	    charMap.put('�', "U");
	    charMap.put('�', "F");
	    charMap.put('�', "Kh");
	    charMap.put('�', "C");
	    charMap.put('�', "Ch");
	    charMap.put('�', "Sh");
	    charMap.put('�', "Sch");
	    charMap.put('�', "^");
	    charMap.put('�', "Y");
	    charMap.put('�', "'");
	    charMap.put('�', "E");
	    charMap.put('�', "U");
	    charMap.put('�', "Ya");
	    charMap.put('�', "A");
	    charMap.put('�', "B");
	    charMap.put('�', "V");
	    charMap.put('�', "H");
	    charMap.put('�', "G");
	    charMap.put('�', "D");
	    charMap.put('�', "E");
	    charMap.put('�', "Jo");
	    charMap.put('�', "Zh");
	    charMap.put('�', "Z");
	    charMap.put('�', "I");
	    charMap.put('�', "J");
	    charMap.put('�', "K");
	    charMap.put('�', "L");
	    charMap.put('�', "M");
	    charMap.put('�', "N");
	    charMap.put('�', "O");
	    charMap.put('�', "P");
	    charMap.put('�', "R");
	    charMap.put('�', "S");
	    charMap.put('�', "T");
	    charMap.put('�', "U");
	    charMap.put('�', "F");
	    charMap.put('�', "Kh");
	    charMap.put('�', "C");
	    charMap.put('�', "Ch");
	    charMap.put('�', "Sh");
	    charMap.put('�', "Sch");
	    charMap.put('�', "^");
	    charMap.put('�', "Y");
	    charMap.put('�', "'");
	    charMap.put('�', "E");
	    charMap.put('�', "U");
	    charMap.put('�', "Ya");
	
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