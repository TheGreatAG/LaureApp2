package it.uniba.dib.sms2223.laureapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Tesi {

    //AGGIUNGERE I TASK NEL COSTRUTTORE

    public String titolo,descrizione, dataPubblicazione;
    public int votoConsigliato,tempoDiLavoroStimatoInOre;
    public ArrayList<String> propedeuticita;

    public Relatore relatore;
    public Corelatore corelatore;
    public Studente studente;

    public ETipoTesi tipoTesi;

    public Tesi(){}
    public Tesi(ETipoTesi tipoTesi){
        this.tipoTesi = tipoTesi;
    }

    public Tesi(String titolo, String descrizione, String dataPubblicazione,
                int votoConsigliato, int tempoDiLavoroStimatoInOre,
                ArrayList<String> propedeuticita, Relatore relatore,
                Corelatore corelatore, Studente studente) {

        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataPubblicazione = dataPubblicazione;
        this.votoConsigliato = votoConsigliato;
        this.tempoDiLavoroStimatoInOre = tempoDiLavoroStimatoInOre;
        this.propedeuticita = propedeuticita;
        this.relatore = relatore;
        this.corelatore = corelatore;
        this.studente = studente;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
