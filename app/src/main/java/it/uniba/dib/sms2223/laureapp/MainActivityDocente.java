package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;

public class MainActivityDocente extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_docente);

        AspettoActivity aspettoActivity = new AspettoActivity(this);
        aspettoActivity.impostaColoreStatusBar(R.color.background,true);//imposto il colore della status bar


        bottomNavigationView = findViewById(R.id.navigation_bar_studente);
        //con api 19+
        bottomNavigationView.getMenu().getItem(1).setChecked(true);//imposto di default l'icona della home selezionata, perchè la prima pagina in cui ci troviamo è la home

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}