package it.uniba.dib.sms2223.laureapp.ui.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import it.uniba.dib.sms2223.laureapp.R;
import it.uniba.dib.sms2223.laureapp.business.Utente;

public class Impostazioni extends PreferenceFragmentCompat {

    private Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.impostazioni, rootKey);
        findPreference("cambia_corso").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Toast.makeText(getContext(),"vvyvyv",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        findPreference("cambia_pw").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Toast.makeText(getContext(),"66666",Toast.LENGTH_SHORT).show();
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

                new Utente(context).logOut(); //non funziona. Il problema è che se chiamato elimina l'activity che ospita il fragment, cioe ProfiloStudente ma non elimina dalla pila l'activity che c'era prima cioe MainActivityStudente
                return false;
            }
        });

    }
}
