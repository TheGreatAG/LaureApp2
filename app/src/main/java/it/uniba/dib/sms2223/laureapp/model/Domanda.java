package it.uniba.dib.sms2223.laureapp.model;

public class Domanda {

    public String dataDomanda,dataRisposta,domanda,risposta,task;

    public Domanda(String dataDomanda, String dataRisposta, String domanda, String risposta, String task) {
        this.dataDomanda = dataDomanda;
        this.dataRisposta = dataRisposta;
        this.domanda = domanda;
        this.risposta = risposta;
        this.task = task;
    }
}
