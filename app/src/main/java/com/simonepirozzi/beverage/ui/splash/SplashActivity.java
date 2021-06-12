package com.simonepirozzi.beverage.ui.splash;


import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.simonepirozzi.beverage.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatImageView img = findViewById(R.id.vipImage);
        SplashPresenter mPresenter = new SplashPresenter(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        img.startAnimation(animation);
        mPresenter.startAnimation();
    }
}
