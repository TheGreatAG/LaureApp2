package it.uniba.dib.sms2223.laureapp.model;

import java.util.ArrayList;

public class RichiestaTesi {

    public Studente studente;
    public Tesi tesi;
    public String note,dataRichiesta;
    public int mediaVotiStudente;
    public ArrayList<String> propedeuticita;

    public RichiestaTesi(Studente studente, Tesi tesi, String note, String dataRichiesta, int mediaVotiStudente, ArrayList<String> propedeuticita) {
        this.studente = studente;
        this.tesi = tesi;
        this.note = note;
        this.dataRichiesta = dataRichiesta;
        this.mediaVotiStudente = mediaVotiStudente;
        this.propedeuticita = propedeuticita;
    }
}
