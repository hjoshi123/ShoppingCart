package com.test.ecommerceft.room.items;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Item implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private int SNo;
    private String name;
    private String variant;
    private int price;
    private int inventory;
    private int count = 0;

    //Empty constructor for Firebase
    public Item() {

    }

    protected Item(Parcel in) {
        SNo = in.readInt();
        name = in.readString();
        variant = in.readString();
        price = in.readInt();
        inventory = in.readInt();
        count = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getSNo() {
        return SNo;
    }

    public void setSNo(int SNo) {
        this.SNo = SNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SNo);
        dest.writeString(name);
        dest.writeString(variant);
        dest.writeInt(price);
        dest.writeInt(inventory);
        dest.writeInt(count);
    }
}
