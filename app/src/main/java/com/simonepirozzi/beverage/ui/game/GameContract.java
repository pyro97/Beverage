package com.simonepirozzi.beverage.ui.game;

import android.content.Intent;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.simonepirozzi.beverage.data.db.model.Category;
import com.simonepirozzi.beverage.data.db.model.Character;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GameContract {

    interface Presenter{
        void showRules();
        void showQuestion();
    }

    interface View{
        void setCharacterLayout(Character character);
        SweetAlertDialog startDialog(String title, String message, int tipo);
        void cancelDialog();
    }
}
