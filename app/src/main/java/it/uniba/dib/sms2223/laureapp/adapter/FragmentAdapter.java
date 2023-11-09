package it.uniba.dib.sms2223.laureapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> arrayListFragment = new ArrayList<>();
    private int position;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        this.position = position;
        Fragment frag = arrayListFragment.get(position);


        return frag;
    }
    @Override
    public int getItemCount() {
        return arrayListFragment.size();
    }

    public void aggiungiFragment(Fragment fragment){
        arrayListFragment.add(fragment);
    }


    /**
     *
     * @return restituisce la collezione dei fragment tramite l'oggetto ArrayList<Fragment>
     */
    public ArrayList<Fragment> getListaFragment(){
        return arrayListFragment;
    }
}
