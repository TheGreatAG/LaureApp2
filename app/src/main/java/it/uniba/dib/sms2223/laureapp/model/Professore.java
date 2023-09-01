package it.uniba.dib.sms2223.laureapp.model;

public class Professore extends Persona{

    String dipartimento,corso,insegnamento;
    public Professore(String nome, String cognome, String email,
                      String dipartimento, String corso, String insegnamento) {
        super(nome, cognome,email);
        this.dipartimento = dipartimento;
        this.corso = corso;
        this.insegnamento = insegnamento;
    }
}
