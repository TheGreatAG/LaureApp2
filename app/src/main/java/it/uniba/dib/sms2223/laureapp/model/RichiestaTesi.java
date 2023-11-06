package it.uniba.dib.sms2223.laureapp.model;

import java.util.ArrayList;

public class RichiestaTesi {

    public Studente studente;
    public Tesi tesi;
    public String note,dataRichiesta;
    public double mediaVotiStudente;
    public int esamiMancanti;
    public ArrayList<String> propedeuticita;
    public int id;

    public RichiestaTesi(Studente studente, Tesi tesi, String note, String dataRichiesta,
                         double mediaVotiStudente,int esamiMancanti, ArrayList<String> propedeuticita) {
        this.studente = studente;
        this.tesi = tesi;
        this.note = note;
        this.esamiMancanti = esamiMancanti;
        this.dataRichiesta = dataRichiesta;
        this.mediaVotiStudente = mediaVotiStudente;
        this.propedeuticita = propedeuticita;
    }

    @Override
    public String toString() {
        return "RichiestaTesi{" +
                "studente=" + studente +
                ", tesi=" + tesi +
                ", note='" + note + '\'' +
                ", dataRichiesta='" + dataRichiesta + '\'' +
                ", mediaVotiStudente=" + mediaVotiStudente +
                ", esamiMancanti=" + esamiMancanti +
                ", propedeuticita=" + propedeuticita +
                '}';
    }
}
