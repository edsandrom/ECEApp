package com.example.eceapp.businessLayer;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.DaycareClass;
import com.example.eceapp.dataLayer.DaycareClassDL;

import java.util.ArrayList;

public class DaycareClassBL {



    DaycareClassDL daycareClassDL = DaycareClassDL.getInstance();

    public DaycareClass getDaycareById(int id){
        return daycareClassDL.fetchDaycareById(id);
    }

    public ArrayList<DaycareClass> ListOfDaycares(){
        ArrayList<DaycareClass> daycares = new ArrayList<>();
        DaycareClass dc = new DaycareClass();
        daycares = dc.getAllDaycares();
        return daycares;
    }



}

