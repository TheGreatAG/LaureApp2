package it.uniba.dib.sms2223.laureapp.business;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
import it.uniba.dib.sms2223.laureapp.model.RichiestaTesi;
import it.uniba.dib.sms2223.laureapp.model.Studente;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

public class GestioneTesi {

    public static void inviaRichiestaPerLaTesi(Context context, Tesi tesi, String note, Studente studente, double mediaVoti, int esamiMancanti, ArrayList listaEsamiRichiesti, Dialog dialog) {
        Date dataCorrente = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        //numTesi++;
        String dataRichiesta = formatoData.format(dataCorrente);


        if ((mediaVoti == 0.0 || esamiMancanti == 0) || (mediaVoti < 18 || esamiMancanti < 0)) {
            Toast.makeText(context, context.getString(R.string.campi_richiesta_mancanti), Toast.LENGTH_LONG).show();
        } else {
            RichiestaTesi richiestaTesi = new RichiestaTesi(studente, tesi, note, dataRichiesta, mediaVoti, esamiMancanti, listaEsamiRichiesti);
            //Log.d("FGF",richiestaTesi.toString());

            //invio i dati al db-----------
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String emailRelatore = tesi.sRelatore.split(" ")[2];
            Log.d("REE", emailRelatore);

            db.collection("professori").document(emailRelatore).collection("richieste").add(richiestaTesi) //se si vuole lasciare al sistema la creazione in automatico di un id per il documento usare collection().add()
                    //asltrimenti collection().document(ID documento).set()
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(context, context.getString(R.string.richiesta_inviata), Toast.LENGTH_LONG).show();
                            dialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, context.getString(R.string.errore), Toast.LENGTH_LONG).show();

                            //Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }

    public void aggiungiTesi() {
    }

    public void eliminaTesi(Tesi tesi, Context context, CustomAdapterListDocente adapter, int indice) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("professori").document(emailDocente).collection("Tesi").document(tesi.id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                adapter.listaElementi.remove(indice);
                adapter.notifyDataSetChanged();

                //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                Toast.makeText(context, "Insegnamento eliminato", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "ERRORE, riprova", Toast.LENGTH_SHORT).show();

                // Log.w(TAG, "Error deleting document", e);
            }
        });
    }

    public void assegnaTesi(Relatore relatore, Studente studente, RichiestaTesi richiestaTesi, CustomAdapterListDocente adpter, Context context, int indice) {
        // Ottieni un riferimento al documento da aggiornare

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(ICostanti.COLLECTION_PROF).document(relatore.email).collection(ICostanti.COLLECTION_TESI).document(richiestaTesi.tesi.id);

// Crea un oggetto Map con i campi da aggiornare
        Map<String, Object> updates = new HashMap<>();
        updates.put("studente", studente.email);

// Esegui l'aggiornamento sul documento
        documentReference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // L'aggiornamento è avvenuto con successo
                Toast.makeText(context, context.getString(R.string.tesi_assegnata), Toast.LENGTH_SHORT).show();
                eliminaRichiesta(adpter, richiestaTesi, db, indice, context);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, context.getString(R.string.errore), Toast.LENGTH_LONG).show();

                // Si è verificato un errore durante l'aggiornamento
            }
        });

    }

    public void eliminaRichiesta(CustomAdapterListDocente adapter, RichiestaTesi richiestaTesi, FirebaseFirestore db, int indice, Context context) {
        String emailProf = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection(ICostanti.COLLECTION_PROF).document(emailProf).collection(ICostanti.COLLECTION_RICHIESTE).document(String.valueOf(richiestaTesi.id)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                adapter.listaElementi.remove(indice);
                adapter.notifyDataSetChanged();
                if (adapter.listaElementi.size() == 0) {
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, context.getString(R.string.errore), Toast.LENGTH_LONG).show();

            }
        });
    }

    /**
     * imposta dei filtri sulle tesi
     *
     * @param adapter   l'adapter da cui estrarre le informazioni da filtrare
     * @param mediaVoti indica la media dei voti ricercata da una tesi
     * @param ore       indica il numero di ore di lavoro massimo stimato per una tesi
     * @param ambito    indica l'ambito della tesi che si interessa ricercare
     * @param context
     * @param nofiltri  un intero che se impostato a zero applica i filtri e mostra una nuova lista con le tesi che soddisfano i vincoli, se diverso da zero restituisce la lista delle tesi di partenza togliendo i filtri
     */
    public void filtraTesi(CustomAdapterList adapter, int mediaVoti, int ore, String ambito, Context context, int nofiltri, RecyclerView recyclerView) {
        ArrayList<Tesi> listaTesiCompleta = adapter.listaElementi;
        ArrayList<Tesi> listaTesiFiltrate = new ArrayList<>();

        if (nofiltri == 0) {
            if (!ambito.equals("--Tutti gli argomenti--")) {
                if (mediaVoti >= 18 && ore == 0) { // filtra solo per la media e l'ambito
                    Log.d("DRD", "nel secondo filtro");

                    for (int i = 0; i < listaTesiCompleta.size(); i++) {
                        Log.d("DRD", "media tesi [" + listaTesiCompleta.get(i).mediaRichiesta + "]" + " media inserita: " + mediaVoti + " ambito tesi [" + listaTesiCompleta.get(i).ambito + "]" + "ambito inserito " + ambito);

                        if (listaTesiCompleta.get(i).mediaRichiesta <= mediaVoti && listaTesiCompleta.get(i).ambito.equals(ambito))
                            listaTesiFiltrate.add(listaTesiCompleta.get(i));
                    }
                }
                if (mediaVoti >= 18 && ore != 0) { //filtra per la media, per le ore e l'ambito
                    Log.d("DRD", "nel terzo filtro");

                    for (int i = 0; i < listaTesiCompleta.size(); i++) {
                        if (listaTesiCompleta.get(i).mediaRichiesta <= mediaVoti && listaTesiCompleta.get(i).durata <= ore && listaTesiCompleta.get(i).ambito.equals(ambito))
                            listaTesiFiltrate.add(listaTesiCompleta.get(i));
                    }
                }
            } else {
                if (mediaVoti >= 18 && ore == 0) { // filtra solo per la media
                    Log.d("DRD", "nel secondo filtro");

                    for (int i = 0; i < listaTesiCompleta.size(); i++) {
                        Log.d("DRD", "media tesi [" + listaTesiCompleta.get(i).mediaRichiesta + "]" + " media inserita: " + mediaVoti + " ambito tesi [" + listaTesiCompleta.get(i).ambito + "]" + "ambito inserito " + ambito);

                        if (listaTesiCompleta.get(i).mediaRichiesta <= mediaVoti)
                            listaTesiFiltrate.add(listaTesiCompleta.get(i));
                    }
                }
                if (mediaVoti >= 18 && ore != 0) { //filtra per la media e per le ore
                    Log.d("DRD", "nel terzo filtro");

                    for (int i = 0; i < listaTesiCompleta.size(); i++) {
                        if (listaTesiCompleta.get(i).mediaRichiesta <= mediaVoti && listaTesiCompleta.get(i).durata <= ore)
                            listaTesiFiltrate.add(listaTesiCompleta.get(i));
                    }
                }
            }
            CustomAdapterList adapter1 = new CustomAdapterList(listaTesiFiltrate, context, R.layout.layout_lista_tesi_preferite, GenericViewHolder.LISTA_TESI, null);////////modificato con ultimo parametro
            recyclerView.setAdapter(adapter1);

        } else recyclerView.setAdapter(adapter);
        if (listaTesiFiltrate.size() == 0)
            Toast.makeText(context, "Non ci sono tesi", Toast.LENGTH_SHORT).show();
        // adapter.notifyDataSetChanged();

    }

    public Dialog impostaDialog(CustomAdapterList adapter, Context context, RecyclerView recyclerView) {
        //RichiestaTesi richiestaTesi = new RichiestaTesi();
        Dialog customDialog = new Dialog(context, R.style.CustomAlertDialog);
        customDialog.setContentView(R.layout.dialog_filtra_tesi);

        NumberPicker numberPicker = customDialog.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(18); // Imposto il valore minimo del NumberPicker
        numberPicker.setMaxValue(30); // Imposto il valore massimo del NumberPicker
        numberPicker.setValue(18); // Imposto il valore di default
        numberPicker.setWrapSelectorWheel(false); // Disabilito lo scroll infinito

        EditText edtOreLavoro = customDialog.findViewById(R.id.edt_ore_lavoro);
        Spinner spinnerAmbito = customDialog.findViewById(R.id.spinnerAmbito);

        MaterialButton btnNoFiltri = customDialog.findViewById(R.id.btn_no_filtri);

        Button btnApplicaFiltri = customDialog.findViewById(R.id.btn_applica_filtri);


        btnApplicaFiltri.setOnClickListener(view -> {
            int mediaConsigliata = numberPicker.getValue();
            int oreLavoroMassime = 0;
            if (!edtOreLavoro.getText().toString().equals(""))
                oreLavoroMassime = Integer.parseInt(edtOreLavoro.getText().toString());
            String ambito = spinnerAmbito.getSelectedItem().toString();

            new GestioneTesi().filtraTesi(adapter, mediaConsigliata, oreLavoroMassime, ambito, context, 0, recyclerView);
            customDialog.dismiss();
        });

        btnNoFiltri.setOnClickListener(view -> {
            new GestioneTesi().filtraTesi(adapter, 0, 0, null, context, 5, recyclerView);
            customDialog.dismiss();

        });


        return customDialog;
    }

    public void modificaTesi() {
    }

    public void consegnaTesi() {
    }
}
