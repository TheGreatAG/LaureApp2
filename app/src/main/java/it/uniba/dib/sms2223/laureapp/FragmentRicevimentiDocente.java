package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Corelatore;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
import it.uniba.dib.sms2223.laureapp.model.Ricevimento;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRicevimentiDocente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRicevimentiDocente extends Fragment implements ICostanti {

    private Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentRicevimentiDocente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRicevimentiDocente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRicevimentiDocente newInstance(String param1, String param2) {
        FragmentRicevimentiDocente fragment = new FragmentRicevimentiDocente();
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
        View v = inflater.inflate(R.layout.fragment_ricevimenti_docente, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.lista_richieste_ricevimenti);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        recyclerView.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        recyclerView.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));

        recuperaRicevimenti(recyclerView);
        return v;
    }

    private void recuperaRicevimenti(RecyclerView recyclerView){
        String emailDocente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//--------------------------------------da finire, voglio recuperare le tesi create del prof ---------------------------
        db.collection(COLLECTION_PROF).document(emailDocente).collection(COLLECTION_RICEVIMENTI)//mi recuper tutti gli insegnamenti di un dato prof
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    Map<String,Object> valori = new HashMap<>();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList <Ricevimento> listaRicevimenti = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String dataRichiesta = document.getString("data");
                                String id = document.getId();
                                String descrizione = document.getString("descrizione");
                                Map<String,Object> mapTesi= (Map<String,Object>)document.get("tesi");
                                Tesi tesi = new Tesi();
                                tesi.studente = mapTesi.get("studente").toString(); //non funziona
                                tesi.id = mapTesi.get("id").toString();
                                tesi.titolo = mapTesi.get("titolo").toString();

                                Ricevimento ricevimento = new Ricevimento(tesi,descrizione,dataRichiesta,id);
                                listaRicevimenti.add(ricevimento);
                            }

                            CustomAdapterListDocente adapter = new CustomAdapterListDocente(listaRicevimenti, context
                                    ,R.layout.layout_lista_richieste_ricevimenti, GenericViewHolderDocente.LISTA_RICEVIMENTI_STUDENTI,null);//anche queste righe sono da sistemare
                            recyclerView.setAdapter(adapter);
                        }
                    }

                });

    }
}