package it.uniba.dib.sms2223.laureapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.core.content.ContextCompat;

public class AspettoActivity {

    private Context context;
    private Window window;
    public AspettoActivity(){}

    //public AspettoActivity(Context context){
        //this.context = context;
   // }

    public AspettoActivity(Context context){
        this.context = context;
        Activity c = (Activity) context;
        window = c.getWindow();
    }

    /**
     * imposta il colore della status bar di Android
     * @param colore intero che deve fare riferimento alla risorsa di tipo color
     * @param iconeScure se true imposta le icone di colore scuro
     */
    public void impostaColoreStatusBar(int colore,boolean iconeScure){
        window.setStatusBarColor(ContextCompat.getColor(context, colore));//colore è il colore della status bar
        View decor = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // per ragioni di compatibilià
            if (iconeScure){ //iconeScure = true -> icone scure
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//imposta le icone della status bar a in colore scuro
            } else{ //iconeScure = true -> icone bianche
                decor.setSystemUiVisibility(0);//imposta il colore delle icone della status bar di bianco
            }
        }
        //---- NOTE
        // clear FLAG_TRANSLUCENT_STATUS flag:
        //  window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        //  window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }



}
