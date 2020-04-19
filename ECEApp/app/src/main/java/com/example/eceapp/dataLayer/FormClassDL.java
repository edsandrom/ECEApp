package com.example.eceapp.dataLayer;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.FormClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//Class used for Forms and Templates

public class FormClassDL {

    private static FormClassDL instance;

    //Database Connection
    private static ConnectionClass connection = new ConnectionClass();
    private static Connection conn;
    //

    public static FormClassDL getInstance() {
        if (instance == null) {
            //first person in, we need to instantiate the object
            instance = new FormClassDL();
            conn = connection.Connect();
        }
        return instance;
    }

    public ArrayList<FormClass> fetchAllStudentForms() { //Active and Not Active
        int formId, templateId, studentId;
        String filename, dateCreate;
        Boolean active;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchAllStudentForms");
            ArrayList<FormClass> formsList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                studentId = rs.getInt(3);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setStudent_id(studentId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<FormClass> fetchActiveStudentForms() { //Active and Not Active
        int formId, templateId, studentId;
        String filename, dateCreate;
        Boolean active;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchActiveStudentForms");
            ArrayList<FormClass> formsList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                studentId = rs.getInt(3);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setStudent_id(studentId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<FormClass> fetchAllTeacherForms() { //Active and Not Active
        int formId, templateId, teacherId;
        String filename, dateCreate;
        Boolean active;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchAllTeacherForms");
            ArrayList<FormClass> formsList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                teacherId = rs.getInt(3);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setTeacher_id(teacherId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<FormClass> fetchActiveTeacherForms() { //Active and Not Active
        int formId, templateId, teacherId;
        String filename, dateCreate;
        Boolean active;

        try {
            CallableStatement fetchStatement = conn.prepareCall("call fetchActiveTeacherForms");
            ArrayList<FormClass> formsList = new ArrayList<>();
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();//move cursor to the first row
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                teacherId = rs.getInt(3);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setTeacher_id(teacherId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;

        } catch (SQLException ex) {
            //swallow the exception
        }
        //only gets here if an error occurs
        return null;
    }

    public boolean uploadForm(int template_id, String form_type, int user_id, String timestamp, String fileName) {

        //Form type must be stu or teach;
        //Timestamp: new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (form_type.equalsIgnoreCase("STU")) {
            try {
                CallableStatement fetchStatement = conn.prepareCall("call uploadStudentForm(?,?,?,?)");
                fetchStatement.setInt(1, template_id);
                fetchStatement.setInt(2, user_id);
                fetchStatement.setString(3, timestamp);
                fetchStatement.setString(4, fileName);

                int result = fetchStatement.executeUpdate();
                if (result > 0) {
                    if (result == 1) {
                        return true;
                    } else {
                        System.out.println("More than one line were updated");
                        return false;
                    }
                } else {
                    System.out.println("No lines were updated");
                    return false;
                }

            } catch (SQLException ex) {
                return false;
            }
        } else if (form_type.equalsIgnoreCase("TEACH")) {
            try {
                CallableStatement fetchStatement = conn.prepareCall("call uploadTeacherForm(?,?,?,?)");
                fetchStatement.setInt(1, template_id);
                fetchStatement.setInt(2, user_id);
                fetchStatement.setString(3, timestamp);
                fetchStatement.setString(4, fileName);

                int result = fetchStatement.executeUpdate();
                if (result > 0) {
                    if (result == 1) {
                        return true;
                    } else {
                        System.out.println("More than one line were updated");
                        return false;
                    }
                } else {
                    System.out.println("No lines were updated");
                    return false;
                }
            } catch (SQLException ex) {
                return false;
            }
        } else {
            System.out.println("Please use form_type either STU or TEACH");
            return false;
        }
    }

    public ArrayList<FormClass> fetchFormsByDaycareId(int daycareId) {

        CallableStatement fetchStatement = null;
        ArrayList<FormClass> formsList = new ArrayList<>();
        try {
            fetchStatement = conn.prepareCall("call fetchFormsByDaycareId (?)");
            fetchStatement.setInt(1, daycareId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                FormClass form = new FormClass();
                form.setId(rs.getInt(1));
                form.setFilename(rs.getString(2));
                formsList.add(form);
            } while (rs.next());
            return formsList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<FormClass> fetchAllFormsByStudentId(int studentId) {
        int formId, templateId;
        String filename, dateCreate;
        Boolean active;
        CallableStatement fetchStatement = null;
        ArrayList<FormClass> formsList = new ArrayList<>();
        try {
            fetchStatement = conn.prepareCall("call fetchAllFormsByStudentId (?)");
            fetchStatement.setInt(1, studentId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setTeacher_id(studentId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<FormClass> fetchAllFormsByTeacherId(int teacherId) {
        int formId, templateId;
        String filename, dateCreate;
        Boolean active;
        CallableStatement fetchStatement = null;
        ArrayList<FormClass> formsList = new ArrayList<>();
        try {
            fetchStatement = conn.prepareCall("call fetchAllFormsByTeacherId (?)");
            fetchStatement.setInt(1, teacherId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setTeacher_id(teacherId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<FormClass> fetchActiveFormsByStudentId(int studentId) {
        int formId, templateId;
        String filename, dateCreate;
        Boolean active;
        CallableStatement fetchStatement = null;
        ArrayList<FormClass> formsList = new ArrayList<>();
        try {
            fetchStatement = conn.prepareCall("call fetchActiveFormsByStudentId (?)");
            fetchStatement.setInt(1, studentId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setTeacher_id(studentId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<FormClass> fetchActiveFormsByTeacherId(int teacherId) {
        int formId, templateId;
        String filename, dateCreate;
        Boolean active;
        CallableStatement fetchStatement = null;
        ArrayList<FormClass> formsList = new ArrayList<>();
        try {
            fetchStatement = conn.prepareCall("call fetchActiveFormsByTeacherId (?)");
            fetchStatement.setInt(1, teacherId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                formId = rs.getInt(1);
                templateId = rs.getInt(2);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setTeacher_id(teacherId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                formsList.add(form);
            } while (rs.next());
            return formsList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public FormClass fetchTeacherFormById(int formId) {
        int templateId, teacherId;
        String filename, dateCreate;
        Boolean active;
        CallableStatement fetchStatement = null;
        try {
            fetchStatement = conn.prepareCall("call fetchTeacherFormById (?)");
            fetchStatement.setInt(1, formId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
                templateId = rs.getInt(2);
                teacherId = rs.getInt(3);
                filename = rs.getString(4);
                dateCreate = rs.getString(5);
                active = rs.getBoolean(6);
                FormClass form = new FormClass();
                form.setId(formId);
                form.setTemplate_id(templateId);
                form.setTeacher_id(teacherId);
                form.setFilename(filename);
                form.setDate_create(dateCreate);
                form.setActive(active);
                return form;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public FormClass fetchStudentFormById(int formId) {
        int templateId, studentId;
        String filename, dateCreate;
        Boolean active;
        CallableStatement fetchStatement = null;
        try {
            fetchStatement = conn.prepareCall("call fetchStudentFormById (?)");
            fetchStatement.setInt(1, formId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            templateId = rs.getInt(2);
            studentId = rs.getInt(3);
            filename = rs.getString(4);
            dateCreate = rs.getString(5);
            active = rs.getBoolean(6);
            FormClass form = new FormClass();
            form.setId(formId);
            form.setTemplate_id(templateId);
            form.setStudent_id(studentId);
            form.setFilename(filename);
            form.setDate_create(dateCreate);
            form.setActive(active);
            return form;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public FormClass fetchTeacherFormByNameAndStatus(String filename, Boolean activeStatus) {
        int templateId, teacherId, formId;
        String dateCreate;
        CallableStatement fetchStatement = null;
        try {
            fetchStatement = conn.prepareCall("call fetchTeacherFormByNameAndStatus (?,?)");
            fetchStatement.setString(1, filename);
            fetchStatement.setBoolean(2, activeStatus);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            formId = rs.getInt(1);
            templateId = rs.getInt(2);
            teacherId = rs.getInt(3);
            filename = rs.getString(4);
            dateCreate = rs.getString(5);
            FormClass form = new FormClass();
            form.setId(formId);
            form.setTemplate_id(templateId);
            form.setTeacher_id(teacherId);
            form.setFilename(filename);
            form.setDate_create(dateCreate);
            form.setActive(activeStatus);
            return form;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public FormClass fetchStudentFormByNameAndStatus(String filename, Boolean activeStatus) {
        int templateId, studentId, formId;
        String dateCreate;
        CallableStatement fetchStatement = null;
        try {
            fetchStatement = conn.prepareCall("call fetchStudentFormByNameAndStatus (?,?)");
            fetchStatement.setString(1, filename);
            fetchStatement.setBoolean(2, activeStatus);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            formId = rs.getInt(1);
            templateId = rs.getInt(2);
            studentId = rs.getInt(3);
            filename = rs.getString(4);
            dateCreate = rs.getString(5);
            FormClass form = new FormClass();
            form.setId(formId);
            form.setTemplate_id(templateId);
            form.setStudent_id(studentId);
            form.setFilename(filename);
            form.setDate_create(dateCreate);
            form.setActive(activeStatus);
            return form;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }
}
