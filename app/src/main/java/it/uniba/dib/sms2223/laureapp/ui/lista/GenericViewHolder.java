package it.uniba.dib.sms2223.laureapp.ui.lista;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.model.Tesi;


//C'è QUALCOSA DA COMPLETARE----------------------------------------------------------

public class GenericViewHolder extends RecyclerView.ViewHolder {

    //TOLTI I PULSANTI MODIFICA ED ELIMINA DAGLI ANNUNCI PERSONALE, METTERE TALI FUNZIONI AL CLICK SULL'ANNUNCIO E ACCORCIARE LA DATA
    //DI CREAZIONE CHE APPARE SULL'ANNUNCIO DEL PROFILO PERSONALE

    Button btnDaCompletare,btnInLavorazione,btnCompletato;
    private TextView txtTitoloTask,txtDescrizioneTask,txtUltimaModifica,txtTitoloTesi,txtNomeRelatore,txtEmailRelatore
            ,txtCoRelatore,txtEmailCoRelatore,txtDescrizioneTesi;

    private Button btnRichiediTesi,btnPreferiti,btnCondividi;
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
        if (tipoDiLista == LISTA_TESI){
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
        if (tipoDilista == LISTA_TESI){
            Log.d("corelatore", " "+ tesi.corelatore);
            btnPreferiti.setBackground(context.getDrawable(R.drawable.ic_preferito));
            txtTitoloTesi.setText(tesi.titolo);
            //txtEmailRelatore.setText(null);
            if (tesi.corelatore !=null){
                txtCoRelatore.setText(tesi.corelatore.nome + " " + tesi.corelatore.cognome);
                txtEmailCoRelatore.setText(tesi.corelatore.email);

            } else {
                txtCoRelatore.setVisibility(View.GONE);
                txtEmailCoRelatore.setVisibility(View.GONE);
            }
            txtDescrizioneTesi.setText(tesi.descrizione);
        }
//inserire qui i valori da inizializzare nell'elenco della lista
        // txtTitoloTask.setText(task.titolo);
        //txtDescrizioneTask.setText(task.descrizione);
        //txtUltimaModifica.setText(task.ultimaModifica);

    }


}
