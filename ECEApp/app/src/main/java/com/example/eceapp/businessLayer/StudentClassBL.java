package com.example.eceapp.businessLayer;

import android.util.Log;

import com.example.eceapp.StudentClass;
import com.example.eceapp.dataLayer.StudentClassDL;

import java.util.ArrayList;

public class StudentClassBL {

    StudentClassDL studentClassDL = StudentClassDL.getInstance();

    public ArrayList<StudentClass> getStudents() {
        return studentClassDL.fetchAllStudents();
    }

    public StudentClass getStudentById(int studentId) {
        return studentClassDL.fetchStudentById(studentId);
    }

    public ArrayList<String> getParentsByStudentId(int studentId) {
        return studentClassDL.fetchParentsByStudentId(studentId);
    }

    public ArrayList<StudentClass> getStudents_address() {
        return studentClassDL.fetchAllStudents_address();
    }

    public int updateStudent(StudentClass stu) {
        return studentClassDL.updateStudent(stu);
    }

    public ArrayList<StudentClass> getStudentsByDaycareId(int id) {
        Log.e("StudentBL", "Inside getStudentByDaycareId in StudentClassBL");
        return studentClassDL.fetchStudentsByDaycareId(id);
    }

    public StudentClass getStudentInfo(StudentClass student){
        return new StudentClassDL().fetchStudentEmergencyInfoById(student);
    }

    public int[] addStudent(StudentClass stu){
        return studentClassDL.addStudent(stu);
    }
    public int[] addStudentInfo (StudentClass stu) { return studentClassDL.addStudentInfo(stu);}
    public ArrayList<StudentClass> fetchStudentsByDaycareId(int id){
        return studentClassDL.fetchStudentInfoByDaycareId(id);//Phoebe
    }
    public ArrayList<StudentClass> searchStudent(String keyword) {
        return studentClassDL.searchStudent(keyword);
    }
    public StudentClass getStudentByName(String firstname, String lastname) {
        return studentClassDL.fetchStudentByName(firstname, lastname);
    }
}

