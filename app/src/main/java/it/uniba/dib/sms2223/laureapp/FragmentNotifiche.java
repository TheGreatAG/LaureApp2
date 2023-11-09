package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Relatore;
import it.uniba.dib.sms2223.laureapp.model.RichiestaTesi;
import it.uniba.dib.sms2223.laureapp.model.Studente;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;


public class FragmentNotifiche extends Fragment implements ICostanti {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public FragmentNotifiche() {
        // Required empty public constructor
    }

    public static FragmentNotifiche newInstance(String param1, String param2) {
        FragmentNotifiche fragment = new FragmentNotifiche();
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
        View v = inflater.inflate(R.layout.fragment_notifiche, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.lista_richieste_tesi);
        TextView txtNoRichieste = v.findViewById(R.id.txt_no_richieste);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        recyclerView.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        recyclerView.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));


        recuperaRichieste(recyclerView,txtNoRichieste);

        return v;
    }

    private void recuperaRichieste(RecyclerView recyclerView, TextView txtNoRichieste){
        String emailProf = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_PROF).document(emailProf).collection(COLLECTION_RICHIESTE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<RichiestaTesi> listaRichieste = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {//recupero le tesi
                                Map<String,Object> mapTesi= (Map<String,Object>)document.get("tesi");
                                Map<String,Object> mapStudente= (Map<String,Object>)document.get("studente");


                                ArrayList<String> esamiStudente = (ArrayList)document.get("propedeuticita");
                                Log.d("HJH 2",""+esamiStudente.size());

                                String titolo = mapTesi.get("titolo").toString();
                                String corsoDiLaurea = mapTesi.get("corsoDiLaurea").toString();
                                String dataPubblicazione = mapTesi.get("dataPubblicazione").toString();
                                String idTesi = mapTesi.get("id").toString();

                                String sCorelatore = null;
                                if (mapTesi.get("sCorelatore") != null) {
                                    sCorelatore = mapTesi.get("sCorelatore").toString();
                                }

                                ArrayList<String> esamiPropedeutici =(ArrayList) mapTesi.get("esamiRichiesti");

                                int mediaRichiesta = Integer.parseInt(mapTesi.get("mediaRichiesta").toString());

                                String emailStudente = mapStudente.get("email").toString();

                                String notePerIlDocente = document.getString("note");
                                double mediaVotiStudente = Double.parseDouble(document.get("mediaVotiStudente").toString());
                                String dataRichiesta = document.getString("dataRichiesta");
                                int esamiMancanti = Integer.parseInt(document.get("esamiMancanti").toString());

                                Studente stud = new Studente();
                                stud.email = emailStudente;

                                String infoRelatore [] = mapTesi.get("sRelatore").toString().split(" ");
                                String nome = infoRelatore[0];
                                String cognome = infoRelatore[1];
                                String email = infoRelatore[2];
                                Relatore relatore =  new Relatore(nome,cognome,email,null,null,null);

                                Tesi tesi = new Tesi();
                                tesi.titolo = titolo;
                                tesi.corsoDiLaurea = corsoDiLaurea;
                                tesi.dataPubblicazione = dataPubblicazione;
                                tesi.sCorelatore = sCorelatore;
                                tesi.esamiRichiesti = esamiPropedeutici;
                                tesi.mediaRichiesta = mediaRichiesta;
                                tesi.relatore = relatore;
                                tesi.id = idTesi;
                                String id = document.getId();


                                RichiestaTesi richiestaTesi = new RichiestaTesi(stud,tesi,notePerIlDocente,dataRichiesta,mediaVotiStudente,esamiMancanti,esamiStudente);
                                richiestaTesi.id = id;
                                listaRichieste.add(richiestaTesi);

                            }
                            if (listaRichieste.size() ==0){
                                txtNoRichieste.setVisibility(View.VISIBLE);
                            }else {
                                CustomAdapterListDocente adapterListDocente = new CustomAdapterListDocente(listaRichieste, context,
                                        R.layout.layout_lista_richieste_tesi, GenericViewHolderDocente.LISTA_RICHIESTE_TESI, null);
                                recyclerView.setAdapter(adapterListDocente);
                            }
                        }
                    }
                });
    }
}