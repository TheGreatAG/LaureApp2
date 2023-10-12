package it.uniba.dib.sms2223.laureapp.business;

public class Credenziali implements ICostanti{

    /**
     * Controlla la validità delle credenziali inserite come; il dominio dell'email che deve
     * appartenere nell'elenco dei domini definiti nell'interfaccia ICostanti
     * @param email l'email dello studente o professore
     * @param password la password dell'utente, deve contenere almeno 8 caratteri
     * @return ritorna vero se le credenziali sono valide, altrimenti falso
     * @see ICostanti
     */
    public boolean validitaCredenziali(String email,String password){
        return false;
    }

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
     * recupera l'email dell'utente che ha fatto il login
     * @return l'emai dell'utente se l'utente ha fatto il login altrimenti il valore sarà null
     */
    public String recuperaEmailUtente(String email){// IL PARAMETRO è USATO SOLO PER TEST, ELIMINA IL PARAMETRO SE DEVI IMPLEMENTARE CORRETTAMENTE QUESTO METODO----------------------------

        return email;//SOLO PER TEST ELIMINA QUESTA RIGA SE DEVI IMPLEMENTARE QUESTO METODO--------------------------
        //return null; da decommentare
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
