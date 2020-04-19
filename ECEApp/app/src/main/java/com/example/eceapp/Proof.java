package com.example.eceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Proof implements Parcelable {
    private int proofId;
    private String proof_diptheria, proof_tetanus, proof_polio, proof_pertussis, proof_rubella,
            proof_varicella, proof_mening_disease, proof_pneum_disease, proof_mumps, proof_measles, proof_influenza_b;

    public Proof() {
    }

    protected Proof(Parcel in) {
        proofId = in.readInt();
        proof_diptheria = in.readString();
        proof_tetanus = in.readString();
        proof_polio = in.readString();
        proof_pertussis = in.readString();
        proof_rubella = in.readString();
        proof_varicella = in.readString();
        proof_mening_disease = in.readString();
        proof_pneum_disease = in.readString();
        proof_mumps = in.readString();
        proof_measles = in.readString();
        proof_influenza_b = in.readString();
    }

    public static final Creator<Proof> CREATOR = new Creator<Proof>() {
        @Override
        public Proof createFromParcel(Parcel in) {
            return new Proof(in);
        }

        @Override
        public Proof[] newArray(int size) {
            return new Proof[size];
        }
    };

    public int getProofId() {
        return proofId;
    }

    public void setProofId(int proofId) {
        this.proofId = proofId;
    }

    public String getProof_diptheria() {
        return proof_diptheria;
    }

    public void setProof_diptheria(String proof_diptheria) {
        this.proof_diptheria = proof_diptheria;
    }

    public String getProof_tetanus() {
        return proof_tetanus;
    }

    public void setProof_tetanus(String proof_tetanus) {
        this.proof_tetanus = proof_tetanus;
    }

    public String getProof_polio() {
        return proof_polio;
    }

    public void setProof_polio(String proof_polio) {
        this.proof_polio = proof_polio;
    }

    public String getProof_pertussis() {
        return proof_pertussis;
    }

    public void setProof_pertussis(String proof_pertussis) {
        this.proof_pertussis = proof_pertussis;
    }

    public String getProof_rubella() {
        return proof_rubella;
    }

    public void setProof_rubella(String proof_rubella) {
        this.proof_rubella = proof_rubella;
    }

    public String getProof_varicella() {
        return proof_varicella;
    }

    public void setProof_varicella(String proof_varicella) {
        this.proof_varicella = proof_varicella;
    }

    public String getProof_mening_disease() {
        return proof_mening_disease;
    }

    public void setProof_mening_disease(String proof_mening_disease) {
        this.proof_mening_disease = proof_mening_disease;
    }

    public String getProof_pneum_disease() {
        return proof_pneum_disease;
    }

    public void setProof_pneum_disease(String proof_pneum_disease) {
        this.proof_pneum_disease = proof_pneum_disease;
    }

    public String getProof_mumps() {
        return proof_mumps;
    }

    public void setProof_mumps(String proof_mumps) {
        this.proof_mumps = proof_mumps;
    }

    public String getProof_measles() {
        return proof_measles;
    }

    public void setProof_measles(String proof_measles) {
        this.proof_measles = proof_measles;
    }

    public String getProof_influenza_b() {
        return proof_influenza_b;
    }

    public void setProof_influenza_b(String proof_influenza_b) {
        this.proof_influenza_b = proof_influenza_b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(proofId);
        dest.writeString(proof_diptheria);
        dest.writeString(proof_tetanus);
        dest.writeString(proof_polio);
        dest.writeString(proof_pertussis);
        dest.writeString(proof_rubella);
        dest.writeString(proof_varicella);
        dest.writeString(proof_mening_disease);
        dest.writeString(proof_pneum_disease);
        dest.writeString(proof_mumps);
        dest.writeString(proof_measles);
        dest.writeString(proof_influenza_b);
    }
}
