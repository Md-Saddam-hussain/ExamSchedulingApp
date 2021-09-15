package com.amier.modernloginregister.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Centers implements Parcelable {
    private String name,centerId,address,website,phone,openHours ;

    public Centers() {
    }

    protected Centers(Parcel in) {
        name = in.readString();
        centerId = in.readString();
        address = in.readString();
        website = in.readString();
        phone = in.readString();
        openHours = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(centerId);
        dest.writeString(address);
        dest.writeString(website);
        dest.writeString(phone);
        dest.writeString(openHours);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Centers> CREATOR = new Creator<Centers>() {
        @Override
        public Centers createFromParcel(Parcel in) {
            return new Centers(in);
        }

        @Override
        public Centers[] newArray(int size) {
            return new Centers[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }
}
