package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//Coded By Ethan Steeves Feb 12/2020
public class ManualClass {

    private String name;
    private String url;
    //private ArrayList<String> manuals;

    public ManualClass(){

    }

    public ManualClass(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        return name; //this is used for the spinner to retrieve the name of the manual
    }

    public ArrayList<ManualClass> GetManualList(){
        String name, url;
        ArrayList<ManualClass> manuals = new ArrayList<>();
        try{
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            PreparedStatement ps = conn.prepareCall("call fetchAllManuals()");
            ResultSet rs = ps.executeQuery();
            rs.first();

            do{
                name = rs.getString(1);
                url = rs.getString(2);

                ManualClass manualItem = new ManualClass(name, url);
                manuals.add(manualItem);

            }while(rs.next());

            return manuals;
        }
        catch(SQLException ex){
            Log.e("SQL ERROR", ex.getMessage());
        }
        catch(Exception ex){
            Log.e("EXCEPTION", "I AM HERE " + ex.getMessage());
        }

        return null;
    }
}
