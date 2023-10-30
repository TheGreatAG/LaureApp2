package it.uniba.dib.sms2223.laureapp.model;

import androidx.annotation.NonNull;

public class Persona {

    public String nome, cognome, email;

    public Persona(){}
    public Persona(String nome, String cognome,String email){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return "Persona {nome: "+nome+ " cognome :" + cognome+" email: "+email+"}";
    }
}
