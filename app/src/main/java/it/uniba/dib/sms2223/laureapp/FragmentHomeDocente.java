package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.Corelatore;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.model.Universita;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;

public class FragmentHomeDocente extends Fragment {

    private int numTesi;

    private Context context;

    private ArrayList<Tesi> listaTesi = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHomeDocente() {
        // Required empty public constructor
    }

    public static FragmentHomeDocente newInstance(String param1, String param2) {
        FragmentHomeDocente fragment = new FragmentHomeDocente();
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

        return inflater.inflate(R.layout.fragment_home_docente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listaTesiProf = view.findViewById(R.id.lista_tesi_prof);
        TextView txtNoConnesione = view.findViewById(R.id.txt_no_connessione);
        ProgressBar progressBar = view.findViewById(R.id.progressbar_caricamento_tesi);
        Button btnRicarica = view.findViewById(R.id.btn_ricarica);


        if (new Utile(context).connessione()){//se c'è connessione carica la lista
            inizializzaListatesiProf(listaTesi,listaTesiProf,progressBar,view,txtNoConnesione);
        }else {
            btnRicarica.setVisibility(View.VISIBLE);
            txtNoConnesione.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            btnRicarica.setOnClickListener(view1 -> {
                progressBar.setVisibility(View.VISIBLE);
                txtNoConnesione.setVisibility(View.GONE);
                btnRicarica.setVisibility(View.GONE);
                inizializzaListatesiProf(listaTesi,listaTesiProf,progressBar,view,txtNoConnesione);

            });
        }
        FloatingActionButton fab = view.findViewById(R.id.fab);


        Toolbar mToolbar = view.findViewById(R.id.toolbar_home);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoPrimario));

        fab.setOnClickListener(view1 -> {
            startActivity(new Intent(context,CreaTesi.class));
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btn_toolbar_profilo_personale) {
                    startActivity(new Intent(context, ProfiloUtente.class).putExtra("chiamante",true));
                }
                return false;
            }
        });
    }

    private void inizializzaListatesiProf(ArrayList<Tesi> listaTesi, RecyclerView recyclerView, ProgressBar progressBar, View v, TextView txt){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        recyclerView.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        recyclerView.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("professori").document(emailDocente).collection("Tesi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    Map<String,Object> valori = new HashMap<>();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //numTesi++;
                                String id = document.getId();
                                String titolo = document.get("titolo").toString();
                                String descrizione =  document.get("descrizione").toString();
                                String ambito =  document.get("ambito").toString();
                                String tipo = document.get("tipo").toString();
                                String corsoDiLaurea = document.get("corsoDiLaurea").toString();
                                String dataPubblicazione = document.get("dataPubblicazione").toString();
                                String studente = document.getString("studente");//riga nuova
                                String stato = document.getString("stato");
                                int mediaVoti = Integer.parseInt(document.get("mediaRichiesta").toString());
                                int durata = Integer.parseInt(document.get("durata").toString());
                                Relatore relatore = null;
                                Corelatore corelatore = null;
                                try {
                                    String [] infoRelatore = document.getString("sRelatore").split(" ");
                                    String [] infoCorelatore = document.getString("sCorelatore").split(" ");

                                    relatore = new Relatore(infoRelatore[0],infoRelatore[1],infoRelatore[2],null,null,null);
                                    corelatore = new Corelatore(infoCorelatore[0],infoCorelatore[1],infoCorelatore[2]);
                                } catch (NullPointerException e){
                                    e.printStackTrace();
                                }

                                ArrayList<String> li = new ArrayList<>();
                                li = (ArrayList) document.get("esamiRichiesti");
                                Tesi tesi = new Tesi(id,titolo,tipo,descrizione,ambito,corsoDiLaurea,dataPubblicazione,mediaVoti,durata,relatore,corelatore,li);
                                tesi.stato = stato;
                                tesi.studente = studente;
                                listaTesi.add(tesi);

                            }

                            if (listaTesi.size() ==0){
                                v.findViewById(R.id.img_no_elementi).setVisibility(View.VISIBLE);
                                v.findViewById(R.id.img_no_elementi).setBackground(context.getResources().getDrawable(R.drawable.no_tesi_trovate));
                                txt.setVisibility(View.VISIBLE);
                                txt.setText("Ancora nessuna tesi creata");
                            } else {
                                v.findViewById(R.id.img_no_elementi).setVisibility(View.GONE);
                                txt.setVisibility(View.GONE);
                            }
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            CustomAdapterListDocente adapter = new CustomAdapterListDocente(listaTesi, context, R.layout.layout_lista_tesi_prof, GenericViewHolderDocente.LISTA_TESI_PROF_HOME,fragmentManager,"frfr");//anche queste righe sono da sistemare
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}