package it.uniba.dib.sms2223.laureapp.ui.lista;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
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
import it.uniba.dib.sms2223.laureapp.RichiestaTesiStudente;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.business.GestioneTask;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.Corelatore;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.utils.QRCodeGenerator;



public class GenericViewHolder extends RecyclerView.ViewHolder implements ICostanti {

    //TOLTI I PULSANTI MODIFICA ED ELIMINA DAGLI ANNUNCI PERSONALE, METTERE TALI FUNZIONI AL CLICK SULL'ANNUNCIO E ACCORCIARE LA DATA
    //DI CREAZIONE CHE APPARE SULL'ANNUNCIO DEL PROFILO PERSONALE

    private TextView txtTitoloTask,txtDescrizioneTask,txtUltimaModifica,txtTitoloTesi,txtNomeRelatore,txtEmailRelatore
            ,txtCoRelatore,txtEmailCoRelatore,txtDescrizioneTesi,txtDataDomanda,txtDataRisposta,txtDomanda,txtRisposta;

    private Button btnRichiediTesi,btnInviaRisposta,btnStatoTask;
    private ImageButton btnPreferiti,btnCondividi;
    private final int tipoDilista;
    private RelativeLayout annuncio;
    private LinearLayout lytRispostaDomanda,lytRispostaDocente,lytTaskConfermato,lytContenitoreBottoniTaskRelatore,lytBottoniStudente;
    //private ViewGroup layoutAnnuncio;
    private TextInputLayout edtRisposta;

    public static final int LISTA_TASK_LATO_RELATORE = 50;//lista dei risultati
    public static final int LISTA_2 = 51;//lista profilo personale

    protected MaterialButton btnDaCompletare,btnInLavorazione,btnCompletato,btnEliminaTask,btnConfermaStatoTask;
    public static final int LISTA_DOMANDE_RISPOSTE_LATO_STUD = 52;//lista profilo personale
    public static final int LISTA_TESI = 54;//lista tesi disponibili
    public static final int LISTA_DOMANDE_RISPOSTE_LATO_RELATORE =60;


    public GenericViewHolder(@NonNull View view, int tipoDiLista) {
        super(view);
        this.tipoDilista = tipoDiLista; //se la lista dei task del prof o dello studente

        //QUA METTERE GLI IF PER CONTROLLARE IL TIPO DI LISTA E IN BASE A QUESTO INIZIALIZZARE LE VIEW CORRISPONDENTI CON FINDVIEWBYID()
        if (tipoDiLista == LISTA_2 || tipoDiLista == LISTA_TASK_LATO_RELATORE) {
            txtTitoloTask = view.findViewById(R.id.txt_titolo_task);
            txtDescrizioneTask = view.findViewById(R.id.txt_descrizione_task);
            txtUltimaModifica = view.findViewById(R.id.txt_data_ultima_modifica);
            btnDaCompletare = view.findViewById(R.id.btn_da_completare);
            btnInLavorazione = view.findViewById(R.id.btn_in_lavorazione);
            btnCompletato = view.findViewById(R.id.btn_completato);
            lytTaskConfermato = view.findViewById(R.id.lyt_conferma_task);
            lytContenitoreBottoniTaskRelatore = view.findViewById(R.id.lyt_contenitore_btn_gestione_relatore);
            lytBottoniStudente = view.findViewById(R.id.lyt_contenitore_btn_di_stato);
            btnEliminaTask = view.findViewById(R.id.btn_elimina_task);
            btnConfermaStatoTask = view.findViewById(R.id.btn_conferma_completamento);
            btnStatoTask = view.findViewById(R.id.btn_stato_attualeTask);
        }
        if (tipoDiLista == LISTA_TESI || tipoDiLista == LISTA_TESI_PREFERITE){
            txtTitoloTesi = view.findViewById(R.id.txt_titolo_tesi);
            txtNomeRelatore = view.findViewById(R.id.txt_nome_relatore);
            txtEmailRelatore = view.findViewById(R.id.txt_email_relatore);
            txtCoRelatore = view.findViewById(R.id.txt_corelatore);
            txtEmailCoRelatore = view.findViewById(R.id.txt_email_corelatore);
            txtDescrizioneTesi = view.findViewById(R.id.txt_descrizione);
            btnRichiediTesi = view.findViewById(R.id.btn_richiedi_tesi);
            btnPreferiti = view.findViewById(R.id.btn_preferito);
            btnCondividi = view.findViewById(R.id.btn_condividi_info_tesi);
        }
        if (tipoDiLista == LISTA_DOMANDE_RISPOSTE_LATO_STUD || tipoDiLista == LISTA_DOMANDE_RISPOSTE_LATO_RELATORE){
            txtTitoloTask = view.findViewById(R.id.txt_num_task);
            txtDataDomanda = view.findViewById(R.id.txt_data_domanda);
            txtDataRisposta = view.findViewById(R.id.txt_data_risposta);
            txtDomanda = view.findViewById(R.id.txt_domanda);
            txtRisposta = view.findViewById(R.id.txt_risposta);
            lytRispostaDomanda = view.findViewById(R.id.lyt_risposta);

            lytRispostaDocente= view.findViewById(R.id.lyt_contenitore_risposta);
            btnInviaRisposta = view.findViewById(R.id.btn_rispondi);
            edtRisposta = view.findViewById(R.id.edt_risposta);


        }

    }

    /**
     *inizializza la lista In base al Tipo. Se è una lista data da una ricerca o una lista del profilo personale
     * con i vari listener per i vari bottoni presenti sugli elementi della lista
     * @param task Oggetto task su cui prelevare i dati
     * @param context
     * @param adapter l'adapter da assegnare per la vista della ReciclerView
     * @param posizione un intero per indicare la posizione corrente dell'elemento nella lista

     */

    public void setView(Task task, Context context,
                        CustomAdapterList adapter, int posizione, Tesi tesi){

        txtTitoloTask.setText(task.titolo);
        txtDescrizioneTask.setText(task.descrizione);
        txtUltimaModifica.setText(context.getString(R.string.ultima_modifica) + "\n" +task.ultimaModifica);

        if (task.confermaDefinitivaDelRelatore) {
            lytTaskConfermato.setVisibility(View.VISIBLE);
        }

        if (tipoDilista == LISTA_TASK_LATO_RELATORE){
            Log.d("RRD","nei task relatore");
            lytBottoniStudente.setVisibility(View.GONE);
            lytContenitoreBottoniTaskRelatore.setVisibility(View.VISIBLE);

            btnStatoTask.setText(task.stato);
            switch (task.stato){
                case TASK_DA_COMPLETARE:
                    btnStatoTask.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_da_completare)));
                    break;
                case TASK_IN_LAVORAZIONE:
                    btnStatoTask.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_in_lavorazione)));
                    break;
                case TASK_COMPLETATO:
                    btnStatoTask.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_completato)));
            }

            if (task.stato.equals(TASK_COMPLETATO))
                btnConfermaStatoTask.setVisibility(View.VISIBLE);

            btnConfermaStatoTask.setOnClickListener(view -> {
                new GestioneTask().contrassegnaTaskComeCompletato(lytTaskConfermato,tesi,task,context);
            });

            btnEliminaTask.setOnClickListener(view -> {
                new GestioneTask().eliminaTask(adapter,task,tesi,posizione,context);
            });


        } else {
            if (task.stato.equals(TASK_DA_COMPLETARE)){
                btnDaCompletare.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_da_completare)));
                btnDaCompletare.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else if (task.stato.equals(TASK_IN_LAVORAZIONE)){
                btnInLavorazione.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_in_lavorazione)));
                btnInLavorazione.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else if(task.stato.equals(TASK_COMPLETATO)){
                btnCompletato.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_completato)));
                btnCompletato.setTextColor(ContextCompat.getColor(context, R.color.white));

            }
        }


        btnDaCompletare.setOnClickListener(view -> {
            if (!task.confermaDefinitivaDelRelatore) {
                impostaStatoTask(task, TASK_DA_COMPLETARE, tesi, txtUltimaModifica, context);
                btnDaCompletare.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_da_completare)));
                btnDaCompletare.setTextColor(ContextCompat.getColor(context, R.color.white));

                btnInLavorazione.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                btnInLavorazione.setTextColor(ContextCompat.getColor(context, R.color.testo_task_in_lavorazione));
                btnCompletato.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                btnCompletato.setTextColor(ContextCompat.getColor(context, R.color.testo_task_completato));
            }

        });

        btnInLavorazione.setOnClickListener(view -> {
            if (!task.confermaDefinitivaDelRelatore) {
                impostaStatoTask(task, TASK_IN_LAVORAZIONE, tesi, txtUltimaModifica, context);

                btnInLavorazione.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_in_lavorazione)));
                btnInLavorazione.setTextColor(ContextCompat.getColor(context, R.color.white));

                btnDaCompletare.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                btnDaCompletare.setTextColor(ContextCompat.getColor(context, R.color.testo_task_da_completare));
                btnCompletato.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                btnCompletato.setTextColor(ContextCompat.getColor(context, R.color.testo_task_completato));
            }
        });

        btnCompletato.setOnClickListener(view -> {
            if (!task.confermaDefinitivaDelRelatore) {
                impostaStatoTask(task, TASK_COMPLETATO, tesi, txtUltimaModifica, context);

                btnCompletato.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.testo_task_completato)));
                btnCompletato.setTextColor(ContextCompat.getColor(context, R.color.white));

                btnInLavorazione.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                btnInLavorazione.setTextColor(ContextCompat.getColor(context, R.color.testo_task_in_lavorazione));
                btnDaCompletare.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                btnDaCompletare.setTextColor(ContextCompat.getColor(context, R.color.testo_task_da_completare));
            }
        });

    }

    public void setView(Domanda domanda, Context context, ArrayList listaDomande,
                        CustomAdapterList adapter, int posizione, FragmentAdapter fragmentAdapter){
        txtTitoloTask.setText("Task: " + domanda.titoloTask);
        txtDataDomanda.setText(domanda.dataDomanda);
        txtDomanda.setText(domanda.domanda);

        if (domanda.risposta != null) {
            lytRispostaDomanda.setVisibility(View.VISIBLE);
            txtDataRisposta.setText(context.getString(R.string.risposto_il)+ " " +domanda.dataRisposta);
            txtRisposta.setText(domanda.risposta);
        }
        if (domanda.risposta == null && tipoDilista == LISTA_DOMANDE_RISPOSTE_LATO_RELATORE) {
            lytRispostaDocente.setVisibility(View.VISIBLE);
        }

        btnInviaRisposta.setOnClickListener(view -> {//vale solo per relatore
            String risposta = String.valueOf(edtRisposta.getEditText().getText());
            inviaRispostaAllaDomanda(risposta,lytRispostaDocente,domanda,lytRispostaDomanda
                    ,txtRisposta,txtDataRisposta,context);
        });

    }

    public void setView(Tesi tesi, Context context, ArrayList listaTesiPreferite,
                        CustomAdapterList adapter, int posizione, FragmentAdapter fragmentAdapter){
        Corelatore corelatore;
        Log.d("ASD 2", "" +tesi.sCorelatore);

        if (tesi.sCorelatore != null) {
            String[] datiCoRelatore = tesi.sCorelatore.split(" ");
            corelatore = new Corelatore(datiCoRelatore[0], datiCoRelatore[1], datiCoRelatore[2]);
            Log.d("ASD 3",""+corelatore.nome);
            txtCoRelatore.setText(corelatore.nome + " " + corelatore.cognome);
            txtEmailCoRelatore.setText(corelatore.email);
        } else {
            txtCoRelatore.setVisibility(View.GONE);
            txtEmailCoRelatore.setVisibility(View.GONE);
        }
        txtTitoloTesi.setText(tesi.titolo);
        String datiRelatore []= tesi.sRelatore.split(" ");
        txtNomeRelatore.setText(datiRelatore[0] + " " +datiRelatore[1]);
        txtEmailRelatore.setText(datiRelatore[2]);
        txtDescrizioneTesi.setText(tesi.descrizione);

        btnCondividi.setOnClickListener(view -> {
            showPopup(btnCondividi,context,tesi);
        });

        btnRichiediTesi.setOnClickListener(view -> {
            context.startActivity(new Intent(context, RichiestaTesiStudente.class).putExtra("Tesi",tesi));
        });

        if (tipoDilista == LISTA_TESI_PREFERITE){

            btnPreferiti.setOnClickListener(view -> {
                rimuoviTesiDaiPrerferiti(btnPreferiti,tesi,context);
            });
        }

        if (tipoDilista == LISTA_TESI){
            Log.d("corelatore", " "+ tesi.corelatore);
            btnPreferiti.setBackground(context.getDrawable(R.drawable.ic_preferito));

            btnPreferiti.setOnClickListener(view -> {
                aggiungiTesiAiPreferiti(btnPreferiti,tesi,context);
            });
        }
    }

    private void aggiungiTesiAiPreferiti(ImageButton btnPreferiti,Tesi tesi,Context context){
        String emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseFirestore db1 = FirebaseFirestore.getInstance();

        db1.collection("studenti").document(emailStudente).collection("Tesi_preferite").document(tesi.id)
                .set(tesi)//se si vuole lasciare al sistema la creazione in automatico di un id per il documento usare collection().add()
                //asltrimenti collection().document(ID documento).set()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,context.getString(R.string.teasi_preferita_aggiunta) , Toast.LENGTH_LONG).show();
                        btnPreferiti.setBackground(context.getDrawable(R.drawable.ic_preferito_selezionato));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }

    private void rimuoviTesiDaiPrerferiti(ImageButton btnPreferiti,Tesi tesi,Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("studenti").document(emailStudente).
                collection("Tesi_preferite")
                .document(tesi.id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        btnPreferiti.setBackground(context.getDrawable(R.drawable.ic_preferito));
                        Toast.makeText(context,context.getString(R.string.tesi_no_preferiti),Toast.LENGTH_SHORT).show();

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

    private void showPopup(View v, Context context, Tesi tesi){//mostra il menu flottante con le opzioni di condivisione della tesi
        PopupMenu popupMenu = new PopupMenu(context,v);
        popupMenu.inflate(R.menu.popup_menu_condividi_tesi);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.btn_condividi_testo){
                    Toast.makeText(context,"Condividi",Toast.LENGTH_SHORT).show();
                    new Utile(context).condividiInfo(null,null, ICostanti.INVIO_TESTO,tesi.toString());
                    return true;

                } else if (menuItem.getItemId() == R.id.btn_mostra_qr) {
                    Toast.makeText(context,"elimina",Toast.LENGTH_SHORT).show();
                    Dialog customDialog = new Dialog(context);
                    customDialog.setContentView(R.layout.popup_qr);
                    ImageView imageView = customDialog.findViewById(R.id.imageView);
                    Bitmap qrCode = QRCodeGenerator.generateQRCode(tesi.toString(),500,500);
                    imageView.setImageBitmap(qrCode);
                    customDialog.show();
                    return true;

                }
                return false;
            }
        });
    }

    public void impostaStatoTask(Task task,String stato,Tesi tesi,TextView txtUltimaModifica,Context context){

        Log.d("ASD", COLLECTION_PROF + " "+ tesi.relatore.email+" "+COLLECTION_TESI+" "+tesi.id+ " " +COLLECTION_TASK+ " "+ task.id);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(ICostanti.COLLECTION_PROF)
                .document(tesi.relatore.email).collection(ICostanti.COLLECTION_TESI)
                .document(tesi.id).collection(COLLECTION_TASK).document(task.id);

        Date dataCorrente = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String ultimaModifica = formatoData.format(dataCorrente);

        Map<String, Object> updates = new HashMap<>();
        updates.put("stato", stato);
        updates.put("ultimaModifica", ultimaModifica);

// Esegui l'aggiornamento sul documento
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        txtUltimaModifica.setText(context.getString(R.string.ultima_modifica) + "\n" +ultimaModifica);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si è verificato un errore durante l'aggiornamento
                    }
                });
    }

    private void inviaRispostaAllaDomanda(String risposta, ViewGroup viewGroup,Domanda domanda
            ,ViewGroup viewGroup2,TextView txtRisposta,TextView txtDataRisposta,Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = db.collection(ICostanti.COLLECTION_PROF)
                .document(email).collection(ICostanti.COLLECTION_TESI)
                .document(domanda.tesiId).collection(COLLECTION_DOMANDE_TASK).document(domanda.id);

        Date dataCorrente = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String data = formatoData.format(dataCorrente);

        Map<String, Object> updates = new HashMap<>();
        updates.put("dataRisposta",data );
        updates.put("risposta", risposta);

// Esegui l'aggiornamento sul documento
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        viewGroup.setVisibility(View.GONE);
                        viewGroup2.setVisibility(View.VISIBLE);
                        txtRisposta.setText(risposta);
                        txtDataRisposta.setText(context.getString(R.string.risposto_il)+ " " +data);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si è verificato un errore durante l'aggiornamento
                    }
                });
    }


}
