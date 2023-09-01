package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        Button btnAvanti = findViewById(R.id.btn_avanti);

        btnAvanti.setOnClickListener(view -> { //invece di setOnClickListener si possono usare le espressioni lambda introdotte in Java 8 che sono più veloci da scrivere
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());
            String confermaPw = String.valueOf(edtConfermaPassword.getEditText().getText());


            mAuth.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //invia l'email di verifica
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){// ancora non ho capito bene in base a cosa restituisce il valore
                                            Toast.makeText(getApplicationContext(),"utente registrato correttamente",Toast.LENGTH_SHORT ).show();// mostra il messagio di Toast
                                        }else
                                            Toast.makeText(getApplicationContext(),"Email non confermata",Toast.LENGTH_SHORT ).show();// mostra il messagio di Toast

                                    }
                                });

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(),"utente registrato " + user,Toast.LENGTH_SHORT ).show();// mostra il messagio di Toast

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            //fare i controlli con il DB per vedere se le credenziali sono corrette
            Toast.makeText(this,"email: " + email + "\npw: "+ pw,Toast.LENGTH_SHORT ).show();// mostra il messagio di Toast
        });
    }
}