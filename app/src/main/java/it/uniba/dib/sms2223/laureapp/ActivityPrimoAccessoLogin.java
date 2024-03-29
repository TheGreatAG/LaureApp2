package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.model.Universita;

public class ActivityPrimoAccessoLogin extends AppCompatActivity implements ICostanti {
    private String dipartimento,corso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primo_accesso_login);

        Button btnConferma = findViewById(R.id.btn_conferma);

        Spinner spnDipartimento = findViewById(R.id.spinner1);
        Spinner spnCorso = findViewById(R.id.spinner2);

        Toolbar toolbr = findViewById(R.id.tlb_corso_stud);

        boolean b = getIntent().getBooleanExtra("key",false);

        if (b) {//se provengo dalla schermata profilo aggiungo la freccia di navigazione nella toolbar
            setSupportActionBar(toolbr);
            ActionBar ab = getSupportActionBar();
            if (ab != null)
                ab.setDisplayHomeAsUpEnabled(true);
        }

        setAdapterSpinner(spnDipartimento,R.array.department_options);

        spnDipartimento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dipartimento = spnDipartimento.getSelectedItem().toString();
                setAdapterSpinner(spnCorso,getArrayResource(dipartimento));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnCorso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                corso = spnCorso.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnConferma.setOnClickListener(view -> {
            inserisciCorsoStudenteNelDb(new Universita(dipartimento,corso,null,"c_s"),b);
        });
    }

    private void inserisciCorsoStudenteNelDb(Universita universita,boolean b){
        String emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> uni = new HashMap<>();
        uni.put("Dipartimento", universita.dipartimento);
        uni.put("Corso", universita.corso);

        db.collection(COLLECTION_STUDENTI).document(emailStudente).
                collection("Corso").document(universita.id)
                .set(uni)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!b) {
                            new Utente().impostaValoreDiAccesso(ICostanti.COLLECTION_STUDENTI, emailStudente);
                            startActivity(new Intent(ActivityPrimoAccessoLogin.this, MainActivityStudente.class));
                            finish();
                        }
                        Toast.makeText(getApplicationContext(),"Corso di studi inserito nel tuo profilo",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"ERRORE riprova",Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private int getArrayResource(String nomeArray){
        switch (nomeArray){
            case "Informatica":
                return R.array.Informatica;
            case "Corso di informatica":
                return R.array.informtica;
            case "I.T.P.S.":
                return R.array.I_T_P_S_;
        }
        return 0;
    }


    /**
     * imposta l'adapter per lo spinner
     * @param spinner lo spinner al quale applicare l'adapter
     * @param arrayResource la risorsa array da passare all'adapter
     */
    private void setAdapterSpinner(Spinner spinner, int arrayResource){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResource,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}