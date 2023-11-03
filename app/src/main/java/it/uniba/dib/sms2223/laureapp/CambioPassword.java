package it.uniba.dib.sms2223.laureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.android.material.textfield.TextInputLayout;

public class CambioPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_password);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.tlb_cambio_pw);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        TextInputLayout edtPasswordAttuale = findViewById(R.id.edt_passwor_attuale);//associo il TextInputLayout alla omologa variabile Java
        TextInputLayout edtPasswordNuova = findViewById(R.id.edt_nuova_password);
        TextInputLayout edtPasswordConferma = findViewById(R.id.edt_conferma_password);

        Button btnSalva = findViewById(R.id.btn_salva);
        btnSalva.setOnClickListener(view -> {
            //SOME_SECURE-PASSWORD
            //String edtPasswordAttualeString = String.valueOf(edtPasswordAttuale.getEditText().getText());
            String edtPasswordNuovaString = String.valueOf(edtPasswordNuova.getEditText().getText());
            String edtPasswordConfermaString = String.valueOf(edtPasswordConferma.getEditText().getText());

            if(confrontaPassword(edtPasswordNuovaString,edtPasswordConfermaString)){

                user.updatePassword(edtPasswordConfermaString) //l'utente deve aver facco un accesso recente per poter modificare la password, quindi una soluzione oltre a farlo uscire dal sistema e farlo loggare di nuovo, fare un login istantaneo a questo punto
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),getString(R.string.password_cambiata),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });

    }

    private boolean confrontaPassword(String password1, String password2){
        if(password1.equals(password2)){
            return true;
        }else return false;
    }

}