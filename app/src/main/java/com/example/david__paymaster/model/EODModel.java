package com.example.david__paymaster.model;

import android.graphics.drawable.Drawable;

public class EODModel {

    public int image;
    public Drawable imageDrw;
    public String name;
    public String amount;
    public boolean section = false;

    public EODModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public EODModel(String name, boolean section) {
        this.name = name;
        this.section = section;
    }
}
