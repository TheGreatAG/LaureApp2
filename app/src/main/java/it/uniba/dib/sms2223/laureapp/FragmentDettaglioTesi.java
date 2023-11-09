package it.uniba.dib.sms2223.laureapp;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.GestioneTesi;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.Ricevimento;
import it.uniba.dib.sms2223.laureapp.model.Studente;
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

    private boolean docente =false;
    private String emailStudente;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (Credenziali.validitaEmailProf(emailStudente)){
            docente =true;
        }
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

        TextView etichettaRelatore = v.findViewById(R.id.t1);
        TextView etichettaCorelatore = v.findViewById(R.id.t2);
        TextView txtNomeRelatore = v.findViewById(R.id.txt_nome_relatore);
        TextView txtNomeCorelatore = v.findViewById(R.id.txt_nome_corelatore);
        MaterialButton btnInvioEmailRicevimento1 = v.findViewById(R.id.btn_invio_email_ricevimento1);
        MaterialButton btnInvioEmailRicevimento2 = v.findViewById(R.id.btn_invio_email_ricevimento2);
        TextView txtDescrizioneTesi = v.findViewById(R.id.txt_descrizione_tesi);
        TextView txtNomeFile = v.findViewById(R.id.txt_nome_file);
        MaterialButton btnCaricaTesi = v.findViewById(R.id.btn_carica_tesi);
        Button btnInvioConsegnaTesi = v.findViewById(R.id.btn_invio_tesi);


        Button btnApprovaTesi = v.findViewById(R.id.btn_approva);
        Button btnRifiutaTesi = v.findViewById(R.id.btn_rigetta);

        ProgressBar progressBar = v.findViewById(R.id.progress_carica_file);

        RelativeLayout lytTxtCorelatore = v.findViewById(R.id.lyt_txt_corelatore);

        txtStatoTesi.setText(tesi.stato);
        txtTitoloTesi.setText(tesi.titolo);
        txtTipoTesi.setText(tesi.tipo);
        txtDescrizioneTesi.setText(tesi.descrizione);

        //imposta lo stato della tesi nella UI---
        if (tesi.stato.equals(STATO_TESI_CONSEGNATA)){
            imgStatoTesi.setVisibility(View.GONE);
            txtStatoTesi.setText(STATO_TESI_CONSEGNATA);
            txtStatoTesi.setTextColor(getContext().getColor(R.color.testo_task_completato));
            if (!docente) {
                ColorStateList colorStateList = ColorStateList.valueOf(getContext().getResources().getColor(R.color.bottone_inattivo));
                btnInvioConsegnaTesi.setBackgroundTintList(colorStateList);
                btnInvioConsegnaTesi.setText(getContext().getString(R.string.attendi_la_revisione));
            }

        }
        if (tesi.stato.equals(STATO_TESI_APPROVATA)) {
            imgStatoTesi.setVisibility(View.VISIBLE);
            imgStatoTesi.setBackground(getContext().getDrawable(R.drawable.ic_positivo));
            txtStatoTesi.setText(STATO_TESI_APPROVATA);
            txtStatoTesi.setTextColor(getContext().getColor(R.color.testo_task_completato));
            if (!docente){
                ColorStateList colorStateList = ColorStateList.valueOf(getContext().getResources().getColor(R.color.bottone_inattivo));
                btnInvioConsegnaTesi.setBackgroundTintList(colorStateList);
            }
        }
        if (tesi.stato.equals(STATO_TESI_RIGETTATA)) {
            imgStatoTesi.setVisibility(View.VISIBLE);
            imgStatoTesi.setBackground(getContext().getDrawable(R.drawable.ic_negativo));
            txtStatoTesi.setText(STATO_TESI_RIGETTATA);
            txtStatoTesi.setTextColor(getContext().getColor(R.color.rosso));
        }
        //-------------

        if (!docente){
            impostaLayoutPerStudente(txtNomeRelatore,txtNomeCorelatore,lytTxtCorelatore);
        } else {
            impostaLayoutPerRelatore(etichettaRelatore,txtNomeRelatore,etichettaCorelatore,txtNomeCorelatore,
                    null,lytTxtCorelatore,btnApprovaTesi,btnRifiutaTesi,btnInvioConsegnaTesi,btnCaricaTesi,
                    btnInvioEmailRicevimento2,btnInvioEmailRicevimento1,tesi,txtNomeFile);
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

        btnApprovaTesi.setOnClickListener(view -> {
            if (tesi.stato.equals(STATO_TESI_CONSEGNATA) || tesi.stato.equals(STATO_TESI_RIGETTATA)|| tesi.stato.equals(STATO_TESI_APPROVATA)) {
                aggiornaStatoTesi(tesi, null, imgStatoTesi, txtStatoTesi, STATO_TESI_APPROVATA);
            }
        });


        btnRifiutaTesi.setOnClickListener(view -> {
            if (tesi.stato.equals(STATO_TESI_CONSEGNATA) || tesi.stato.equals(STATO_TESI_RIGETTATA)|| tesi.stato.equals(STATO_TESI_APPROVATA)){
                    aggiornaStatoTesi(tesi, null, imgStatoTesi, txtStatoTesi, STATO_TESI_RIGETTATA);
                }

        });

        btnCaricaTesi.setOnClickListener(view -> {
            if (docente)
                downloadTesi(tesi);
            else
                filePickerLauncher.launch("*/*"); //in questo caso il filePicker è generico e può
        });

        btnInvioConsegnaTesi.setOnClickListener(view -> {
            if (tesi.stato.equals(STATO_TESI_DA_CONSEGNARE) || tesi.stato.equals(STATO_TESI_RIGETTATA)) {
                aggiornaStatoTesi(tesi, btnInvioConsegnaTesi, imgStatoTesi, txtStatoTesi, STATO_TESI_CONSEGNATA);
            } else
                Toast.makeText(getContext(),getString(R.string.attendi_la_revisione),Toast.LENGTH_SHORT).show();
        });

        btnInvioEmailRicevimento1.setOnClickListener(view -> {
            inizializzaDialogRicevimento(tesi,"relatore").show();
        });

        btnInvioEmailRicevimento2.setOnClickListener(view -> {
            if (!docente)
                inizializzaDialogRicevimento(tesi,"corelatore").show();
            else {
                String email = tesi.studente;
                new Utile(getContext()).condividiInfo(email, getString(R.string.app_name) + ": " + tesi.titolo, ICostanti.INVIO_EMAIL, null);
            }
        });
    }

    private void impostaLayoutPerStudente(TextView txtNomeRelatore,TextView txtNomeCorelatore,
                                          RelativeLayout lytTxtCorelatore){
        txtNomeRelatore.setText(tesi.relatore.nome + " " + tesi.relatore.cognome);

        if (tesi.corelatore == null)
            lytTxtCorelatore.setVisibility(View.GONE);
        else
            txtNomeCorelatore.setText(tesi.corelatore.nome + " "+ tesi.corelatore.cognome);

    }

    private void impostaLayoutPerRelatore(TextView etichettaRelatore,TextView txtNomeRelatore,TextView etichettaCorelatore,TextView txtNomeCorelatore,
                                          RelativeLayout lytTxtRelatore,RelativeLayout lytTxtCorelatore , Button btnApprovaTesi,Button btnRifiutaTesi,
                                          Button btnInvioConsegnaTesi,MaterialButton btnCaricaTesi,MaterialButton btnInvioEmailRicevimento2,
                                          MaterialButton btnInvioEmailRicevimento1,Tesi tesi,TextView txtDownloadFile){

        btnApprovaTesi.setVisibility(View.VISIBLE);
        btnRifiutaTesi.setVisibility(View.VISIBLE);
        btnInvioConsegnaTesi.setVisibility(View.GONE);

        btnCaricaTesi.setText(getContext().getString(R.string.scarica));
        btnCaricaTesi.setIconResource(R.drawable.ic_download);
        txtDownloadFile.setText(getString(R.string.download_file));

        btnInvioEmailRicevimento1.setVisibility(View.GONE);

        if (tesi.corelatore == null)
            lytTxtRelatore.setVisibility(View.GONE);
        else {
            etichettaRelatore.setText(getString(R.string.corelatore));
            txtNomeRelatore.setText(tesi.corelatore.nome + " " + tesi.corelatore.cognome);
        }
        etichettaCorelatore.setText(getContext().getString(R.string.studente_assegnato));
        if (tesi.studente == null){
            txtNomeCorelatore.setText("-- --");
        } else {
            txtNomeCorelatore.setText(tesi.studente);
        }

        btnInvioEmailRicevimento2.setText(getContext().getString(R.string.contatta));

    }

    private void downloadTesi(Tesi tesi) {

        //METTERE UN MESSAGGIO DI TOAST PER DIRE CHE NON CI SONO TESI DA SCARICARE
        storageReference = FirebaseStorage.getInstance().getReference();
        String fileName = "TESI_" + tesi.id + "_" + tesi.studente;

        StorageReference fileRef = storageReference.child("files/" + fileName);
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            String localFilePath = downloadDir.getAbsolutePath() + "/laureapp/" + fileName;
            File localFile = new File(localFilePath);
            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdir();
            }
            fileRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                // il file è stato scaricato con successo nella posizione locale
                Toast.makeText(getContext(), "File scaricato in: " + localFilePath, Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // si è verificato un errore durante lo scaricamento del file
                    Toast.makeText(getContext(), "si è verificato un errore", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void uploadFileToFirestore(Uri fileUri,Tesi tesi,ProgressBar progressBar,TextView txtFileCaricato,String file) {

        //*******************IL PROBLEMA è QUI, BISOGNA SPECIFICARE IL FORMATO DEL FILE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!------------
        String fileName = "TESI_" + tesi.id + "_" + tesi.studente; // Genera un nome per il file, in questo caso
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

    /**
     * Aggiorna lo stato di una tesi
     * @param tesi
     * @param btnConsegna può essere nullo se siamo nel lato docente
     * @param imageView
     * @param txtStatoTesi
     */
    private void aggiornaStatoTesi(Tesi tesi,Button btnConsegna,ImageView imageView,TextView txtStatoTesi,String statoTesi){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection(ICostanti.COLLECTION_PROF)
                .document(tesi.relatore.email).collection(ICostanti.COLLECTION_TESI)
                .document(tesi.id);



// Crea un oggetto Map con i campi da aggiornare
            Map<String, Object> updates = new HashMap<>();
            updates.put("stato", statoTesi);

// Esegui l'aggiornamento sul documento
            documentReference.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (!docente) {
                                ColorStateList colorStateList = ColorStateList.valueOf(getContext().getResources().getColor(R.color.bottone_inattivo));
                                btnConsegna.setBackgroundTintList(colorStateList);
                                btnConsegna.setText(getContext().getString(R.string.attendi_la_revisione));

                                imageView.setVisibility(View.GONE);
                                txtStatoTesi.setText(STATO_TESI_CONSEGNATA);
                                txtStatoTesi.setTextColor(getContext().getColor(R.color.testo_task_completato));
                            } else {
                                if (statoTesi.equals(STATO_TESI_APPROVATA)) {
                                    imageView.setVisibility(View.VISIBLE);
                                    imageView.setBackground(getContext().getDrawable(R.drawable.ic_positivo));
                                    txtStatoTesi.setText(STATO_TESI_APPROVATA);
                                    txtStatoTesi.setTextColor(getContext().getColor(R.color.testo_task_completato));
                                }
                                if (statoTesi.equals(STATO_TESI_RIGETTATA)) {
                                    imageView.setVisibility(View.VISIBLE);
                                    imageView.setBackground(getContext().getDrawable(R.drawable.ic_negativo));
                                    txtStatoTesi.setText(STATO_TESI_RIGETTATA);
                                    txtStatoTesi.setTextColor(getContext().getColor(R.color.rosso));
                                }
                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Si è verificato un errore durante l'aggiornamento
                        }
                    });

    }

    private Dialog inizializzaDialogRicevimento(Tesi tesi,String docente){

            //RichiestaTesi richiestaTesi = new RichiestaTesi();
            Dialog customDialog = new Dialog(getContext(),R.style.CustomAlertDialog);
            customDialog.setContentView(R.layout.dialog_ricevimento);
            EditText editText = customDialog.findViewById(R.id.edt_descrizione);
            Button btnInvia = customDialog.findViewById(R.id.btn_richiedi_ricevimento);


            btnInvia.setOnClickListener(view -> {
                String descrizione = editText.getText().toString();

                Date dataCorrente = new Date();
                SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String data = formatoData.format(dataCorrente);
                Ricevimento ricevimento = new Ricevimento(tesi,descrizione,data,null);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String email =null;
                if (docente.equals("relatore")){
                    email = tesi.relatore.email;
                } if (docente.equals("corelatore")){
                    email = tesi.corelatore.email;
                }

                Log.d("CTC",ricevimento.tesi.toString());
                db.collection(COLLECTION_PROF).document(email).collection(COLLECTION_RICEVIMENTI).add(ricevimento) //se si vuole lasciare al sistema la creazione in automatico di un id per il documento usare collection().add()
                        //asltrimenti collection().document(ID documento).set()
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), getString(R.string.richiesta_inviata), Toast.LENGTH_LONG).show();
                               customDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            });
            return customDialog;

    }
}