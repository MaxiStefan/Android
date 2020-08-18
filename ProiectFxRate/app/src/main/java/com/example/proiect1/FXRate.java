package com.example.proiect1;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="fxrates")
public class FXRate {

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Ignore
    private String uid;

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String date;
    private String EUR;
    private String GBP;
    private String USD;
    private String XAU;

    @Ignore //to exclude
    public FXRate(){

    }

    public FXRate(int id, String date, String EUR, String GBP, String USD, String XAU) {
        this.id = id;
        this.date = date;
        this.EUR = EUR;
        this.GBP = GBP;
        this.USD = USD;
        this.XAU = XAU;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getEUR() {
        return EUR;
    }

    public String getGBP() {
        return GBP;
    }

    public String getUSD() {
        return USD;
    }

    public String getXAU() {
        return XAU;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEUR(String EUR) {
        this.EUR = EUR;
    }

    public void setGBP(String GBP) {
        this.GBP = GBP;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public void setXAU(String XAU) {
        this.XAU = XAU;
    }

    @Override
    public String toString() {
        return id + " "+ date + " " + EUR+ " " + USD + " " + GBP + " " + XAU;
    }
}
