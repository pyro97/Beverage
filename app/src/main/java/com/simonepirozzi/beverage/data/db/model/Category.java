package com.simonepirozzi.beverage.data.db.model;

public class Category {

    public String id;
    public String stato;
    public String versione;


    public Category(String id, String stato, String versione) {
        this.id = id;
        this.stato = stato;
        this.versione=versione;

    }
    public Category(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStato() {
        return stato;
    }

    public String getVersione() {
        return versione;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }


}
