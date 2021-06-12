package com.simonepirozzi.beverage.ui.main;

import android.content.Intent;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainContract {

    interface Presenter{
        void initializeBilling();
        void subscribeBilling();
        void releaseBilling();
        boolean handleActivityBilling(int requestCode, int resultCode, Intent data);
        void clearTiny();
    }

    interface View{
        SweetAlertDialog startDialog(String title, String message, int type);
        void cancelDialog(SweetAlertDialog s);
        void isRemoveVisible(int isVisible);
    }
}
