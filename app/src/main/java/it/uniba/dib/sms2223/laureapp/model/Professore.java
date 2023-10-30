package it.uniba.dib.sms2223.laureapp.model;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Professore extends Persona implements Comparator<Professore> {

    String dipartimento,corso,insegnamento;

    public Professore(){
        super();
    }
    public Professore(String nome, String cognome, String email,
                      String dipartimento, String corso, String insegnamento) {
        super(nome, cognome,email);
        this.dipartimento = dipartimento;
        this.corso = corso;
        this.insegnamento = insegnamento;
    }

    @NonNull
    @Override
    public String toString() {
        return nome + " " + cognome + " " + email;
    }

    @Override
    public int compare(Professore professore, Professore t1) {
        return professore.nome.compareTo(t1.nome);
    }
}
