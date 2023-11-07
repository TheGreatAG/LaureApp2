package it.uniba.dib.sms2223.laureapp;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.model.Tesi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDettaglioTesi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDettaglioTesi extends Fragment implements ICostanti {

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

    private StorageReference storageReference;
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
        Button btnInvioConsegnaTesi = v.findViewById(R.id.btn_invio_tesi);

        ProgressBar progressBar = v.findViewById(R.id.progress_carica_file);

        RelativeLayout lytTxtCorelatore = v.findViewById(R.id.lyt_txt_corelatore);

        txtStatoTesi.setText(tesi.stato);
        txtTitoloTesi.setText(tesi.titolo);
        txtTipoTesi.setText(tesi.tipo);
        txtNomeRelatore.setText(tesi.relatore.nome + " " + tesi.relatore.cognome);

        if (tesi.corelatore == null)
            lytTxtCorelatore.setVisibility(View.GONE);
        else
            txtNomeCorelatore.setText(tesi.corelatore.nome + " "+ tesi.corelatore.cognome);

        txtDescrizioneTesi.setText(tesi.descrizione);

        if (tesi.stato.equals(STATO_TESI_CONSEGNATA)){
            imgStatoTesi.setVisibility(View.GONE);
            txtStatoTesi.setText(STATO_TESI_CONSEGNATA);
            txtStatoTesi.setTextColor(getContext().getColor(R.color.testo_task_completato));
            ColorStateList colorStateList = ColorStateList.valueOf(getContext().getResources().getColor(R.color.bottone_inattivo));
            btnInvioConsegnaTesi.setBackgroundTintList(colorStateList);
            btnInvioConsegnaTesi.setText(getContext().getString(R.string.attendi_la_revisione));
        }

        ActivityResultLauncher<String> filePickerLauncher;
        storageReference = FirebaseStorage.getInstance().getReference();

        // Inizializza il launcher per la selezione del file
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            // Il risultato selezionato è restituito qui
            if (uri != null) {
                // Passa l'uri del file selezionato a fireStore
                uploadFileToFirestore(uri,tesi,progressBar,txtNomeFile,uri.toString());
            }
        });

        btnCaricaTesi.setOnClickListener(view -> {
            filePickerLauncher.launch("*/*"); //in questo caso il filePicker è generico e può
        });

        btnInvioConsegnaTesi.setOnClickListener(view -> {
            aggiornaStatoTesi(tesi,btnInvioConsegnaTesi,imgStatoTesi,txtStatoTesi);
        });
    }

    private void uploadFileToFirestore(Uri fileUri,Tesi tesi,ProgressBar progressBar,TextView txtFileCaricato,String file) {

        String fileName = "TESI_"+tesi.id+"_"+tesi.studente+"_" + System.currentTimeMillis(); // Genera un nome per il file, in questo caso
        // univoco con il timeStamp, ma si modifica a seconda della necessità, con l'id della tesi, del task o di quel che è
        StorageReference fileReference = storageReference.child("files/" + fileName); // qui al posto di files si inserisce il nome della raccolta
        // in cui andare a inserire il file
        progressBar.setVisibility(View.VISIBLE);
        // Carica il file nel Firebase Cloud Storage col metodo putFile
        fileReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            // Fare cose una volta caricato il file
            txtFileCaricato.setText(file.split("document/")[1]);
            progressBar.setVisibility(View.GONE);
        }).addOnFailureListener(e -> {
            // Fare cose una volta che il caricamento è fallito
        });
    }

    private void aggiornaStatoTesi(Tesi tesi,Button btnConsegna,ImageView imageView,TextView txtStatoTesi){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection(ICostanti.COLLECTION_PROF)
                .document(tesi.relatore.email).collection(ICostanti.COLLECTION_TESI)
                .document(tesi.id);

        if (tesi.stato.equals(STATO_TESI_DA_CONSEGNARE)) {

// Crea un oggetto Map con i campi da aggiornare
            Map<String, Object> updates = new HashMap<>();
            updates.put("stato", STATO_TESI_CONSEGNATA);

// Esegui l'aggiornamento sul documento
            documentReference.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ColorStateList colorStateList = ColorStateList.valueOf(getContext().getResources().getColor(R.color.bottone_inattivo));
                            btnConsegna.setBackgroundTintList(colorStateList);
                            btnConsegna.setText(getContext().getString(R.string.attendi_la_revisione));

                            imageView.setVisibility(View.GONE);
                            txtStatoTesi.setText(STATO_TESI_CONSEGNATA);
                            txtStatoTesi.setTextColor(getContext().getColor(R.color.testo_task_completato));


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Si è verificato un errore durante l'aggiornamento
                        }
                    });
        } else {
            Toast.makeText(getContext(),getContext().getString(R.string.attendi_la_revisione),Toast.LENGTH_SHORT).show();
        }

    }
}