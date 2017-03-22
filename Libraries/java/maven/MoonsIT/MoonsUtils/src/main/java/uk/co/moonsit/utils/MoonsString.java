/* 
  *  This software is the property of Moon's Information Technology Ltd.
  * 
  *  All rights reserved.
  * 
  *  The software is only to be used for development and research purposes.
  *  Commercial use is only permitted under license or agreement.
  * 
  *  Copyright (C)  Moon's Information Technology Ltd.
  *  
  *  Author: rupert@moonsit.co.uk
  * 
  * 
 */
package uk.co.moonsit.utils;

import java.util.ArrayList;

public class MoonsString {

    public static String[] split(String s, String S) {
        ArrayList<String> list = new ArrayList<>();
        int start = 0;
        while (start < S.length()) {
            int end = S.indexOf(s, start);
            if (end < 0) {
                break;
            }

            list.add(S.substring(start, end));
            start = end + s.length();
        }
        if (start < S.length()) {
            list.add(S.substring(start));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String shortenTextFilter(String text) {
        if (Environment.getInstance().isShortNames()) {
            text = MoonsString.shortenText(text);
        }
        return text;
    }

    public static String shortenText(String text) {
        StringBuilder sb = new StringBuilder();
        boolean keepLower = false;
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            if (c < 91) {
                sb.append(c);
                keepLower = true;
            } else if (keepLower) {
                sb.append(c);
                keepLower = false;
            }
        }
        return sb.toString();
    }

    public static String formatPlaces(Double d, int places) {
        double pow = Math.pow(10, places);

        double dd = (Math.rint(d * pow)) / pow;

        return String.valueOf(dd);
    }

    public static String formatStringPlaces(Double d, int places) {
        String format = "%."+places+"f";
        
        return String.format(format, d);
    }

    private static String getParametersString(String p) {
        int index = p.indexOf("&");

        return p.substring(0, index);

    }

    public static void main(String[] args) {

        String[] s = MoonsString.split("&", "&NegativeTransitionControlOutput^0^null~PositiveTransitionControlOutput^1^null~");
        System.out.println(s[0]);
        System.out.println(s[1]);

        String[] pars = MoonsString.split("~", "");

        System.out.println(pars.length);
    }
}
