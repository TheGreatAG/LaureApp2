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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Universita;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;

public class PrimoAccessoDocente extends AppCompatActivity implements ICostanti {

    //METTERE il pulsante back nella toolbar solo se si accede dalla home


    //prima di mandare l'app in produzione sistemare la faccenda del recupero email per ottenere gli insegnamenti
    private String dipartimento,corso,insegnamento;
    private ArrayList<Universita> listaInsegnamenti = new ArrayList<>();
    private CustomAdapterListDocente adapter;

    private int numInsegnamenti; //salvare su file questo valore per le future modifiche dal profilo del prof?

    private boolean activityVisitata = false;
    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primo_accesso_docente);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        activityVisitata = sharedPreferences.getBoolean(PRIMO_ACCESSO_DOCENTE,false);

        boolean b = getIntent().getBooleanExtra("chiamante",false);
        Toolbar toolbar = findViewById(R.id.toolbar_primo_accesso_docente);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (activityVisitata && !b) {//se non è la prima volta che vedo questa schermata si passa all'activity successiva, se invece accedo a questa schermata dall'activity home del docente mi fa rimanere
            startActivity(new Intent(this, MainActivityDocente.class));
            finish();
        }else if (ab !=null){
            ab.setDisplayHomeAsUpEnabled(true);

        }

        Button btnAggiungi = findViewById(R.id.btn_aggiungi_insegnamento);

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

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PRIMO_ACCESSO_DOCENTE,true);
        editor.apply();//non è un'azione importante quindi uso apply()
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
            numInsegnamenti++;
            Universita universita = new Universita(dipartimento,corso,insegnamento,"Insegnamento"+numInsegnamenti);
            salvaInsegnamenti(universita);

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
    private void salvaInsegnamenti(Universita universita){//da finire non funziona
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> uni = new HashMap<>();
        uni.put("ID", universita.id);
        uni.put("Dipartimento", universita.dipartimento);
        uni.put("Corso", universita.corso);
        uni.put("Insegnamento", universita.insegnamento);

        db.collection("professori").document(emailDocente).
                collection("Insegnamento").document(universita.id)
                .set(uni)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Insegnamento inserito",Toast.LENGTH_SHORT).show();
                        adapter.listaElementi.add(universita);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "DocumentSnapshot successfully written!");
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

    private void recuperaInsegnamenti(){//da finire vedi nome utente
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("professori").document(emailDocente).collection("Insegnamento")//mi recuper tutti gli insegnamenti di un dato prof
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                numInsegnamenti++;
                                Universita universita = new Universita(document.get("Dipartimento").toString()
                                        ,document.get("Corso").toString()
                                        ,document.get("Insegnamento").toString()
                                        ,document.get("ID").toString());
                                adapter.listaElementi.add(universita);
                                adapter.notifyDataSetChanged();
                                Log.d("contenuto", document.getId() + " => " + document.get("Dipartimento").toString());
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