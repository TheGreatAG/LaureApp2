package it.uniba.dib.sms2223.laureapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

public class CustomAdapterListaTask extends RecyclerView.Adapter<GenericViewHolder> {

    public ArrayList<Task> listaAnnunci;
    private Context context;
    private int layoutLista;
    private int tipoDiLista;  //flag per capire se la lista è lato prof o studente
    private FragmentAdapter fragmentAdapter;///

    public CustomAdapterListaTask(final ArrayList<Task> listaAnnunci, Context context, int layoutLista, int tipoDiLista, FragmentAdapter fragmentAdapter){//
        this.context = context;
        this.layoutLista = layoutLista;
        this.listaAnnunci = listaAnnunci;
        this.tipoDiLista = tipoDiLista;  //decommenta
        this.fragmentAdapter = fragmentAdapter;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//non viene invocato sempre come nel caso della listView.getView() ma solo se c'è bisogno di una nuova istanza
        //il primo parametro di inflate indica il tipo di layout da usare
        //il secondo,indica in quale viewGroup deve essere inserita la view dopo l'inflate
        final View layout = LayoutInflater.from(parent.getContext()).inflate(layoutLista,parent,false);
        return new GenericViewHolder(layout,tipoDiLista);//
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.setView(listaAnnunci.get(position),context,listaAnnunci,this,position,fragmentAdapter);
    }

    @Override
    public int getItemCount() {
        return listaAnnunci.size();
    }

    public int getLayoutLista() {
        return layoutLista;
    }

    public int getTipoDilista(){
        return tipoDiLista;
    }


}
