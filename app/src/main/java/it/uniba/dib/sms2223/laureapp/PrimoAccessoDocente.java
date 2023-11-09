package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.model.Universita;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;

public class PrimoAccessoDocente extends AppCompatActivity implements ICostanti {



    private String dipartimento,corso,insegnamento;
    private ArrayList<Universita> listaInsegnamenti = new ArrayList<>();
    private CustomAdapterListDocente adapter;


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primo_accesso_docente);


        boolean b = getIntent().getBooleanExtra("chiamante",false);

        Toolbar toolbar = findViewById(R.id.toolbar_primo_accesso_docente);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (b && ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Button btnAggiungi = findViewById(R.id.btn_aggiungi_insegnamento);
        Button btnConferma = findViewById(R.id.btn_conferma);

        Spinner spnDipartimento = findViewById(R.id.spinner_dipartimento);
        Spinner spnCorso = findViewById(R.id.spinner_corso);
        Spinner spnInsegnamento = findViewById(R.id.spinner_insegnamento);

        RecyclerView lista = findViewById(R.id.lista_insegnamenti);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        lista.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        lista.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));

        adapter = new CustomAdapterListDocente(listaInsegnamenti,this, R.layout.layout_lista_insegnamenti_docente, GenericViewHolderDocente.LISTA_1,null);
        lista.setAdapter(adapter);

        setAdapterSpinner(spnDipartimento,R.array.department_options);

        recuperaInsegnamenti();


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
                setAdapterSpinner(spnInsegnamento,getArrayResource(corso));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnInsegnamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                insegnamento = spnInsegnamento.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.fatto) {
                    if (adapter.listaElementi.size()!=0)
                        startActivity(new Intent(PrimoAccessoDocente.this,MainActivityDocente.class));
                    else
                        Toast.makeText(getApplicationContext(), getString(R.string.insegnamenti_mancanti_avviso), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        btnAggiungi.setOnClickListener(view -> {
           // numInsegnamenti++;
            Universita universita = new Universita(dipartimento,corso,insegnamento,null/*"Insegnamento"+numInsegnamenti*/);
            salvaInsegnamenti(universita,b,adapter);

        });

        if (b)
            btnConferma.setVisibility(View.GONE);

        btnConferma.setOnClickListener(view -> {
            if (!b) {
                startActivity(new Intent(this, MainActivityDocente.class));
                finish();
            }
        });
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
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
    }

    /**
     * Salva gli insegnamenti del professore nel db
     * @param universita l'istanza contenente le info sull'insegnamento da salvare
     */
    private void salvaInsegnamenti(Universita universita,boolean b,CustomAdapterListDocente adapter) {
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("professori").document(emailDocente).collection(COLLECTION_INSEGNAMENTI).add(universita) //se si vuole lasciare al sistema la creazione in automatico di un id per il documento usare collection().add()
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Insegnamento inserito", Toast.LENGTH_SHORT).show();
                        universita.id = documentReference.getId();
                        adapter.listaElementi.add(universita);
                        adapter.notifyDataSetChanged();
                        Log.d("WWR", "" + adapter.listaElementi.size());

                        if (!b) {
                            new Utente().impostaValoreDiAccesso(ICostanti.COLLECTION_PROF, emailDocente);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }


    private void recuperaInsegnamenti(){
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("professori").document(emailDocente).collection("Insegnamento")//mi recuper tutti gli insegnamenti di un dato prof
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                             //   numInsegnamenti++;
                                Universita universita = new Universita(document.getString("dipartimento")
                                        ,document.getString("corso")
                                        ,document.getString("insegnamento")
                                        ,document.getId());

                                Log.d("WWE",""+adapter.listaElementi.size());
                                adapter.listaElementi.add(universita);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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
}