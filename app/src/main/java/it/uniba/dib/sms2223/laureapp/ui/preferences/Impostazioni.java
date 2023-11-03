package it.uniba.dib.sms2223.laureapp.ui.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;

import it.uniba.dib.sms2223.laureapp.ActivityPrimoAccessoLogin;
import it.uniba.dib.sms2223.laureapp.CambioPassword;
import it.uniba.dib.sms2223.laureapp.PrimoAccessoDocente;
import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.business.Credenziali;
import it.uniba.dib.sms2223.laureapp.business.Utente;

public class Impostazioni extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.impostazioni, rootKey);
        findPreference("cambia_corso").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Toast.makeText(getContext(),"vvyvyv",Toast.LENGTH_SHORT).show();
                if (Credenziali.validitaEmailStudente(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    getContext().startActivity(new Intent(getContext(), ActivityPrimoAccessoLogin.class).putExtra("key", true));
                } else {
                    getContext().startActivity(new Intent(getContext(), PrimoAccessoDocente.class).putExtra("chiamante",true));
                }
                return false;
            }
        });
        findPreference("cambia_pw").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                getContext().startActivity(new Intent(getContext(), CambioPassword.class));
                return false;
            }
        });
        findPreference("elimina_profilo").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Toast.makeText(getContext(),"88888",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        findPreference("esci").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                new Utente(getContext()).logOut();
                return false;
            }
        });

    }
}
