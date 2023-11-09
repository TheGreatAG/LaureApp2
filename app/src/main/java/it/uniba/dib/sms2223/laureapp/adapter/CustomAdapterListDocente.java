package it.uniba.dib.sms2223.laureapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.model.Ricevimento;
import it.uniba.dib.sms2223.laureapp.model.RichiestaTesi;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.model.Universita;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;

public class CustomAdapterListDocente extends RecyclerView.Adapter<GenericViewHolderDocente>{

    public ArrayList listaElementi;

    private Context context;
    private int layoutLista;
    private int tipoDiLista;  //flag per capire se la lista è lato prof o studente
    private FragmentAdapter fragmentAdapter;
    private FragmentManager fragmentManager;

    public CustomAdapterListDocente(final ArrayList listaElementi, Context context, int layoutLista, int tipoDiLista, FragmentAdapter fragmentAdapter){//
        this.context = context;
        this.layoutLista = layoutLista;
        this.listaElementi = listaElementi;
        this.tipoDiLista = tipoDiLista;
        this.fragmentAdapter = fragmentAdapter;
        Log.d("HJH",""+this.listaElementi.size());
    }

    public CustomAdapterListDocente(final ArrayList listaElementi, Context context, int layoutLista, int tipoDiLista, FragmentManager fragmentManager,String str){//
        this.context = context;
        this.layoutLista = layoutLista;
        this.listaElementi = listaElementi;
        this.tipoDiLista = tipoDiLista;  //decommenta
        this.fragmentManager = fragmentManager;
        Log.d("HJH",""+this.listaElementi.size());
    }

    @NonNull
    @Override
    public GenericViewHolderDocente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View layout = LayoutInflater.from(parent.getContext()).inflate(layoutLista,parent,false);
        return new GenericViewHolderDocente(layout,tipoDiLista);//
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolderDocente holder, int position) {
        if (tipoDiLista == GenericViewHolderDocente.LISTA_1)
            holder.setView((Universita) listaElementi.get(position),context,this,position);
        if (tipoDiLista == GenericViewHolderDocente.LISTA_TESI_PROF_HOME)
            holder.setView((Tesi) listaElementi.get(position),context,this,position,fragmentManager);
        if (tipoDiLista == GenericViewHolderDocente.LISTA_RICEVIMENTI_STUDENTI)
            holder.setView((Ricevimento) listaElementi.get(position),context,this,position);
        if (tipoDiLista == GenericViewHolderDocente.LISTA_RICHIESTE_TESI)
            holder.setView((RichiestaTesi) listaElementi.get(position),context,this,position);

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
