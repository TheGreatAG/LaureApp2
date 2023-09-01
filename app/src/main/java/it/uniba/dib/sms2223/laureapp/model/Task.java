package it.uniba.dib.sms2223.laureapp.model;

public class Task {

    public String titolo,descrizione,ultimaModifica,stato;

    public Task(String titolo, String descrizione, String ultimaModifica, String stato) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.ultimaModifica = ultimaModifica;
        this.stato = stato;
    }
}
