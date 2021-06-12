package com.simonepirozzi.beverage.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirestoreManager {
    public static final String CATEGORY_COLLECTION = "/categorie";
    public static final String BONUS_COLLECTION = "/bonus";
    public static final String DEFAULT_COLLECTION = "/";
    public static final String PREMIUM_STATE = "premium";
    public static final String FREE_STATE = "premium";
    public static final String MALUS_DOC = "malusGame";
    public static final String BONUS_DOC = "bonusGame";

    public static List<String> BONUSARRAY;
    public static List<String> MALUSARRAY;

    public static List<String> getBONUSARRAY() {
        return BONUSARRAY;
    }

    public static void setBONUSARRAY(List<String> BONUSARRAY) {
        FirestoreManager.BONUSARRAY = BONUSARRAY;
    }

    public static List<String> getMALUSARRAY() {
        return MALUSARRAY;
    }

    public static void setMALUSARRAY(List<String> MALUSARRAY) {
        FirestoreManager.MALUSARRAY = MALUSARRAY;
    }



    public static int getNBonus(){
        return BONUSARRAY.size();
    }
    public static int getNMalus(){
        return MALUSARRAY.size();
    }

    public static FirebaseFirestore getInstance() {
        return FirebaseFirestore.getInstance();
    }
}
