package com.simonepirozzi.beverage;

import android.app.Activity;
import android.app.Person;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelezionaCategorieActivity extends Activity implements BillingProcessor.IBillingHandler{

    private FirebaseFirestore db;
    SweetAlertDialog dialogo;
    TinyDB tinyDB;
    CheckBox checkBox;
    List<Personaggio> personaggi;
    ArrayList<Object> personaggi2;
    Intent intent;
    BillingProcessor bp;
    Button buttonMusic;
    Categoria cat;
    List<Categoria> categoriaList;
    List<Personaggio> personaggioList;
    LinearLayout linearLayout;
    String nomeCat;
    Bonus bonus;
    List<Button> bottoni;
    Button button1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleziona_categorie);

        db = FirebaseFirestore.getInstance();

        personaggi=new ArrayList<>();
        personaggi2=new ArrayList<>();
        categoriaList=new ArrayList<>();
        personaggioList=new ArrayList<>();
        linearLayout=findViewById(R.id.checkboxCategorie);
        bottoni=new ArrayList<>();
        cat=new Categoria();



        tinyDB=new TinyDB(SelezionaCategorieActivity.this);
        Log.d("cazzo",tinyDB.getListObject("game",Personaggio.class).size()+"");
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApLs8FDADBCl+lFsIZeaA24oQBOsc4p4Itdgu3bgjqVj8LbB2rue1lJ7w/WW17ysGFQwJX/5H2p031OUoNe5SYorWmclXj25zYFu97+ygPkETeRJFeHbHTDPlchb4nOzpsIrZGW2uaxHdaDyGNBRgIaZY7a9gZRpwGsckIVpUoOoDCVlPXVF2MIcU5brAsram88nRMlrm60g3qHZkvLD5G7oSS+wukydYrGka/tHaqAz/wN5NDUapDFhccstZK/+eRgo/1XFAVDpw+UCdCPVTmYn8LUsK9mb2piDXU/FT7vbM/qrX3ORiK8DnNdn24taf1EoGkX1Lua/HEAYJMv13rwIDAQAB", this);
        bp.initialize();
        if(dialogo!=null)   cancelDialogo(dialogo);
        dialogo=startDialogo("","caricamento",SweetAlertDialog.PROGRESS_TYPE);











        db.collection("/categorie").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                categoriaList= task.getResult().toObjects(Categoria.class);


                for(int i=0;i<categoriaList.size();i++){
                    nomeCat=categoriaList.get(i).getId();
                    final Button button=new Button(SelezionaCategorieActivity.this);
                    LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                    int dpValue = 20; // margin in dips
                    int dpValue1 = 30; // margin in dips
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int)(dpValue * d);
                    int margin1 = (int)(dpValue1 * d);
                    params.setMargins(margin,0,margin,margin1);
                    button.setLayoutParams(params);
                    button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    button.setTextColor(getResources().getColor(R.color.sweet_dialog_bg_color));
                    button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    button.setTypeface(ResourcesCompat.getFont(SelezionaCategorieActivity.this, R.font.font1));
                    button.setTag(nomeCat);
                    button.setText("   "+nomeCat);
                    if(categoriaList.get(i).getStato().equalsIgnoreCase("premium") && !bp.isSubscribed(categoriaList.get(i).getId())){
                        button.setBackground(getResources().getDrawable(R.drawable.rounded3));
                        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_lock,0);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button button1=(Button) v;
                                if(dialogo!=null)  cancelDialogo(dialogo);
                                bp.subscribe(SelezionaCategorieActivity.this,v.getTag().toString());
                            }
                        });

                    }else if(categoriaList.get(i).getStato().equalsIgnoreCase("premium") && bp.isSubscribed(categoriaList.get(i).getId())){
                        button.setBackground(getResources().getDrawable(R.drawable.rounded5));
                        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow,0);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button button1=(Button) v;
                                intent=new Intent(SelezionaCategorieActivity.this,GiocoActivity.class);
                                if(dialogo!=null)  cancelDialogo(dialogo);
                                dialogo=caricamento("Inizio partita in corso","A volte il caricamento potrebbe impiegare fino ad un minuto. Attendi per favore.",SweetAlertDialog.PROGRESS_TYPE);
                                db.collection("/"+button1.getTag().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isComplete()){
                                            personaggi=task.getResult().toObjects(Personaggio.class);
                                            for(int i=0;i<personaggi.size();i++){
                                                personaggi2.add(personaggi.get(i));
                                            }
                                            Collections.shuffle(personaggi2);
                                            db.collection("/bonus").document("bonusGame").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    bonus=task.getResult().toObject(Bonus.class);
                                                    Costanti.setBONUSARRAY(bonus.getNome());

                                                }
                                            });
                                            db.collection("/bonus").document("malusGame").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    bonus=task.getResult().toObject(Bonus.class);
                                                    Costanti.setMALUSARRAY(bonus.getNome());

                                                }
                                            });
                                            tinyDB.putListObject("game",personaggi2);
                                            tinyDB.putInt("domanda",0);
                                            Log.d("cazzo",tinyDB.getListObject("game",Personaggio.class).size()+"");
                                            if(dialogo!=null)  cancelDialogo(dialogo);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });


                    }else{
                        button.setBackground(getResources().getDrawable(R.drawable.rounded5));
                        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow,0);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Button button1=(Button) v;
                                intent=new Intent(SelezionaCategorieActivity.this,GiocoActivity.class);
                                if(dialogo!=null)  cancelDialogo(dialogo);
                                dialogo=caricamento("Inizio partita in corso","A volte il caricamento potrebbe impiegare fino ad un minuto. Attendi per favore.",SweetAlertDialog.PROGRESS_TYPE);

                                db.collection("/"+button1.getTag().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isSuccessful()){

                                            personaggi=task.getResult().toObjects(Personaggio.class);
                                            for(int i=0;i<personaggi.size();i++){
                                                personaggi2.add(personaggi.get(i));
                                            }
                                            Collections.shuffle(personaggi2);

                                            db.collection("/bonus").document("bonusGame").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    bonus=task.getResult().toObject(Bonus.class);
                                                    Costanti.setBONUSARRAY(bonus.getNome());

                                                }
                                            });
                                            db.collection("/bonus").document("malusGame").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    bonus=task.getResult().toObject(Bonus.class);
                                                    Costanti.setMALUSARRAY(bonus.getNome());

                                                }
                                            });

                                            tinyDB.putListObject("game",personaggi2);
                                            tinyDB.putInt("domanda",0);
                                            Log.d("cazzo",tinyDB.getListObject("game",Personaggio.class).size()+"");
                                            if(dialogo!=null)  cancelDialogo(dialogo);
                                            startActivity(intent);

                                        }
                                    }
                                });
                            }
                        });

                    }

                    bottoni.add(button);






                    Log.d("fessa",categoriaList.get(i).getId());
                }

                Collections.sort(categoriaList, new Comparator<Categoria>() {
                    @Override
                    public int compare(Categoria o1, Categoria o2) {
                        if (o1.getStato().equalsIgnoreCase("free") && o2.getStato().equalsIgnoreCase("free")) {
                            return 0;
                        } else if (o1.getStato().equalsIgnoreCase("premium") && o2.getStato().equalsIgnoreCase("free")) {

                            if(bp.isSubscribed(o1.getId())){
                                return 0;
                            }else{
                                return 1;
                            }

                        } else if (o1.getStato().equalsIgnoreCase("free") && o2.getStato().equalsIgnoreCase("premium")) {
                            if(bp.isSubscribed(o2.getId())){
                                return 0;
                            }else{
                                return -1;
                            }

                        } else if (o1.getStato().equalsIgnoreCase("premium") && o2.getStato().equalsIgnoreCase("premium")) {
                            if(bp.isSubscribed(o1.getId()) && bp.isSubscribed(o2.getId())){
                                return 0;
                            }else if(bp.isSubscribed(o1.getId()) && !bp.isSubscribed(o2.getId())){
                                return -1;
                            }else if(!bp.isSubscribed(o1.getId()) && bp.isSubscribed(o2.getId())){
                                return 1;
                            }else{
                                return 0;
                            }

                        }

                        return 0;
                    }


                });

                for(int k=0;k<categoriaList.size();k++){

                    for(int m=0;m<bottoni.size();m++){
                        if(bottoni.get(m).getTag().toString().equalsIgnoreCase(categoriaList.get(k).getId())){
                            linearLayout.addView(bottoni.get(m));
                        }
                    }
                }


                if(dialogo!=null)   cancelDialogo(dialogo);
            }
        });







    }


    public SweetAlertDialog startDialogo(String title, String message, int tipo){


        SweetAlertDialog pDialog = new SweetAlertDialog(SelezionaCategorieActivity.this, tipo);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        if(!message.equalsIgnoreCase("caricamento")){
            if(message.equalsIgnoreCase("Premi Ok per aggiornare")){
                pDialog.setContentText(message);
                pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        intent=new Intent(SelezionaCategorieActivity.this,MainActivity.class);
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

    public SweetAlertDialog caricamento(String title, String message, int tipo){


        SweetAlertDialog pDialog = new SweetAlertDialog(SelezionaCategorieActivity.this, tipo);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
       pDialog.setContentText(message);

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
        if(dialogo!=null)  cancelDialogo(dialogo);
        dialogo=startDialogo("Prodotto Acquistato","Premi Ok per aggiornare",SweetAlertDialog.SUCCESS_TYPE);


    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        if(dialogo!=null)  cancelDialogo(dialogo);
        dialogo=startDialogo("Operazione fallita","Chiudi l'app e riprova",SweetAlertDialog.ERROR_TYPE);
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
}

