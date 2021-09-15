package com.amier.modernloginregister.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Courser implements Parcelable {
    private String Name,courseId;
    //username and password and rating

    public Courser() {
    }

    protected Courser(Parcel in) {
        Name = in.readString();
        courseId = in.readString();
    }

    public static final Creator<Courser> CREATOR = new Creator<Courser>() {
        @Override
        public Courser createFromParcel(Parcel in) {
            return new Courser(in);
        }

        @Override
        public Courser[] newArray(int size) {
            return new Courser[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(courseId);
    }
}
