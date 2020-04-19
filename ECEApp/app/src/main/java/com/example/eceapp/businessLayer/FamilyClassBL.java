package com.example.eceapp.businessLayer;

import com.example.eceapp.FamilyRelationshipClass;
import com.example.eceapp.dataLayer.FamilyClassDL;

public class FamilyClassBL {
    FamilyClassDL familyClassDL = FamilyClassDL.getInstance();
    public int[] addFamilyRealtionship(FamilyRelationshipClass fam){
        return familyClassDL.addFamilyRelationship(fam);
    }
    public int editFamilyRealtionship(FamilyRelationshipClass fam){
        return familyClassDL.editFamilyRelationship(fam);
    }
}
