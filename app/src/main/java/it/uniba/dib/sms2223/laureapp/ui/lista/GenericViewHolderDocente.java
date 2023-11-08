package it.uniba.dib.sms2223.laureapp.ui.lista;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import it.uniba.dib.sms2223.laureapp.FragmentHomeStudente;
import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.GestioneTesi;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.Persona;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
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
    private Button btnRimuoviInsegnamento,btnTesiProf,btnAccetta,btnRifiuta;

    private TextView txtData,txtTitoloTesi,txtCorsoDiLaurea,txtCorelatore,txtTipoTesi
            ,txtDescrizione,txtMediaVoti,txtTempoRichiesto,txtStudenteAssegnato,txtTaskDaSvolgere,
            txtMittente;
    private GridLayout gridLayout;

    private MaterialButton btnEliminaRicevimento,btnRispondi;
    ViewGroup elementoLista,espansioneLista;

    TextView txtMediaVotiStudente,txtNote,txtEmail,txtMediaVotiConsigliata;
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
                break;
            case LISTA_RICHIESTE_TESI:
                txtTitoloTesi = v.findViewById(R.id.txt_titolo_tesi);
                txtData = v.findViewById(R.id.txt_data_richiesta);
                txtMediaVotiConsigliata = v.findViewById(R.id.txt_voto_consigliato);
                txtMediaVotiStudente = v.findViewById(R.id.txt_voto_studente);
                txtNote = v.findViewById(R.id.txt_note_studente);
                txtEmail = v.findViewById(R.id.txt_email_studente);
                gridLayout = v.findViewById(R.id.lyt_contenitore_propedeu);
                btnAccetta = v.findViewById(R.id.btn_accetta);
                btnRifiuta = v. findViewById(R.id.btn_rifiuta);
                break;
            case LISTA_RICEVIMENTI_STUDENTI:
                txtMittente = v.findViewById(R.id.txt_mittente);
                txtTitoloTesi = v.findViewById(R.id.txt_oggetto);
                txtData = v.findViewById(R.id.txt_data_richiesta);
                txtDescrizione = v.findViewById(R.id.txt_descrizione);
                btnEliminaRicevimento = v.findViewById(R.id.btn_archivia);
                btnRispondi = v.findViewById(R.id.btn_rispondi);

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

    public void setView(Tesi tesi, Context context, CustomAdapterListDocente adapter, int indice,FragmentManager fragmentManager){ //da completare la condivisione della tesi

        //metterre alla pressione del btnTesiProf un menu con condividi, elimina
        btnTesiProf.setOnClickListener(view -> {
            showPopup(btnTesiProf,context,tesi,adapter,indice);
        });
        elementoLista.setOnClickListener(view -> {
            //apri i fragment di dettaglio
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_home_d, new FragmentHomeStudente(tesi), null)
                    // .setReorderingAllowed(true)
                    //  .addToBackStack(null)
                    .commit();
            Toast.makeText(context,"Elemento selezionato",Toast.LENGTH_SHORT).show();

        });
        txtData.setText(tesi.dataPubblicazione);
        txtTitoloTesi.setText(tesi.titolo);
        txtCorsoDiLaurea.setText(tesi.corsoDiLaurea);
        txtCorelatore.setText(tesi.corelatore.nome + " " + tesi.corelatore.cognome);
        txtTipoTesi.setText(tesi.tipo);
        txtTaskDaSvolgere.setText(String.valueOf(tesi.esamiRichiesti.size()));//passare un array di task
        txtDescrizione.setText(tesi.descrizione);
    }

    public void setView(Ricevimento ricevimento, Context context, CustomAdapterListDocente adapter, int indice) {
        txtMittente.setText(ricevimento.tesi.studente);
        txtTitoloTesi.setText(ricevimento.tesi.titolo);
        txtData.setText(ricevimento.data);
        txtDescrizione.setText(ricevimento.descrizione);
//        btnEliminaRicevimento ;
  //      btnRispondi ;
    }

    public void setView(RichiestaTesi richiestaTesi, Context context, CustomAdapterListDocente adapter, int indice) { //DA FINIRE COMPLETARE CON L'accetta e rifiuta e mostrare gli esami dati dallo studente
        txtData.setText(richiestaTesi.dataRichiesta);
        txtTitoloTesi.setText(richiestaTesi.tesi.titolo);
        txtMediaVotiConsigliata.setText(context.getString(R.string.media_consigliata) + ": "+String.valueOf(richiestaTesi.tesi.mediaRichiesta));
        txtMediaVotiStudente.setText(context.getString(R.string.media_voti_attuale) + ": " +String.valueOf(richiestaTesi.mediaVotiStudente));
        txtNote.setText(richiestaTesi.note);
        txtEmail.setText(richiestaTesi.studente.email);

        int indiceDaCuiRipartire =0;


        int propedeuticaIndex = 0; // Inizializza l'indice per scorrere richiestaTesi.propedeuticita

        for (int i = 0; i < richiestaTesi.tesi.esamiRichiesti.size(); i++) {
            String esameRichiesto = richiestaTesi.tesi.esamiRichiesti.get(i);

            MaterialButton testSignIn = new MaterialButton(context);
            testSignIn.setText(esameRichiesto);
            testSignIn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background)));

            if (propedeuticaIndex < richiestaTesi.propedeuticita.size() && esameRichiesto.equals(richiestaTesi.propedeuticita.get(propedeuticaIndex))) {//esame soddisfatto
                // L'esame corrisponde, imposta il colore corrispondente
               // testSignIn.setBackground(context.getDrawable(R.drawable.esame_soddisfatto));
                testSignIn.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.richiamo_azione)));
                testSignIn.setTextColor(ContextCompat.getColor(context, R.color.richiamo_azione));
                propedeuticaIndex++; // Passa all'elemento successivo di richiestaTesi.propedeuticita
            } else {
                // L'esame non corrisponde, imposta un altro colore
                //testSignIn.setBackground(AppCompatResources.getDrawable(context,R.drawable.esame_non_soddisfatto));

                testSignIn.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.rosso)));
                testSignIn.setTextColor(ContextCompat.getColor(context, R.color.rosso));
            }

            testSignIn.setStrokeWidth(3);
            gridLayout.addView(testSignIn);
        }

        btnAccetta.setOnClickListener(view -> {

            new GestioneTesi().assegnaTesi(richiestaTesi.tesi.relatore,richiestaTesi.studente,richiestaTesi,adapter,context);
        });

        btnRifiuta.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            new GestioneTesi().eliminaRichiesta(adapter,richiestaTesi,db);
        });
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
