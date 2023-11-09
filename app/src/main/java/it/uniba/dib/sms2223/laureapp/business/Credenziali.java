package it.uniba.dib.sms2223.laureapp.business;

import android.content.Context;
import android.content.SharedPreferences;

public class Credenziali implements ICostanti{


    /** metodo per controllare la validità di una password
     * @param pw la password da controllare
     * @return true se la pw è valida, false altrimenti
     */
    public boolean validitaPassword(String pw){
        if (pw.length() >= 6){
            return true;
        } else
            return false;

    }


    /**
     * metodo che verifica che l'email sia validà. per essere valida un email
     * deve confenere il dominio presente nell array DOMINIO_VALIDO_EMAIL_STUDENTE o
     * DOMINIO_VALIDO_PROF presenti nell'interfaccia ICostanti
     * @param email l'email da verificare
     * @return true se l'email è valida, false altrimenti
     */
    public boolean verificaValiditaEmail(String email){
        for (String email1: DOMINIO_VALIDO_EMAIL_STUDENTE){
            if (email.contains(email1)){
                return true;
            }
        }
        for (String email2 : DOMINIO_VALIDO_PROF){
            if (email.contains(email2)){
                return true;
            }
        }
        return false;
    }

    /**
     * verifica che l'email passata si una email valida per uno studente
     * @param email email da verificare
     * @return true se l'email è valida, false altrimenti
     */
    public static boolean validitaEmailStudente(String email){
        for (String email1: DOMINIO_VALIDO_EMAIL_STUDENTE){
            if (email.contains(email1)){
                return true;
            }
        }
        return false;
    }
    /**
     * verifica che l'email passata si una email valida per un prof
     * @param email email da verificare
     * @return true se l'email è valida, false altrimenti
     */
    public static boolean validitaEmailProf(String email){
        for (String email1: DOMINIO_VALIDO_PROF){
            if (email.contains(email1)){
                return true;
            }
        }
        return false;
    }

}
