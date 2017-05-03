/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moons.control.run;

/**
 *
 * @author Rupert Young
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String str = "--0.1^0.8--";
        String[] arr = str.split("\\^");
        System.out.println(arr[0]);
        System.out.println(arr[1]);

        String rpExists = escapeCaret("0.1^0.8");
        String rpReplace = escapeCaret("0.9^0.95");
        System.out.println(rpExists);
        System.out.println(rpReplace);

        int ind = str.indexOf('^');
        System.out.println(ind);
        str = str.replaceAll(rpExists, rpReplace);
        System.out.println(str);

    }

    private static String escapeCaret(String s) {
        int ind = s.indexOf('^');

        return s.substring(0, ind) + "\\^" + s.substring(ind+1);
    }
}
