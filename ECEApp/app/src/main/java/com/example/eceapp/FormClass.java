package com.example.eceapp;

import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;

public class FormClass {

    int id = 0;
    int template_id = 0;
    int student_id = 0;
    int teacher_id = 0;
    String location = "";
    String description = "";
    String filename = "";
    Boolean student_allowed = true;
    Boolean teacher_allowed = true;
    Boolean active = true;
    private String date_create;

    public FormClass() {
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getStudent_allowed() {
        return student_allowed;
    }

    public void setStudent_allowed(Boolean student_allowed) {
        this.student_allowed = student_allowed;
    }

    public Boolean getTeacher_allowed() {
        return teacher_allowed;
    }

    public void setTeacher_allowed(Boolean teacher_allowed) {
        this.teacher_allowed = teacher_allowed;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public FormClass(String description, Boolean student_allowed, Boolean teacher_allowed, String location) {
        this.description = description;
        this.student_allowed = student_allowed;
        this.teacher_allowed = teacher_allowed;
        this.location = location;
    }

    public FormClass(int id, String description, Boolean student_allowed, Boolean teacher_allowed, String location, Boolean active) {
        this.id = id;
        this.description = description;
        this.student_allowed = student_allowed;
        this.teacher_allowed = teacher_allowed;
        this.location = location;
        this.active = active;
    }

    public FormClass(int id, int template_id, int student_id, int teacher_id, String location, String description, String filename,
                     Boolean student_allowed, Boolean teacher_allowed, Boolean active) {
        this.id = id;
        this.template_id = template_id;
        this.student_id = student_id;
        this.teacher_id = teacher_id;
        this.location = location;
        this.description = description;
        this.filename = filename;
        this.student_allowed = student_allowed;
        this.teacher_allowed = teacher_allowed;
        this.active = active;
    }

    public ArrayList<FormClass> getAllFormTemplates() {
        ArrayList<FormClass> templates = new ArrayList<FormClass>();
        try {
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call fetchAllFormTemplates()");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                FormClass f = new FormClass();
                f.setId(result.getInt("template_id"));
                f.setDescription(result.getString("description"));
                f.setStudent_allowed(result.getBoolean("student_allowed"));
                f.setTeacher_allowed(result.getBoolean("teacher_allowed"));
                f.setLocation(result.getString("location"));
                f.setActive(result.getBoolean("isActive"));
                templates.add(f);
            }
            statement.close();
        } catch (SQLException ex) {
            Log.e("SQL Error", Objects.requireNonNull(ex.getMessage()));
        } catch (Exception ex) {
            Log.e("Other ERROR", Objects.requireNonNull(ex.getMessage()));
        }
        return templates;
    }//end of getAllFormTemplates

    public int UpdateForm(FormClass fc) {
        try {
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call edit_form(?,?,?,?,?,?,?)");
            statement.setInt(1, fc.id);
            statement.setString(2, fc.description);
            statement.setBoolean(3, fc.student_allowed);
            statement.setBoolean(4, fc.teacher_allowed);
            statement.setString(5, fc.location);
            statement.setBoolean(6, fc.active);
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
    }//end of UpdateForm method

    public int DisableForm(int id) {
        try {
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call disable_form(?,?)");
            statement.setInt(1, id);
            statement.registerOutParameter(2, Types.INTEGER);
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
    public int InsertForm(FormClass newForm){
        try{
            ConnectionClass connection = new ConnectionClass();
            Connection conn = connection.Connect();
            CallableStatement statement = conn.prepareCall("call create_form(?,?,?,?,?)");
            statement.setString(1, newForm.description);
            statement.setBoolean(2, newForm.student_allowed);
            statement.setBoolean(3,newForm.teacher_allowed);
            statement.setString(4,newForm.location);
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
}
