package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.Practitioner;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PractitionerDL {
    private static PractitionerDL instance;

    //Database Connection
    private static Connection conn;
    //

    public static PractitionerDL getInstance() {
        if (instance == null) {
            ConnectionClass connection = new ConnectionClass();
            instance = new PractitionerDL();
            conn = connection.Connect();
            //System.out.println(conn);
        }
        return instance;
    }
    public int[] addPractitioner(Practitioner p){
        int practitionerID=0;
        int isExist=0;
        int[] practitionerID_isExist = new int[2];
        try {
            CallableStatement mystmt = conn.prepareCall("call add_practitioner(?, ?, ?, ?, ?, ?)");
            mystmt.setString(1, p.getfName());
            mystmt.setString(2, p.getlName());
            mystmt.setString(3, p.getPhone());
            mystmt.setInt(4, p.getAddressId());

            mystmt.registerOutParameter(5, Types.INTEGER);
            //check duplicate student record
            mystmt.registerOutParameter(6, Types.INTEGER);
            mystmt.executeUpdate();
            practitionerID_isExist[0] =mystmt.getInt(5);
            practitionerID_isExist[1] = mystmt.getInt(6);

            return practitionerID_isExist;
        } catch (SQLException ex) {
            //RepeatMethods.showAlertDialog(CreateStudentActivity.this, "Error", "Something wrong happened. Please try again");
           // Log.e("SQL ERROR", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return practitionerID_isExist;
    }
    public int editPractitioner(Practitioner p) {
        int isUpdate = -1;
        try {
            CallableStatement mystmt = conn.prepareCall("call edit_practioner(?, ?, ?, ?)");
            mystmt.setInt(1, p.getPractitionerId());
            mystmt.setString(2, p.getfName());
            mystmt.setString(3, p.getlName());
            mystmt.setString(4, p.getPhone());
            isUpdate=mystmt.executeUpdate();
            return isUpdate;
        } catch (SQLException ex) {
           // Log.e("SQL Practitioner ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());
        }
        return isUpdate;
    }
    public Practitioner fetchPractitionerById (int id){
        try {
            CallableStatement stmt;
            stmt = conn.prepareCall("call fetchPractitionerById("+id+")");
            ResultSet rs = stmt.executeQuery();
            rs.first();
            Practitioner n = new Practitioner();
            n.setPractitionerId(rs.getInt(1));
            n.setfName(rs.getString(2));
            n.setlName(rs.getString(3));
            n.setPhone(rs.getString(4));
            n.setAddressId(rs.getInt(5));

            //System.out.println(n);
            return n;
        } catch (SQLException ex) {
            //Log.e("SQL Practitioner ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other PractitionerERROR", ex.getMessage());
        }
        return null;
    }
}
