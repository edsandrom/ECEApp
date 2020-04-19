package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.AddressClass;
import com.example.eceapp.ConnectionClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AddressDL {
    private static AddressDL instance;

    //Database Connection
    private static Connection conn;
    //

    public static AddressDL getInstance() {
        if (instance == null) {
            ConnectionClass connection = new ConnectionClass();
            instance = new AddressDL();
            conn = connection.Connect();
            //System.out.println(conn);
        }
        return instance;
    }
    public int[] addAddress(AddressClass address){
        int addressId=0;
        int isExist=0;
        int[] addressID_isExist = new int[2];
        try {
            CallableStatement mystmt = conn.prepareCall("call add_address(?, ?, ?, ?, ?, ?, ?, ?)");
            mystmt.setString(1, address.getAptNo());
            mystmt.setString(2, address.getStrNo());
            mystmt.setString(3, address.getStreet());
            mystmt.setString(4, address.getCity());
            mystmt.setString(5, address.getProv());
            mystmt.setString(6, address.getPostCode());

            //get address id (Existing or new one), id id = 0, there is an error
            mystmt.registerOutParameter(7, Types.INTEGER);
            //check duplicate student record
            mystmt.registerOutParameter(8, Types.INTEGER);
            mystmt.executeUpdate();
            addressId = mystmt.getInt(7);
            isExist = mystmt.getInt(8);
            addressID_isExist[0] =addressId;
            addressID_isExist[1] = isExist;

            return addressID_isExist;
        } catch (SQLException ex) {
            //RepeatMethods.showAlertDialog(CreateStudentActivity.this, "Error", "Something wrong happened. Please try again");
            //Log.e("SQL ERROR", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return addressID_isExist;
    }
    public int editAddress(AddressClass address) {
        int isUpdate = -1;
        try {
            CallableStatement mystmt = conn.prepareCall("call edit_address(?, ?, ?, ?, ?, ?, ?)");
            mystmt.setString(1, address.getAptNo());
            mystmt.setString(2, address.getStrNo());
            mystmt.setString(3, address.getStreet());
            mystmt.setString(4, address.getCity());
            mystmt.setString(5, address.getProv());
            mystmt.setString(6, address.getPostCode());
            mystmt.setInt(7, address.getAddressId());
            isUpdate=mystmt.executeUpdate();
            return isUpdate;
        } catch (SQLException ex) {
            Log.e("SQL editAddress ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other adddress ERROR", ex.getMessage());
        }
        return isUpdate;
    }
    public AddressClass fetchAddressById (int id){
        try {
            CallableStatement stmt;
            stmt = conn.prepareCall("call fetchAddressById("+id+")");
            ResultSet rs = stmt.executeQuery();
            rs.first();
            AddressClass n = new AddressClass();
            n.setAddressId(rs.getInt(1));
            n.setStreet(rs.getString(2));
            n.setStrNo(rs.getString(3));
            n.setAptNo(rs.getString(4));
            n.setCity(rs.getString(5));
            n.setProv(rs.getString(6));
            n.setPostCode(rs.getString(7));

            //System.out.println(n);
            return n;
        } catch (SQLException ex) {
            Log.e("SQL Address ERROR", ex.getMessage()+"fetch Address by id: "+id);
        } catch (Exception ex) {
            Log.e("Other PractitionerERROR", ex.getMessage()+"fetch Address by id: "+id);
        }
        return null;
    }
}
