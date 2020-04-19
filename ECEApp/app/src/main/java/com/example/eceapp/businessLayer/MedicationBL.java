package com.example.eceapp.businessLayer;

import com.example.eceapp.Medication;
import com.example.eceapp.dataLayer.MedicationDL;

public class MedicationBL {
    MedicationDL medicationDL = MedicationDL.getInstance();
    public int addMedication(Medication m){
        return medicationDL.addMedication(m);
    }
    public int editMedication(Medication m){
        return medicationDL.editMedication(m);
    }
    public Medication getMedicationById(int id){
        return medicationDL.fetchMedicationById(id);
    }
}
