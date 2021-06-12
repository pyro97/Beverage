package com.simonepirozzi.beverage.ui.game.category;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.simonepirozzi.beverage.ui.game.GameActivity;
import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.data.db.model.Category;
import com.simonepirozzi.beverage.data.db.model.Character;
import com.simonepirozzi.beverage.data.repository.FirestoreManager;
import com.simonepirozzi.beverage.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoryActivity extends Activity implements CategoryContract.View {

    SweetAlertDialog dialog;
    Intent intent;
    Category cat;
    List<Character> characterList;
    LinearLayout linearLayout;
    String nomeCat;
    List<Button> buttonList;
    CategoryPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        characterList = new ArrayList<>();
        linearLayout = findViewById(R.id.checkboxCategories);
        buttonList = new ArrayList<>();
        cat = new Category();
        mPresenter = new CategoryPresenter(this, this);
        mPresenter.initializeBilling();
        dialog = startDialog("", getString(R.string.loading_message_dialog), SweetAlertDialog.PROGRESS_TYPE);
        mPresenter.getCategories();
    }


    public SweetAlertDialog startDialog(String title, String message, int type) {
        if (dialog != null) {
            cancelDialog();
        }
        SweetAlertDialog pDialog = new SweetAlertDialog(CategoryActivity.this, type);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        switch (type) {
            case SweetAlertDialog.SUCCESS_TYPE:
                pDialog.setContentText(message);
                pDialog.setConfirmButton(getString(R.string.ok_dialog), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        intent = new Intent(CategoryActivity.this, MainActivity.class);
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

    public void cancelDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void createCategoriesLayout(List<Category> categoryList) {
        for (int i = 0; i < categoryList.size(); i++) {
            nomeCat = categoryList.get(i).getId();
            final Button button = new Button(CategoryActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            int dpValue = 20; // margin in dips
            int dpValue1 = 30; // margin in dips
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);
            int margin1 = (int) (dpValue1 * d);
            params.setMargins(margin, 0, margin, margin1);
            button.setLayoutParams(params);
            button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            button.setTextColor(getResources().getColor(R.color.sweet_dialog_bg_color));
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            button.setTypeface(ResourcesCompat.getFont(CategoryActivity.this, R.font.font1));
            button.setTag(nomeCat);
            button.setText("   " + nomeCat);
            if (categoryList.get(i).getStato().equalsIgnoreCase(FirestoreManager.PREMIUM_STATE)
                    && !mPresenter.isBillingSubscribed(categoryList.get(i).getId())) {
                button.setBackground(getResources().getDrawable(R.drawable.rounded3));
                button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_lock, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDialog();
                        mPresenter.subscribeBilling(v.getTag().toString());
                    }
                });

            } else if (categoryList.get(i).getStato().equalsIgnoreCase(FirestoreManager.PREMIUM_STATE)
                    && mPresenter.isBillingSubscribed(categoryList.get(i).getId())) {
                button.setBackground(getResources().getDrawable(R.drawable.rounded5));
                button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button1 = (Button) v;
                        dialog = startDialog(getString(R.string.start_match_dialog_title), getString(R.string.start_match_dialog_desc), SweetAlertDialog.PROGRESS_TYPE);
                        mPresenter.getCategoryCharacters(FirestoreManager.DEFAULT_COLLECTION + button1.getTag().toString());
                    }
                });
            } else {
                button.setBackground(getResources().getDrawable(R.drawable.rounded5));
                button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Button button1 = (Button) v;
                        intent = new Intent(CategoryActivity.this, GameActivity.class);
                        dialog = startDialog(getString(R.string.start_match_dialog_title), getString(R.string.start_match_dialog_desc), SweetAlertDialog.PROGRESS_TYPE);
                        mPresenter.getCategoryCharacters(FirestoreManager.DEFAULT_COLLECTION + button1.getTag().toString());
                    }
                });

            }
            buttonList.add(button);
        }

        mPresenter.sortCategory(categoryList);
    }

    @Override
    public void addCategoriesView(List<Category> categoryList) {
        for (int k = 0; k < categoryList.size(); k++) {
            for (int m = 0; m < buttonList.size(); m++) {
                if (buttonList.get(m).getTag().toString().equalsIgnoreCase(categoryList.get(k).getId())) {
                    linearLayout.addView(buttonList.get(m));
                }
            }
        }
        cancelDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mPresenter.handleActivityBilling(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.releaseBilling();
        super.onDestroy();
    }
}

