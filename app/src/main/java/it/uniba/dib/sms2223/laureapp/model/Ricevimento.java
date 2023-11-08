package it.uniba.dib.sms2223.laureapp.model;

public class Ricevimento {

    public String titolo,descrizione,data,id;
    public Tesi tesi;

    public Ricevimento(Tesi tesi, String descrizione, String data,String id) {
        this.tesi = tesi;
        this.id = id;
        this.descrizione = descrizione;
        this.data = data;
    }
}
