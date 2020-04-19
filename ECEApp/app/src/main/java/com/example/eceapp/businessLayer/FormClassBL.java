package com.example.eceapp.businessLayer;


import com.example.eceapp.FormClass;
import com.example.eceapp.dataLayer.FormClassDL;

import java.util.ArrayList;

public class FormClassBL {

    private static FormClassDL staticFormClassDL = new FormClassDL();

    FormClassDL formClassDL = FormClassDL.getInstance();

    public ArrayList<FormClass> getAllStudentForms() {
        return formClassDL.fetchAllStudentForms();
    }

    public ArrayList<FormClass> getAllTeacherForms() {
        return formClassDL.fetchAllTeacherForms();
    }

    public ArrayList<FormClass> getActiveStudentForms() {
        return formClassDL.fetchActiveStudentForms();
    }

    public ArrayList<FormClass> getActiveTeacherForms() {
        return formClassDL.fetchActiveTeacherForms();
    }

    public ArrayList<FormClass> getFormsByDaycareId(int daycareId) {
        return formClassDL.fetchFormsByDaycareId(daycareId);
    }

    public ArrayList<FormClass> getAllFormsByStudentId(int studentId) {
        return formClassDL.fetchAllFormsByStudentId(studentId);
    }

    public ArrayList<FormClass> getAllFormsByTeacherId(int teacherId) {
        return formClassDL.fetchAllFormsByTeacherId(teacherId);
    }

    public ArrayList<FormClass> getActiveFormsByStudentId(int studentId) {
        return formClassDL.fetchActiveFormsByStudentId(studentId);
    }

    public ArrayList<FormClass> getActiveFormsByTeacherId(int teacherId) {
        return formClassDL.fetchActiveFormsByStudentId(teacherId);
    }

    public FormClass getTeacherFormById(int formId) {
        return formClassDL.fetchTeacherFormById(formId);
    }

    public FormClass getStudentFormById(int formId) {
        return formClassDL.fetchStudentFormById(formId);
    }

    public FormClass getTeacherFormByNameAndStatus(String filename, Boolean activeStatus) {
        return formClassDL.fetchTeacherFormByNameAndStatus(filename, activeStatus);
    }

    public FormClass getStudentFormByNameAndStatus(String filename, Boolean activeStatus) {
        return formClassDL.fetchStudentFormByNameAndStatus(filename, activeStatus);
    }

    public static boolean saveForm(int template_id, String form_type, int user_id, String timestamp, String fileName) {

        if (staticFormClassDL.uploadForm(template_id, form_type, user_id, timestamp, fileName)) {
            return true;
        } else {
            return false;
        }
    }
}
