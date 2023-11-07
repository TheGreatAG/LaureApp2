package it.uniba.dib.sms2223.laureapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import it.uniba.dib.sms2223.laureapp.model.Tesi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDettaglioTesi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDettaglioTesi extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Tesi tesi;

    public FragmentDettaglioTesi() {
        // Required empty public constructor
    }

    public FragmentDettaglioTesi(Tesi tesi){
        this.tesi = tesi;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDettaglioTesi.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDettaglioTesi newInstance(String param1, String param2) {
        FragmentDettaglioTesi fragment = new FragmentDettaglioTesi();
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
            tesi = getArguments().getParcelable("tesi");
            // Fai qualcosa con l'oggetto Parcelable ricevuto
        }

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater vi, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return vi.inflate(R.layout.fragment_dettaglio_tesi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TextView txtTitoloTesi = v.findViewById(R.id.txt_titolo_tesi);
        ImageView imgStatoTesi = v.findViewById(R.id.img_stato_tesi);
        TextView txtStatoTesi = v.findViewById(R.id.txt_stato_tesi);
        TextView txtTipoTesi = v.findViewById(R.id.txt_tipo_tesi);
        TextView txtNomeRelatore = v.findViewById(R.id.txt_nome_relatore);
        TextView txtNomeCorelatore = v.findViewById(R.id.txt_nome_corelatore);
        MaterialButton btnInvioEmailRicevimento1 = v.findViewById(R.id.btn_invio_email_ricevimento1);
        MaterialButton btnInvioEmailRicevimento2 = v.findViewById(R.id.btn_invio_email_ricevimento2);
        TextView txtDescrizioneTesi = v.findViewById(R.id.txt_descrizione_tesi);
        TextView txtNomeFile = v.findViewById(R.id.txt_nome_file);
        MaterialButton btnCaricaTesi = v.findViewById(R.id.btn_carica_tesi);
        Button btnInvioTesi = v.findViewById(R.id.btn_invio_tesi);

        RelativeLayout lytTxtCorelatore = v.findViewById(R.id.lyt_txt_corelatore);

        txtTitoloTesi.setText(tesi.titolo);
        txtTipoTesi.setText(tesi.tipo);
        txtNomeRelatore.setText(tesi.relatore.nome + " " + tesi.relatore.cognome);

        if (tesi.corelatore == null)
            lytTxtCorelatore.setVisibility(View.GONE);
        else
            txtNomeCorelatore.setText(tesi.corelatore.nome + " "+ tesi.corelatore.cognome);

        txtDescrizioneTesi.setText(tesi.descrizione);
    }
}