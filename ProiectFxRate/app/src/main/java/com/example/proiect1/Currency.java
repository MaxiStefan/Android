package com.example.proiect1;

public class Currency {
    private String currencyName;
    private Double currencyValue;

    public Currency(){}

    public Currency(String currencyName, Double currencyValue){
        this.currencyName=currencyName;
        this.currencyValue=currencyValue;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(Double currencyValue) {
        this.currencyValue = currencyValue;
    }

    @Override
    public String toString(){
        return "Currency{" + "currencyName='" + currencyName +'\'' + ", currencyValue=" + currencyValue + '}';
    }
}
