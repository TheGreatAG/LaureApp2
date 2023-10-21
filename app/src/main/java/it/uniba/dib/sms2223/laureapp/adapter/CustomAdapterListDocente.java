package it.uniba.dib.sms2223.laureapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.model.Universita;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;

public class CustomAdapterListDocente extends RecyclerView.Adapter<GenericViewHolderDocente>{

    public ArrayList <Universita>listaUniversita;

    private Context context;
    private int layoutLista;
    private int tipoDiLista;  //flag per capire se la lista è lato prof o studente
    private FragmentAdapter fragmentAdapter;///

    public CustomAdapterListDocente(final ArrayList listaAnnunci, Context context, int layoutLista, int tipoDiLista, FragmentAdapter fragmentAdapter){//
        this.context = context;
        this.layoutLista = layoutLista;
        this.listaUniversita = listaAnnunci;
        this.tipoDiLista = tipoDiLista;  //decommenta
        this.fragmentAdapter = fragmentAdapter;
    }

    @NonNull
    @Override
    public GenericViewHolderDocente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//non viene invocato sempre come nel caso della listView.getView() ma solo se c'è bisogno di una nuova istanza
        //il primo parametro di inflate indica il tipo di layout da usare
        //il secondo,indica in quale viewGroup deve essere inserita la view dopo l'inflate
        final View layout = LayoutInflater.from(parent.getContext()).inflate(layoutLista,parent,false);
        return new GenericViewHolderDocente(layout,tipoDiLista);//
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolderDocente holder, int position) {
        if (tipoDiLista == GenericViewHolderDocente.LISTA_1)
            holder.setView(listaUniversita.get(position),context,this,position);
       /* if (tipoDiLista == GenericViewHolder.LISTA_2)
            holder.setView((Task) listaUniversita.get(position),context,listaUniversita,this,position,fragmentAdapter);
        else if (tipoDiLista == GenericViewHolder.LISTA_DOMANDE_RISPOSTE_LATO_STUD) {
            holder.setView((Domanda) listaUniversita.get(position),context,listaUniversita,this,position,fragmentAdapter);
        } else if (tipoDiLista == ICostanti.LISTA_TESI_PREFERITE) {
            holder.setView((Tesi) listaUniversita.get(position),context,listaUniversita,this,position,fragmentAdapter);

        }*/
    }

    @Override
    public int getItemCount() {
        return listaUniversita.size();
    }

    public int getLayoutLista() {
        return layoutLista;
    }

    public int getTipoDilista(){
        return tipoDiLista;
    }
}
