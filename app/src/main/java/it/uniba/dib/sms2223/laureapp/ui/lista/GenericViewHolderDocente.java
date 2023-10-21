package it.uniba.dib.sms2223.laureapp.ui.lista;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.Universita;

public class GenericViewHolderDocente extends RecyclerView.ViewHolder{ //da completare
    public static final int LISTA_1 = 99;
    private TextView txtDip,txtCorso,txtMateria;
    private int tipoLista;
    private Button btnRimuoviInsegnamento;
    public GenericViewHolderDocente(@NonNull View itemView, int tipoLista) {
        super(itemView);
        Log.d("lista docente", "siamo nella lista del docente");

        //this.tipoLista = tipoLista;
        if (tipoLista == LISTA_1){
            txtDip = itemView.findViewById(R.id.txt_dipartimento);
            txtCorso = itemView.findViewById(R.id.txt_corso);
            txtMateria = itemView.findViewById(R.id.txt_insegnamento);
            btnRimuoviInsegnamento = itemView.findViewById(R.id.btn_elimina_insegnamento);
            Log.d("lista docente", "siamo nella lista del docente");
        }
    }

    public void setView(Universita universita, Context context, CustomAdapterListDocente adapter,int indice){
        txtDip.setText(universita.dipartimento);
        txtCorso.setText(universita.corso);
        txtMateria.setText(universita.insegnamento);

        btnRimuoviInsegnamento.setOnClickListener(view -> {
            if (new Utile(context).connessione())
                eliminainsegnamento(universita,adapter,context,indice);
            else
                Toast.makeText(context,"Nessuna connessione ad Internet, riprova",Toast.LENGTH_SHORT).show();

        });
    }

    private void eliminainsegnamento(Universita universita, CustomAdapterListDocente adapter,
                                     Context context,int indice){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String emailProf = "l.camicia@uniba.it";// solo per test, sostituire con i giusti metodi del firestore
        db.collection("professori").document(emailProf).
                collection("Insegnamento")
                .document(universita.id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.listaUniversita.remove(indice);
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
}
