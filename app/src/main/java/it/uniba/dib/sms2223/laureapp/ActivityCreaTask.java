package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.uniba.dib.sms2223.laureapp.model.Task;

public class ActivityCreaTask extends AppCompatActivity {

    public final String DA_COMPLETARE= "da completare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_task);

        boolean provenienzaDaListaTAskStudente = getIntent().getBooleanExtra("taskDocente",false);


        TextInputLayout edtOggettoTask = findViewById(R.id.edt_oggetto_task);
        TextInputLayout edtDescrizioneTask = findViewById(R.id.edt_desscrizione_task);

        Button btnAggiungi = findViewById(R.id.btn_aggiungi_task);
        Button btnSalvatesi = findViewById(R.id.btn_salva_tesi);
        Toolbar toolbar = findViewById(R.id.toolbar_task);

        if (provenienzaDaListaTAskStudente)
            btnSalvatesi.setVisibility(View.GONE);

        btnSalvatesi.setOnClickListener(view -> {
            startActivity(new Intent(this,MainActivityDocente.class));
            finish();
        });


        btnAggiungi.setOnClickListener(view -> {
            String oggetto = String.valueOf(edtOggettoTask.getEditText().getText());
            String descrizione = String.valueOf(edtDescrizioneTask.getEditText().getText());

            Date dataCorrente = new Date();
            SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String dataPubblicazione = formatoData.format(dataCorrente);

            Task task = new Task(oggetto,descrizione,dataPubblicazione,DA_COMPLETARE);
            salvatask(task);
        });
    }

    private void salvatask(Task task){
        String emailProfessore = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String idTesi = getIntent().getStringExtra("idtesi");
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        db1.collection("professori")
                .document(emailProfessore).collection("Tesi").document(idTesi).collection("TASK").
                add(task).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "TASK inserito" + " con successo!", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Ops, qualcosa è " + "andato storto!", Toast.LENGTH_LONG).show());


    }
}