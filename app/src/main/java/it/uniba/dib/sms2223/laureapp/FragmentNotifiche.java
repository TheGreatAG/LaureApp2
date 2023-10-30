package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterListDocente;
import it.uniba.dib.sms2223.laureapp.model.RichiestaTesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolderDocente;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNotifiche#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNotifiche extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNotifiche.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notifiche, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.lista_richieste_tesi);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        recyclerView.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        recyclerView.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));

        ArrayList listaTesi = new ArrayList();
        listaTesi.add(new RichiestaTesi(null,null,null,null,0,null));
        listaTesi.add(new RichiestaTesi(null,null,null,null,0,null));

        CustomAdapterListDocente adapter = new CustomAdapterListDocente(listaTesi, context, R.layout.layout_lista_richieste_tesi, GenericViewHolderDocente.LISTA_RICHIESTE_TESI,null);//anche queste righe sono da sistemare
        recyclerView.setAdapter(adapter);
        return v;
    }
}