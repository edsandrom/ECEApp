package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.NonAuthorized;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class NonAuthorizedDL {
    private static NonAuthorizedDL instance;

    //Database Connection
    private static Connection conn;
    //

    public static NonAuthorizedDL getInstance() {
        if (instance == null) {
            ConnectionClass connection = new ConnectionClass();
            instance = new NonAuthorizedDL();
            conn = connection.Connect();
            //System.out.println(conn);
        }
        return instance;
    }


    public int[] addNonAuthorized(NonAuthorized contact){
        int nonAuId=-1;
        int isExist=0;
        int[] nonAuId_isExist = new int[2];
        try {
            CallableStatement mystmt = conn.prepareCall("call add_nonAuthorized(?, ?, ?, ?)");
            mystmt.setString(1, contact.getFirstName());
            mystmt.setString(2, contact.getLastName());

            //check duplicate Non_authorized record
            mystmt.registerOutParameter(3, Types.INTEGER);
            mystmt.registerOutParameter(4, Types.INTEGER);
            mystmt.executeUpdate();
            nonAuId = mystmt.getInt(3);
            isExist = mystmt.getInt(4);
            nonAuId_isExist[0] =nonAuId;
            nonAuId_isExist[1] = isExist;
            return nonAuId_isExist;
        } catch (SQLException ex) {
            //Log.e("SQL ERROR AddNonAu", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return nonAuId_isExist;
    }
    public int[] addNonAuthorizedRealtionship(NonAuthorized contact){
        int nonAuId=-1;
        int isExist=0;
        int[] nonAuId_isExist = new int[2];
        try {
            CallableStatement mystmt = conn.prepareCall("call add_nonAuthorized_Relationship(?, ?, ?, ?)");
            mystmt.setInt(1, contact.getNonAuId());
            mystmt.setInt(2, contact.getStuId());

            //check duplicate Non_authorized record
            mystmt.registerOutParameter(3, Types.INTEGER);
            mystmt.registerOutParameter(4, Types.INTEGER);
            mystmt.executeUpdate();
            nonAuId = mystmt.getInt(3);
            isExist = mystmt.getInt(4);
            nonAuId_isExist[0] =nonAuId;
            nonAuId_isExist[1] = isExist;
            return nonAuId_isExist;
        } catch (SQLException ex) {

            //Log.e("SQL ERROR", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return nonAuId_isExist;
    }
    public int editNonAuthorized(NonAuthorized p) {
        int isUpdate = -1;
        try {
            CallableStatement mystmt = conn.prepareCall("call edit_nonAuthorized(?, ?, ?, ?)");
            mystmt.setInt(1, p.getNonAuId());
            mystmt.setString(2, p.getFirstName());
            mystmt.setString(3, p.getLastName());
            mystmt.setInt(4, p.getIsActive());
            isUpdate=mystmt.executeUpdate();
            return isUpdate;
        } catch (SQLException ex) {
            //Log.e("SQL NonAuthorized ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());
        }
        return isUpdate;
    }
    public ArrayList<NonAuthorized> fetchNonAuthorizedByStudentId (int id){
        ArrayList<NonAuthorized> nonAuthorizeds = new ArrayList<>();
        try {
            CallableStatement stmt;
            stmt = conn.prepareCall("call fetchNonAuthorizedByStudentId("+id+")");
            ResultSet rs = stmt.executeQuery();

            rs.first();
            do {
                NonAuthorized n = new NonAuthorized();
                n.setNonAuId(rs.getInt(1));
                n.setFirstName(rs.getString(2));
                n.setLastName(rs.getString(3));
                n.setIsActive(rs.getInt(4));

                //System.out.println(n);
                nonAuthorizeds.add(n);
            } while (rs.next());
            return nonAuthorizeds;
        } catch (SQLException ex) {
           // Log.e("SQL NonAuthorized ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());
        }
        return null;
    }
}
