package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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

import it.uniba.dib.sms2223.laureapp.model.Tesi;

public class CreaTesi extends AppCompatActivity {
    String idDocente;
    CheckBox[] checkBoxes;

    @Override
    protected void onStart() {
        super.onStart();
        idDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_tesi);

        Toolbar toolbar = findViewById(R.id.toolbar_crea_tesi);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        String idDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Context context = this;
        final GridLayout gridView = findViewById(R.id.grid_view_insegnamenti);
        Spinner spinnerCorsi = findViewById(R.id.spinnerCorsoDiLaurea);
        Spinner spinnerAmbiti = findViewById(R.id.spinnerAmbito);

        Spinner spinnerCorelatori = findViewById(R.id.spinnerCorelatore);
        TextInputLayout edtTitolo = findViewById(R.id.edt_titolo_tesi);
        TextInputLayout edtDescrizione = findViewById(R.id.edt_Descrizione);
        RadioGroup radioGroupTipo = findViewById(R.id.radioGroupTipo);
        TextInputLayout edtDurata = findViewById(R.id.edt_durata);

        HashMap<String, String> mapProf = new HashMap<>();
        mapProf.put("Nessun co-relatore", "Nessun co-relatore");
        ArrayList<String> profListSpinner = new ArrayList<>();
        profListSpinner.add("Nessun co-relatore"); //aggiungo come prima opzione "Nessun co-relatore"
        db.collection("professori").whereNotEqualTo(FieldPath.documentId(), idDocente) //interrogo il db richiedendo i dati
                // di tutti i professori ad eccezione di quello loggato
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> professorsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nome = document.getString("nome");
                            String cognome = document.getString("cognome");
                            String id = document.getId();
                            String label = cognome + " " + nome + ", " + id;
                            professorsList.add(label); //memorizzo nome, cognome e mail dei professori
                            mapProf.put(label, id);
                        }
                        Collections.sort(professorsList);
                        profListSpinner.addAll(professorsList); //aggiungo la lista ordinata dei professori
                        // alla lista delle opzioni da caricare nello spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreaTesi.this, android.R.layout.simple_spinner_item, profListSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCorelatori.setAdapter(adapter); //carico nello spinner le opzioni per il co-relatore
                    }
                });


        spinnerCorsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String corsoSelezionato = parent.getSelectedItem().toString();
                if (corsoSelezionato.equalsIgnoreCase("Informatica")) {
                    gridView.removeAllViews(); //rimuovo eventuali checkBox presenti
                    String[] checkBoxValues;
                    checkBoxValues = getResources().getStringArray(R.array.ins_informatica);
                    checkBoxes = new CheckBox[checkBoxValues.length];
                    Arrays.sort(checkBoxValues);
                    for (int i = 0; i < checkBoxValues.length; i++) {
                        //assegno ad ogni checkBox l'insegnamento e la aggiungo alla view
                        checkBoxes[i] = new CheckBox(context);
                        checkBoxes[i].setText(checkBoxValues[i]);
                        gridView.addView(checkBoxes[i]);
                    }
                } else if (corsoSelezionato.equalsIgnoreCase("Informatica e tecnologia" + " per la produzione del software")) {
                    gridView.removeAllViews();
                    String[] checkBoxValues;
                    checkBoxValues = getResources().getStringArray(R.array.ins_itps);
                    checkBoxes = new CheckBox[checkBoxValues.length];
                    Arrays.sort(checkBoxValues);
                    for (int i = 0; i < checkBoxValues.length; i++) {
                        checkBoxes[i] = new CheckBox(context);
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

        Button btn_avanti = findViewById(R.id.btn_avanti);
        btn_avanti.setOnClickListener(v -> {

            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            ArrayList<String> esamiRichiesti = new ArrayList<>();
            String titolo = String.valueOf(edtTitolo.getEditText().getText());
            String descrizione = String.valueOf(edtDescrizione.getEditText().getText());
            if (descrizione.isEmpty()) {
                descrizione = "Nessuna descrizione";
            }
            String corelatore = mapProf.get(spinnerCorelatori.getSelectedItem().toString());
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

                String dataPubblicazione = formatoData.format(dataCorrente);
                Tesi tesi = new Tesi(titolo, tipoTesi, descrizione, ambito, corsoDiLaurea, dataPubblicazione, mediaRichiesta, durata, idDocente, corelatore, esamiRichiesti);

                db1.collection("tesi").document(titolo + "_" + corsoDiLaurea).set(tesi).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Tesi registrata" + " con successo!", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Dati aggiunti al db");
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Ops, qualcosa è " + "andato storto!", Toast.LENGTH_LONG).show());
            } else {
                Toast.makeText(getApplicationContext(), "Non dimenticarti di compilare" + " tutti i campi!", Toast.LENGTH_LONG).show();
            }
        });
    }
}