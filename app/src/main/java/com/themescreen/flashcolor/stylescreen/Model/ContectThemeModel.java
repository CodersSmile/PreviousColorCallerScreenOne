package com.themescreen.flashcolor.stylescreen.Model;

public class ContectThemeModel {
    String number;
    String theamevalue;

    public ContectThemeModel(String str, String str2) {
        this.number = str;
        this.theamevalue = str2;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public String getTheamevalue() {
        return this.theamevalue;
    }

    public void setTheamevalue(String str) {
        this.theamevalue = str;
    }
}
