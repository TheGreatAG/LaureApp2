package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentQA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentQA extends Fragment {

    private Context context;

    public ArrayList<Domanda> listaDomande;

    CustomAdapterList adapter;

    FragmentAdapter fragmentAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d("ciclo vita", "onCreateView");

    }

    public FragmentQA() {
        // Required empty public constructor
    }

    public FragmentQA(ArrayList<Domanda> listaDomande, FragmentAdapter fragmentAdapter){
        this.fragmentAdapter = fragmentAdapter;
        this.listaDomande = listaDomande;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentQA.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentQA newInstance(String param1, String param2) {
        FragmentQA fragment = new FragmentQA();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_q_a, container, false);

        RecyclerView lista = v.findViewById(R.id.lista);
        FloatingActionButton fab = v.findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            startActivity(new Intent(getContext(),DomandaStudente.class));
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentFragment().getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        lista.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        lista.addItemDecoration(new FragmentQA.DivisoreElementi(FragmentTask.DivisoreElementi.SPAZIO_DI_DEFAULT-150));

      /*  VolleyPost volleyPost = new VolleyPost(context);

        // ----- RECUPERARE TUTTI GLI ANNUNCI ALL'APERTURA DELL'APP FACENDO ATTENDERE CON UNA SPLAH SCREEN
        ///// ---------------------- modificare la stringa dell'email in modo che la recuperi in automatico è solo per  PROVA ------
        //Verificare perchè l'email ha la prima lettera maiuscola NON DEVE ESISTERE DA NESSUNA PARTE !!!!!!!!!!!!!!!!!
        volleyPost.getAnnunciUtente(Account.ottieniEmail(context).toLowerCase(),null,lista); -*-*-*-*-*-*-*-*TOGLI QUESTO COMMENTO SE NON FUNZIONA*/
        adapter = new CustomAdapterList(listaDomande, context, R.layout.layout_lista_domande, GenericViewHolder.LISTA_DOMANDE_RISPOSTE_LATO_STUD,fragmentAdapter);
        lista.setAdapter(adapter);

        return v;
    }

    class DivisoreElementi  extends  RecyclerView.ItemDecoration {

        private final int spazioTraElementi;
        //non la uso in questa classe ma quando chiamo i metodi di questa classe
        public static final int SPAZIO_DI_DEFAULT = 220;//lo metto qua e non in getItemOffSet per non avere una cosa fissa ma variare valore all'occorrenza

        public DivisoreElementi(int spazioTraElementi){
            this.spazioTraElementi = spazioTraElementi;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = spazioTraElementi;//dovrebbe andare in pixel non ne sono sicuro
        }
    }
}