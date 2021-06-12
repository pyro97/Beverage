package com.simonepirozzi.beverage.ui.game;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.data.db.TinyDB;
import com.simonepirozzi.beverage.data.db.TinyManager;
import com.simonepirozzi.beverage.data.db.model.Bonus;
import com.simonepirozzi.beverage.data.db.model.Category;
import com.simonepirozzi.beverage.data.db.model.Character;
import com.simonepirozzi.beverage.data.repository.FirestoreManager;
import com.simonepirozzi.beverage.data.repository.MainRepository;
import com.simonepirozzi.beverage.ui.game.category.CategoryContract;
import com.simonepirozzi.beverage.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GamePresenter implements GameContract.Presenter{

    private GameContract.View view;
    private Activity activity;
    private TinyDB tinyDB;

    public GamePresenter(Activity activity, GameContract.View view) {
        this.activity = activity;
        this.view = view;
        this.tinyDB = TinyManager.getInstance(activity);
    }

    @Override
    public void showRules() {
        int index = tinyDB.getInt(TinyManager.QUESTION_INDEX);
        if(index == 0){
            StringBuilder stringBuilder = new StringBuilder();
            String[] rules = activity.getResources().getStringArray(R.array.rules_list_game);
            for(String rule : rules){
                stringBuilder.append(rule);
            }
            view.startDialog(activity.getString(R.string.title_rules_game),
                    stringBuilder.toString(),SweetAlertDialog.NORMAL_TYPE);
        }
    }

    @Override
    public void showQuestion() {
        if(tinyDB.getListObject(TinyManager.GAME_LIST, Character.class).size() > 0){
            Character character=(Character)tinyDB.getListObject(TinyManager.GAME_LIST, Character.class).get(0);
            tinyDB.putObject(TinyManager.CHARACTER_KEY,character);
            view.setCharacterLayout(character);
        }
    }
}
