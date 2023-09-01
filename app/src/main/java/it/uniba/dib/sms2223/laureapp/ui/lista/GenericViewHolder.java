package it.uniba.dib.sms2223.laureapp.ui.lista;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.model.Task;


//C'è QUALCOSA DA COMPLETARE----------------------------------------------------------

public class GenericViewHolder extends RecyclerView.ViewHolder {

    //TOLTI I PULSANTI MODIFICA ED ELIMINA DAGLI ANNUNCI PERSONALE, METTERE TALI FUNZIONI AL CLICK SULL'ANNUNCIO E ACCORCIARE LA DATA
    //DI CREAZIONE CHE APPARE SULL'ANNUNCIO DEL PROFILO PERSONALE

    LinearLayout btnEliminaAnnuncio,btnModificaAnnuncio;
    private ImageView iconaApp;
    private final int tipoDilista;
    private RelativeLayout annuncio;
    //private ViewGroup layoutAnnuncio;

    public static final int LISTA_1 = 50;//lista dei risultati
    public static final int LISTA_2 = 51;//lista profilo personale

    public GenericViewHolder(@NonNull View view, int tipoDiLista) {
        super(view);
        this.tipoDilista = tipoDiLista;
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
                        CustomAdapter adapter, int posizione, FragmentAdapter fragmentAdapter){//gruppoStudio è l'annuncio

    }

}
