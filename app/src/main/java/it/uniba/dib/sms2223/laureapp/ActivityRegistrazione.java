package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ActivityRegistrazione extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        FirebaseAuth mAuth;
// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextInputLayout edtEmail = findViewById(R.id.edt_email);//associo il TextInputLayout alla omologa variabile Java
        TextInputLayout edtConfermaPassword = findViewById(R.id.edt_conferma_password);
        TextInputLayout edtPassword = findViewById(R.id.edt_password);
        TextInputLayout edtNome = findViewById(R.id.edt_nome);
        TextInputLayout edtCognome = findViewById(R.id.edt_Cognome);


        Button btnAvanti = findViewById(R.id.btn_avanti);
        TextView registrati = findViewById(R.id.txt_registrati);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference persona1Ref = db.collection("collectionProva").document("prova@mail.it");

        persona1Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String emailUtente = documentSnapshot.getId();
                    String nomeUtente=documentSnapshot.getString("nome");
                    registrati.setText("Prova riuscita!\n"+ emailUtente+" "+nomeUtente);
                } else {
                    // Il documento non esiste
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Gestisci errori durante il recupero dei dati
            }
        });

        btnAvanti.setOnClickListener(view -> { //invece di setOnClickListener si possono usare le espressioni lambda introdotte in Java 8 che sono più veloci da scrivere
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());
            String confermaPw = String.valueOf(edtConfermaPassword.getEditText().getText());
            String nome = String.valueOf(edtNome.getEditText().getText());
            String cognome = String.valueOf(edtCognome.getEditText().getText());


            if (pw.equals(confermaPw))  {
                mAuth.createUserWithEmailAndPassword(email, pw)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //invia l'email di verifica
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {// ancora non ho capito bene in base a cosa restituisce il valore
                                                registraNelDb(email, nome, cognome);
                                                //Toast.makeText(getApplicationContext(), "utente registrato correttamente", Toast.LENGTH_SHORT).show();// mostra il messagio di Toast
                                            } else
                                                Toast.makeText(getApplicationContext(), "Email non confermata", Toast.LENGTH_SHORT).show();// mostra il messagio di Toast

                                        }
                                    });

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user.getEmail().endsWith(getString(R.string.mail_studente))) {
                                        startActivity(new Intent(ActivityRegistrazione.this, MainActivityStudente.class));
                                        finish();
                                    } else if (user.getEmail().endsWith(getString(R.string.mail_docente))) {
                                        Log.i(TAG, "Accesso professore!");
                                        startActivity(new Intent(ActivityRegistrazione.this, MainActivityDocente.class));
                                        finish();
                                    }
                                    Toast.makeText(getApplicationContext(), "utente registrato " + user, Toast.LENGTH_SHORT).show();// mostra il messagio di Toast

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(this, "Le due password non coincidono!", Toast.LENGTH_SHORT).show();// mostra il messagio di Toast
            }
        });
    }

    private void registraNelDb(String email, String nome, String cognome) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("nome", nome);
        user.put("cognome", cognome);
        if (email.endsWith(getString(R.string.mail_studente))) {
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

        } else if (email.endsWith(getString(R.string.mail_docente))){

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