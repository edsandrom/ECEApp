package com.example.eceapp.businessLayer;

import com.example.eceapp.MedicalHistory;
import com.example.eceapp.dataLayer.MedicalHistoryDL;

public class MedicalHistoryBL {
    MedicalHistoryDL medicalHistoryDL = MedicalHistoryDL.getInstance();
    public int addMedicalHistory(MedicalHistory m){
        return medicalHistoryDL.addMedicalHistory(m);
    }
    public int editMedicalHistory(MedicalHistory m){
        return medicalHistoryDL.editMedicalHistory(m);
    }
    public MedicalHistory getMedicalHistoryById(int id){
        return medicalHistoryDL.fetchMedicalHistoryById(id);
    }
}
