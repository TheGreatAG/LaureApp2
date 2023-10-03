package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.model.Domanda;
import it.uniba.dib.sms2223.laureapp.model.Task;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeStudente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeStudente extends Fragment {

    Context context;

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

        //mToolbar.setTitle("La tua tesi");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoPrimario));

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btn_toolbar_profilo_personale)
                    Toast.makeText(context,"bybhnj",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        tabLayout = layout.findViewById(R.id.tab_layout);
        viewPager2 = layout.findViewById(R.id.pager);


        //trovare il modo di avere sempre un riferimento aggiornato alla lista annunci il resto fara tutto

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(),getLifecycle());//DECOMMENTARE *********************
       /* ArrayList<FragmentHomeStudente> arrayListTest = new ArrayList<>();
        arrayListTest.add(new FragmentHomeStudente());
        arrayListTest.add(new FragmentHomeStudente());  //TEST SEMI-INUTILE
        arrayListTest.add(new FragmentHomeStudente());*/

        //ArrayList<Annuncio> annunciNonVisibili = new AnnuncioB(context).ottieniLista(listaAnnunci,ICostanti.ANNUNCI_NON_VISIBILI); //DECOMMENTARE *********************

        //FragmentAnnunci fragmentAnnunci = new FragmentAnnunci(listaAnnunci);
        //fragmentAdapter.aggiungiFragment(fragmentAnnunci); //decommentare queste righe se si vuole inserire pure la sezione tutti gli annunci


        //fragmentAdapter.aggiungiFragment(new TestFragment());
        ArrayList<Domanda> listaDomande = new ArrayList<>();
        listaDomande.add(new Domanda("","","","",""));
        listaDomande.add(new Domanda("","","","",""));
        listaDomande.add(new Domanda("","","","",""));
        listaDomande.add(new Domanda("","","","",""));

        fragmentAdapter.aggiungiFragment(new FragmentQA(listaDomande,fragmentAdapter));
        fragmentAdapter.aggiungiFragment(new FragmentDettaglioTesi());


        ArrayList <Task> lis = new ArrayList();
        lis.add(new Task("Un titolo per il task","la descrizione del task","02/02/2022","incompleto"));
        lis.add(new Task("Un titolo per il task","la descrizione del task","02/02/2022","incompleto"));
        lis.add(new Task("Un titolo per il task","la descrizione del task","02/02/2022","incompleto"));
        lis.add(new Task("Un titolo per il task","la descrizione del task","02/02/2022","incompleto"));
        lis.add(new Task("Un titolo per il task","la descrizione del task","02/02/2022","incompleto"));

        fragmentAdapter.aggiungiFragment(new FragmentTask(lis,fragmentAdapter));

        //fragmentAdapter.aggiungiFragment(new FragmentAnnunci(annunciNonVisibili,fragmentAdapter));//DECOMMENTARE *********************

        viewPager2.setAdapter(fragmentAdapter); //DECOMMENTARE *********************

        new TabLayoutMediator(tabLayout,viewPager2, (tab1, position1) -> {//DA SISTEMARE deve aggiotnare il numero di annunci quando se ne elimina uno
            //in set setx bisongna passare un array di stringhe per tutti,visibili e non visibili
            String str []= {getString(R.string.faq),getString(R.string.dettagli),getString(R.string.task)};
            tab1.setText(str[position1]);//tab è il titolo che si da ai tab sopra, deve essere sostituito rispettivamente da tutti, visibili e non visibili

        }).attach();

        //-------------------------------------------------------------------------------------
        return layout;
    }


}