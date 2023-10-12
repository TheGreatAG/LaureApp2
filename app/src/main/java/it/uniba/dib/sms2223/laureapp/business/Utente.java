package it.uniba.dib.sms2223.laureapp.business;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.dib.sms2223.laureapp.MainActivity;

public class Utente {

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();// Initialize Firebase Auth

    public Utente(){}

    private Context context;
    public Utente (Context context){
        this.context = context;
    }

    /**
     * controlla che l'utente sia loggato o già loggato
     * @return true se l'utente è loggato, false se l'utente non è loggato
     */
    public static boolean utenteLoggato(){// IL METODO è DA IMPLEMENTARE -----------------------------------------
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {//utente già loggato
            return true;
        }else
            return false;// valore di default usato solo per test
    }

    public void registraUtente(String nome,String cognome,String email, String pw,String confermaPw){


        if (nome != null) {//se il campo nome non è vuoto
            if (cognome != null) { //se il campo cognome non è vuoto
                if (pw.equals(confermaPw)) {
                    if (new Credenziali().validitaPassword(pw)) {
                        mAuth.createUserWithEmailAndPassword(email, pw)
                                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // l'utente è registrato nel db
                                            //FirebaseUser user = mAuth.getCurrentUser(); //per recuperare l'utente attuale
                                            Toast.makeText(context, "utente registrato", Toast.LENGTH_SHORT).show();
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {// l'utente ha verificato la sua email
                                                        Toast.makeText(context, "utente registrato, verifica la tua email", Toast.LENGTH_SHORT).show();// mostra il messagio di Toast
                                                        context.startActivity(new Intent(context, MainActivity.class));
                                                    } else
                                                        Toast.makeText(context, "Email non confermata", Toast.LENGTH_SHORT).show();// mostra il messagio di Toast
                                                }
                                            });
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else
                        Toast.makeText(context, "La password deve contenere almeno 6 caratteri", Toast.LENGTH_SHORT).show();
                } else {
                    //le password non combaciano
                }
            } else {
                //inserisci un cognome
            }

        }else {
                //messaggio inserisci un nome
        }
    }

    private void inserisciDatiUtente(String nome,String cognome){

    }


    public void eliminaAccount(){//implementare qui le funzioni lato DB per eliminare un utente dal sistema e con lui anche tutto quello che ha pubblicato

    }

}
