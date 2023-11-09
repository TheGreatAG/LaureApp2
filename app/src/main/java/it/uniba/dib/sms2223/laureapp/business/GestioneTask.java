package it.uniba.dib.sms2223.laureapp.business;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.model.Tesi;

public class GestioneTask implements ICostanti {

    public void contrassegnaTaskComeCompletato(ViewGroup viewGroup, Tesi tesi, Task task, Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(COLLECTION_PROF)
                .document(tesi.relatore.email).collection(COLLECTION_TESI)
                .document(tesi.id).collection(COLLECTION_TASK).document(task.id);

        Date dataCorrente = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String ultimaModifica = formatoData.format(dataCorrente);

        Map<String, Object> updates = new HashMap<>();
        updates.put("confermaDefinitivaDelRelatore", true);
        updates.put("ultimaModifica", ultimaModifica);

// Esegui l'aggiornamento sul documento
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        viewGroup.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,context.getString(R.string.errore),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
