package com.simonepirozzi.beverage.ui.game.endGame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.ui.main.MainActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EndGameActivity extends Activity implements EndGameContract.View {

    SweetAlertDialog dialog;
    TextView finalQuestions;
    Button finalButton;
    EndGamePresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        finalQuestions = findViewById(R.id.finalQuestions);
        finalButton = findViewById(R.id.finalButton);
        mPresenter = new EndGamePresenter(this, this);

        mPresenter.getResultQuestions();

        mPresenter.configRating();

        finalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.endAction();
            }
        });
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

    @Override
    public void setLayoutQuestions(int number) {
        finalQuestions.setText(number + getString(R.string.total_questions));
    }

    public SweetAlertDialog startDialog(String title, String message, int type) {
        if (dialog != null) {
            cancelDialog();
        }
        SweetAlertDialog pDialog = new SweetAlertDialog(EndGameActivity.this, type);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        TextView textView = new TextView(EndGameActivity.this);
        textView.setText(message);
        textView.setHeight(20);
        pDialog.setContentView(textView);
        pDialog.setCancelText(getString(R.string.cancel_dialog));
        pDialog.setConfirmButton(getString(R.string.ok_dialog), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
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
}

