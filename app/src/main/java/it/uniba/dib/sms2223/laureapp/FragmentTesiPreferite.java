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

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.ETipoTesi;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTesiPreferite#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTesiPreferite extends Fragment implements ICostanti {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private ArrayList<Tesi> listaTesiPreferite = new ArrayList<>();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public FragmentTesiPreferite() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTesiPreferite.
     */
    // TODO: Rename and change types and number of parameters
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

       //è QUESTO IL POSTO GIUSTO DOVE METTERE L'ARRAYLIST

        listaTesiPreferite.add(new Tesi("","","",0,0,null,null,null,null,ETipoTesi.Compilativa));
        listaTesiPreferite.add(new Tesi("","","",0,0,null,null,null,null,ETipoTesi.Compilativa));
        listaTesiPreferite.add(new Tesi("","","",0,0,null,null,null,null,ETipoTesi.Compilativa));
        listaTesiPreferite.add(new Tesi("","","",0,0,null,null,null,null,ETipoTesi.Compilativa));


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tesi_preferite, container, false);

        RecyclerView listaTesiPrefe = v.findViewById(R.id.lista_tesi_preferite);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        listaTesiPrefe.setLayoutManager(layoutManager);//una recycler view deve avere un layout manager
        listaTesiPrefe.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-80));
        CustomAdapterList adapter = new CustomAdapterList(listaTesiPreferite, context, R.layout.layout_lista_tesi_preferite, LISTA_TESI_PREFERITE,null);////////modificato con ultimo parametro
        listaTesiPrefe.setAdapter(adapter);

        return v;
    }
}