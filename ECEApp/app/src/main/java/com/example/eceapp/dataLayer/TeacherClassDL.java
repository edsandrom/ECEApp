package com.example.eceapp.dataLayer;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.TeacherClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeacherClassDL {

    private static TeacherClassDL instance;

    //Database Connection
    private static Connection conn;
    //

    public static TeacherClassDL getInstance() {
        if (instance == null) {
            ConnectionClass connection = new ConnectionClass();
            instance = new TeacherClassDL();
            conn = connection.Connect();
            System.out.println(conn);
        }
        return instance;
    }

    public TeacherClass fetchTeacherByUsername(String username) {

        TeacherClass teacher = new TeacherClass();

        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetchTeacherByUsername(?)");
            fetchStatement.setString(1, username);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            teacher.setTeacherID(rs.getInt(1));
            teacher.setDaycareID(rs.getInt(2));
            teacher.setFirstName(rs.getString(4));
            teacher.setLastName(rs.getString(5));
            teacher.setUsername(username);
            teacher.setAdmin(rs.getBoolean(9));

            return teacher;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<TeacherClass> fetchAllTeachers() {

        ArrayList<TeacherClass> teacherList = new ArrayList<>();
        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetchAllTeachers");
            ResultSet rs = fetchStatement.executeQuery();

            rs.first();
            do {
                TeacherClass teacher = new TeacherClass();
                teacher.setTeacherID(rs.getInt(1));
                teacher.setFirstName(rs.getString(4));
                teacher.setLastName(rs.getString(5));
                teacher.setDaycareID(rs.getInt(2));
                teacherList.add(teacher);
            } while (rs.next());

            return teacherList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public TeacherClass fetchTeacherById(int teacherId) {
        String firstname, lastname;
        Boolean active;
        int daycareId, addressId;
        CallableStatement fetchStatement = null;
        try {
            fetchStatement = conn.prepareCall("call fetchTeacherById (?)");
            fetchStatement.setInt(1, teacherId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            daycareId = rs.getInt(2);
            addressId = rs.getInt(3);
            firstname = rs.getString(4);
            lastname = rs.getString(5);
            TeacherClass teacher = new TeacherClass();
            teacher.setDaycareID(daycareId);
            teacher.setAddressID(addressId);
            teacher.setFirstName(firstname);
            teacher.setLastName(lastname);
            return teacher;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public TeacherClass fetchTeacherByName(String firstname, String lastname) {

        TeacherClass teacher = new TeacherClass();

        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetchTeacherByName(?,?)");
            fetchStatement.setString(1, firstname);
            fetchStatement.setString(2, lastname);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            teacher.setTeacherID(rs.getInt(1));
            teacher.setFirstName(rs.getString(4));
            teacher.setLastName(rs.getString(5));
            teacher.setDaycareID(rs.getInt(2));

            return teacher;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

}
