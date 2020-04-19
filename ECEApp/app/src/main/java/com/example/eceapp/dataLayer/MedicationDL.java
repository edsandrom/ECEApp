package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.Medication;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class MedicationDL {
    private static MedicationDL instance;
    private static Connection conn;
    public static MedicationDL getInstance(){
        if(instance==null){
            ConnectionClass connection = new ConnectionClass();
            instance = new MedicationDL();
            conn=connection.Connect();
            //System.out.println(conn);
        }
        return instance;
    }
    public int addMedication(Medication m){
        int mediId = 0;
        try{
            CallableStatement stmt = conn.prepareCall("call add_medication(?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, m.getMedication1());
            stmt.setString(2,m.getCondition1());
            stmt.setString(3,m.getDosage1());
            stmt.setString(4, m.getMedication2());
            stmt.setString(5,m.getCondition2());
            stmt.setString(6,m.getDosage2());
            stmt.registerOutParameter(7, Types.INTEGER);
            stmt.executeUpdate();
            mediId=stmt.getInt(7);
            return mediId;
        }catch(SQLException ex){
            //Log.e("SQL error: ", ex.getMessage());
        }catch(Exception ex){
            Log.e("Other error: ", ex.getMessage());
        }
        return mediId;
    }
    public int editMedication(Medication p) {
        int isUpdate = -1;
        try {
            CallableStatement mystmt = conn.prepareCall("call edit_medication(?, ?, ?, ?,? , ?,?)");
            mystmt.setString(1, p.getMedication1());
            mystmt.setString(2, p.getCondition1());
            mystmt.setString(3, p.getDosage1());
            mystmt.setString(4, p.getMedication2());
            mystmt.setString(5, p.getCondition2());
            mystmt.setString(6, p.getDosage2());
            mystmt.setInt(7, p.getMedicationId());
            isUpdate=mystmt.executeUpdate();
            return isUpdate;
        } catch (SQLException ex) {
            //Log.e("SQL Medication ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other Medication ERROR", ex.getMessage());
        }
        return isUpdate;
    }
    public Medication fetchMedicationById (int id){
        try {
            CallableStatement stmt;
            stmt = conn.prepareCall("call fetchMedicationById("+id+")");
            ResultSet rs = stmt.executeQuery();
            rs.first();
                Medication n = new Medication();
                n.setMedicationId(rs.getInt(1));
                n.setMedication1(rs.getString(2));
                n.setCondition1(rs.getString(3));
                n.setDosage1(rs.getString(4));
                n.setMedication2(rs.getString(5));
                n.setCondition2(rs.getString(6));
                n.setDosage2(rs.getString(7));
                //System.out.println(n);
            return n;
        } catch (SQLException ex) {
            //Log.e("SQL Medication ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other Medication ERROR", ex.getMessage());
        }
        return null;
    }
}
