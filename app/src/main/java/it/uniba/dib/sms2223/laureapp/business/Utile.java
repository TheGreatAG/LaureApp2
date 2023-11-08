package it.uniba.dib.sms2223.laureapp.business;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;
import static android.net.NetworkCapabilities.TRANSPORT_WIFI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import it.uniba.dib.sms2223.laureapp.R;

public class Utile implements ICostanti{

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

    /**
     * Prepara l'Intent per l'invio di informazioni
     * @param emailDestinatario email del destinatario
     * @param oggetto l'oggetto dell'email
     * @param tipoIntent specifica se l'Intent deve essere impostato per l'apertura di un servizio di email o un generico servizio per l'invio di un testo. per inviare le info tramite email usare INVIO_EMAIL
     * @param testo il testo da inviare. se si è scelto di usare un intent generico
     */
    public void condividiInfo(String emailDestinatario, String oggetto, int tipoIntent, String testo){
        Intent intent=new Intent(Intent.ACTION_SEND);
        if (tipoIntent == INVIO_EMAIL) {
            intent.putExtra(Intent.EXTRA_EMAIL, emailDestinatario);
            intent.putExtra(Intent.EXTRA_SUBJECT, oggetto);
            intent.setType("text/html");
            //intent.setPackage("com.google.android.gm");//se elimino questa riga mi fa scegliere con quale app inviare il messaggio
        } else {
            // Impostare il tipo di dati come testo
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, testo);

        }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.condividi_con)));





    }

}
