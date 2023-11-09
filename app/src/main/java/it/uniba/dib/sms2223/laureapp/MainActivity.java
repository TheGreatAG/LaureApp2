package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;

import android.content.ContentValues;

public class MainActivity extends AppCompatActivity implements ICostanti {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();

        if (Utente.utenteLoggato()){
            if (Credenziali.validitaEmailProf(Utente.mAuth.getCurrentUser().getEmail()))
                startActivity(new Intent(MainActivity.this,MainActivityDocente.class));
            //else if (Credenziali.validitaEmailStudente(Utente.mAuth.getCurrentUser().getEmail())) //momentaneo
              //  startActivity(new Intent(MainActivity.this,MainActivityStudente.class)); //commento momentaneo
            else
                startActivity(new Intent(MainActivity.this,MainActivityStudente.class));//riga di test
                //Toast.makeText(this,"Errore in fase di riconoscimento email",Toast.LENGTH_SHORT).show(); //momentaneo
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextInputLayout edtEmail = findViewById(R.id.edt_email);//associo il TextInputLayout alla omologa variabile Java
        TextInputLayout edtPassword = findViewById(R.id.edt_password);

        TextView txtRegistrati = findViewById(R.id.txt_registrati);
        TextView txtOspite = findViewById(R.id.txt_ospite);

        Button btnAccedi = findViewById(R.id.btn_accedi);

        AspettoActivity aspettoActivity = new AspettoActivity(this);
        aspettoActivity.impostaColoreStatusBar(R.color.background, true);//imposto il colore della status bar


        btnAccedi.setOnClickListener(view -> { //invece di setOnClickListener si possono usare le espressioni lambda introdotte in Java 8 che sono più veloci da scrivere
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());

           // Intent intent = new Intent(this, MainActivityStudente.class); //test
           // startActivity(intent);///test
            login(email,pw);
        });


        txtOspite.setOnClickListener(view -> {


            /*------------Righe di test per passare direttamente alla schermata che mi serve-----------------*/
            Intent intent = new Intent(this, PrimoAccessoDocente.class);
            startActivity(intent);
            /*------------------------------------------------------------------------------------------------*/
            Toast.makeText(this, "Premuto accedi come ospite", Toast.LENGTH_SHORT).show();
        });

        txtRegistrati.setOnClickListener(view -> {
            Toast.makeText(this, "Premuto registrati", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityRegistrazione.class);
            startActivity(intent);
        });
    }

    private void login(String email,String pw){

    String collection = null;
    if (Credenziali.validitaEmailProf(email)) {
        collection = COLLECTION_PROF;
    }
    if (Credenziali.validitaEmailStudente(email)) {
        collection = COLLECTION_STUDENTI;
    }
    if ( collection != null) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(collection).document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        boolean primoAccesso = document.getBoolean("primo accesso");

                        login(primoAccesso,email,pw);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    } else {
        Toast.makeText(MainActivity.this, getString(R.string.inserisci_email_valida),
                Toast.LENGTH_SHORT).show();
    }

    }

    private void login(boolean primoAccesso,String email,String pw){
        Utente.mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//se il login è andato a buon fine
                            // if (mAuth.getCurrentUser().isEmailVerified()){//se è stata verificata l'email  //TEST togli il commento--------------------------
                            if (Credenziali.validitaEmailStudente(email)) {//se l'email è di uno studente
                                if (primoAccesso)
                                    startActivity(new Intent(MainActivity.this, ActivityPrimoAccessoLogin.class));
                                else
                                    startActivity(new Intent(MainActivity.this, MainActivityStudente.class));

                            } else if (Credenziali.validitaEmailProf(email)) {//se l'email è di un prof

                                if (primoAccesso)
                                    startActivity(new Intent(MainActivity.this, PrimoAccessoDocente.class));
                                else
                                    startActivity(new Intent(MainActivity.this, MainActivityDocente.class));

                            } else
                                startActivity(new Intent(MainActivity.this, MainActivityDocente.class));//TEST--

                            finish();
                            //Toast.makeText(getApplicationContext(),"Email non valida",Toast.LENGTH_SHORT).show();
                            // } else //TEST togli il commento****************************************
                            // Toast.makeText(MainActivity.this, "Per procedere verifica la tua email",Toast.LENGTH_SHORT).show(); //TEST  togli il commento****************************
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}