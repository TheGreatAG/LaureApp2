package it.uniba.dib.sms2223.laureapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Tesi {

    //AGGIUNGERE I TASK NEL COSTRUTTORE

    public String titolo, descrizione, dataPubblicazione, ambito, tipo, corsoDiLaurea, sRelatore,
            sCorelatore, studente;
    public int mediaRichiesta, durata;
    public String id;
    public ArrayList<String> esamiRichiesti;

    public Relatore relatore;
    public Corelatore corelatore;


    public Tesi() {
    }

    /*  public Tesi(ETipoTesi tipoTesi) {
          this.tipoTesi = tipoTesi;
      }
  */

    public Tesi(String id,String titolo, String tipo, String descrizione, String ambito, String corsoDiLaurea, String dataPubblicazione,
                int mediaRichiesta, int durata, String relatore,
                String corelatore, ArrayList<String> esamiRichiesti) {

        this.titolo = titolo;
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.ambito = ambito;
        this.corsoDiLaurea = corsoDiLaurea;
        this.sRelatore = relatore;
        this.sCorelatore = corelatore;
        this.mediaRichiesta = mediaRichiesta;
        this.durata = durata;
        this.esamiRichiesti = esamiRichiesti;
        this.dataPubblicazione = dataPubblicazione;
        this.id = id;
    }

    public Tesi(String id,String titolo, String tipo, String descrizione, String ambito, String corsoDiLaurea, String dataPubblicazione,
                int mediaRichiesta, int durata, Relatore relatore,
                Corelatore corelatore, ArrayList<String> esamiRichiesti) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataPubblicazione = dataPubblicazione;
        this.ambito = ambito;
        this.tipo = tipo;
        this.corsoDiLaurea = corsoDiLaurea;
        this.studente = studente;
        this.mediaRichiesta = mediaRichiesta;
        this.durata = durata;
        this.id = id;
        this.esamiRichiesti = esamiRichiesti;
        this.relatore = relatore;
        this.corelatore = corelatore;
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
