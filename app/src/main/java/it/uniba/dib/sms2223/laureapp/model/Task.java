package it.uniba.dib.sms2223.laureapp.model;

public class Task {


    public String titolo,descrizione,ultimaModifica,stato,id;
    public boolean confermaDefinitivaDelRelatore;

    public Task(String titolo, String descrizione, String ultimaModifica, String stato) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.ultimaModifica = ultimaModifica;
        this.stato = stato;
    }

    public Task(String id,String titolo, String descrizione, String ultimaModifica, String stato) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.ultimaModifica = ultimaModifica;
        this.stato = stato;
        this.id = id;
    }
}
