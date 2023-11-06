package it.uniba.dib.sms2223.laureapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Tesi implements Parcelable {

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

    protected Tesi(Parcel in) {
        titolo = in.readString();
        descrizione = in.readString();
        dataPubblicazione = in.readString();
        ambito = in.readString();
        tipo = in.readString();
        corsoDiLaurea = in.readString();
        sRelatore = in.readString();
        sCorelatore = in.readString();
        studente = in.readString();
        mediaRichiesta = in.readInt();
        durata = in.readInt();
        id = in.readString();
        esamiRichiesti = in.createStringArrayList();
    }

    public static final Creator<Tesi> CREATOR = new Creator<Tesi>() {
        @Override
        public Tesi createFromParcel(Parcel in) {
            return new Tesi(in);
        }

        @Override
        public Tesi[] newArray(int size) {
            return new Tesi[size];
        }
    };

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
        return "TITOLO TESI\n"+ titolo + "\nDESCRIZIONE\n"+ descrizione+
        "\nDATA DI PUBBLICAZIONE\n"+ dataPubblicazione+ "\nAMBITO\n "+ambito +
        "\nTIPO\n"+tipo + "\nCORSO DI LAUREA\n " +corsoDiLaurea +
        "\nINFO RELATORE\n"+ sRelatore +"\nINFO CORELATORE\n"+sCorelatore+
                "\nMEDIA VOTI RICHIESTA\n"+ mediaRichiesta +"\nTEMPO STIMATO DI LAVORO\n"+durata+
                "\nESAMI RICHIESTI:\n"+ esamiRichiesti.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(titolo);
        parcel.writeString(descrizione);
        parcel.writeString(dataPubblicazione);
        parcel.writeString(ambito);
        parcel.writeString(tipo);
        parcel.writeString(corsoDiLaurea);
        parcel.writeString(sRelatore);
        parcel.writeString(sCorelatore);
        parcel.writeString(studente);
        parcel.writeInt(mediaRichiesta);
        parcel.writeInt(durata);
        parcel.writeString(id);
        parcel.writeStringList(esamiRichiesti);
    }
}
