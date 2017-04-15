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

        String s = "0^1";
        String[] arr = s.split("\\^");
        System.out.println(arr[0]);
        System.out.println(arr[1]);
    }

}
