package com.simonepirozzi.beverage.ui.game.endGame;

import com.simonepirozzi.beverage.data.db.model.Character;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EndGameContract {

    interface Presenter{

        void getResultQuestions();

        void configRating();

        void endAction();
    }

    interface View{
        void setLayoutQuestions(int number);

        SweetAlertDialog startDialog(String title, String message, int tipo);
        void cancelDialog();
    }
}
