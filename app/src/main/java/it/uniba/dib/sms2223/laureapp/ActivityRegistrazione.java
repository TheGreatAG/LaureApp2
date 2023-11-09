package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.Utente;

public class ActivityRegistrazione extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        mAuth = FirebaseAuth.getInstance();


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


        btnAvanti.setOnClickListener(view -> {
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());
            String confermaPw = String.valueOf(edtConfermaPassword.getEditText().getText());
            String nome = String.valueOf(edtNome.getEditText().getText());
            String cognome = String.valueOf(edtCognome.getEditText().getText());

            new Utente(this).registraUtente(nome,cognome,email,pw,confermaPw);

        });
    }

}