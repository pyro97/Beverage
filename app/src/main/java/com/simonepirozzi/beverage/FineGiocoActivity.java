package com.simonepirozzi.beverage;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FineGiocoActivity extends Activity {

    private FirebaseFirestore db;
    SweetAlertDialog dialogo;
    TinyDB tinyDB;
    TextView nomePersonaggio;
    ImageView fotoDomanda;
    Button risultato;
    ArrayList<Object> personaggiosObject;
    ArrayList<Personaggio> personaggios;
    TextView numDomande;
    Button buttonFine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_gioco);

        db = FirebaseFirestore.getInstance();

        tinyDB=new TinyDB(FineGiocoActivity.this);

        numDomande=findViewById(R.id.numDomandeFinale);
        buttonFine=findViewById(R.id.buttonFine);

        numDomande.setText((tinyDB.getInt("domanda"))+" domande effettuate");


        buttonFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.remove("game");
                tinyDB.remove("domanda");
                Intent intent=new Intent(FineGiocoActivity.this,MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        RateThisApp.Config config = new RateThisApp.Config();
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);
        RateThisApp.init(config);

// Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);






    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(dialogo!=null)   cancelDialogo(dialogo);
            dialogo=startDialogo("Attenzione","Stai per uscire dal gioco",SweetAlertDialog.WARNING_TYPE);
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }

    }




    public SweetAlertDialog startDialogo(String title, String message, int tipo){


        SweetAlertDialog pDialog = new SweetAlertDialog(FineGiocoActivity.this, tipo);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        TextView textView=new TextView(FineGiocoActivity.this);
        textView.setText(message);
        textView.setHeight(20);
        pDialog.setContentView(textView);
        pDialog.setCancelText("Annulla");
        pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent=new Intent(FineGiocoActivity.this,MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;

    }

    public void cancelDialogo(SweetAlertDialog s){

        s.cancel();
    }

    public boolean isNetwork(Context context){
        ConnectivityManager manager =(ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(activeNetwork!=null && activeNetwork.getState()== NetworkInfo.State.CONNECTED)   return true;
        else    return false;
    }




}

