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
public class ErrorMessages {
    
    //Errors
    private static String GENERAL = "Qualcosa e' andato storto";
    private static String PARAMETERS = "Parametri non validi";
    private static String USERNAME = "Username non valido";
    private static String PASSWORD = "Password non valida";
    private static String EMAIL = "E-Mail non valida";
    private static String PASSNONCOBACIANO = "Le due password non combaciano";
    private static String USERINESISTENTE = "Username o email inseistenti";
    private static String LOGGED = "Sei già autenticato";
    private static String IMAGENOTVALID = "I formati accettati sono png, bitmap o jpeg";

    public static String getErrorMessage(int code) {
        switch (code) {
            case 0:
                return PARAMETERS;
            case 1:
                return USERNAME;
            case 2:
                return PASSWORD;
            case 3:
                return EMAIL;
            case 4:
                return PASSNONCOBACIANO;
            case 5:
                return USERINESISTENTE;
            case 6:
                return LOGGED;
            case 7:
                return IMAGENOTVALID;
            default:
                return GENERAL;
        }
    }

}
