package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AddressClass implements Parcelable {

    ArrayList provinces = new ArrayList();
    private int addressId;
    private String aptNo;
    private String strNo;
    private String street;
    private String city;
    private String prov;
    private String postCode;



    public AddressClass() {
        provinces.add("NB");
        provinces.add("NS");
        provinces.add("PE");
        provinces.add("NL");
        provinces.add("BC");
        provinces.add("AB");
        provinces.add("SK");
        provinces.add("MB");
        provinces.add("ON");
        provinces.add("QC");
    }

    protected AddressClass(Parcel in) {
        addressId = in.readInt();
        aptNo = in.readString();
        strNo = in.readString();
        street = in.readString();
        city = in.readString();
        prov = in.readString();
        postCode = in.readString();
    }

    public static final Creator<AddressClass> CREATOR = new Creator<AddressClass>() {
        @Override
        public AddressClass createFromParcel(Parcel in) {
            return new AddressClass(in);
        }

        @Override
        public AddressClass[] newArray(int size) {
            return new AddressClass[size];
        }
    };

    public ArrayList getProvinces() {

        return provinces;
    }

    public void setProvinces(ArrayList provinces) {
        this.provinces = provinces;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAptNo() {
        return aptNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(addressId);
        dest.writeString(aptNo);
        dest.writeString(strNo);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(prov);
        dest.writeString(postCode);
    }
}
