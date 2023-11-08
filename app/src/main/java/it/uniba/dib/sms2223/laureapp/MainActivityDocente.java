package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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

        fragmentManager.beginTransaction().replace(R.id.fragment_container_home_d, FragmentHomeDocente.class, null)//R.id.fragment_container è colui che contiene il fragment - IlMioFrag.class è il fragment che voglio nel contenitore
                .setReorderingAllowed(true)
                .addToBackStack("home")//questo metodo permette di inserire il fragment in una pila in modo che se si preme indietrto si torna indietro con la navigazione dei fragment. Quando non ci sono più fragment si torna al ciclo di vita dell'activity
                //senza addToBackStack() il fragment viene ditrutto e quindi impossibile tornare indietro a recuperarlo quando è sostituito
                //è la pila in caso si preme il pulsante back mi fa gestire la cosa "nome" può essere null
                .commit();

       // cambiaFragment(fragmentManager, new FragmentHomeDocente(), "home");

        bottomNavigationView.setOnItemSelectedListener(item -> {


            if (item.getItemId() == R.id.btn_nvg_home) {
                Toast.makeText(this, "sei nella Home", Toast.LENGTH_SHORT).show();
                cambiaFragment(fragmentManager, new FragmentHomeDocente(), "home");

            } else if (item.getItemId() == R.id.btn_nvg_ricevimenti) {
                Toast.makeText(this, "sei nella Home", Toast.LENGTH_SHORT).show();
                cambiaFragment(fragmentManager, new FragmentRicevimentiDocente(), null);

            } else if (item.getItemId() == R.id.btn_nvg_richieste) {
                Toast.makeText(this, "sei nella richiesta", Toast.LENGTH_SHORT).show();
                cambiaFragment(fragmentManager, new FragmentNotifiche(), null);
            }

           /* if (item.getItemId() != R.id.btn_nvg_home ) {
                Toast.makeText(this, "sei nella Home", Toast.LENGTH_SHORT).show();

                cambiaFragment(fragmentManager, new FragmentHomeDocente(), null);

            } else if (item.getItemId() == R.id.btn_nvg_richieste) {

                cambiaFragment(fragmentManager, new FragmentRicevimentiDocente(), null);//test il fragment non è valido

                Toast.makeText(this, "hai premuto richieste", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.btn_nvg_ricevimenti) {

                cambiaFragment(fragmentManager, new FragmentRicevimentiDocente(), null);

                Toast.makeText(this, "hai premuto ricevimenti", Toast.LENGTH_SHORT).show();
            }*/
            return true;
        });


        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void cambiaFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {//*******aggiungere come secondo parametro il fragment con cui sostituire UNA PARTE DEL PROBLEMA RISOLTO PERò NON SALVARE NELLO STACK I FRAGMENT QUANDO NELLO STACK CI SONO PIù DI 3 ELEMENTI
        Fragment f = fragmentManager.findFragmentByTag(tag);
        int count = fragmentManager.getBackStackEntryCount();

        if (tag != null){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_home_d, fragment, tag)
                    // .setReorderingAllowed(false)
                     .addToBackStack(null)
                    .commit();
        } else {

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_home_d, fragment, tag)
                     .setReorderingAllowed(false)
                     .addToBackStack(null)
                    .commit();
        }
    }
}