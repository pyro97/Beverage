package com.simonepirozzi.beverage.ui.game.resultGame;

import com.simonepirozzi.beverage.data.db.model.Character;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ResultGameContract {

    interface Presenter{
        void initializeBilling();
        boolean isBillingSubscribed(String productId);
        void incrementQuestionList();
        void getCharacter();
        void nextQuestion();
    }

    interface View{
        void createResultLayout(Character character);
        SweetAlertDialog startDialog(String title, String message, int type);
        void cancelDialog();
        void nextActivity(int numQuestion);
    }
}
