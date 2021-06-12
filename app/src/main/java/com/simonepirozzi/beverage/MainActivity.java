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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends Activity implements BillingProcessor.IBillingHandler {

    Button gioca,rimuovi,informazioni,istruzioni;
    BillingProcessor bp;
    SweetAlertDialog dialogo;
    Intent intent;
    TinyDB tinyDB; //new
    private FirebaseFirestore db; //new
    String versioneCorrente; //new
    boolean scaricato=false; //new
    List<Categoria> categoriaList; //new
    ArrayList<Object> categoriaPreference; //new
    String versioneDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gioca=findViewById(R.id.gioca);
        rimuovi=findViewById(R.id.removeAds);
        informazioni=findViewById(R.id.paginaInfo);
        istruzioni=findViewById(R.id.paginaIstruzioni);
        tinyDB=new TinyDB(MainActivity.this); //new
        db = FirebaseFirestore.getInstance(); //new
        categoriaList=new ArrayList<>(); //new
        categoriaPreference=new ArrayList<>(); //new
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApLs8FDADBCl+lFsIZeaA24oQBOsc4p4Itdgu3bgjqVj8LbB2rue1lJ7w/WW17ysGFQwJX/5H2p031OUoNe5SYorWmclXj25zYFu97+ygPkETeRJFeHbHTDPlchb4nOzpsIrZGW2uaxHdaDyGNBRgIaZY7a9gZRpwGsckIVpUoOoDCVlPXVF2MIcU5brAsram88nRMlrm60g3qHZkvLD5G7oSS+wukydYrGka/tHaqAz/wN5NDUapDFhccstZK/+eRgo/1XFAVDpw+UCdCPVTmYn8LUsK9mb2piDXU/FT7vbM/qrX3ORiK8DnNdn24taf1EoGkX1Lua/HEAYJMv13rwIDAQAB", this);
        bp.initialize();
        tinyDB.clear();

        MobileAds.initialize(this,"ca-app-pub-9751551150368721~5178342708");

        if(bp.isSubscribed("remove_ads")){
            rimuovi.setVisibility(View.GONE);
        }else{
            rimuovi.setVisibility(View.VISIBLE);

        }
        gioca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,SelezionaCategorieActivity.class);
                startActivity(intent);


            }
        });

        rimuovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bp.isSubscribed("remove_ads")){
                    bp.subscribe(MainActivity.this,"remove_ads");

                }
            }
        });

        informazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });

        istruzioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,IstruzioniActivity.class);
                startActivity(intent);
            }
        });








    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if(dialogo!=null)  cancelDialogo(dialogo);
        dialogo=startDialogo("Prodotto Acquistato","Premi Ok per aggiornare",SweetAlertDialog.SUCCESS_TYPE);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        if(dialogo!=null)  cancelDialogo(dialogo);
        dialogo=startDialogo("Operazione fallita","Chiudi l'app e riprova", SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void onBillingInitialized() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("cazzo","onactivityresult");

        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
            Log.d("cazzo","ondestroy");

        }
        super.onDestroy();
    }

    public SweetAlertDialog startDialogo(String title, String message, int tipo){


        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, tipo);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        if(!message.equalsIgnoreCase("caricamento")){
            if(message.equalsIgnoreCase("Premi Ok per aggiornare")){
                pDialog.setContentText(message);
                pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        intent=new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });

            }
            else{
                pDialog.setContentText(message);
                pDialog.setConfirmText("Ok");
            }

        }

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
