package com.example.evidenciamhd;

public class Vozidlo {
    private int evc;
    private String typ;
    private String stk;
    private String turnus;

    public String getTurnus() {
        return turnus;
    }

    public void setTurnus(String turnus) {
        this.turnus = turnus;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getStk() {
        return stk;
    }

    public void setStk(String stk) {
        this.stk = stk;
    }

    public int getEvc() {
        return evc;
    }

    public Vozidlo() {
    }

    public void setEvc(int evc) {
        this.evc = evc;
    }

}

