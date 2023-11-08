package it.uniba.dib.sms2223.laureapp.business;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.ActivityCreaTask;
import it.uniba.dib.sms2223.laureapp.CreaTesi;
import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
import it.uniba.dib.sms2223.laureapp.model.RichiestaTesi;
import it.uniba.dib.sms2223.laureapp.model.Studente;
import it.uniba.dib.sms2223.laureapp.model.Tesi;

public class GestioneTesi {

    public void aggiungiTesi(){}

    public void eliminaTesi(Tesi tesi, Context context, CustomAdapterListDocente adapter,int indice){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("professori").document(emailDocente).
                collection("Tesi")
                .document(tesi.id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.listaElementi.remove(indice);
                        adapter.notifyDataSetChanged();

                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(context,"Insegnamento eliminato",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"ERRORE, riprova",Toast.LENGTH_SHORT).show();

                        // Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public static void inviaRichiestaPerLaTesi(Context context, Tesi tesi, String note, Studente studente,
                                               double mediaVoti, int esamiMancanti, ArrayList listaEsamiRichiesti, Dialog dialog){
        Date dataCorrente = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        //numTesi++;
        String dataRichiesta = formatoData.format(dataCorrente);


        if ((mediaVoti == 0.0 || esamiMancanti ==0) || (mediaVoti <18 || esamiMancanti <0)){
            Toast.makeText(context, context.getString(R.string.campi_richiesta_mancanti), Toast.LENGTH_LONG).show();
        } else {
            RichiestaTesi richiestaTesi = new RichiestaTesi(studente, tesi, note, dataRichiesta, mediaVoti, esamiMancanti, listaEsamiRichiesti);
            //Log.d("FGF",richiestaTesi.toString());

            //invio i dati al db-----------
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String emailRelatore =tesi.sRelatore.split(" ")[2];
            Log.d("REE",emailRelatore);

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

    public void assegnaTesi(Relatore relatore,Studente studente,RichiestaTesi richiestaTesi,
                            CustomAdapterListDocente adpter,Context context,int indice){
        // Ottieni un riferimento al documento da aggiornare

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(ICostanti.COLLECTION_PROF)
                .document(relatore.email).collection(ICostanti.COLLECTION_TESI)
                .document(richiestaTesi.tesi.id);

// Crea un oggetto Map con i campi da aggiornare
        Map<String, Object> updates = new HashMap<>();
        updates.put("studente", studente.email);

// Esegui l'aggiornamento sul documento
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // L'aggiornamento è avvenuto con successo
                        Toast.makeText(context, context.getString(R.string.tesi_assegnata) ,Toast.LENGTH_SHORT).show();
                        eliminaRichiesta(adpter,richiestaTesi,db,indice,context);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, context.getString(R.string.errore), Toast.LENGTH_LONG).show();

                        // Si è verificato un errore durante l'aggiornamento
                    }
                });

    }

    public void eliminaRichiesta(CustomAdapterListDocente adapter, RichiestaTesi richiestaTesi, FirebaseFirestore db, int indice,Context context){
        String emailProf = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection(ICostanti.COLLECTION_PROF).document(emailProf).collection(ICostanti.COLLECTION_RICHIESTE)
                .document(String.valueOf(richiestaTesi.id))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.listaElementi.remove(indice);
                        adapter.notifyDataSetChanged();
                        if (adapter.listaElementi.size()==0){
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, context.getString(R.string.errore), Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void modificaTesi(){}
    public void consegnaTesi(){}
}
