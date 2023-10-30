package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import it.uniba.dib.sms2223.laureapp.model.Professore;
import it.uniba.dib.sms2223.laureapp.model.Tesi;

public class CreaTesi extends AppCompatActivity { //da sistemare la parte xml con i giusti spazi tra gli elementi --------------------------------------------
    String emailProf;
    CheckBox[] checkBoxes;


    @Override
    protected void onStart() {
        super.onStart();
        //emailProf = FirebaseAuth.getInstance().getCurrentUser().getEmail();//serve qua? non basta nell'onCreate?
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_tesi);

      //  numTesi = getIntent().getIntExtra("num tesi",0);

        Toolbar toolbar = findViewById(R.id.toolbar_crea_tesi);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        String emailProfessore = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       // final Context context = this;
        final GridLayout gridView = findViewById(R.id.grid_view_insegnamenti);
        Spinner spinnerCorsi = findViewById(R.id.spinnerCorsoDiLaurea);
        Spinner spinnerAmbiti = findViewById(R.id.spinnerAmbito);
        Spinner spinnerCorelatori = findViewById(R.id.spinnerCorelatore);

        TextInputLayout edtTitolo = findViewById(R.id.edt_titolo_tesi);
        TextInputLayout edtDescrizione = findViewById(R.id.edt_Descrizione);
        TextInputLayout edtDurata = findViewById(R.id.edt_durata);

        Button btn_avanti = findViewById(R.id.btn_avanti);


        RadioGroup radioGroupTipo = findViewById(R.id.radioGroupTipo);



        db.collection("professori").whereNotEqualTo(FieldPath.documentId(), emailProfessore) //interrogo il db richiedendo i dati
                // di tutti i professori ad eccezione di quello loggato
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Professore> professorsList = new ArrayList<>();
                        Professore professoreNullo = new Professore();
                        professoreNullo.nome ="--";
                        professoreNullo.cognome ="--";
                        professoreNullo.email ="--";
                        professorsList.add(professoreNullo);
                        for (QueryDocumentSnapshot document : task.getResult()) { //recupero dal db i prof
                            Professore prof = new Professore(document.getString("nome"),document.getString("cognome"),document.getId(),null,null,null);
                            professorsList.add(prof);
                        }
                     Collections.sort(professorsList, new Professore());//ordina la lista dei corelatori per nome
                     setAdapterSpinner(spinnerCorelatori,professorsList);
                    }
                });


        spinnerCorsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String corsoSelezionato = parent.getSelectedItem().toString();
                if (corsoSelezionato.equalsIgnoreCase("Corso di informatica")) {
                    gridView.removeAllViews(); //rimuovo eventuali checkBox presenti
                    String[] checkBoxValues;
                    checkBoxValues = getResources().getStringArray(R.array.informtica);//R.array.ins_informatica
                    checkBoxes = new CheckBox[checkBoxValues.length];
                    Arrays.sort(checkBoxValues);
                    for (int i = 0; i < checkBoxValues.length; i++) {
                        //assegno ad ogni checkBox l'insegnamento e la aggiungo alla view
                        checkBoxes[i] = new CheckBox(getApplicationContext());
                        checkBoxes[i].setText(checkBoxValues[i]);
                        gridView.addView(checkBoxes[i]);
                    }
                } else if (corsoSelezionato.equalsIgnoreCase("I_T_P_S")) {
                    gridView.removeAllViews();
                    String[] checkBoxValues;
                    checkBoxValues = getResources().getStringArray(R.array.I_T_P_S_);
                    checkBoxes = new CheckBox[checkBoxValues.length];
                    Arrays.sort(checkBoxValues);
                    for (int i = 0; i < checkBoxValues.length; i++) {
                        checkBoxes[i] = new CheckBox(getApplicationContext());
                        checkBoxes[i].setText(checkBoxValues[i]);
                        gridView.addView(checkBoxes[i]);
                        Log.i(TAG, "Prova log: " + checkBoxes[i].getText());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(18); // Imposto il valore minimo del NumberPicker
        numberPicker.setMaxValue(30); // Imposto il valore massimo del NumberPicker
        numberPicker.setValue(18); // Imposto il valore di default
        numberPicker.setWrapSelectorWheel(false); // Disabilito lo scroll infinito




        btn_avanti.setOnClickListener(v -> {
            ArrayList<String> esamiRichiesti = new ArrayList<>();
            String titolo = String.valueOf(edtTitolo.getEditText().getText());
            String descrizione = String.valueOf(edtDescrizione.getEditText().getText());
            if (descrizione.isEmpty()) {
                descrizione = "Nessuna descrizione";//meglio nessuna descrizione perchè dopo da problemi nella lista, descrizione deve essere un campo obbligatorio quindi in ogni caso non deve creare problemi
            }
            String corelatore = spinnerCorelatori.getSelectedItem().toString();
            String corsoDiLaurea = spinnerCorsi.getSelectedItem().toString();
            String ambito = spinnerAmbiti.getSelectedItem().toString();
            int mediaRichiesta = numberPicker.getValue();
            int durata;
            String durataString = String.valueOf(edtDurata.getEditText().getText());

            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    String text = checkBox.getText().toString();
                    esamiRichiesti.add(text);
                }
            }

            int tipoTesiId = radioGroupTipo.getCheckedRadioButtonId();

            if (!titolo.isEmpty() && !ambito.isEmpty() && tipoTesiId != -1 && !durataString.isEmpty()) {
                RadioButton selectedTipoTesi = findViewById(tipoTesiId);
                String tipoTesi = selectedTipoTesi.getText().toString();
                durata = Integer.parseInt(durataString);
                Date dataCorrente = new Date();
                SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                //numTesi++;
                String dataPubblicazione = formatoData.format(dataCorrente);
                Tesi tesi = new Tesi(null,titolo, tipoTesi, descrizione, ambito, corsoDiLaurea,
                        dataPubblicazione, mediaRichiesta, durata, emailProfessore,
                        corelatore, esamiRichiesti);

                FirebaseFirestore db1 = FirebaseFirestore.getInstance();


                db1.collection("professori").document(emailProfessore).collection("Tesi")
                        .add(tesi) //se si vuole lasciare al sistema la creazione in automatico di un id per il documento usare collection().add()
                        //asltrimenti collection().document(ID documento).set()
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Tesi registrata" + " con successo!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CreaTesi.this,ActivityCreaTask.class).putExtra("idtesi",documentReference.getId()));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });


                /*FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                db1.collection("professori")
                        .document(emailProfessore).collection("Tesi").document()//.document("Tesi"+ tesi.id)
                        .set(tesi).addOnSuccessListener(unused -> {
                            Toast.makeText(getApplicationContext(), "Tesi registrata" + " con successo!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this,ActivityCreaTask.class));
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Ops, qualcosa è " + "andato storto!", Toast.LENGTH_LONG).show());
            */
            } else {
                Toast.makeText(getApplicationContext(), "Non dimenticarti di compilare" + " tutti i campi!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setAdapterSpinner(Spinner spinner, ArrayList arrayResource){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayResource);

// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
    }
}