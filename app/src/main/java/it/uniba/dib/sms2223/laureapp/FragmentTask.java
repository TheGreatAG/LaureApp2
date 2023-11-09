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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;


public class FragmentTask extends Fragment implements ICostanti {

    private Context context;

    public ArrayList<Task> listaTask;

    CustomAdapterList adapter;

    FragmentAdapter fragmentAdapter;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d("ciclo vita", "onCreateView");

    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Tesi tesi;

    public FragmentTask() {
        // Required empty public constructor
    }

    public FragmentTask(Tesi tesi, FragmentAdapter fragmentAdapter) {
        this.fragmentAdapter = fragmentAdapter;
        //this.listaTask = listaTask;
        this.tesi = tesi;

    }

    public static FragmentTask newInstance(String param1, String param2) {
        FragmentTask fragment = new FragmentTask();
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

        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);


        RecyclerView lista = v.findViewById(R.id.lista);
        FloatingActionButton fab = v.findViewById(R.id.fab);
        // TextView txtTaskDaCompletare = v.findViewById(R.id.txt_num_task);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentFragment().getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        lista.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        lista.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));

        SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe_container);

        Log.d("GYG",""+tesi.id);
        recuperaInizializzaListaTask(tesi,lista,swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recuperaInizializzaListaTask(tesi,lista,swipeRefreshLayout);
            }
        });

        if (docente)
            fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(view -> {
            startActivity(new Intent(context,ActivityCreaTask.class).putExtra("taskDocente",true).putExtra("idtesi",tesi.id));
        });
    }

    private void recuperaInizializzaListaTask(Tesi tesi, RecyclerView recyclerView,SwipeRefreshLayout swipeRefreshLayout){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_PROF).document(tesi.relatore.email).
                collection(COLLECTION_TESI).document(tesi.id).collection(COLLECTION_TASK)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Task> listaTask = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String descrizione=document.getString("descrizione");
                                String id=document.getId();
                                String stato=document.getString("stato");
                                String titolo=document.getString("titolo");
                                String ultimaModifica=document.getString("ultimaModifica");
                                boolean confermaRelatore = document.getBoolean("confermaDefinitivaDelRelatore");
                                Task task1 = new Task(id,titolo,descrizione,ultimaModifica,stato);
                                task1.confermaDefinitivaDelRelatore = confermaRelatore;
                                listaTask.add(task1);
                            }
                          //  txtTaskDaCompletare.setText(context.getString(R.string.task_da_completare)+ " "+ listaTask.size());
                            CustomAdapterList adapter;
                            if (docente){
                                Log.d("JJI","adapter per task docente");
                                adapter = new CustomAdapterList(listaTask, context, R.layout.layout_lista_task
                                        ,GenericViewHolder.LISTA_TASK_LATO_RELATORE,null,tesi);

                            } else {
                                adapter = new CustomAdapterList(listaTask, context, R.layout.layout_lista_task
                                        , GenericViewHolder.LISTA_2, null, tesi);
                            }
                            recyclerView.setAdapter(adapter);
                            swipeRefreshLayout.setRefreshing(false);


                        }
                    }
                });
    }



}