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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;
import android.content.ContentValues;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getEmail().endsWith("@studenti.uniba.it")) {
                startActivity(new Intent(this, MainActivityStudente.class));
                finish();
            } else if (currentUser.getEmail().endsWith("@uniba.it")) {
                Log.i(TAG, "Accesso professore!");
                startActivity(new Intent(this, MainActivityDocente.class));
                finish();
            }
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

        btnAccedi.setOnClickListener(view -> {
            String email = String.valueOf(edtEmail.getEditText().getText());
            String password = String.valueOf(edtPassword.getEditText().getText());

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user.getEmail().endsWith("@studenti.uniba.it")) {
                                    startActivity(new Intent(MainActivity.this, MainActivityStudente.class));
                                    finish();
                                } else if (user.getEmail().endsWith("@uniba.it")) {
                                    startActivity(new Intent(MainActivity.this, MainActivityDocente.class));
                                    finish();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }
                    });
        });
        /*
        btnAccedi.setOnClickListener(view -> { //invece di setOnClickListener si possono usare le espressioni lambda introdotte in Java 8 che sono più veloci da scrivere
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());


            if (new Utente().utenteLoggato()) {//controllo che l'utente abbia ricevuto esito positivo in fase di login
                Intent intent;
                if (new Credenziali().recuperaEmailUtente(email).endsWith("@studenti.uniba.it")) {//dalla email recuperata in fase di login controllo che sia uno studente
                    intent = new Intent(this, MainActivityStudente.class);
                    startActivity(intent);
                } else { //dalla email recuperata in fase di login controllo che sia un professore
                    intent = new Intent(this, MainActivityDocente.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Ho riscontrato un problema in fase di accesso" + pw, Toast.LENGTH_SHORT).show();// mostra il messagio di Toast
            }
        });
*/
        txtOspite.setOnClickListener(view -> {
            /*------------Righe di test per passare direttamente alla schermata che mi serve-----------------*/
            Intent intent = new Intent(this, ActivityPrimoAccessoLogin.class);
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
}