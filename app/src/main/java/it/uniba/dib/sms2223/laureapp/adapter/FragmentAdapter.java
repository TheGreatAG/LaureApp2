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
        //inserire qua la collezione di fragment ovvero i layout con gli annunci visibili non visibili e tutti
        //vedere da blank adapter come creare il fragment con la lista di tutti gli annunci
        //Fragment frag = new FragmentAnnunci();
        //ES.
        Fragment frag = arrayListFragment.get(position);



        //  Bundle args = new Bundle();
        //  args.putString("key","/*ciao dal FragmantState " + position);
        //  frag.setArguments(args);
        return frag;
    }
    //DA MODIFICARE E FAR TORNARE IL NUMERO DI ELEMENTI DELL' ARRYLIST
    @Override
    public int getItemCount() {
        return arrayListFragment.size();
    }

    //chiamare questo metodo da HomeFragment (?)
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
