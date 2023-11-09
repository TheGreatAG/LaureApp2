package it.uniba.dib.sms2223.laureapp;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.business.GestioneTesi;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Studente;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.model.Universita;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCercaTesi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCercaTesi extends Fragment implements ICostanti {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ArrayList<Tesi> listaTesi = new ArrayList<>();
    ArrayList<Tesi> listaTesiPrecedenti;
    private Context context;
    private Universita universita;
    private CustomAdapterList adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentCercaTesi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCercaTesi.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCercaTesi newInstance(String param1, String param2) {
        FragmentCercaTesi fragment = new FragmentCercaTesi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*

        listaTesi.add(new Tesi("", "", "", 0, 0, null, null, null, null, ETipoTesi.Compilativa));
        listaTesi.add(new Tesi("", "", "", 0, 0, null, null, null, null, ETipoTesi.Compilativa));
        listaTesi.add(new Tesi("", "", "", 0, 0, null, null, null, null, ETipoTesi.Compilativa));
        listaTesi.add(new Tesi("", "", "", 0, 0, null, null, null, null, ETipoTesi.Compilativa));
*/


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void updateQRCodeResult(String result) {
        //risultato scanner
        //qrCodeResultTextView.setText(result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cerca_tesi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        Toolbar toolbar = v.findViewById(R.id.toolbar_cerca_tesi);
        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        ImageView imgNoTesi = v.findViewById(R.id.img_no_tesi_disponibili);
        TextView txtNoTesi = v.findViewById(R.id.txt_no_tesi_trovate);
        RecyclerView recyclerView = v.findViewById(R.id.lista_tesi);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        recyclerView.setLayoutManager(layoutManager);//una recycler view deve avere un layout manager
        recyclerView.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT - 80));

       /* universita = new Universita();//in Java il passaggio di parametri è per valore, non avrebbe effetto l'impostazione della variabile università in questo modo
        recuperaCorsoStudente(universita);//definisco la variabile globale Universita con dipartimento e corso NON FUNZIONA BENE A VOLTE RESTITUISCE UN OGGETTO UNIVERSITA NULLO **************************************
        Log.d("WSD", ""+universita.corso);*/

        recuperaCorsoETesi(recyclerView, imgNoTesi, txtNoTesi, listaTesi);
        // getTesi(new Universita("Informatica","Corso di informatica",null,null),"andrea@uniba.it",FirebaseFirestore.getInstance(),recyclerView);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                 if (item.getItemId() == R.id.filtra) {

                     new GestioneTesi().impostaDialog(adapter,context,recyclerView).show();

                   }
                return false;
            }
        });

        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.titolo_tesi));

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    adapter.listaAnnunci = listaTesiPrecedenti;
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {//query è il testo inserito che per noi corrisponderà al titolo della tesi
                    Log.d("RICERCA", "cerca1 " + query); //quando si preme sul tasto "cerca" dopo aver inserito il testo


                    ArrayList<Tesi> listaRisultatiRicercaTesi = new ArrayList<>();
                    for (int i = 0; i < adapter.listaAnnunci.size(); i++) {
                        Tesi tesi = (Tesi) adapter.listaAnnunci.get(i);
                        Log.d("BHM 1", query + " -> " + tesi.titolo);
                        String[] titoloTesi = tesi.titolo.split(" ");
                        for (int j = 0; j < titoloTesi.length; j++) {
                            if (query.equals(titoloTesi[j])) {
                                listaRisultatiRicercaTesi.add(tesi);
                                Log.d("BHM 2", query + " -> " + tesi.titolo);
                            }
                        }

                    }
                    adapter.listaAnnunci = listaRisultatiRicercaTesi;

                    adapter.notifyDataSetChanged();
                    //adapter.listaAnnunci = listaTesiPrecedenti;

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("RICERCA", "cerca2 " + newText);
                    if (newText.equals("")) {
                        adapter.listaAnnunci = listaTesiPrecedenti;
                        adapter.notifyDataSetChanged();
                    }
                    Log.d("FGF", " no ricerca");


                    return false;
                }
            });
        }

    }

    private void recuperaTesi(RecyclerView recyclerView, ImageView img, TextView txt, ArrayList<Tesi> listaTesi,Universita universita) {

        //------recupero i professori---------------------

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("professori")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("ABC 2.1", "recupero i professori " + task.getResult());

                            for (QueryDocumentSnapshot document : task.getResult()) {//recupero i professori
                                Log.d("ABC 2", "recupero i professori " + document.getId());

                                getTesi(universita, document.getId(), db, recyclerView, img, txt, listaTesi);//recupero le tesi dei prof del corso specificato


                            }
                        } else {
                        }
                    }
                });

        //recuperare il corso dello studente
        //recuperare le tesi di quel corso

    }

    private void getTesi(Universita universita, String emailProf, FirebaseFirestore db,
                         RecyclerView recyclerView, ImageView img, TextView txt, ArrayList<Tesi> listaTesi) {
        Log.d("siamo nel recupero", emailProf);

        db.collection("professori").document(emailProf).collection("Tesi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {//recupero le tesi
                                Log.d("ABC 3", "recupero le tesi");

                                if (document.getString("corsoDiLaurea").equals(universita.corso)) {//universita è nullo non funziona

                                    String id = document.getId();
                                    String corelatore = null;
                                    if (!document.getString("sCorelatore").equals("-- -- --")) {
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
                                    String stato = document.getString("stato");
                                    String studente = document.getString("studente");//fare il controllo su studente
                                    // perchè se è null allora si mostra la tesi altrimenti vuol dire che è già stata assegnata e quindi non viene resa visibile

                                    String tipo = document.getString("tipo");
                                    String titolo = document.getString("titolo");
                                    String ambito = document.getString("ambito");
                                    if (studente == null) {
                                        Log.d("GBV", relatore);
                                        Tesi tesi = new Tesi(id, titolo, tipo, descrizione, ambito, corsoDiLaurea, dataPubblicazione, mediaRichiesta, durata, relatore, corelatore, li);
                                        tesi.stato = stato;
                                        listaTesi.add(tesi);
                                        Log.i("TAG", String.valueOf(listaTesi.size()));
                                    }

                                    if (listaTesi.size() == 0) {
                                        img.setVisibility(View.VISIBLE);
                                        txt.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    } else {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        img.setVisibility(View.GONE);
                                        txt.setVisibility(View.GONE);
                                        Log.d("tipo lista A", "" + GenericViewHolder.LISTA_TESI);
                                        listaTesiPrecedenti = listaTesi;

                                        adapter = new CustomAdapterList(listaTesi, context, R.layout.layout_lista_tesi_preferite, GenericViewHolder.LISTA_TESI, null);////////modificato con ultimo parametro
                                        Log.d("TGT", "num " + adapter.getItemCount());
                                        recyclerView.setAdapter(adapter);
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                }

                            }
                        }
                    }
                });
        Log.i("TAG", "Alla fine di getTesi " + String.valueOf(listaTesi.size()));
    }

    private void recuperaCorsoETesi(RecyclerView recyclerView, ImageView img, TextView txt, ArrayList<Tesi> listaTesi) {
        String emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("studenti").document(emailStudente).collection("Corso").document("c_s");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Universita universita = new Universita(document.getString("Dipartimento"), document.getString("Corso"), null, null);

                        recuperaTesi(recyclerView,img,txt,listaTesi,universita);

                      //  Log.d("ABC 1", "recupero il corso dello studente " + "Dip: " + FragmentCercaTesi.this.universita.dipartimento + " corso: " + FragmentCercaTesi.this.universita.corso);

                    } else {
                        //universita = null;
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }






}