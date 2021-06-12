package com.simonepirozzi.beverage.ui.splash;

import android.app.Activity;
import android.content.Intent;

import com.simonepirozzi.beverage.ui.main.MainActivity;

public class SplashPresenter implements SplashContract {

    private Activity activity;

    public SplashPresenter(Activity activity){
        this.activity = activity;
    }

    @Override
    public void startAnimation() {
        final Intent intent = new Intent(activity, MainActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        };
        timer.start();
    }
}
