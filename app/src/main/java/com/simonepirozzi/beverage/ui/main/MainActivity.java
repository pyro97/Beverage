package com.simonepirozzi.beverage.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;
import com.simonepirozzi.beverage.ui.info.InfoActivity;
import com.simonepirozzi.beverage.ui.rules.RulesActivity;
import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.ui.game.category.CategoryActivity;
import com.simonepirozzi.beverage.data.db.model.Category;
import com.simonepirozzi.beverage.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends Activity implements MainContract.View {

    Button play, remove, info, rules;
    SweetAlertDialog dialog;
    Intent intent;
    List<Category> categoryList;
    ArrayList<Object> categoryPreference;
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);
        remove = findViewById(R.id.removeAds);
        info = findViewById(R.id.infoPage);
        rules = findViewById(R.id.rulesPage);
        categoryList = new ArrayList<>();
        categoryPreference = new ArrayList<>();
        mPresenter = new MainPresenter(this,this);
        mPresenter.initializeBilling();
        mPresenter.clearTiny();

        MobileAds.initialize(this, Constants.KEY_MOBILE_ADS);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribeBilling();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mPresenter.handleActivityBilling(requestCode,resultCode,data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.releaseBilling();
        super.onDestroy();
    }

    public SweetAlertDialog startDialog(String title, String message, int type){
        if (dialog != null){
            cancelDialog(dialog);
        }
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, type);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        switch (type){
            case SweetAlertDialog.SUCCESS_TYPE:
                pDialog.setContentText(message);
                pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        intent=new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
                break;
            case SweetAlertDialog.ERROR_TYPE:
                pDialog.setConfirmText(getString(R.string.ok_dialog));
                break;
            default:
                break;
        }
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public void cancelDialog(SweetAlertDialog s) {
        if(s!= null){
            s.cancel();
        }
    }

    public void isRemoveVisible(int isVisible){
        remove.setVisibility(isVisible);
    }
}
