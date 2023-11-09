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
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.fragment_container_home_d, FragmentHomeDocente.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .commit();


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

            return true;
        });


        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void cambiaFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {

        if (tag != null){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_home_d, fragment, tag)
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