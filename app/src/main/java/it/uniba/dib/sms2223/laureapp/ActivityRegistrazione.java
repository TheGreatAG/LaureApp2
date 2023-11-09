package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.Utente;

public class ActivityRegistrazione extends AppCompatActivity { //da finire, deve poter salvare il nom e cognome del prof e studente

    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // controlla se l'utente è gia registrato --------------------------------------------------------------------------------
       // FirebaseUser currentUser = mAuth.getCurrentUser();
       // if(currentUser != null){
        //    reload();
       // }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        mAuth = FirebaseAuth.getInstance();// Initialize Firebase Auth

        //ma va Meglio la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_registrazione);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        TextInputLayout edtEmail = findViewById(R.id.edt_email);//associo il TextInputLayout alla omologa variabile Java
        TextInputLayout edtConfermaPassword = findViewById(R.id.edt_conferma_password);
        TextInputLayout edtPassword = findViewById(R.id.edt_password);
        TextInputLayout edtNome = findViewById(R.id.edt_nome);
        TextInputLayout edtCognome = findViewById(R.id.edt_Cognome);


        Button btnAvanti = findViewById(R.id.btn_avanti);
        TextView registrati = findViewById(R.id.txt_registrati);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference persona1Ref = db.collection("collectionProva").document("prova@mail.it");


        btnAvanti.setOnClickListener(view -> { //invece di setOnClickListener si possono usare le espressioni lambda introdotte in Java 8 che sono più veloci da scrivere
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());
            String confermaPw = String.valueOf(edtConfermaPassword.getEditText().getText());
            String nome = String.valueOf(edtNome.getEditText().getText());
            String cognome = String.valueOf(edtCognome.getEditText().getText());

            new Utente(this).registraUtente(nome,cognome,email,pw,confermaPw);

        });
    }

    private void registraNelDb(String email, String nome, String cognome) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("nome", nome);
        user.put("cognome", cognome);
        if (Credenziali.validitaEmailStudente(email)) {
            db.collection("studenti").document(email).set(user).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, email + " studente aggiunto con successo");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Errore!", e);
                        }
                    });

        } else if (Credenziali.validitaEmailProf(email)){

            db.collection("professori").document(email).set(user).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, email + " professore aggiunto con successo");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Errore!", e);
                        }
                    });
        } else {
            db.collection("collectionProva").document(email).set(user).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, email + " user aggiunto con successo");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Errore!", e);
                        }
                    });
        }

    }
}