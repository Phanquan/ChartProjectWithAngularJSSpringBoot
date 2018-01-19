package com.chart.model;

public class ExelData {
    private String datapa;
    private String pa;
    private String ps;

    public ExelData() {
    }

    public ExelData(String datapa, String pa, String ps) {
        this.datapa = datapa;
        this.pa = pa;
        this.ps = ps;
    }

    public String getDatapa() {
        return datapa;
    }

    public void setDatapa(String datapa) {
        this.datapa = datapa;
    }

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

}
