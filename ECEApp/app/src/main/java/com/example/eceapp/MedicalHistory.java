package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicalHistory implements Parcelable {
    private Proof proof;
    private Medication medication;
    private int medical_history_id, proofId, medicationId, measles, mumps, meningitis, rubella, chicken_pox, pertussis, asthma,
    eczema, diabetes, epilepsy;
    private String other1, other2, restricted_activities, dietary_restrictions;

    public MedicalHistory() {
    }

    protected MedicalHistory(Parcel in) {
        proof = in.readParcelable(Proof.class.getClassLoader());
        medication = in.readParcelable(Medication.class.getClassLoader());
        medical_history_id = in.readInt();
        proofId = in.readInt();
        medicationId = in.readInt();
        measles = in.readInt();
        mumps = in.readInt();
        meningitis = in.readInt();
        rubella = in.readInt();
        chicken_pox = in.readInt();
        pertussis = in.readInt();
        asthma = in.readInt();
        eczema = in.readInt();
        diabetes = in.readInt();
        epilepsy = in.readInt();
        other1 = in.readString();
        other2 = in.readString();
        restricted_activities = in.readString();
        dietary_restrictions = in.readString();
    }

    public static final Creator<MedicalHistory> CREATOR = new Creator<MedicalHistory>() {
        @Override
        public MedicalHistory createFromParcel(Parcel in) {
            return new MedicalHistory(in);
        }

        @Override
        public MedicalHistory[] newArray(int size) {
            return new MedicalHistory[size];
        }
    };

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public int getMedical_history_id() {
        return medical_history_id;
    }

    public void setMedical_history_id(int medical_history_id) {
        this.medical_history_id = medical_history_id;
    }

    public int getProofId() {
        return proofId;
    }

    public void setProofId(int proofId) {
        this.proofId = proofId;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public int getMeasles() {
        return measles;
    }

    public void setMeasles(int measles) {
        this.measles = measles;
    }

    public int getMumps() {
        return mumps;
    }

    public void setMumps(int mumps) {
        this.mumps = mumps;
    }

    public int getMeningitis() {
        return meningitis;
    }

    public void setMeningitis(int meningitis) {
        this.meningitis = meningitis;
    }

    public int getRubella() {
        return rubella;
    }

    public void setRubella(int rubella) {
        this.rubella = rubella;
    }

    public int getChicken_pox() {
        return chicken_pox;
    }

    public void setChicken_pox(int chicken_pox) {
        this.chicken_pox = chicken_pox;
    }

    public int getPertussis() {
        return pertussis;
    }

    public void setPertussis(int pertussis) {
        this.pertussis = pertussis;
    }

    public int getAsthma() {
        return asthma;
    }

    public void setAsthma(int asthma) {
        this.asthma = asthma;
    }

    public int getEczema() {
        return eczema;
    }

    public void setEczema(int eczema) {
        this.eczema = eczema;
    }

    public int getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(int diabetes) {
        this.diabetes = diabetes;
    }

    public int getEpilepsy() {
        return epilepsy;
    }

    public void setEpilepsy(int epilepsy) {
        this.epilepsy = epilepsy;
    }

    public String getOther1() {
        return other1;
    }

    public void setOther1(String other1) {
        this.other1 = other1;
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2;
    }

    public String getRestricted_activities() {
        return restricted_activities;
    }

    public void setRestricted_activities(String restricted_activities) {
        this.restricted_activities = restricted_activities;
    }

    public String getDietary_restrictions() {
        return dietary_restrictions;
    }

    public void setDietary_restrictions(String dietary_restrictions) {
        this.dietary_restrictions = dietary_restrictions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(proof, flags);
        dest.writeParcelable(medication, flags);
        dest.writeInt(medical_history_id);
        dest.writeInt(proofId);
        dest.writeInt(medicationId);
        dest.writeInt(measles);
        dest.writeInt(mumps);
        dest.writeInt(meningitis);
        dest.writeInt(rubella);
        dest.writeInt(chicken_pox);
        dest.writeInt(pertussis);
        dest.writeInt(asthma);
        dest.writeInt(eczema);
        dest.writeInt(diabetes);
        dest.writeInt(epilepsy);
        dest.writeString(other1);
        dest.writeString(other2);
        dest.writeString(restricted_activities);
        dest.writeString(dietary_restrictions);
    }
}
