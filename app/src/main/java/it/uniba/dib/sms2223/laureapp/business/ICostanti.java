package it.uniba.dib.sms2223.laureapp.business;

public interface ICostanti {

    String [] DOMINIO_VALIDO_EMAIL_STUDENTE = {"@studenti.uniba.it"};
    String [] DOMINIO_VALIDO_PROF = {"@uniba.it"};
    int LISTA_TESI_PREFERITE = 53;
    int LISTA_TESI = 53;
    int INVIO_EMAIL = 22;
    int INVIO_TESTO =23;

    String COLLECTION_TASK ="TASK";
    String TASK_DA_COMPLETARE= "da completare";
    String TASK_IN_LAVORAZIONE="in lavorazione";
    String TASK_COMPLETATO = "completato";
    String COLLECTION_DOMANDE_TASK = "DOMANDE TASK";

    String COLLECTION_PROF = "professori";
    String COLLECTION_RICHIESTE = "richieste";
    String COLLECTION_TESI = "Tesi";

    String NOME_COGNOME_STUD = "file dati stud";
    String INFO_STUD = "info";
    String PRIMO_ACCESSO_DOCENTE = "primo doc";
    String PRIMOACCESSO_STUDENTE = "primo stud";

}
