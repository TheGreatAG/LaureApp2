package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;


public class MainActivity extends AppCompatActivity implements ICostanti {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();

        if (Utente.utenteLoggato()){
            if (Credenziali.validitaEmailProf(Utente.mAuth.getCurrentUser().getEmail()))
                startActivity(new Intent(MainActivity.this,MainActivityDocente.class));
            else
                startActivity(new Intent(MainActivity.this,MainActivityStudente.class));

            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        TextInputLayout edtEmail = findViewById(R.id.edt_email);
        TextInputLayout edtPassword = findViewById(R.id.edt_password);

        TextView txtRegistrati = findViewById(R.id.txt_registrati);
        TextView txtOspite = findViewById(R.id.txt_ospite);

        Button btnAccedi = findViewById(R.id.btn_accedi);

        AspettoActivity aspettoActivity = new AspettoActivity(this);
        aspettoActivity.impostaColoreStatusBar(R.color.background, true);


        btnAccedi.setOnClickListener(view -> {
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());

            login(email,pw);
        });


        txtOspite.setOnClickListener(view -> {

            Intent intent = new Intent(this, MainActivityStudente.class);
            startActivity(intent);
        });

        txtRegistrati.setOnClickListener(view -> {
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
                        boolean primoAccesso = document.getBoolean("primo accesso");

                        login(primoAccesso,email,pw);


                    } else {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
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
                            if (Credenziali.validitaEmailStudente(email)) {//se l'email è di uno studente
                                if (primoAccesso)
                                    startActivity(new Intent(MainActivity.this, ActivityPrimoAccessoLogin.class));
                                else
                                    startActivity(new Intent(MainActivity.this, MainActivityStudente.class));

                            } else if (Credenziali.validitaEmailProf(email)) {//se l'email è di un Docente

                                if (primoAccesso)
                                    startActivity(new Intent(MainActivity.this, PrimoAccessoDocente.class));
                                else
                                    startActivity(new Intent(MainActivity.this, MainActivityDocente.class));

                            } else
                                Toast.makeText(MainActivity.this, "Error",
                                        Toast.LENGTH_SHORT).show();

                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}