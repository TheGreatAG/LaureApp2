package it.uniba.dib.sms2223.laureapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Tesi {

    //AGGIUNGERE I TASK NEL COSTRUTTORE

    private String titolo, descrizione, dataPubblicazione, ambito, tipo, corsoDiLaurea, relatore,
            corelatore, studente;
    private int mediaRichiesta, durata;
    private ArrayList<String> esamiRichiesti;


    public Tesi() {
    }

    /*  public Tesi(ETipoTesi tipoTesi) {
          this.tipoTesi = tipoTesi;
      }
  */

    public Tesi(String titolo, String tipo, String descrizione, String ambito, String corsoDiLaurea, String dataPubblicazione,
                int mediaRichiesta, int durata, String relatore,
                String corelatore, ArrayList<String> esamiRichiesti) {

        this.titolo = titolo;
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.ambito = ambito;
        this.corsoDiLaurea = corsoDiLaurea;
        this.relatore = relatore;
        this.corelatore = corelatore;
        this.mediaRichiesta = mediaRichiesta;
        this.durata = durata;
        this.esamiRichiesti = esamiRichiesti;
        this.dataPubblicazione = dataPubblicazione;
    }


    // Costruttore aggiunto
    public Tesi(String s, String s1, String s2, int i, int i1, Object o, Object o1, Object o2, Object o3, ETipoTesi compilativa) {
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getDataPubblicazione() {
        return dataPubblicazione;
    }

    public String getAmbito() {
        return ambito;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCorsoDiLaurea() {
        return corsoDiLaurea;
    }

    public String getRelatore() {
        return relatore;
    }

    public String getCorelatore() {
        return corelatore;
    }

    public int getMediaRichiesta() {
        return mediaRichiesta;
    }

    public int getDurata() {
        return durata;
    }

    public ArrayList<String> getEsamiRichiesti() {
        return esamiRichiesti;
    }

    public String getStudente() {
        return studente;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
