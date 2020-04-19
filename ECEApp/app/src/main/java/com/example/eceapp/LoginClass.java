package com.example.eceapp;

import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class LoginClass {
    int status = 0;
    String username;
    String password;
    int daycareID;
    boolean isAdmin = false;

    public LoginClass() {
    }

    public LoginClass(String username, String password, int daycareID, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.daycareID = daycareID;
        this.isAdmin = isAdmin;
    }

    public LoginClass(int status, String username, String password, int daycareID, boolean isAdmin) {
        this.status = status;
        this.username = username;
        this.password = password;
        this.daycareID = daycareID;
        this.isAdmin = isAdmin;
    }

    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDaycareID() {
        return daycareID;
    }

    public void setDaycareID(int daycareID) {
        this.daycareID = daycareID;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public LoginClass login(String username, String password){
        try{
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            int status = -2;
            CallableStatement mystmt = conn.prepareCall("call Login3(?, ?, ?, ?, ?)");
            mystmt.setString(1, username);
            mystmt.registerOutParameter(2, Types.VARCHAR);
            mystmt.registerOutParameter(3, Types.INTEGER);
            mystmt.registerOutParameter(4, Types.INTEGER);
            mystmt.registerOutParameter(5, Types.BOOLEAN);
            mystmt.execute();
            String pass = mystmt.getString(2);
            PasswordHash hash = new PasswordHash();
            password = hash.generatedPassword(password); //generates the hash based on the input
            LoginClass loginData = new LoginClass();

            if(pass == null){
                loginData.status = -2;
                return loginData;
            }
            //int status = -1; //returns -1 if no connection to database
            if(pass.equals(password)) status = 1; //returns 1 if password is match
            else status = 0; //returns 0 if password does not match

            int daycareID = mystmt.getInt(4);
            boolean isAdmin = mystmt.getBoolean(5);
            loginData.status = status;
            loginData.username = username;
            loginData.daycareID = daycareID;
            loginData.password = pass;
            loginData.daycareID = daycareID;
            loginData.isAdmin = isAdmin;

            return loginData;
        }
        catch(SQLException ex){
            // Log.e("Database Error", Objects.requireNonNull(ex.getMessage()));
            Log.e("User name does notexist", "not user name in sqlexception");
        }

        catch(Exception ex){
            Log.e("Other Error", Objects.requireNonNull(ex.getMessage()));
            return new LoginClass(-1, null, null, -1, false);
            // Log.e("User name does notexist", "not user name in exception");
        }
        return null; //this is returned if there was an SQL error even if there are no records matching counter variable should return 0
    }//end of login
}
