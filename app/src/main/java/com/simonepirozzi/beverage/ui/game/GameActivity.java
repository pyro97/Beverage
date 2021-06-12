package com.simonepirozzi.beverage.ui.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simonepirozzi.beverage.ui.game.endGame.EndGameActivity;
import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.ui.game.resultGame.ResultGameActivity;
import com.simonepirozzi.beverage.data.db.model.Character;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GameActivity extends Activity implements GameContract.View {
    SweetAlertDialog dialog;
    TextView characterName;
    ImageView questionPhoto;
    Button resultButton;
    ProgressBar progressBar;
    GamePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        questionPhoto = findViewById(R.id.image_question);
        characterName = findViewById(R.id.character_name);
        resultButton = findViewById(R.id.result);
        mPresenter = new GamePresenter(this, this);
        progressBar = findViewById(R.id.nextAge);
        progressBar.setVisibility(View.GONE);
        mPresenter.showRules();
        mPresenter.showQuestion();

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultButton.setClickable(false);
                resultButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(GameActivity.this, ResultGameActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    @Override
    public void setCharacterLayout(Character character) {
        try {
            byte[] encodeByte = Base64.decode(character.getFoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            questionPhoto.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 600, false));
        } catch (Exception e) {
            questionPhoto.setImageDrawable(GameActivity.this.getResources().getDrawable(R.drawable.ic_img));
            e.getMessage();
        }
        characterName.setText(character.getNome());
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
        SweetAlertDialog pDialog = new SweetAlertDialog(GameActivity.this, type);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        switch (type) {
            case SweetAlertDialog.WARNING_TYPE:
                TextView textView = new TextView(GameActivity.this);
                textView.setText(message);
                textView.setHeight(20);
                pDialog.setContentView(textView);
                pDialog.setCancelText(getString(R.string.cancel_dialog));
                pDialog.setConfirmButton(getString(R.string.ok_dialog), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent(GameActivity.this, EndGameActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
                break;
            case SweetAlertDialog.NORMAL_TYPE:
                pDialog.setConfirmText(getString(R.string.play_main));
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
}

