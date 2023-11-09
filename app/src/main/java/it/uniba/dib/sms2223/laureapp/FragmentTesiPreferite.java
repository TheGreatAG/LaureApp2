package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.business.GestioneTesi;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.model.ETipoTesi;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;


public class FragmentTesiPreferite extends Fragment implements ICostanti {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private CustomAdapterList adapter;


    private ArrayList<Tesi> listaTesiPreferite = new ArrayList<>();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public FragmentTesiPreferite() {
        // Required empty public constructor
    }

    public static FragmentTesiPreferite newInstance(String param1, String param2) {
        FragmentTesiPreferite fragment = new FragmentTesiPreferite();
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
        return inflater.inflate(R.layout.fragment_tesi_preferite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        Toolbar toolbar = v.findViewById(R.id.toolbar);

        RecyclerView listaTesiPrefe = v.findViewById(R.id.lista_tesi_preferite);
        TextView txtNoTesiPreferite = v.findViewById(R.id.txt_no_tesi_preferite);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        listaTesiPrefe.setLayoutManager(layoutManager);//una recycler view deve avere un layout manager
        listaTesiPrefe.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-80));

        recuperTesiPreferite(listaTesiPrefe,txtNoTesiPreferite);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.filtra) {
                    if (Utente.utenteLoggato())
                        new GestioneTesi().impostaDialog(adapter,context,listaTesiPrefe).show();
                    else
                        Toast.makeText(context,getString(R.string.loggati),Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void recuperTesiPreferite(RecyclerView recyclerView, TextView txtNoTesi){
        if (Utente.utenteLoggato()) {
            String emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("studenti").document(emailStudente).collection("Tesi_preferite")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("ABC 2.1", "recupero i professori " + task.getResult());
                                ArrayList<Tesi> listaTesi = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {//recupero i professori


                                    String id = document.getId();
                                    String corelatore = null;
                                    if (document.getString("sCorelatore") != null) {
                                        corelatore = document.getString("sCorelatore");
                                    }
                                    // String corelatore = document.getString("corelatore");
                                    String corsoDiLaurea = document.getString("corsoDiLaurea");
                                    String dataPubblicazione = document.getString("dataPubblicazione");
                                    String descrizione = document.getString("descrizione");
                                    int durata = Integer.parseInt(String.valueOf(document.get("durata"))); //intero
                                    ArrayList<String> li = new ArrayList<>();
                                    li = (ArrayList) document.get("esamiRichiesti");
                                    int mediaRichiesta = Integer.parseInt(String.valueOf(document.get("mediaRichiesta")));
                                    String relatore = document.getString("sRelatore");
                                    String studente = document.getString("studente");
                                    String tipo = document.getString("tipo");
                                    String titolo = document.getString("titolo");
                                    String ambito = document.getString("ambito");
                                    // if (studente != null) {
                                    Log.d("GBV", relatore);
                                    Tesi tesi = new Tesi(id, titolo, tipo, descrizione, ambito, corsoDiLaurea, dataPubblicazione, mediaRichiesta, durata, relatore, corelatore, li);
                                    Log.d("GTG 2", "" + studente);

                                    listaTesi.add(tesi);

                                }
                                if (listaTesi.size() == 0) {
                                    txtNoTesi.setVisibility(View.VISIBLE);
                                } else {
                                    adapter = new CustomAdapterList(listaTesi, context, R.layout.layout_lista_tesi_preferite, GenericViewHolder.LISTA_TESI_PREFERITE, null);////////modificato con ultimo parametro
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        }
                    });
        }

    }
}