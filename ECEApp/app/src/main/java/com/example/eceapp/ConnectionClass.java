package com.example.eceapp;

import android.content.res.Resources;
import android.os.StrictMode;
import android.util.Log;

import com.example.eceapp.dataLayer.ManualClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;

//Coded by Ethan Steeves January 28/20   Last Edited By Adam Brewer 23/03/2020

public class ConnectionClass {

    Resources res = Resources.getSystem();

//    private String url = com.example.eceapp.BuildConfig.
    private String driver = "com.mysql.jdbc.Driver";
    private String url = BuildConfig.DB_URL;
    private String username = BuildConfig.DB_UNAME;
    private String password = BuildConfig.DB_ENCPASS;
    Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ConnectionClass() {
        this.connection = Connect();
    }

    public Connection Connect(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;

        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException ex){
            Log.e("CONNECT ERROR1", Objects.requireNonNull(ex.getMessage()));
        }
        catch(ClassNotFoundException ex){
            Log.e("CONNECT ERROR2", Objects.requireNonNull(ex.getMessage()));
        }
        catch(Exception ex){
            Log.e("CONNECT ERROR3", Objects.requireNonNull(ex.getMessage()));
        }

        return conn;
    }
}//end of connect
/*
    public void Disconnect(Connection conn){
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Log.e("DISCONNECT ERROR1", Objects.requireNonNull(ex.getMessage()));
            } catch (Exception ex) {
                Log.e("DISCONNECT ERROR2", Objects.requireNonNull(ex.getMessage()));
            }
        }
    }//end of Disconnect

    public LoginClass login(String username, String password){
        try{
            //Maybe make counter -2 here?

            Connection conn = Connect();
            int status = -2;
            CallableStatement mystmt = conn.prepareCall("call Login3(?, ?, ?, ?, ?)"); //change sql stored procedure in database, currently login(???) would not work for this, change login to logintest

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

            //LoginClass loginData = new LoginClass(status, username, pass, daycareID, isAdmin);

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

    public int CreateTeacher(String firstName, String lastName, String username, String password, int daycareID, boolean isAdmin){
        try{
            Connection connection = Connect();
            CallableStatement statement = connection.prepareCall("call Create_Teacher(?,?,?,?,?,?,?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setInt(5, daycareID);
            statement.setBoolean(6, isAdmin);
            statement.registerOutParameter(7, Types.INTEGER);
            statement.execute();
            int result = statement.getInt(7);
            return result;
        } catch (SQLException ex) {
            Log.e("SQL Error", Objects.requireNonNull(ex.getMessage()));
            return 2;
        } catch (Exception ex) {
            Log.e("Other ERROR", Objects.requireNonNull(ex.getMessage()));
            return 3;
        }
    }

    public ArrayList<TeacherClass> getAllTeachers() {
        ArrayList<TeacherClass> teachers = new ArrayList<TeacherClass>();
        try{
            Connection connection = Connect();
            PreparedStatement statement = connection.prepareStatement("Select * from ece_database.teacher");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                TeacherClass t = new TeacherClass();
                t.setFirstName(result.getString("first_name"));
                t.setLastName(result.getString("last_name"));
                t.setUsername(result.getString("username"));
                t.setPassword(result.getString("password"));
                t.setDaycareID(result.getInt("daycare_id"));
                t.setAdmin(result.getBoolean("is_admin"));
                teachers.add(t);
            }
            statement.close();

        } catch (SQLException ex) {
            Log.e("SQL Error", Objects.requireNonNull(ex.getMessage()));
        } catch (Exception ex) {
            Log.e("Other ERROR", Objects.requireNonNull(ex.getMessage()));
        }
        return teachers;
    }

    public ArrayList<DaycareClass> getAllDaycares(){
        ArrayList<DaycareClass> daycares = new ArrayList<DaycareClass>();
        try{
            Connection connection = Connect();
            PreparedStatement statement = connection.prepareStatement("Select * from ece_database.daycare");
            ResultSet result = statement.executeQuery();
            while(result.next()) {
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
        }   catch (Exception ex) {
            Log.e("Other ERROR", Objects.requireNonNull(ex.getMessage()));
        }
        return daycares;
    }

    public int UpdateTeacher(String username, String password, boolean isAdmin){
        try {
            Connection connection = Connect();
            CallableStatement statement = connection.prepareCall("call Edit_Teacher(?,?,?,?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setBoolean(3, isAdmin);
            statement.registerOutParameter(4, Types.INTEGER);
            statement.execute();
            int result = statement.getInt(4);
            return result;
        } catch (SQLException ex) {
            Log.e("SQL Error", Objects.requireNonNull(ex.getMessage()));
            return 2;
        } catch (Exception ex) {
            Log.e("Other ERROR", Objects.requireNonNull(ex.getMessage()));
            return 3;
        }
    }//end of create teacher

    public ArrayList<ManualClass> GetManualList(){
        String name, url;
        ArrayList<ManualClass> manuals = new ArrayList<>();


        try{
            Connection conn = Connect();
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
    }//end of getManualList() */

