package com.simonepirozzi.beverage.ui.game.endGame;

import android.app.Activity;
import android.content.Intent;

import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.data.db.TinyDB;
import com.simonepirozzi.beverage.data.db.TinyManager;
import com.simonepirozzi.beverage.data.db.model.Character;
import com.simonepirozzi.beverage.ui.game.GameContract;
import com.simonepirozzi.beverage.ui.main.MainActivity;
import com.simonepirozzi.beverage.ui.rating.RateThisApp;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EndGamePresenter implements EndGameContract.Presenter {

    private EndGameContract.View view;
    private Activity activity;
    private TinyDB tinyDB;

    public EndGamePresenter(Activity activity, EndGameContract.View view) {
        this.activity = activity;
        this.view = view;
        this.tinyDB = TinyManager.getInstance(activity);
    }

    @Override
    public void getResultQuestions() {
        int num = tinyDB.getInt(TinyManager.QUESTION_INDEX);
        view.setLayoutQuestions(num);
    }

    @Override
    public void configRating() {
        RateThisApp.Config config = new RateThisApp.Config();
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);
        RateThisApp.init(config);
// Monitor launch times and interval from installation
        RateThisApp.onCreate(activity);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(activity);
    }

    @Override
    public void endAction() {
        tinyDB.remove(TinyManager.GAME_LIST);
        tinyDB.remove(TinyManager.QUESTION_INDEX);
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finishAffinity();
    }
}
