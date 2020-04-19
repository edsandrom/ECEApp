package com.example.eceapp;

import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;

public class DaycareClass {

    int daycareId;
    String name;
    int addressId;
    String phone;

    //default constructor
    public DaycareClass() {
    }

    //overloaded constructor
    public DaycareClass(int daycareId, String name, int addressId, String phone) {
        this.daycareId = daycareId;
        this.name = name;
        this.addressId = addressId;
        this.phone = phone;
    }

    //getters and setters
    public int getDaycareId() {
        return daycareId;
    }

    public void setDaycareId(int daycareId) {
        this.daycareId = daycareId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name;
    }

    public ArrayList<DaycareClass> getAllDaycares() {
        ArrayList<DaycareClass> daycares = new ArrayList<DaycareClass>();
        try {
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call fetchAllDayCares()");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DaycareClass d = new DaycareClass();
                d.setDaycareId(result.getInt("daycare_id"));
                d.setName(result.getString("name"));
                d.setPhone(result.getString("phone"));
                d.setAddressId(result.getInt("address_id"));
                daycares.add(d);
            }
            statement.close();
        } catch (SQLException ex) {
            Log.e("SQL Error", Objects.requireNonNull(ex.getMessage()));
        } catch (Exception ex) {
            Log.e("Other ERROR", Objects.requireNonNull(ex.getMessage()));
        }
        return daycares;
    }
}
