package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.TemplateClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TemplateClassDL {

    private static TemplateClassDL instance;

    //Database Connection
    private static ConnectionClass connection = new ConnectionClass();
    private static Connection conn;
    //

    public static TemplateClassDL getInstance() {
        if (instance == null) {
            //first person in, we need to instantiate the object
            instance = new TemplateClassDL();
            conn = connection.Connect();
        }
        return instance;
    }

    public ArrayList<TemplateClass> fetchActiveStudentsTemplates() { //Active only!!
        String formDesc, formLocation;
        int formId;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchActiveStudentsTemplates");
            ArrayList<TemplateClass> templateList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                formId = rs.getInt(1);
                formDesc = rs.getString(2);
                formLocation = rs.getString(5);
                TemplateClass template = new TemplateClass();
                template.setTemplateId(formId);
                template.setDescription(formDesc);
                template.setLocation(formLocation);
                templateList.add(template);
            } while (rs.next());
            return templateList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<TemplateClass> fetchAllStudentsTemplates() { //Active and Not Active
        String tempDesc, tempLocation;
        int tempId;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchAllStudentsTemplates");
            ArrayList<TemplateClass> templateList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                tempId = rs.getInt(1);
                tempDesc = rs.getString(2);
                tempLocation = rs.getString(5);
                TemplateClass template = new TemplateClass();
                template.setTemplateId(tempId);
                template.setDescription(tempDesc);
                template.setLocation(tempLocation);
                templateList.add(template);
            } while (rs.next());
            return templateList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<TemplateClass> fetchActiveTeachersTemplates() { //Active
        String tempDesc, tempLocation;
        int tempId;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchActiveTeachersTemplates");
            ArrayList<TemplateClass> templateList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                tempId = rs.getInt(1);
                tempDesc = rs.getString(2);
                tempLocation = rs.getString(5);
                TemplateClass template = new TemplateClass();
                template.setTemplateId(tempId);
                template.setDescription(tempDesc);
                template.setLocation(tempLocation);
                templateList.add(template);
            } while (rs.next());
            return templateList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<TemplateClass> fetchAllTeachersTemplates() { //Active and Not Active
        String tempDesc, tempLocation;
        int tempId;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchAllTeachersTemplates");
            ArrayList<TemplateClass> templateList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                tempId = rs.getInt(1);
                tempDesc = rs.getString(2);
                tempLocation = rs.getString(5);
                TemplateClass template = new TemplateClass();
                template.setTemplateId(tempId);
                template.setDescription(tempDesc);
                template.setLocation(tempLocation);
                templateList.add(template);
            } while (rs.next());
            return templateList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public TemplateClass fetchTemplateByDescription(String tempDescription) {
        String tempLocation;
        Boolean active;
        int templateId;
        CallableStatement fetchStatement = null;
        try {
            fetchStatement = conn.prepareCall("call fetchTemplateByDescription (?)");
            fetchStatement.setString(1, tempDescription);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            templateId = rs.getInt(1);
            tempLocation = rs.getString(5);
            active = rs.getBoolean(6);
            TemplateClass template = new TemplateClass();
            template.setTemplateId(templateId);
            template.setDescription(tempDescription);
            template.setLocation(tempLocation);
            template.setActive(active);
            return template;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public TemplateClass fetchTemplateById(int templateId) {
        String tempDesc, tempLocation;
        Boolean active;
        CallableStatement fetchStatement = null;
        try {
            fetchStatement = conn.prepareCall("call fetchTemplateById (?)");
            fetchStatement.setInt(1, templateId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            tempDesc = rs.getString(2);
            tempLocation = rs.getString(5);
            active = rs.getBoolean(6);
            TemplateClass template = new TemplateClass();
            template.setTemplateId(templateId);
            template.setDescription(tempDesc);
            template.setLocation(tempLocation);
            template.setActive(active);
            return template;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public boolean addTemplate (TemplateClass temp) {

        try {
            CallableStatement statement = conn.prepareCall("call addTemplate(?, ?, ?, ?, ?)");
            statement.setString(1, temp.getDescription());
            statement.setBoolean(2, temp.isStudent_allowed());
            statement.setBoolean(3, temp.isTeacher_allowed());
            statement.setString(4, temp.getLocation());
            statement.setBoolean(5, temp.isActive());
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            Log.e("SQL ERROR", ex.getMessage());
            return false;
        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());
            return false;
        }
    }
}
