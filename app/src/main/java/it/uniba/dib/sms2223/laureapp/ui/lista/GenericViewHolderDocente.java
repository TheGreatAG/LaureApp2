package it.uniba.dib.sms2223.laureapp.ui.lista;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.GestioneTesi;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.Ricevimento;
import it.uniba.dib.sms2223.laureapp.model.RichiestaTesi;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.model.Universita;

public class GenericViewHolderDocente extends RecyclerView.ViewHolder{ //da completare
    public static final int LISTA_1 = 99;
    public static final int LISTA_TESI_PROF_HOME = 100;
    public static final int LISTA_RICEVIMENTI_STUDENTI = 101;
    public static final int LISTA_RICHIESTE_TESI = 102;


    private TextView txtDip,txtCorso,txtMateria;
    private int tipoLista;
    private Button btnRimuoviInsegnamento,btnTesiProf;

    private TextView txtData,txtTitoloTesi,txtCorsoDiLaurea,txtCorelatore,txtTipoTesi
            ,txtDescrizione,txtMediaVoti,txtTempoRichiesto,txtStudenteAssegnato,txtTaskDaSvolgere;
    private GridLayout gridLayout;
    ViewGroup elementoLista,espansioneLista;
    public GenericViewHolderDocente(@NonNull View v, int tipoLista) {
        super(v);
        Log.d("lista docente", "siamo nella lista del docente");

        //this.tipoLista = tipoLista;
        switch (tipoLista) {
            case LISTA_1:
                txtDip = v.findViewById(R.id.txt_dipartimento);
                txtCorso = v.findViewById(R.id.txt_corso);
                txtMateria = v.findViewById(R.id.txt_insegnamento);
                btnRimuoviInsegnamento = v.findViewById(R.id.btn_elimina_insegnamento);
                Log.d("lista docente", "siamo nella lista del docente");
            break;
            case LISTA_TESI_PROF_HOME: //alcune view non servono vedi quale togliere
                txtData = v.findViewById(R.id.txt_data_pubblicazione);
                txtTitoloTesi = v.findViewById(R.id.txt_titolo_tesi);
                txtCorsoDiLaurea = v.findViewById(R.id.txt_valore_corso);
                txtCorelatore = v.findViewById(R.id.txt_valore_corelatore);
                txtTipoTesi = v.findViewById(R.id.txt_valore_tipo);
                txtTaskDaSvolgere = v.findViewById(R.id.txt_valore_task);
                txtDescrizione = v.findViewById(R.id.txt_descrizione_tesi);
                btnTesiProf = v.findViewById(R.id.btn_tesi_prof);
                elementoLista = v.findViewById(R.id.lyt_elemento);
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

    public void setView(Tesi tesi, Context context, CustomAdapterListDocente adapter, int indice){ //da completare la condivisione della tesi

        //metterre alla pressione del btnTesiProf un menu con condividi, elimina
        btnTesiProf.setOnClickListener(view -> {
            showPopup(btnTesiProf,context,tesi,adapter,indice);
        });
        elementoLista.setOnClickListener(view -> {
            Toast.makeText(context,"Elemento selezionato",Toast.LENGTH_SHORT).show();

        });
        txtData.setText(tesi.dataPubblicazione);
        txtTitoloTesi.setText(tesi.titolo);
        txtCorsoDiLaurea.setText(tesi.corsoDiLaurea);
        txtCorelatore.setText(tesi.corelatore);
        txtTipoTesi.setText(tesi.tipo);
        txtTaskDaSvolgere.setText(String.valueOf(tesi.esamiRichiesti.size()));//passare un array di task
        txtDescrizione.setText(tesi.descrizione);
    }

    public void setView(Ricevimento tesi, Context context, CustomAdapterListDocente adapter, int indice) {

    }

    public void setView(RichiestaTesi tesi, Context context, CustomAdapterListDocente adapter, int indice) {

    }




    private void eliminainsegnamento(Universita universita, CustomAdapterListDocente adapter,
                                     Context context,int indice){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("professori").document(emailDocente).
                collection("Insegnamento")
                .document(universita.id)
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

    private void showPopup(View v,Context context, Tesi tesi,CustomAdapterListDocente adapter,int indice){
        PopupMenu popupMenu = new PopupMenu(context,v);
        popupMenu.inflate(R.menu.popup_menu_lista_tesi_prof);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.btn_condividi){
                    Toast.makeText(context,"Condividi",Toast.LENGTH_SHORT).show();
                    return true;

                } else if (menuItem.getItemId() == R.id.btn_elimina) {
                    Toast.makeText(context,"elimina",Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                            .setTitle("Elimina tesi")
                            .setMessage("Sicuro di voler eliminare questa tesi?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("ELIMINA TESI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new GestioneTesi().eliminaTesi(tesi,context,adapter,indice);

                                }
                            })
                            .setNegativeButton("ANNULLA", null)
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;

                }
                return false;
            }
        });
    }
}
