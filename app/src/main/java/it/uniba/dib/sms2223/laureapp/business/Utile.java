package it.uniba.dib.sms2223.laureapp.business;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;
import static android.net.NetworkCapabilities.TRANSPORT_WIFI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utile {

    private Context context;
    public Utile(Context context){
        this.context = context;
    }


    /**
     * Controlla che ci sia la connessione ad Internet
     * @return true se iInternet è disponibile, false altrimenti
     */
    public boolean connessione(){
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiCon = con.getNetworkInfo(TRANSPORT_WIFI);
        NetworkInfo mobileCon = con.getNetworkInfo(TRANSPORT_CELLULAR);

        if (wifiCon != null && wifiCon.isConnected() || (mobileCon != null && mobileCon.isConnected())){
            return true;
        } else {
            return false;
        }
    }


    /**
     * abbrevia la stringa fornita in input impostando i caratteri ammessi e sostituiti gli ultimi 3 caratteri con tre punti
     * @param testo la stringa da abbreviare
     * @param numCaratteriAmmessi il numero di caratteri ammessi, a questo intero verrà sottratto 3 per sostituire gli ultimi 3 caratteri con tre punti
     * @return la stringa abbreviata con tre punti
     */
    public String abbreviaTesto(String testo, int numCaratteriAmmessi){

        if (testo.length() > numCaratteriAmmessi){
            return testo.substring(0,numCaratteriAmmessi-3) + "...";
        } else return testo;
    }

}
