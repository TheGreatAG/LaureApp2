package it.uniba.dib.sms2223.laureapp;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.adapter.CustomAdapterList;
import it.uniba.dib.sms2223.laureapp.adapter.FragmentAdapter;
import it.uniba.dib.sms2223.laureapp.model.Task;
import it.uniba.dib.sms2223.laureapp.ui.lista.DivisoreElementi;
import it.uniba.dib.sms2223.laureapp.ui.lista.GenericViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTask extends Fragment {

    private Context context;

    public ArrayList<Task> listaTask;

    CustomAdapterList adapter;

    FragmentAdapter fragmentAdapter;




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d("ciclo vita", "onCreateView");

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTask() {
        // Required empty public constructor
    }

    public FragmentTask(ArrayList <Task> listaTask, FragmentAdapter fragmentAdapter) {
        this.fragmentAdapter = fragmentAdapter;
        this.listaTask = listaTask;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTask.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTask newInstance(String param1, String param2) {
        FragmentTask fragment = new FragmentTask();
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
        View v = inflater.inflate(R.layout.fragment_task, container, false);

        RecyclerView lista = v.findViewById(R.id.lista);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentFragment().getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);//mostra a partire dall'elemento 0 in questo caso
        lista.setLayoutManager(layoutManager);//una recycler view deve avere per forza un layout manager
        lista.addItemDecoration(new DivisoreElementi(DivisoreElementi.SPAZIO_DI_DEFAULT-150));


        adapter = new CustomAdapterList(listaTask, context, R.layout.layout_lista_task, GenericViewHolder.LISTA_2,fragmentAdapter);
        lista.setAdapter(adapter);

        return v;
    }



}