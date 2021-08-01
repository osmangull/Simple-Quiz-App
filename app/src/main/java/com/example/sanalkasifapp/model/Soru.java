package com.example.sanalkasifapp.model;

import java.util.ArrayList;
import java.util.List;

public class Soru {

    private String ID;
    private int kategori;
    private String soruMetni;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getKategori() {
        return kategori;
    }

    public void setKategori(int kategori) {
        this.kategori = kategori;
    }

    public String getSoruMetni() {
        return soruMetni;
    }

    public void setSoruMetni(String soruMetni) {
        this.soruMetni = soruMetni;
    }

}
