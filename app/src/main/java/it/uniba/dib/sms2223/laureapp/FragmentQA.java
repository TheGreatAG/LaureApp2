package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentQA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentQA extends Fragment implements ICostanti {

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

    private Tesi tesi;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d("ciclo vita", "onCreateView");

    }

    public FragmentQA() {
        // Required empty public constructor
    }

    public FragmentQA(Tesi tesi){
        this.tesi = tesi;
        this.listaDomande = new ArrayList<>();

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

    private boolean docente =false;
    private String emailStudente;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (Credenziali.validitaEmailProf(emailStudente)){
            docente =true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_q_a, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);


        RecyclerView lista = v.findViewById(R.id.lista);
        FloatingActionButton fab = v.findViewById(R.id.fab);
        ProgressBar progressBar = v.findViewById(R.id.progressBar);
        TextView txtNoDomnde = v.findViewById(R.id.txt_no_domande_presenti);

        if (docente)
            fab.setVisibility(View.GONE);

        fab.setOnClickListener(view -> {
            startActivity(new Intent(getContext(),DomandaStudente.class).putExtra("Tesi",tesi));
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentFragment().getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        lista.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        lista.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));

        recuperaDomande(tesi,lista,progressBar,txtNoDomnde);
    }

    private void recuperaDomande(Tesi tesi, RecyclerView recyclerView,ProgressBar progressBar,TextView txtNoDomande){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_PROF).document(tesi.relatore.email).collection(COLLECTION_TESI)
                .document(tesi.id).collection(COLLECTION_DOMANDE_TASK)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Domanda> listaDomande = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {//recupero le domande

                                String dataDomanda = document.getString("dataDomanda");
                                String dataRisposta = document.getString("dataRisposta");
                                String domanda = document.getString("domanda");
                                String id = document.getId();
                                String idTask = document.getString("idTask");
                                String risposta = document.getString("risposta");
                                String titoloTask = document.getString("titoloTask");
                                String tesiId = document.getString("tesiId");

                                Domanda domanda1 = new Domanda(dataDomanda,dataRisposta,domanda,risposta,titoloTask,idTask,id,tesiId);

                                listaDomande.add(domanda1);

                            }
                            progressBar.setVisibility(View.GONE);

                            if (listaDomande.size()==0){
                                txtNoDomande.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                txtNoDomande.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                CustomAdapterList adapter;
                                if (docente) {
                                    adapter = new CustomAdapterList(listaDomande, context, R.layout.layout_lista_domande, GenericViewHolder.LISTA_DOMANDE_RISPOSTE_LATO_RELATORE, null);
                                }else {
                                    adapter = new CustomAdapterList(listaDomande, context, R.layout.layout_lista_domande, GenericViewHolder.LISTA_DOMANDE_RISPOSTE_LATO_STUD, null);
                                }
                                recyclerView.setAdapter(adapter);
                            }

                        }
                    }
                });
    }


}