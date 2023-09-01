package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextInputLayout edtEmail = findViewById(R.id.edt_email);//associo il TextInputLayout alla omologa variabile Java
        TextInputLayout edtPassword = findViewById(R.id.edt_password);

        TextView txtRegistrati = findViewById(R.id.txt_registrati);
        TextView txtOspite = findViewById(R.id.txt_ospite);

        Button btnAccedi = findViewById(R.id.btn_accedi);

        AspettoActivity aspettoActivity = new AspettoActivity(this);
        aspettoActivity.impostaColoreStatusBar(R.color.background,true);//imposto il colore della status bar

        btnAccedi.setOnClickListener(view -> { //invece di setOnClickListener si possono usare le espressioni lambda introdotte in Java 8 che sono più veloci da scrivere
            String email = String.valueOf(edtEmail.getEditText().getText());
            String pw = String.valueOf(edtPassword.getEditText().getText());


            if (new Utente().utenteLoggato()){//controllo che l'utente abbia ricevuto esito positivo in fase di login
                Intent intent;
                if (new Credenziali().recuperaEmailUtente(email).contains("@studenti.")) {//dalla email recuperata in fase di login controllo che sia uno studente
                    intent = new Intent(this, MainActivityStudente.class);
                    startActivity(intent);
                } else { //dalla email recuperata in fase di login controllo che sia un professore
                    intent = new Intent(this, MainActivityDocente.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this,"Ho riscontrato un problema in fase di accesso"+ pw,Toast.LENGTH_SHORT ).show();// mostra il messagio di Toast
            }
        });

        txtOspite.setOnClickListener(view -> {
            /*------------Righe di test per passare direttamente alla schermata che mi serve-----------------*/
            Intent intent = new Intent(this, ActivityPrimoAccessoLogin.class);
            startActivity(intent);
            /*------------------------------------------------------------------------------------------------*/
            Toast.makeText(this,"Premuto accedi come ospite",Toast.LENGTH_SHORT ).show();
        });

        txtRegistrati.setOnClickListener(view -> {
            Toast.makeText(this,"Premuto registrati",Toast.LENGTH_SHORT ).show();
            Intent intent = new Intent(this,ActivityRegistrazione.class);
            startActivity(intent);
        });
    }
}