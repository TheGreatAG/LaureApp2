package it.uniba.dib.sms2223.laureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import it.uniba.dib.sms2223.laureapp.ui.AspettoActivity;

public class MainActivityStudente extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_studente);


        AspettoActivity aspettoActivity = new AspettoActivity(this);
        aspettoActivity.impostaColoreStatusBar(R.color.background, true);//imposto il colore della status bar
        //------------------------------------------------------------------------------

        bottomNavigationView = findViewById(R.id.navigation_bar_studente);
        //con api 19+
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.fragment_container_home_s, FragmentHomeStudente.class, null)//R.id.fragment_container è colui che contiene il fragment - IlMioFrag.class è il fragment che voglio nel contenitore
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .commit();
        //-------------------------------------------------------------------------------------------------------

        bottomNavigationView.setOnItemSelectedListener(item -> {


            if (item.getItemId() != R.id.btn_nvg_preferiti && item.getItemId() != R.id.btn_nvg_cerca) {

                cambiaFragment(fragmentManager, new FragmentHomeStudente(), null);

            } else if (item.getItemId() == R.id.btn_nvg_preferiti) {

                cambiaFragment(fragmentManager, new FragmentTesiPreferite(), null);

            } else if (item.getItemId() == R.id.btn_nvg_cerca) {

                cambiaFragment(fragmentManager, new FragmentCercaTesi(), null);
            }
            return true;
        });
    }


    //----------------------------------codice in fase di test -----------------------------------------------------
    private void cambiaFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_home_s, fragment, tag)
                // .setReorderingAllowed(true)
                  .addToBackStack(null)
                .commit();



    }

    //------------------------------------------------------------------------------------------------------------
}
