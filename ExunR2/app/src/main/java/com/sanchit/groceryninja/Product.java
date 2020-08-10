package com.sanchit.groceryninja;

import android.os.Parcel;
import android.os.Parcelable;

public class Product {
    private String name, cost, desc, picURL, available;

    Product() {}

    Product(String name, String cost, String desc, String picURL, String available){
        this.name = name;
        this.cost = cost;
        this.desc = desc;
        this.picURL = picURL;
        this.available = available;
    }


    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public String getDesc() {
        return desc;
    }

    public String getPicURL() {
        return picURL;
    }

    public String getAvailable() {
        return available;
    }
}
