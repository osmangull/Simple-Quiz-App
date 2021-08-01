package com.example.sanalkasifapp.model;

public enum SoruKategorisi {
    Bilim(0),
    Tarih(1),
    Oyun(3);

    private int value;
    SoruKategorisi(int value){this.value = value;}
    public int getValue(){
        return value;
    }
}
