package it.uniba.dib.sms2223.laureapp.ui.lista;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.RichiestaTesiStudente;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.Corelatore;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.utils.QRCodeGenerator;


//C'è QUALCOSA DA COMPLETARE----------------------------------------------------------

public class GenericViewHolder extends RecyclerView.ViewHolder implements ICostanti {

    //TOLTI I PULSANTI MODIFICA ED ELIMINA DAGLI ANNUNCI PERSONALE, METTERE TALI FUNZIONI AL CLICK SULL'ANNUNCIO E ACCORCIARE LA DATA
    //DI CREAZIONE CHE APPARE SULL'ANNUNCIO DEL PROFILO PERSONALE

    Button btnDaCompletare,btnInLavorazione,btnCompletato;
    private TextView txtTitoloTask,txtDescrizioneTask,txtUltimaModifica,txtTitoloTesi,txtNomeRelatore,txtEmailRelatore
            ,txtCoRelatore,txtEmailCoRelatore,txtDescrizioneTesi;

    private Button btnRichiediTesi;
    private ImageButton btnPreferiti,btnCondividi;
    private final int tipoDilista;
    private RelativeLayout annuncio;
    //private ViewGroup layoutAnnuncio;

    public static final int LISTA_1 = 50;//lista dei risultati
    public static final int LISTA_2 = 51;//lista profilo personale

    public static final int LISTA_DOMANDE_RISPOSTE_LATO_STUD = 52;//lista profilo personale
    public static final int LISTA_TESI = 54;//lista tesi disponibili



    public GenericViewHolder(@NonNull View view, int tipoDiLista) {
        super(view);
        //QUA METTERE GLI IF PER CONTROLLARE IL TIPO DI LISTA E IN BASE A QUESTO INIZIALIZZARE LE VIEW CORRISPONDENTI CON FINDVIEWBYID()

        this.tipoDilista = tipoDiLista; //se la lista dei task del prof o dello studente
        txtTitoloTask = view.findViewById(R.id.txt_titolo_task);
        txtDescrizioneTask = view.findViewById(R.id.txt_descrizione_task);
        txtUltimaModifica = view.findViewById(R.id.txt_data_ultima_modifica);
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

        //corso = view.findViewById(R.id.corso_annuncio_txt_lista);
        //dipartimento = view.findViewById(R.id.dipartimento_annuncio_txt_lista);

        //REFERENZIARE GLI ELEMENTI DELLA LISTA CON CUI INTERAGIRE
      /*
        if (tipoDiLista == LISTA_1) {
            iconaApp = view.findViewById(R.id.img_app);
        }
        else if (tipoDiLista == LISTA_2) {
            btnEliminaAnnuncio = view.findViewById(R.id.btn_elimina);
            btnModificaAnnuncio = view.findViewById(R.id.btn_modifica);
        }*/
    }

    /**
     *inizializza la lista In base al Tipo. Se è una lista data da una ricerca o una lista del profilo personale
     * con i vari listener per i vari bottoni presenti sugli elementi della lista
     * @param task Oggetto task su cui prelevare i dati
     * @param context
     * @param listaAnnunci ArrayList contenenti i task
     * @param adapter l'adapter da assegnare per la vista della ReciclerView
     * @param posizione un intero per indicare la posizione corrente dell'elemento nella lista
     * @param fragmentAdapter di tipo FragmentAdapter serve per aggiornare i dati, ovvero il numero di annunci presenti
     *                        nel TabLayoutMediator presente nella classe HomeFragment
     */

    public void setView(Task task, Context context, ArrayList<Task> listaAnnunci,
                        CustomAdapterList adapter, int posizione, FragmentAdapter fragmentAdapter){

        txtTitoloTask.setText(task.titolo);
        txtDescrizioneTask.setText(task.descrizione);
        txtUltimaModifica.setText(task.ultimaModifica);

    }

    public void setView(Domanda domanda, Context context, ArrayList listaDomande,
                        CustomAdapterList adapter, int posizione, FragmentAdapter fragmentAdapter){
//inserire qui i valori da inizializzare nell'elenco della lista
       // txtTitoloTask.setText(task.titolo);
        //txtDescrizioneTask.setText(task.descrizione);
        //txtUltimaModifica.setText(task.ultimaModifica);

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


}
