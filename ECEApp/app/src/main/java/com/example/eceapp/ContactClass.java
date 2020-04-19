package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactClass implements Parcelable {
    int contactId;
    int addressId;
    String firstName;
    String lastName;
    String homePhone;
    String workPhone;
    String cellPhone;
    String placeOfWork;
    String email;
    int isActive=1;

    int isSameAddress=0;
    FamilyRelationshipClass familyRel;
    AddressClass address;

    String relationship;

    public ContactClass() {
    }

    protected ContactClass(Parcel in) {
        contactId = in.readInt();
        addressId = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        homePhone = in.readString();
        workPhone = in.readString();
        cellPhone = in.readString();
        placeOfWork = in.readString();
        email = in.readString();
        isActive = in.readInt();
        isSameAddress = in.readInt();
        familyRel = in.readParcelable(FamilyRelationshipClass.class.getClassLoader());
        address = in.readParcelable(AddressClass.class.getClassLoader());
    }

    public static final Creator<ContactClass> CREATOR = new Creator<ContactClass>() {
        @Override
        public ContactClass createFromParcel(Parcel in) {
            return new ContactClass(in);
        }

        @Override
        public ContactClass[] newArray(int size) {
            return new ContactClass[size];
        }
    };

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

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

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public FamilyRelationshipClass getFamilyRel() {
        return familyRel;
    }

    public void setFamilyRel(FamilyRelationshipClass familyRel) {
        this.familyRel = familyRel;
    }

    public AddressClass getAddress() {
        return address;
    }

    public void setAddress(AddressClass address) {
        this.address = address;
    }


    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsSameAddress() {
        return isSameAddress;
    }

    public void setIsSameAddress(int isSameAddress) {
        this.isSameAddress = isSameAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(contactId);
        dest.writeInt(addressId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(homePhone);
        dest.writeString(workPhone);
        dest.writeString(cellPhone);
        dest.writeString(placeOfWork);
        dest.writeString(email);
        dest.writeInt(isActive);
        dest.writeInt(isSameAddress);
        dest.writeParcelable(familyRel, flags);
        dest.writeParcelable(address, flags);

    }
}
