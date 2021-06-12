package com.simonepirozzi.beverage.data.db;

import android.app.Activity;
import android.content.Context;

public class TinyManager {
    public static final String GAME_LIST = "game";
    public static final String QUESTION_INDEX = "domanda";
    public static final String CHARACTER_KEY = "personaggio";


    public static TinyDB getInstance(Context context){
        return new TinyDB(context);
    }
}
