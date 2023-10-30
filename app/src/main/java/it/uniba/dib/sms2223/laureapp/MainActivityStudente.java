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

import it.uniba.dib.sms2223.laureapp.business.Utente;
import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;

public class MainActivityStudente extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_studente);


        //----- righe provvisori, forse finiscono in theme ---------------
        AspettoActivity aspettoActivity = new AspettoActivity(this);
        aspettoActivity.impostaColoreStatusBar(R.color.background, true);//imposto il colore della status bar
        //------------------------------------------------------------------------------

        bottomNavigationView = findViewById(R.id.navigation_bar_studente);
        //con api 19+
        bottomNavigationView.getMenu().getItem(1).setChecked(true);//imposto di default l'icona della home selezionata, perchè la prima pagina in cui ci troviamo è la home


        //---------------------------- CODICE IN FASE DI TEST NON TOCCARE ---------------------------------------
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.fragment_container_home_s, FragmentHomeStudente.class, null)//R.id.fragment_container è colui che contiene il fragment - IlMioFrag.class è il fragment che voglio nel contenitore
                .setReorderingAllowed(true)
                .addToBackStack("home")//questo metodo permette di inserire il fragment in una pila in modo che se si preme indietrto si torna indietro con la navigazione dei fragment. Quando non ci sono più fragment si torna al ciclo di vita dell'activity
                //senza addToBackStack() il fragment viene ditrutto e quindi impossibile tornare indietro a recuperarlo quando è sostituito
                //è la pila in caso si preme il pulsante back mi fa gestire la cosa "nome" può essere null
                .commit();
        //-------------------------------------------------------------------------------------------------------

        Button logout = findViewById(R.id.btn_logout2);
        logout.setOnClickListener(view -> {
           new Utente(this).logOut();

        });

        bottomNavigationView.setOnItemSelectedListener(item -> {


            if (item.getItemId() != R.id.btn_nvg_preferiti && item.getItemId() != R.id.btn_nvg_cerca) {
                Toast.makeText(this, "sei nella Home", Toast.LENGTH_SHORT).show();

                cambiaFragment(fragmentManager, new FragmentHomeStudente(), null);

            } else if (item.getItemId() == R.id.btn_nvg_preferiti) {

                cambiaFragment(fragmentManager, new FragmentTesiPreferite(), null);

                Toast.makeText(this, "hai premuto Preferiti", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.btn_nvg_cerca) {

                cambiaFragment(fragmentManager, new FragmentCercaTesi(), null);

                Toast.makeText(this, "hai premuto cerca", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }


    //----------------------------------codice in fase di test -----------------------------------------------------
    private void cambiaFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {//*******aggiungere come secondo parametro il fragment con cui sostituire UNA PARTE DEL PROBLEMA RISOLTO PERò NON SALVARE NELLO STACK I FRAGMENT QUANDO NELLO STACK CI SONO PIù DI 3 ELEMENTI
        Fragment f = fragmentManager.findFragmentByTag(tag);
        int count = fragmentManager.getBackStackEntryCount();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_home_s, fragment, tag)
                // .setReorderingAllowed(true)
                //  .addToBackStack(null)
                .commit();

           /* if (tag.equals(TAG_HOME_FRAGMENT)) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_home_s, fragment, tag)
                        .addToBackStack(null)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_home_s, fragment, tag)
                        // .setReorderingAllowed(true)
                        //  .addToBackStack(null)
                        .commit();

            }*/

    }

    //------------------------------------------------------------------------------------------------------------
}
