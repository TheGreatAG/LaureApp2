package it.uniba.dib.sms2223.laureapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.model.Corelatore;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
import it.uniba.dib.sms2223.laureapp.model.Tesi;

public class FragmentHomeStudente extends Fragment {

    private Context context;

    private String emailStudente;

    private Tesi tesi = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHomeStudente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHomeStudente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHomeStudente newInstance(String param1, String param2) {
        FragmentHomeStudente fragment = new FragmentHomeStudente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //----------------IN LAVORAZIONE NON TOCCARE-----------------------------------------------------
        View layout = inflater.inflate(R.layout.fragment_home_studente, container, false);
        // Inflate the layout for this fragment
        Toolbar mToolbar = layout.findViewById(R.id.toolbar_home);
        LinearLayout lytContenitoreDettaglioTesi = layout.findViewById(R.id.lyt_contenitore1);
        LinearLayout lytNoTesiAssegnata = layout.findViewById(R.id.lyt_contenitore2);
        ProgressBar progressBar = layout.findViewById(R.id.progress_carica_tesi);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoPrimario));

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btn_toolbar_profilo_personale) {
                    Toast.makeText(context, "bybhnj", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, ProfiloUtente.class));
                    //non si può mettere finish qua perche se si preme indietro dalla toolbar della prox activity succede un errore


                }
                return false;
            }
        });

        tabLayout = layout.findViewById(R.id.tab_layout);
        viewPager2 = layout.findViewById(R.id.pager);

        recuperaTesi(lytContenitoreDettaglioTesi,lytNoTesiAssegnata,progressBar,viewPager2,tabLayout);

        return layout;
    }


    private void recuperaTesi(ViewGroup lytContenitoreDettaglioTesi, ViewGroup lytContenitoreNoTesi
            ,ProgressBar progressBar,ViewPager2 viewPager2,TabLayout tabLayout){//da rivedere non mi convince come rrecupera la tesi

        //------recupero i professori---------------------

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("professori")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("ABC 2.1","recupero i professori " + task.getResult());

                            for (QueryDocumentSnapshot document : task.getResult()) {//recupero i professori
                                Log.d("ABC 2","recupero i professori " + document.getId());

                                getTesi(document.getId(),db,lytContenitoreDettaglioTesi,lytContenitoreNoTesi
                                        ,progressBar,viewPager2,tabLayout);//recupero le tesi dei prof del corso specificato
                            }
                        } else {
                        }
                    }
                });
    }

    private void getTesi(String emailProf, FirebaseFirestore db,ViewGroup lytContenitoreDettaglioTesi
            ,ViewGroup lytContenitoreNoTesi,ProgressBar progressBar,ViewPager2 viewPager2,TabLayout tabLayout){
        Log.d("siamo nel recupero", emailProf);

        db.collection("professori").document(emailProf).collection("Tesi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {//recupero le tesi NON SO PERCHè NON MI RECUPERA LE TESI DEL PRIMO PROF PRESENTE NELLA LISTA DEI PROF**********************
                                Log.d("ABC 3", "recupero le tesi");

                                    if (document.get("studente") != null && document.getString("studente").split(" ")[0].equals(emailStudente)) {
                                        String id = document.getId();
                                        Corelatore corelatore = null;
                                        if (!document.getString("sCorelatore").equals("-- -- --")) {
                                            String[] datiCorelatore = document.getString("sCorelatore").split(" ");
                                            corelatore = new Corelatore(datiCorelatore[0], datiCorelatore[1], datiCorelatore[2]);
                                        }
                                        // String corelatore = document.getString("corelatore");
                                        String corsoDiLaurea = document.getString("corsoDiLaurea");
                                        String dataPubblicazione = document.getString("dataPubblicazione");
                                        String descrizione = document.getString("descrizione");
                                        int durata = Integer.parseInt(String.valueOf(document.get("durata"))); //intero
                                        ArrayList<String> li = new ArrayList<>();
                                        li = (ArrayList) document.get("esamiRichiesti");
                                        int mediaRichiesta = Integer.parseInt(String.valueOf(document.get("mediaRichiesta")));
                                       // String relatore = document.getString("relatore");
                                        String studente = document.getString("studente");
                                        String tipo = document.getString("tipo");
                                        String titolo = document.getString("titolo");
                                        String ambito = document.getString("ambito");
                                        String stato = document.getString("stato");

                                        Relatore relatore = null;
                                        if (document.getString("sRelatore") != null) {
                                            String[] datiCorelatore = document.getString("sRelatore").split(" ");
                                            relatore = new Relatore(datiCorelatore[0], datiCorelatore[1], datiCorelatore[2],null,null,null);
                                        }

                                        tesi = new Tesi(id, titolo, tipo, descrizione, ambito, corsoDiLaurea, dataPubblicazione, mediaRichiesta, durata, relatore, corelatore, li);
                                        tesi.studente = studente;
                                        tesi.stato = stato;
                                        tesi.sRelatore = document.getString("sRelatore");
                                        tesi.sCorelatore = document.getString("sCorelatore");

                                        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
                                        fragmentAdapter.aggiungiFragment(new FragmentQA(tesi));
                                        //ArrayList<Domanda> listaDomande = new ArrayList<>();

                                        fragmentAdapter.aggiungiFragment(new FragmentDettaglioTesi(tesi));

                                        fragmentAdapter.aggiungiFragment(new FragmentTask(tesi, fragmentAdapter));

                                        viewPager2.setAdapter(fragmentAdapter); //DECOMMENTARE *********************

                                        new TabLayoutMediator(tabLayout, viewPager2, (tab1, position1) -> {//DA SISTEMARE deve aggiotnare il numero di annunci quando se ne elimina uno
                                            //in set setx bisongna passare un array di stringhe per tutti,visibili e non visibili
                                            String str[] = {getString(R.string.faq), getString(R.string.dettagli), getString(R.string.task)};
                                            tab1.setText(str[position1]);//tab è il titolo che si da ai tab sopra, deve essere sostituito rispettivamente da tutti, visibili e non visibili

                                        }).attach();
                                        break;//esco dal ciclo perchè lo studente può avere solo una tesi assegnata, inutile ciclare ancora dopo aver trovato la tesi
                                    }
                            }
                            if (tesi == null) {
                                lytContenitoreDettaglioTesi.setVisibility(View.GONE);
                                lytContenitoreNoTesi.setVisibility(View.VISIBLE);
                            } else {
                                lytContenitoreDettaglioTesi.setVisibility(View.VISIBLE);
                                lytContenitoreNoTesi.setVisibility(View.GONE);
                                Toast.makeText(context," hai una tesi assegnata",Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        } else {
                        }
                    }
                });
    }
}