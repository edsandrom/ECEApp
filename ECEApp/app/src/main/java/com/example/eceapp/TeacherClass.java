package com.example.eceapp;

import android.telecom.Call;
import android.util.Log;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;

public class TeacherClass {

    int teacherID = 0;
    int addressID = 0;
    String firstName = "";
    String lastName = "";
    String username = "";
    String password = "";
    int daycareID = 0;
    boolean isAdmin = false;
    boolean isActive = true;

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public TeacherClass(int teacherID, int addressID, String firstName, String lastName, String username, String password, int daycareID, boolean isAdmin, boolean isActive) {

        this.teacherID = teacherID;
        this.addressID = addressID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.daycareID = daycareID;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
    }

    //default constructor
    public TeacherClass() {
    }

    public int CreateTeacher(String firstName, String lastName, String username, String password, int daycareID, boolean isAdmin){
        try{
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call Create_Teacher(?,?,?,?,?,?,?)");
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

    public int UpdateTeacher(int id, String password, boolean isAdmin, boolean isActive){
        try {
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call Edit_Teacher(?,?,?,?,?)");
            statement.setInt(1, id);
            statement.setString(2, password);
            statement.setBoolean(3, isAdmin);
            statement.setBoolean(4, isActive);
            statement.registerOutParameter(5, Types.INTEGER);
            statement.execute();
            int result = statement.getInt(5);
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
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call fetchAllTeachers()");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                TeacherClass t = new TeacherClass();
                t.setTeacherID(result.getInt("teacher_id"));
                t.setAddressID(result.getInt("address_id"));
                t.setFirstName(result.getString("first_name"));
                t.setLastName(result.getString("last_name"));
                t.setUsername(result.getString("username"));
                t.setPassword(result.getString("password"));
                t.setDaycareID(result.getInt("daycare_id"));
                t.setAdmin(result.getBoolean("is_admin"));
                t.setActive(result.getBoolean("is_active"));
                teachers.add(t);
            }
            statement.close();

        } catch (SQLException ex) {
            Log.e("SQL Error", Objects.requireNonNull(ex.getMessage()));
        } catch (Exception ex) {
            Log.e("Other ERROR", Objects.requireNonNull(ex.getMessage()));
        }
        return teachers;
    }//end of getAllTeachers

    public boolean IsTeacherActive (String usernm) {
        ArrayList<TeacherClass> teachers = getAllTeachers();
        for(TeacherClass t: teachers){
            if(t.username.equals(usernm)) {
                if(t.isActive) {
                    return true;
                }
            }
        }
        return false;
    }//end of IsTeacherActive
}
