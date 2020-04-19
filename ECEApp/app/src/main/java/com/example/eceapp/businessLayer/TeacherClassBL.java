package com.example.eceapp.businessLayer;

import com.example.eceapp.TeacherClass;
import com.example.eceapp.dataLayer.TeacherClassDL;

import java.util.ArrayList;

public class TeacherClassBL {

    private TeacherClassDL teacherClassDL = TeacherClassDL.getInstance();

    private static TeacherClassDL staticTeacherClassDL = new TeacherClassDL();

    public TeacherClass getTeacherByUsername(String username) {
        return teacherClassDL.fetchTeacherByUsername(username);
    }

    public ArrayList<TeacherClass> getTeachers() {
        return teacherClassDL.fetchAllTeachers();
    }

    public TeacherClass getTeacherById(int teacherId) {
        return teacherClassDL.fetchTeacherById(teacherId);
    }

    public TeacherClass getTeacherByName(String firstname, String lastname) {
        return teacherClassDL.fetchTeacherByName(firstname, lastname);
    }

}
