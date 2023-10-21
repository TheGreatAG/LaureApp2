package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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


        bottomNavigationView = findViewById(R.id.navigation_bar_docente);
        //con api 19+
        bottomNavigationView.getMenu().getItem(1).setChecked(true);//imposto di default l'icona della home selezionata, perchè la prima pagina in cui ci troviamo è la home

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.fragment_container_home_d, FragmentHomeDocente.class, null)//R.id.fragment_container è colui che contiene il fragment - IlMioFrag.class è il fragment che voglio nel contenitore
                .setReorderingAllowed(true)
                .addToBackStack("home")//questo metodo permette di inserire il fragment in una pila in modo che se si preme indietrto si torna indietro con la navigazione dei fragment. Quando non ci sono più fragment si torna al ciclo di vita dell'activity
                //senza addToBackStack() il fragment viene ditrutto e quindi impossibile tornare indietro a recuperarlo quando è sostituito
                //è la pila in caso si preme il pulsante back mi fa gestire la cosa "nome" può essere null
                .commit();

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}