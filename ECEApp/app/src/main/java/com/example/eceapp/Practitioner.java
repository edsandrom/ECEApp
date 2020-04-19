package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Practitioner implements Parcelable {
    int practitionerId, addressId;
    String fName, lName, phone;
    AddressClass addressClass;

    public Practitioner() {
    }

    protected Practitioner(Parcel in) {
        practitionerId = in.readInt();
        addressId = in.readInt();
        fName = in.readString();
        lName = in.readString();
        phone = in.readString();
        addressClass = in.readParcelable(AddressClass.class.getClassLoader());
    }

    public static final Creator<Practitioner> CREATOR = new Creator<Practitioner>() {
        @Override
        public Practitioner createFromParcel(Parcel in) {
            return new Practitioner(in);
        }

        @Override
        public Practitioner[] newArray(int size) {
            return new Practitioner[size];
        }
    };

    public int getPractitionerId() {
        return practitionerId;
    }

    public void setPractitionerId(int practitionerId) {
        this.practitionerId = practitionerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AddressClass getAddressClass() {
        return addressClass;
    }

    public void setAddressClass(AddressClass addressClass) {
        this.addressClass = addressClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(practitionerId);
        dest.writeInt(addressId);
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeString(phone);
        dest.writeParcelable(addressClass, flags);
    }
}
