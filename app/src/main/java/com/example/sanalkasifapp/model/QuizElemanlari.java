package com.example.sanalkasifapp.model;

import java.util.ArrayList;

public class QuizElemanlari {
    private String quizSorusu;
    private ArrayList<Cevaplar> cevaplarList;

    public String getQuizSorusu() {
        return quizSorusu;
    }

    public void setQuizSorusu(String quizSorusu) {
        this.quizSorusu = quizSorusu;
    }

    public ArrayList<Cevaplar> getCevaplarList() {
        return cevaplarList;
    }

    public void setCevaplarList(ArrayList<Cevaplar> cevaplarList) {
        this.cevaplarList = cevaplarList;
    }
}
