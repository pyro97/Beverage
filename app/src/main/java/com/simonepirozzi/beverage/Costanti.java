package com.simonepirozzi.beverage;

import java.util.ArrayList;
import java.util.List;

public final class Costanti {

    public static List<String> BONUSARRAY;
    public static List<String> MALUSARRAY;




    public static List<String> getBONUSARRAY() {
        return BONUSARRAY;
    }

    public static void setBONUSARRAY(List<String> BONUSARRAY) {
        Costanti.BONUSARRAY = BONUSARRAY;
    }

    public static List<String> getMALUSARRAY() {
        return MALUSARRAY;
    }

    public static void setMALUSARRAY(List<String> MALUSARRAY) {
        Costanti.MALUSARRAY = MALUSARRAY;
    }



    public static int getNBonus(){
        return BONUSARRAY.size();
    }
    public static int getNMalus(){
        return MALUSARRAY.size();
    }



}
