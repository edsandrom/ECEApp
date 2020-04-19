package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Medication implements Parcelable {
    private int medicationId;
    private String medication1, condition1, dosage1, medication2, condition2, dosage2;

    public Medication() {
    }

    protected Medication(Parcel in) {
        medicationId = in.readInt();
        medication1 = in.readString();
        condition1 = in.readString();
        dosage1 = in.readString();
        medication2 = in.readString();
        condition2 = in.readString();
        dosage2 = in.readString();
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedication1() {
        return medication1;
    }

    public void setMedication1(String medication1) {
        this.medication1 = medication1;
    }

    public String getCondition1() {
        return condition1;
    }

    public void setCondition1(String condition1) {
        this.condition1 = condition1;
    }

    public String getDosage1() {
        return dosage1;
    }

    public void setDosage1(String dosage1) {
        this.dosage1 = dosage1;
    }

    public String getMedication2() {
        return medication2;
    }

    public void setMedication2(String medication2) {
        this.medication2 = medication2;
    }

    public String getCondition2() {
        return condition2;
    }

    public void setCondition2(String condition2) {
        this.condition2 = condition2;
    }

    public String getDosage2() {
        return dosage2;
    }

    public void setDosage2(String dosage2) {
        this.dosage2 = dosage2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(medicationId);
        dest.writeString(medication1);
        dest.writeString(condition1);
        dest.writeString(dosage1);
        dest.writeString(medication2);
        dest.writeString(condition2);
        dest.writeString(dosage2);
    }
}
