package com.example.sanalkasifapp.model;

public class Cevaplar {
    private String ID;
    private String SoruID;
    private String cevapMetni;
    private Boolean dogruCevapMi;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSoruID() {
        return SoruID;
    }

    public void setSoruID(String soruID) {
        SoruID = soruID;
    }

    public String getCevapMetni() {
        return cevapMetni;
    }

    public void setCevapMetni(String cevapMetni) {
        this.cevapMetni = cevapMetni;
    }

    public Boolean getDogruCevapMi() {
        return dogruCevapMi;
    }

    public void setDogruCevapMi(Boolean dogruCevapMi) {
        this.dogruCevapMi = dogruCevapMi;
    }
}
