package com.simonepirozzi.beverage.ui.game.resultGame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.simonepirozzi.beverage.ui.game.endGame.EndGameActivity;
import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.data.db.model.Age;
import com.simonepirozzi.beverage.data.db.model.Character;
import com.simonepirozzi.beverage.data.repository.FirestoreManager;
import com.simonepirozzi.beverage.ui.game.GameActivity;
import com.simonepirozzi.beverage.utils.AgeUtils;
import com.simonepirozzi.beverage.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.Thread.sleep;

public class ResultGameActivity extends Activity implements ResultGameContract.View {

    SweetAlertDialog dialog;
    TextView result, name, bonus, titleBonus;
    Button button;
    Animation animation;
    LinearLayout linearLayout;
    String bonusCostante;
    ProgressBar progressBar, nextProgress;
    InterstitialAd mInterstitialAd;
    Date birthDate;
    int age;
    ResultGamePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        result = findViewById(R.id.age);
        name = findViewById(R.id.nameResult);
        linearLayout = findViewById(R.id.layoutBonus);
        bonus = findViewById(R.id.textBonus);
        button = findViewById(R.id.result);
        titleBonus = findViewById(R.id.titleBonus);
        progressBar = findViewById(R.id.progressBar);
        nextProgress = findViewById(R.id.nextProgress);
        progressBar.setVisibility(View.VISIBLE);
        nextProgress.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        button.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        cancelDialog();

        mPresenter = new ResultGamePresenter(this, this);
        mPresenter.initializeBilling();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Constants.INTERSTITAL_AD);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mPresenter.incrementQuestionList();
        mPresenter.getCharacter();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(false);
                button.setVisibility(View.GONE);
                nextProgress.setVisibility(View.VISIBLE);
                mPresenter.nextQuestion();
            }
        });


    }

    @Override
    public void createResultLayout(Character character) {
        name.setText(character.getNome() + getString(R.string.add_text_result_name));
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
        try {
            birthDate = sdf.parse(character.getAge());
            Age completeAge = AgeUtils.calculateAge(birthDate);
            age = completeAge.getYears();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setBonusLayout();
        execThread();
    }

    private void execThread() {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                result.setText(age + getString(R.string.add_age_text));
                                result.startAnimation(animation);
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
            }
        };
        thread1.start();
    }

    private void setBonusLayout() {
        Random r = new Random();
        int type = r.nextInt(2);
        int n = r.nextInt(FirestoreManager.getNBonus());
        if (type == 0) {
            bonusCostante = FirestoreManager.getBONUSARRAY().get(n);
            bonus.setText(bonusCostante);
            titleBonus.setText(R.string.bonus_result);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.rounded4));
        } else {
            bonusCostante = FirestoreManager.getMALUSARRAY().get(n);
            bonus.setText(bonusCostante);
            titleBonus.setText(R.string.malus_result);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.rounded));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog = startDialog(getString(R.string.warning_title), getString(R.string.warning_desc), SweetAlertDialog.WARNING_TYPE);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public SweetAlertDialog startDialog(String title, String message, int type) {
        if (dialog != null) {
            cancelDialog();
        }
        SweetAlertDialog pDialog = new SweetAlertDialog(ResultGameActivity.this, type);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        TextView textView = new TextView(ResultGameActivity.this);
        textView.setText(message);
        textView.setHeight(20);
        pDialog.setContentView(textView);
        pDialog.setCancelText(getString(R.string.cancel_dialog));
        pDialog.setConfirmButton(getString(R.string.ok_dialog), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent = new Intent(ResultGameActivity.this, EndGameActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
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
    public void nextActivity(int numQuestion) {
        final Intent intent = new Intent(ResultGameActivity.this, GameActivity.class);
        if (numQuestion % 2 == 0) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            if (mInterstitialAd.isLoaded() && !mPresenter.isBillingSubscribed(Constants.PRODUCT_ID)) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(intent);
                        finishAffinity();
                    }
                });
            } else {
                startActivity(intent);
                finishAffinity();
            }
        } else {
            startActivity(intent);
            finishAffinity();
        }
    }

}

