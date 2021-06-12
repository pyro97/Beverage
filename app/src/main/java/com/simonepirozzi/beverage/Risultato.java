package com.simonepirozzi.beverage;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.Thread.sleep;

public class Risultato extends Activity implements BillingProcessor.IBillingHandler {

    private FirebaseFirestore db;
    SweetAlertDialog dialogo;
    TinyDB tinyDB;
    TextView risultato,nome,bonus,titoloBonus;
    Button button;
    ArrayList<Object> personaggio;
    Animation animation;
    Personaggio p;
    LinearLayout linearLayout;
    String bonusCostante;
    ProgressBar progressBar,successiva;
    InterstitialAd mInterstitialAd;
    Date birthDate;
    int age;
    BillingProcessor bp;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultato);

        db = FirebaseFirestore.getInstance();
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApLs8FDADBCl+lFsIZeaA24oQBOsc4p4Itdgu3bgjqVj8LbB2rue1lJ7w/WW17ysGFQwJX/5H2p031OUoNe5SYorWmclXj25zYFu97+ygPkETeRJFeHbHTDPlchb4nOzpsIrZGW2uaxHdaDyGNBRgIaZY7a9gZRpwGsckIVpUoOoDCVlPXVF2MIcU5brAsram88nRMlrm60g3qHZkvLD5G7oSS+wukydYrGka/tHaqAz/wN5NDUapDFhccstZK/+eRgo/1XFAVDpw+UCdCPVTmYn8LUsK9mb2piDXU/FT7vbM/qrX3ORiK8DnNdn24taf1EoGkX1Lua/HEAYJMv13rwIDAQAB", this);
        bp.initialize();

        tinyDB=new TinyDB(Risultato.this);
        risultato=findViewById(R.id.age);
        nome=findViewById(R.id.nameResult);
        linearLayout=findViewById(R.id.layoutBonus);
        bonus=findViewById(R.id.textBonus);
        button=findViewById(R.id.risultato);
        titoloBonus=findViewById(R.id.titoloBonus);
        progressBar=findViewById(R.id.progressBar);
        successiva=findViewById(R.id.successiva);


        progressBar.setVisibility(View.VISIBLE);
        successiva.setVisibility(View.GONE);

        personaggio=new ArrayList<>();

        animation= AnimationUtils.loadAnimation(this,R.anim.mytransition);

        button.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9751551150368721/6539030993");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if(dialogo!=null)   cancelDialogo(dialogo);

        int num=tinyDB.getInt("domanda");
        int num1=num+1;
        tinyDB.putInt("domanda",num1);

        //p=(Personaggio)tinyDB.getListObject("game",Personaggio.class).get(0);
        p=tinyDB.getObject("personaggio",Personaggio.class);
        nome.setText(p.getNome()+" ha ");

        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
        try {
            birthDate = sdf.parse(p.getAge());
            Age ageCompleta=AgeCalculator.calculateAge(birthDate);
            age=ageCompleta.getYears();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Random r=new Random();
        int tipo= r.nextInt(2);
        int n= r.nextInt(Costanti.getNBonus());
        if(tipo==0){
            bonusCostante=Costanti.getBONUSARRAY().get(n);
            //testo
            bonus.setText(bonusCostante);
            titoloBonus.setText("BONUS");
            //colore
            linearLayout.setBackground(getResources().getDrawable(R.drawable.rounded4));





        }else{

            bonusCostante=Costanti.getMALUSARRAY().get(n);
            //testo
            bonus.setText(bonusCostante);
            titoloBonus.setText("MALUS");
            //colore
           linearLayout.setBackground(getResources().getDrawable(R.drawable.rounded));

        }




        Thread thread1 = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                risultato.setText(age+" anni");
                                risultato.startAnimation(animation);
                                linearLayout.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                linearLayout.startAnimation(animation);
                                button.startAnimation(animation);

                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
        };
        thread1.start();




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(false);
                button.setVisibility(View.GONE);
                successiva.setVisibility(View.VISIBLE);




                if(tinyDB.getListObject("game",Personaggio.class).size()==1){
                    tinyDB.remove("game");
                    Intent intent=new Intent(Risultato.this,FineGiocoActivity.class);
                    startActivity(intent);
                    finishAffinity();

                }

                else{

                    personaggio=tinyDB.getListObject("game",Personaggio.class);
                    personaggio.remove(0);
                    tinyDB.putListObject("game",personaggio);

                   int numDomanda= tinyDB.getInt("domanda");
                   if(numDomanda % 2==0){
                       mInterstitialAd.loadAd(new AdRequest.Builder().build());
                       if (mInterstitialAd.isLoaded() && !bp.isSubscribed("remove_ads")) {

                          Log.d("cazzo",mInterstitialAd.getAdUnitId()) ;
                           mInterstitialAd.show();
                           mInterstitialAd.setAdListener(new AdListener() {
                               @Override
                               public void onAdClosed() {
                                   Intent intent=new Intent(Risultato.this,GiocoActivity.class);
                                   startActivity(intent);
                                   finishAffinity();
                               }
                           });
                       }else{
                           Intent intent=new Intent(Risultato.this,GiocoActivity.class);
                           startActivity(intent);
                           finishAffinity();
                       }
                   }else{
                           Intent intent=new Intent(Risultato.this,GiocoActivity.class);
                           startActivity(intent);
                           finishAffinity();



                   }


                }
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


        SweetAlertDialog pDialog = new SweetAlertDialog(Risultato.this, tipo);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        TextView textView=new TextView(Risultato.this);
        textView.setText(message);
        textView.setHeight(20);
        pDialog.setContentView(textView);
        pDialog.setCancelText("Annulla");
        pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent=new Intent(Risultato.this,FineGiocoActivity.class);
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


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }


    public SweetAlertDialog carica(String title, String message, int tipo){


        SweetAlertDialog pDialog = new SweetAlertDialog(Risultato.this, tipo);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));


        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;

    }

}

