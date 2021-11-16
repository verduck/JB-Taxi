package com.jj.bootcamp.jbtaxi.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Taxi implements Parcelable {
    private int id;
    private User user;
    private String carNumber;

    public Taxi() {
        this.id = 0;
        this.user = null;
        this.carNumber = "";
    }

    public Taxi(int id, User user, String carNumber) {
        this.id = id;
        this.user = user;
        this.carNumber = carNumber;
    }

    protected Taxi(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        carNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(user, flags);
        dest.writeString(carNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Taxi> CREATOR = new Creator<Taxi>() {
        @Override
        public Taxi createFromParcel(Parcel in) {
            return new Taxi(in);
        }

        @Override
        public Taxi[] newArray(int size) {
            return new Taxi[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
