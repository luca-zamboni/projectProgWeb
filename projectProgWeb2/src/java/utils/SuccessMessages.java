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
    private static String PASSWORDCHANGE = "Password aggiornata con success!";
    private static String GROUPSUCCESS = "Inserito con successo il gruppo ";

    public static String getSuccessMessage(int code) {
        switch (code) {
            case 0:
                return REGISTRATION;
            case 1:
                return PASSWORDCHANGE;
            case 2:
                return GROUPSUCCESS;
            default:
                return "";
        }
    }
}
