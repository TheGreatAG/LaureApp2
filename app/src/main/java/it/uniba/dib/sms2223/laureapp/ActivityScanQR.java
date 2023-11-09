package it.uniba.dib.sms2223.laureapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.widget.Button;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.uniba.dib.sms2223.laureapp.model.Tesi;
import it.uniba.dib.sms2223.laureapp.utils.CaptureAct;

public class ActivityScanQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        scanCode();
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.scansiona_qr));
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            Log.d("DSDR", result.getContents());

            // Estrai le informazioni dalla stringa del risultato
            String jsonString = result.getContents();
            try {
                JSONObject json = new JSONObject(jsonString);

                // Estrai tutti i campi dalla stringa JSON
                String id = json.getString("id");
                String titolo = json.getString("titolo");
                String tipo = json.getString("tipo");
                String descrizione = json.getString("descrizione");
                String ambito = json.getString("ambito");
                String corsoDiLaurea = json.getString("corsoDiLaurea");
                String sRelatore = json.getString("sRelatore");
                String sCorelatore = null;
                try {
                    sCorelatore = json.getString("sCorelatore");
                } catch (JSONException e) {
                    // Il campo "sCorelatore" non è presente nel JSON
                    e.printStackTrace();
                }
               // String studente = json.getString("studente");
                int mediaRichiesta = json.getInt("mediaRichiesta");
                int durata = json.getInt("durata");
                String dataPubblicazione = json.getString("dataPubblicazione");
                //String stato = json.getString("stato");

                // Estrai la lista di esamiRichiesti
                JSONArray esamiRichiestiArray = json.getJSONArray("esamiRichiesti");
                ArrayList<String> esamiRichiesti = new ArrayList<>();
                for (int i = 0; i < esamiRichiestiArray.length(); i++) {
                    esamiRichiesti.add(esamiRichiestiArray.getString(i));
                }


                Tesi tesi = new Tesi(id,titolo,tipo,descrizione,ambito,corsoDiLaurea,dataPubblicazione,mediaRichiesta,durata,
                        sRelatore,sCorelatore,esamiRichiesti);

                ActivityScanQR.this.runOnUiThread(() -> {
                    startActivity(new Intent(ActivityScanQR.this, RichiestaTesiStudente.class).putExtra("Tesi", tesi));
                    finish();
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });


}
/* {"titolo":"App Android",
            "descrizione":"La tesi non si fa da sola"
            ,"dataPubblicazione":"03-11-2023"
            ,"ambito":"Analisi dei dati"
            ,"tipo":"Compilativa"
            ,"corsoDiLaurea":"Corso di informatica"
            ,"sRelatore":"Ruggeto Ceglie ceglie@uniba.it"
            ,"mediaRichiesta":19
            ,"durata":66
            ,"id":"Jt60ZjIrYA5wSeRqkIPT"
            ,"esamiRichiesti":["Calcolabilità e complessità","Calcolo numerico","Ingegneria del software","Laboratorio di informatica","Matematica discreta"]}
*/