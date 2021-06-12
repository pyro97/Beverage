package com.simonepirozzi.beverage.data.db.model;

import java.util.List;

public class Bonus {
    public Bonus(){}
    List<String> nome;

    public Bonus(List<String> nome) {
        this.nome = nome;
    }

    public List<String> getNome() {
        return nome;
    }

    public void setNome(List<String> nome) {
        this.nome = nome;
    }
}
