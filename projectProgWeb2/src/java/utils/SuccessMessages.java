/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

/**
 *
 * @author jibbo
 */
public class SuccessMessages {
     private static String REGISTRATION = "Ottimo, adesso puoi effettuare il Log-in!";

    public static String getSuccessMessage(int code) {
        switch (code) {
            case 0:
                return REGISTRATION;
            default:
                return "";
        }
    }
}
