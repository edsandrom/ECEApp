package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class NonAuthorized extends Person implements Parcelable {
    int nonAuId;
    int stuId;
    String firstName;
    String lastName;
    int isActive=1;

    public NonAuthorized() {
    }


    protected NonAuthorized(Parcel in) {
        nonAuId = in.readInt();
        stuId = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        isActive = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nonAuId);
        dest.writeInt(stuId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeInt(isActive);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NonAuthorized> CREATOR = new Creator<NonAuthorized>() {
        @Override
        public NonAuthorized createFromParcel(Parcel in) {
            return new NonAuthorized(in);
        }

        @Override
        public NonAuthorized[] newArray(int size) {
            return new NonAuthorized[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getNonAuId() {
        return nonAuId;
    }

    public void setNonAuId(int nonAuId) {
        this.nonAuId = nonAuId;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }
}
