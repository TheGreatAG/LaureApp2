package it.uniba.dib.sms2223.laureapp.business;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.ActivityPrimoAccessoLogin;
import it.uniba.dib.sms2223.laureapp.MainActivity;
import it.uniba.dib.sms2223.laureapp.PrimoAccessoDocente;

public class Utente implements ICostanti{

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();// Initialize Firebase Auth

    //PER IL TESTER, VERIFICA COSA SUCCEDE A RIGA 82, VEDI IL COMMENTO ************************************

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

    /**
     * registra l'utente inserendo i dati in Authenticator del servizio offerto da Firebase e nel database firestore inviando un'email per verificare l'account
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param email email dell'utente
     * @param pw password inserita dall'utente
     * @param confermaPw
     */
    public void registraUtente(String nome,String cognome,String email, String pw,String confermaPw){

        if (nome != null) {//se il campo nome non è vuoto
            if (cognome != null) { //se il campo cognome non è vuoto
                if (pw.equals(confermaPw)) {
                    if (new Credenziali().validitaPassword(pw)) {
                        mAuth.createUserWithEmailAndPassword(email, pw)// per Authenticator
                                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // l'utente è registrato nel db
                                            //FirebaseUser user = mAuth.getCurrentUser(); //per recuperare l'utente attuale
                                            Toast.makeText(context, "utente registrato", Toast.LENGTH_SHORT).show();

                                            inserisciDatiUtente(nome,cognome,email);


                                          /*  mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {// l'utente ha verificato la sua email
                                                        Toast.makeText(context, "utente registrato, verifica la tua email", Toast.LENGTH_SHORT).show();// mostra il messagio di Toast
                                                        //context.startActivity(new Intent(context, MainActivity.class));
                                                        inserisciDatiUtente(nome,cognome,email);
                                                    } else //Per il tester VERIFICARE COSA SUCCEDE A QUESTO PUNTO DELLA REGISTRAZIONE NEI VARI CASI - SE SI VERIFICA l'email e se non si verifica
                                                        Toast.makeText(context, "Email non confermata", Toast.LENGTH_SHORT).show();// mostra il messagio di Toast
                                                }
                                            });*/
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

    /**
     * Inserisce i dati dell'utente nlla giusta collection nel database differenziando tra studente e professore
     * @param nome nome utente
     * @param cognome cognome utente
     * @param email email dell'utente
     */
    private void inserisciDatiUtente(String nome,String cognome,String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("nome", nome);
        user.put("cognome", cognome);
        user.put("Tesi assegnata",false);
        if (Credenziali.validitaEmailStudente(email)){//se l'email è dello studente
            db.collection("studenti").document(email).set(user).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                         //   salvaInFile(nome,cognome);
                            context.startActivity(new Intent(context, ActivityPrimoAccessoLogin.class));
                            Log.d(TAG, email + " studente aggiunto con successo");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Errore nell'inserimento dei dati. RIPROVA.", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        if(Credenziali.validitaEmailProf(email)){
            db.collection("professori").document(email).set(user).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            context.startActivity(new Intent(context, PrimoAccessoDocente.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Errore nell'inserimento dei dati. RIPROVA.", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    private void salvaInFile(String nome, String cognome){

        SharedPreferences sharedPreferences = context.getSharedPreferences(NOME_COGNOME_STUD,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(INFO_STUD,nome);
        editor.putString(INFO_STUD+"c",cognome);

        editor.apply();
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//L'uso di Intent.FLAG_ACTIVITY_CLEAR_TASK e Intent.FLAG_ACTIVITY_NEW_TASK dovrebbe chiudere tutte le Activity precedenti e creare un nuovo stack con solo DaEliminareActivity.
//
//Tuttavia, tieni presente che questa soluzione è più drastica e rimuoverà tutte le Activity dallo stack
        context.startActivity(intent);
        Activity c = (Activity) context;
        c.finish();

    }


    public void eliminaAccount(){//implementare qui le funzioni lato DB per eliminare un utente dal sistema e con lui anche tutto quello che ha pubblicato

    }

}
