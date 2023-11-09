package it.uniba.dib.sms2223.laureapp.adapter;

import android.content.Context;
import android.util.Log;
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
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

public class CustomAdapterList extends RecyclerView.Adapter<GenericViewHolder> {

    public ArrayList listaElementi;

    private Context context;
    private int layoutLista;
    private int tipoDiLista;  //flag per capire se la lista è lato prof o studente
    private FragmentAdapter fragmentAdapter;
    private Tesi tesi;

    public CustomAdapterList(final ArrayList listaElementi, Context context, int layoutLista, int tipoDiLista, FragmentAdapter fragmentAdapter){//
        this.context = context;
        this.layoutLista = layoutLista;
        this.listaElementi = listaElementi;
        this.tipoDiLista = tipoDiLista;  //decommenta
        this.fragmentAdapter = fragmentAdapter;
        Log.d("tipo lista B",""+ tipoDiLista);

    }

    public CustomAdapterList(final ArrayList listaElementi, Context context,
                             int layoutLista, int tipoDiLista, FragmentAdapter fragmentAdapter, Tesi tesi){
        this.context = context;
        this.layoutLista = layoutLista;
        this.listaElementi = listaElementi;
        this.tipoDiLista = tipoDiLista;
        this.fragmentAdapter = fragmentAdapter;
        this.tesi = tesi;
        Log.d("tipo lista B",""+ tipoDiLista);

    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View layout = LayoutInflater.from(parent.getContext()).inflate(layoutLista,parent,false);
        return new GenericViewHolder(layout,tipoDiLista);//
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        if (tipoDiLista == GenericViewHolder.LISTA_2 || tipoDiLista == GenericViewHolder.LISTA_TASK_LATO_RELATORE)
            holder.setView((Task) listaElementi.get(position),context,this,position,tesi);
        else if (tipoDiLista == GenericViewHolder.LISTA_DOMANDE_RISPOSTE_LATO_STUD || tipoDiLista == GenericViewHolder.LISTA_DOMANDE_RISPOSTE_LATO_RELATORE) {
            holder.setView((Domanda) listaElementi.get(position),context, listaElementi,this,position,fragmentAdapter);
        } else if (tipoDiLista == ICostanti.LISTA_TESI_PREFERITE) {
            holder.setView((Tesi) listaElementi.get(position),context, listaElementi,this,position,fragmentAdapter);
        } else if (tipoDiLista == GenericViewHolder.LISTA_TESI){
            holder.setView((Tesi) listaElementi.get(position), context, listaElementi, this, position, fragmentAdapter);

        }
    }

    @Override
    public int getItemCount() {
        return listaElementi.size();
    }

    public int getLayoutLista() {
        return layoutLista;
    }

    public int getTipoDilista(){
        return tipoDiLista;
    }


}
