package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import it.uniba.dib.sms2223.laureapp.ui.preferences.Impostazioni;

public class ProfiloStudente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_studente);

        Toolbar toolbar = findViewById(R.id.tlb_profilo);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenitore_impostazioni_frag, new Impostazioni())
                .commit();
    }
}