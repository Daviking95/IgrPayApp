package com.example.david__paymaster.model;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

public class People {

    public int image;
    public String imageUrl;
    public Drawable imageDrw;
    public String name;
    public String email;
    public String occupation;

    public People() {
    }

    public People(String name, String occupation, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.occupation = occupation;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("title", this.name);
        result.put("content", this.occupation);

        return result;
    }

}