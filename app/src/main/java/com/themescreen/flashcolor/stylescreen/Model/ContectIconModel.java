package com.themescreen.flashcolor.stylescreen.Model;

public class ContectIconModel {
    String number;
    int recicon;
    int rejicon;

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public int getRecicon() {
        return this.recicon;
    }

    public void setRecicon(int i) {
        this.recicon = i;
    }

    public int getRejicon() {
        return this.rejicon;
    }

    public void setRejicon(int i) {
        this.rejicon = i;
    }

    public ContectIconModel(String str, int i, int i2) {
        this.number = str;
        this.recicon = i;
        this.rejicon = i2;
    }
}
