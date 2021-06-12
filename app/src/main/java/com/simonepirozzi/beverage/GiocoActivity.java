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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class GiocoActivity extends Activity {

    private FirebaseFirestore db;
    SweetAlertDialog dialogo;
    TinyDB tinyDB;
    TextView nomePersonaggio;
    ImageView fotoDomanda;
    Button risultato;
    ArrayList<Object> personaggiosObject;
    ArrayList<Personaggio> personaggios;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioco);

        db = FirebaseFirestore.getInstance();

        tinyDB=new TinyDB(GiocoActivity.this);

        fotoDomanda=findViewById(R.id.imageDomanda);
        nomePersonaggio=findViewById(R.id.nomePersonaggio);
        risultato=findViewById(R.id.risultato);
        personaggios=new ArrayList<>();
        personaggiosObject=new ArrayList<>();
        progressBar=findViewById(R.id.successivaEta);
        progressBar.setVisibility(View.GONE);

        int num=tinyDB.getInt("domanda");

        //vecchio num1 era qui

        if(num==0){
            if(dialogo!=null)   cancelDialogo(dialogo);
            dialogo=startRegole("Regole","Chi indovina l'età esatta vince il turno e non deve bere a differenza degli altri\n\n"+
                    "Se nessuno indovina, il turno viene vinto da chi si è avvicinato di più all'età reale\n\n" +
                    "I bonus e i malus includono già ciò che dovrebbe bere a prescindere chi perde il turno\n\n" +
                    "Le età vincenti sono quelle esatte o più vicine con lo stesso scarto ( 20 e 40 sono entrambe vincenti" +
                    " se 30 è l'età esatta)\n\n" +
                    "Buon divertimento e gioca responsabilmente!\n\n",SweetAlertDialog.NORMAL_TYPE);
        }

       Personaggio p=(Personaggio)tinyDB.getListObject("game",Personaggio.class).get(0);



        try {
            byte [] encodeByte= Base64.decode(p.getFoto(),Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            fotoDomanda.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 600, false));

        } catch(Exception e) {
            fotoDomanda.setImageDrawable(GiocoActivity.this.getResources().getDrawable(R.drawable.ic_img));
            e.getMessage();
        }
        nomePersonaggio.setText(p.getNome());


        risultato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                risultato.setClickable(false);
                risultato.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Intent intent=new Intent(GiocoActivity.this,Risultato.class);
                startActivity(intent);
                finishAffinity();
            }
        });



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

        SweetAlertDialog pDialog = new SweetAlertDialog(GiocoActivity.this, tipo);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        TextView textView=new TextView(GiocoActivity.this);
        textView.setText(message);
        textView.setHeight(20);
        pDialog.setContentView(textView);
        pDialog.setCancelText("Annulla");
        pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent=new Intent(GiocoActivity.this,FineGiocoActivity.class);
                startActivity(intent);
                finishAffinity(); //prova
            }
        });
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public SweetAlertDialog startRegole(String title, String message, int tipo){

        SweetAlertDialog pDialog = new SweetAlertDialog(GiocoActivity.this, tipo);
        pDialog.setTitleText(title);
        TextView textView=new TextView(GiocoActivity.this);
        pDialog.setContentText(message);
        pDialog.setConfirmText("Gioca");
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

