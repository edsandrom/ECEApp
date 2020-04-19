package com.example.eceapp.businessLayer;

import com.example.eceapp.NonAuthorized;
import com.example.eceapp.dataLayer.NonAuthorizedDL;

import java.util.ArrayList;

public class NonAuthorizedBL {
    NonAuthorizedDL nonAuthorizedDL = NonAuthorizedDL.getInstance();
    public int[] addNonAuthorized (NonAuthorized nonAuthorized){
        return nonAuthorizedDL.addNonAuthorized(nonAuthorized);
    }
    public int[] addNonAuthorizedRealtionship(NonAuthorized nonAuthorized){
        return nonAuthorizedDL.addNonAuthorizedRealtionship(nonAuthorized);
    }
    public int editNonAuthorized (NonAuthorized nonAuthorized){
        return nonAuthorizedDL.editNonAuthorized(nonAuthorized);
    }
    public ArrayList<NonAuthorized> getNonAuthorizedByStudentId (int id){
        return nonAuthorizedDL.fetchNonAuthorizedByStudentId(id);
    }
}
