package com.simonepirozzi.beverage.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.data.db.TinyDB;
import com.simonepirozzi.beverage.data.db.TinyManager;
import com.simonepirozzi.beverage.utils.Constants;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainPresenter implements MainContract.Presenter, BillingProcessor.IBillingHandler {

    private MainContract.View view;
    private Activity activity;
    private BillingProcessor bp;
    private TinyDB tinyDB;

    public MainPresenter(Activity activity, MainContract.View view) {
        this.activity = activity;
        this.view = view;
        this.tinyDB = TinyManager.getInstance(activity);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        view.startDialog(activity.getString(R.string.product_buy_title), activity.getString(R.string.ok_billing_dialog), SweetAlertDialog.SUCCESS_TYPE);
    }

    @Override
    public void onPurchaseHistoryRestored() {
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        view.startDialog(activity.getString(R.string.error_buy_title), activity.getString(R.string.error_billing_dialog), SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void onBillingInitialized() {
    }

    @Override
    public void initializeBilling() {
        bp = new BillingProcessor(activity, Constants.LICENSE_KEY, this);
        bp.initialize();
        if (bp.isSubscribed(Constants.PRODUCT_ID)) {
            view.isRemoveVisible(View.GONE);
        } else {
            view.isRemoveVisible(View.VISIBLE);
        }
    }

    @Override
    public void subscribeBilling() {
        if (!bp.isSubscribed(Constants.PRODUCT_ID)) {
            bp.subscribe(activity, Constants.PRODUCT_ID);
        }
    }

    @Override
    public void releaseBilling() {
        if (bp != null) {
            bp.release();
        }
    }

    @Override
    public boolean handleActivityBilling(int requestCode, int resultCode, Intent data) {
        return bp.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void clearTiny() {
        tinyDB.clear();
    }
}
