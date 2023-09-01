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

    /**
     * recupera l'email dell'utente che ha fatto il login
     * @return l'emai dell'utente se l'utente ha fatto il login altrimenti il valore sarà null
     */
    public String recuperaEmailUtente(String email){// IL PARAMETRO è USATO SOLO PER TEST, ELIMINA IL PARAMETRO SE DEVI IMPLEMENTARE CORRETTAMENTE QUESTO METODO----------------------------

        return email;//SOLO PER TEST ELIMINA QUESTA RIGA SE DEVI IMPLEMENTARE QUESTO METODO--------------------------
        //return null; da decommentare
    }
}
