package com.example.eceapp.dataLayer;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.DaycareClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaycareClassDL {

    private static DaycareClassDL instance;

    //Database Connection
    private static ConnectionClass connection = new ConnectionClass();
    private static Connection conn;
    //

    public static DaycareClassDL getInstance() {
        if (instance == null) {
            //first person in, we need to instantiate the object
            instance = new DaycareClassDL();
            conn = connection.Connect();
        }
        return instance;
    }

    public DaycareClass fetchDaycareById(int daycareId) {

        DaycareClass daycare = new DaycareClass();
        CallableStatement fetchStatement = null;

        try {
            fetchStatement = conn.prepareCall("call fetchDaycareById (?)");
            fetchStatement.setInt(1, daycareId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            if (rs != null) {
                daycare.setDaycareId(daycareId);
                daycare.setName(rs.getString(2));
                daycare.setPhone(rs.getString(3));
                return daycare;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }
}
