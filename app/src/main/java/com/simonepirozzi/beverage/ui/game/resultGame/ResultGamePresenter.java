package com.simonepirozzi.beverage.ui.game.resultGame;

import android.app.Activity;
import android.content.Intent;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.simonepirozzi.beverage.ui.game.endGame.EndGameActivity;
import com.simonepirozzi.beverage.data.db.TinyDB;
import com.simonepirozzi.beverage.data.db.TinyManager;
import com.simonepirozzi.beverage.data.db.model.Character;
import com.simonepirozzi.beverage.data.repository.MainRepository;
import com.simonepirozzi.beverage.utils.Constants;

import java.util.ArrayList;

public class ResultGamePresenter implements ResultGameContract.Presenter, BillingProcessor.IBillingHandler {

    private ResultGameContract.View view;
    private Activity activity;
    private BillingProcessor bp;
    private TinyDB tinyDB;
    private MainRepository repo;

    public ResultGamePresenter(Activity activity, ResultGameContract.View view) {
        this.activity = activity;
        this.view = view;
        this.tinyDB = TinyManager.getInstance(activity);
        this.repo = new MainRepository();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
    }

    @Override
    public void onPurchaseHistoryRestored() {
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
    }

    @Override
    public void onBillingInitialized() {
    }

    @Override
    public void initializeBilling() {
        bp = new BillingProcessor(activity, Constants.LICENSE_KEY, this);
        bp.initialize();
    }

    @Override
    public boolean isBillingSubscribed(String productId) {
        return bp.isSubscribed(productId);
    }

    @Override
    public void incrementQuestionList() {
        int num = tinyDB.getInt(TinyManager.QUESTION_INDEX);
        int num1 = num + 1;
        tinyDB.putInt(TinyManager.QUESTION_INDEX, num1);
    }

    @Override
    public void getCharacter() {
        Character character = tinyDB.getObject(TinyManager.CHARACTER_KEY, Character.class);
        view.createResultLayout(character);
    }

    @Override
    public void nextQuestion() {
        if (tinyDB.getListObject(TinyManager.GAME_LIST, Character.class).size() == 1) {
            tinyDB.remove(TinyManager.GAME_LIST);
            Intent intent = new Intent(activity, EndGameActivity.class);
            activity.startActivity(intent);
            activity.finishAffinity();
        } else {
            ArrayList<Object> characters = tinyDB.getListObject(TinyManager.GAME_LIST, Character.class);
            if (characters.size() > 0) {
                characters.remove(0);
                tinyDB.putListObject(TinyManager.GAME_LIST, characters);
                int numQuestion = tinyDB.getInt(TinyManager.QUESTION_INDEX);
                view.nextActivity(numQuestion);
            }


        }
    }
}
