package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FamilyRelationshipClass implements Parcelable {
    int familyRelId;
    int contactId;
    int stuId;
    String relationship;
    int isEmergencyCOntact =0; //1 is true, 0 is false
    int isAuthorizedPickup =0; //1 is true, 0 is false
    ArrayList relationshipValues = new ArrayList();
    int isActive=1;

    public FamilyRelationshipClass() {
        relationshipValues.add("Parent/Guardian");
        relationshipValues.add("Grandparent");
        relationshipValues.add("Aunt");
        relationshipValues.add("Uncle");
        relationshipValues.add("Cousin");
        relationshipValues.add("Step Parent");
        relationshipValues.add("Friend");
        relationshipValues.add("Other");

    }

    protected FamilyRelationshipClass(Parcel in) {
        familyRelId = in.readInt();
        contactId = in.readInt();
        stuId = in.readInt();
        relationship = in.readString();
        isEmergencyCOntact = in.readInt();
        isAuthorizedPickup = in.readInt();
        isActive = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(familyRelId);
        dest.writeInt(contactId);
        dest.writeInt(stuId);
        dest.writeString(relationship);
        dest.writeInt(isEmergencyCOntact);
        dest.writeInt(isAuthorizedPickup);
        dest.writeInt(isActive);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FamilyRelationshipClass> CREATOR = new Creator<FamilyRelationshipClass>() {
        @Override
        public FamilyRelationshipClass createFromParcel(Parcel in) {
            return new FamilyRelationshipClass(in);
        }

        @Override
        public FamilyRelationshipClass[] newArray(int size) {
            return new FamilyRelationshipClass[size];
        }
    };

    public int getFamilyRelId() {
        return familyRelId;
    }

    public void setFamilyRelId(int familyRelId) {
        this.familyRelId = familyRelId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public ArrayList getRelationshipValues() {
        return relationshipValues;
    }

    public void setRelationshipValues(ArrayList relationshipValues) {
        this.relationshipValues = relationshipValues;
    }

    public int getIsEmergencyCOntact() {
        return isEmergencyCOntact;
    }

    public void setIsEmergencyCOntact(int isEmergencyCOntact) {
        this.isEmergencyCOntact = isEmergencyCOntact;
    }

    public int getIsAuthorizedPickup() {
        return isAuthorizedPickup;
    }

    public void setIsAuthorizedPickup(int isAuthorizedPickup) {
        this.isAuthorizedPickup = isAuthorizedPickup;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
