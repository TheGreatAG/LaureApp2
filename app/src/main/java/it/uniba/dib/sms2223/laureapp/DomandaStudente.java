package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

public class DomandaStudente extends AppCompatActivity implements ICostanti {

    private ArrayList<Task> listaTask = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domanda_studente);

        Tesi tesi = getIntent().getExtras().getParcelable("Tesi");

        AspettoActivity aspettoActivity = new AspettoActivity(this);
        aspettoActivity.impostaColoreStatusBar(R.color.background, true);//imposto il colore della status bar



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar  ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        Spinner spinner = findViewById(R.id.spn_selezionaTask);
        TextInputLayout edtDomanda = findViewById(R.id.edt_domanda);
        MaterialButton btnInvia = findViewById(R.id.btn_invia);
        TextView txtInviaA = findViewById(R.id.txt_destinatario);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        LinearLayout lytContenitoreForm = findViewById(R.id.lyt_contenitore1);

        String [] infoRelatore = tesi.sRelatore.split(" ");
        Relatore relatore = new Relatore(infoRelatore[0],infoRelatore[1],infoRelatore[2],null,null,null);
        tesi.relatore = relatore;
        txtInviaA.setText(getString(R.string.invia_a)+ ": "+ infoRelatore[0]+ " " +infoRelatore[1]);

        recuperaTask(tesi,spinner,progressBar,lytContenitoreForm);

        btnInvia.setOnClickListener(view -> {
            String domanda = String.valueOf(edtDomanda.getEditText().getText());
            inviaDomanda(spinner,tesi,domanda);
        });
    }

    private void recuperaTask(Tesi tesi, Spinner spinner, ProgressBar progressBar, ViewGroup viewGroup){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_PROF).document(tesi.relatore.email).
                collection(COLLECTION_TESI).document(tesi.id).collection(COLLECTION_TASK)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> listaTitoliTask = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String descrizione=document.getString("descrizione");
                                String id=document.getId();
                                String stato=document.getString("stato");
                                String titolo=document.getString("titolo");
                                String ultimaModifica=document.getString("ultimaModifica");
                                Task task1 = new Task(id,titolo,descrizione,ultimaModifica,stato);
                                listaTask.add(task1);
                                listaTitoliTask.add(titolo);
                            }
                            progressBar.setVisibility(View.GONE);
                            viewGroup.setVisibility(View.VISIBLE);
                            // Creare un ArrayAdapter con la lista di task
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, listaTitoliTask);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);

                        }
                    }
                });
    }

    private void inviaDomanda(Spinner spinner,Tesi tesi,String domanda){
        Log.d("WWE",""+spinner.getFirstVisiblePosition());

        Date dataCorrente = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dataDomanda = formatoData.format(dataCorrente);

        Domanda domanda1 = new Domanda(dataDomanda,null,domanda,null
                ,listaTask.get(spinner.getFirstVisiblePosition()).titolo,listaTask.get(spinner.getFirstVisiblePosition()).id,null,tesi.id);

        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        db1.collection(COLLECTION_PROF).document(tesi.relatore.email).collection(COLLECTION_TESI)
                .document(tesi.id).collection(COLLECTION_DOMANDE_TASK)
                .add(domanda1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), getString(R.string.domanda_inviata), Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.errore_domanda_inviata), Toast.LENGTH_LONG).show();

                    }
                });
    }
}