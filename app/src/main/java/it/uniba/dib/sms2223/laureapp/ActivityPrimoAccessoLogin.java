package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

public class ActivityPrimoAccessoLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primo_accesso_login);

        Button btnConferma = findViewById(R.id.btn_conferma);

        Spinner spnDipartimento = findViewById(R.id.spinner1);
        Spinner spnCorso = findViewById(R.id.spinner2);

        btnConferma.setOnClickListener(view -> {

        });
    }
}