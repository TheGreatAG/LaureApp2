package it.uniba.dib.sms2223.laureapp.model;

public class Domanda {

    public String dataDomanda,dataRisposta,domanda,risposta,titoloTask,id,idTask,tesiId;

    public Domanda(String dataDomanda, String dataRisposta, String domanda, String risposta
            ,String titoloTask,String idTask,String id,String tesiId) {
        this.dataDomanda = dataDomanda;
        this.dataRisposta = dataRisposta;
        this.domanda = domanda;
        this.risposta = risposta;
        this.titoloTask = titoloTask;
        this.idTask = idTask;
        this.id = id;
        this.tesiId = tesiId;
    }
}
