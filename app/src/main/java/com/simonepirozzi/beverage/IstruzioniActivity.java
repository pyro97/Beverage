package com.simonepirozzi.beverage;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;


public class IstruzioniActivity extends Activity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istruzioni);
        TextView textView =  findViewById(R.id.istruzioniTesto);

        textView.setText("L'obiettivo del gioco consiste nell'indovinare l'età dei personaggi visualizzati.\n\n" +
                "Chi indovina l'età esatta vince il turno corrente e non deve bere a differenza di tutti gli altri.\n\n" +
                "Se nessuno indovina l'età, il turno viene vinto da chi si è avvicinato di più a quella reale," +
                " ricordando che non c'è differenza tra età maggiore o minore di quella reale, se hanno lo stesso scarto. \n(Es: se l'età esatta è 30," +
                " sia 20 e sia 40 hanno lo stesso valore nell'individuazione dei vincitori).\n\n" +
                "Ogni turno può essere vinto anche da più persone, se una certà età nominata da più partecipanti, " +
                " successivamente risulta esatta oppure la più vicina a quella reale (ricordando sempre l'esempio precedente)\n\n" +
                "I moltiplicatori dei Bonus e dei Malus (Es: x1, x2, x3, ..) includono già ciò che dovrebbe bere chi alla fine " +
                "del turno risulta perdente.\n\n" +
                "Buon divertimento e bevi responsabilmente!");


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
