package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.ETipoTesi;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCercaTesi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCercaTesi extends Fragment implements ICostanti {

    private ArrayList<Tesi> listaTesi = new ArrayList<>();
    private Context context;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaTesi.add(new Tesi("","","",0,0,null,null,null,null, ETipoTesi.Compilativa));
        listaTesi.add(new Tesi("","","",0,0,null,null,null,null,ETipoTesi.Compilativa));
        listaTesi.add(new Tesi("","","",0,0,null,null,null,null,ETipoTesi.Compilativa));
        listaTesi.add(new Tesi("","","",0,0,null,null,null,null,ETipoTesi.Compilativa));


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cerca_tesi, container, false);

        Toolbar toolbar = v.findViewById(R.id.toolbar_cerca_tesi);
        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();

        RecyclerView listaTesiPrefe = v.findViewById(R.id.lista_tesi);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        listaTesiPrefe.setLayoutManager(layoutManager);//una recycler view deve avere un layout manager
        listaTesiPrefe.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-80));
        CustomAdapterList adapter = new CustomAdapterList(listaTesi, context, R.layout.layout_lista_tesi_preferite, LISTA_TESI,null);////////modificato con ultimo parametro
        listaTesiPrefe.setAdapter(adapter);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.scan_qr) {
                    //***************************---------------???????????????????????????????????????????????????
                    //SAMUELE METTI QUI LA FUNZIONE PER APRIRE LA FOTOCAMERA PER SCANNERIZZARE CODICI QR
                    //*************************************************************************************************
                    Toast.makeText(context, "premuto QR scanner", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.titolo_tesi));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {//query è il testo inserito che per noi corrisponderà al titolo della tesi
                    Log.d("RICERCA", "cerca1 " + query); //quando si preme sul tasto "cerca" dopo aver inserito il testo

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("RICERCA", "cerca2");

                    return false;
                }
            });
        }

        return v;
    }
}