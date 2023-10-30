package it.uniba.dib.sms2223.laureapp.model;

public class Ricevimento {

    public Professore professore;
    public String titolo,oggetto,descrizione,data;

    public Ricevimento(Professore professore, String titolo, String oggetto, String descrizione, String data) {
        this.professore = professore;
        this.titolo = titolo;
        this.oggetto = oggetto;
        this.descrizione = descrizione;
        this.data = data;
    }
}
