package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.GestioneTesi;
import it.uniba.dib.sms2223.laureapp.business.ICostanti;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.business.Utile;
import it.uniba.dib.sms2223.laureapp.model.RichiestaTesi;
import it.uniba.dib.sms2223.laureapp.model.Studente;
import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.utils.QRCodeGenerator;

public class RichiestaTesiStudente extends AppCompatActivity {

    TextView txtTitoloTesi,txtCorsoDiLaurea, txtTipologiaTesi, txtRelatore
            ,txtCorelatore, txtDescrizione, txtVotazioneConsigliata,txtTempoRichiesto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richiesta_tesi_studente);


        Tesi tesi =getIntent().getExtras().getParcelable("Tesi");


        Button btnRichiedi = findViewById(R.id.btn_richiedi_tesi);


        Toolbar toolbar = findViewById(R.id.tlb_dettaglio_tesi);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        txtTitoloTesi = findViewById(R.id.txt_titolo_tesi);
        txtCorsoDiLaurea = findViewById(R.id.txt_nome_corso);
        txtTipologiaTesi = findViewById(R.id.txt_tipologia_tesi);
        txtRelatore = findViewById(R.id.txt_nome_relatore);
        txtCorelatore = findViewById(R.id.txt_nome_co_relatore);
        txtDescrizione = findViewById(R.id.txt_descrizione_tesi);
        txtVotazioneConsigliata = findViewById(R.id.txt_votazione);
        txtTempoRichiesto = findViewById(R.id.txt_tempo_lavoro);

        GridLayout gridLayout = findViewById(R.id.grid_view_insegnamenti);

        Button btnContattaRelatore = findViewById(R.id.btn_contatta_relatore);
        Button btnContattaCorelatore = findViewById(R.id.btn_contatta_co_relatore);

        btnContattaRelatore.setOnClickListener(view -> {
           String emailrelatore = tesi.sRelatore.split(" ")[2];
            new Utile(this).condividiInfo(emailrelatore,getString(R.string.app_name) +": "+ tesi.titolo, ICostanti.INVIO_EMAIL,null);
        });

        btnContattaCorelatore.setOnClickListener(view -> {
            String emailCorelatore = tesi.sCorelatore.split(" ")[2];
            new Utile(this).condividiInfo(emailCorelatore,getString(R.string.app_name) +": "+ tesi.titolo, ICostanti.INVIO_EMAIL,null);

        });

        Log.d("GTG",""+tesi.studente);
        if (tesi.studente !=null){
            btnRichiedi.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.bottone_inattivo)));
            btnRichiedi.setTextColor(ContextCompat.getColor(this, R.color.white));
        }

        btnRichiedi.setOnClickListener(view -> {
            if (Utente.utenteLoggato()) {
                if (tesi.studente == null) {
                        String emailStudente = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        inizializzaDialog(tesi, emailStudente).show();

                } else
                    Toast.makeText(this, getString(R.string.tesi_assegnata), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(),getString(R.string.loggati),Toast.LENGTH_SHORT).show();
        });



         inizializzaSchermata(tesi,gridLayout);
         txtTitoloTesi.setText(tesi.titolo);
        //Log.d("dati tesi",tesi.esamiRichiesti.get(0));
    }

    private void inizializzaSchermata(Tesi tesi,GridLayout gridLayout){
        txtTitoloTesi.setText(getString(R.string.titolo_tesi));
        txtCorsoDiLaurea.setText(tesi.corsoDiLaurea);
        txtTipologiaTesi.setText(getString(R.string.tipologia_tesi) + ": "+ tesi.tipo);
        txtRelatore.setText(tesi.sRelatore.split(" ")[0] + " " +tesi.sRelatore.split(" ")[1]);//imposto il nome e cognome
        if (tesi.sCorelatore != null)
            txtCorelatore.setText(tesi.sRelatore.split(" ")[0] + tesi.sRelatore.split(" ")[0]);//imposto il nome e cognome
        else{
            findViewById(R.id.lyt_info_corelatore).setVisibility(View.GONE);
        }
        txtDescrizione.setText(tesi.descrizione);
        txtVotazioneConsigliata.setText(getString(R.string.votazione_consigliata) +" "+ tesi.mediaRichiesta);
        txtTempoRichiesto.setText(getString(R.string.tempo_richiesto) + " " +tesi.durata + " ore");

        for (String esame: tesi.esamiRichiesti){
            MaterialButton testSignIn = new MaterialButton(this);
            String buttonText = esame;
            testSignIn.setText(buttonText);
            testSignIn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.background)));
            testSignIn.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.richiamo_azione)));
            testSignIn.setTextColor(ContextCompat.getColor(this, R.color.richiamo_azione));
            testSignIn.setStrokeWidth(3);
            gridLayout.addView(testSignIn);
        }

    }

    private Dialog inizializzaDialog(Tesi tesi,String emailStudente){

        //RichiestaTesi richiestaTesi = new RichiestaTesi();
        Dialog customDialog = new Dialog(this,R.style.CustomAlertDialog);
        customDialog.setContentView(R.layout.dialog_invio_richiesta);
        LinearLayout lytContenitoreEsami = customDialog.findViewById(R.id.lyt_contenitore_esami);
        EditText edtVoto = customDialog.findViewById(R.id.edt_media_voti);
        EditText edtNumEsami = customDialog.findViewById(R.id.edt_num_esami_mancanti);
        Button btnRichiedi = customDialog.findViewById(R.id.btn_invia_info);
        TextInputLayout edtNote = customDialog.findViewById(R.id.edt_note);

        ArrayList<String> esamiSostenuti = new ArrayList<>();

        for (String esame : tesi.esamiRichiesti) {
            // Crea un oggetto LinearLayout orizzontale
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Crea un oggetto CheckBox
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Imposta un tag o un identificatore univoco per il CheckBox in modo da poterlo identificare successivamente
            checkBox.setTag(esame); // Ad esempio, puoi utilizzare il nome dell'esame come tag

            // Crea un oggetto TextView
            TextView textView = new TextView(getApplicationContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(esame);

            // Aggiungi il CheckBox e il TextView al LinearLayout
            linearLayout.addView(checkBox);
            linearLayout.addView(textView);

            // Aggiungi il LinearLayout al tuo layout principale
            lytContenitoreEsami.addView(linearLayout);

            // Aggiungi un listener al CheckBox per gestire la selezione
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        esamiSostenuti.add(buttonView.getTag().toString());
                        Log.d("GVG", buttonView.getTag().toString());
                    } else {
                        esamiSostenuti.remove(buttonView.getTag().toString());
                    }
                }
            });

        }
        btnRichiedi.setOnClickListener(view -> {
            String note = String.valueOf(edtNote.getEditText().getText());
            Studente studente = new Studente(emailStudente,null,null,null,0);
            Log.d("WWR","premuto");

            double mediaVoti=0; int esamiMancanti=0;
            if (!edtVoto.getText().toString().equals("")){
                mediaVoti = Double.parseDouble(edtVoto.getText().toString());
            }
            if (!edtNumEsami.getText().toString().equals("")){
                esamiMancanti = Integer.parseInt(edtNumEsami.getText().toString());
            }
            GestioneTesi.inviaRichiestaPerLaTesi(this,tesi,note,studente,mediaVoti,esamiMancanti,esamiSostenuti,customDialog);
        });
        return customDialog;
    }



}