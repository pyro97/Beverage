package com.simonepirozzi.beverage.data.db.model;

public class Character {
    private String nome;
    private String foto;
    private String age;


    public Character(String nome, String foto, String age) {
        this.nome = nome;
        this.foto = foto;
        this.age = age;
    }

    public Character() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String data) {
        this.age = data;
    }
}
