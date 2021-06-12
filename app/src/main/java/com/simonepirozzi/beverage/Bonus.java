package com.simonepirozzi.beverage;

import java.util.ArrayList;
import java.util.List;

public class Bonus {

    List<String> nome;

    public Bonus(List<String> nome){
        this.nome=nome;
    }

    public Bonus(){}

    public List<String> getNome() {
        return nome;
    }

    public void setNome(List<String> nome) {
        this.nome = nome;
    }
}
