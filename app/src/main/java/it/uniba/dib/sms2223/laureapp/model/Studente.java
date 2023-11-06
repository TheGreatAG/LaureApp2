package it.uniba.dib.sms2223.laureapp.model;

import androidx.annotation.NonNull;

public class Studente extends Persona {

    public String password;
    int mediaVoti;

    public Studente(){

    }
    public Studente(String email,String password,String nome, String cognome,int mediaVoti){
        super(nome,cognome, email);
        this.mediaVoti = mediaVoti;
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
