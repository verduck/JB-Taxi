package com.jj.bootcamp.jbtaxi.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Taxi implements Parcelable {
    private int id;
    private User user;
    private String name;
    private String carNumber;

    public Taxi() {
        this.id = 0;
        this.user = null;
        this.name= "";
        this.carNumber = "";
    }

    public Taxi(int id, User user, String name, String carNumber) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.carNumber = carNumber;
    }

    protected Taxi(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        name = in.readString();
        carNumber = in.readString();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(user, i);
        parcel.writeString(name);
        parcel.writeString(carNumber);
    }
}
