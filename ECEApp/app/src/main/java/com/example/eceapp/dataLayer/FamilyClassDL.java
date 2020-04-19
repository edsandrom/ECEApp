package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.FamilyRelationshipClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class FamilyClassDL {
    private static FamilyClassDL instance;

    //Database Connection
    private static Connection conn;
    //

    public static FamilyClassDL getInstance() {
        if (instance == null) {
            ConnectionClass connection = new ConnectionClass();
            instance = new FamilyClassDL();
            conn = connection.Connect();
            //System.out.println(conn);
        }
        return instance;
    }
    public int[] addFamilyRelationship(FamilyRelationshipClass fam){
        int famId = 0;
        int isExist= 0;
        int[] famId_isExist = new int[2];
        try {
            CallableStatement mystmt = conn.prepareCall("call add_family_relationship(?, ?, ?, ?, ?, ?, ?)");
            mystmt.setInt(1, fam.getContactId());
            mystmt.setInt(2, fam.getStuId());
            mystmt.setString(3, fam.getRelationship());
            mystmt.setInt(4, fam.getIsEmergencyCOntact());
            mystmt.setInt(5, fam.getIsAuthorizedPickup());


            //get family relationship id (Existing or new one), id id = 0, there is an error
            mystmt.registerOutParameter(6, Types.INTEGER);
            //check duplicate student record
            mystmt.registerOutParameter(7, Types.INTEGER);
            mystmt.executeUpdate();
            famId = mystmt.getInt(6);
            isExist = mystmt.getInt(7);
            famId_isExist[0] =famId;
            famId_isExist[1] = isExist;

            return famId_isExist;
        } catch (SQLException ex) {
            //RepeatMethods.showAlertDialog(CreateStudentActivity.this, "Error", "Something wrong happened. Please try again");
           // Log.e("SQL ERROR", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return famId_isExist;
    }
    public int editFamilyRelationship(FamilyRelationshipClass fam) {
        int isUpdate = -1;
        try {
            CallableStatement mystmt = conn.prepareCall("call edit_family_relationship(?, ?, ?, ?, ?)");
            mystmt.setInt(1, fam.getFamilyRelId());
            mystmt.setString(2, fam.getRelationship());
            mystmt.setInt(3, fam.getIsEmergencyCOntact());
            mystmt.setInt(4, fam.getIsAuthorizedPickup());
            mystmt.setInt(5, fam.getIsActive());
            isUpdate=mystmt.executeUpdate();
            return isUpdate;
        } catch (SQLException ex) {
           // Log.e("SQL FamilyRel ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other FamilyRel ERROR", ex.getMessage());
        }
        return isUpdate;
    }

}
