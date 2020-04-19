package com.example.eceapp.businessLayer;

import com.example.eceapp.Practitioner;
import com.example.eceapp.dataLayer.PractitionerDL;

public class PractitionerBL {
    PractitionerDL practitionerDL=PractitionerDL.getInstance();
    public int[] addPractitioner(Practitioner p){
        return practitionerDL.addPractitioner(p);
    }
    public int editPractitioner(Practitioner p){
        return practitionerDL.editPractitioner(p);
    }
    public Practitioner getPractitionerbyId(int id){
        return practitionerDL.fetchPractitionerById(id);
    }
}
