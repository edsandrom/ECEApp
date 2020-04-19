package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.ContactClass;
import com.example.eceapp.StudentClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class StudentClassDL {

    private static StudentClassDL instance;

    //Database Connection
    private static Connection conn;
    //

    public static StudentClassDL getInstance() {
        if (instance == null) {
            ConnectionClass connection = new ConnectionClass();
            instance = new StudentClassDL();
            conn = connection.Connect();
        }
        return instance;
    }

    public ArrayList<StudentClass> fetchAllStudents() {

        ArrayList<StudentClass> studentList = new ArrayList<>();
        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetchAllStudents");
            ResultSet rs = fetchStatement.executeQuery();

            rs.first();
            do {
                StudentClass student = new StudentClass();
                student.setStudentId(rs.getInt(1));
                student.setFname(rs.getString(6));
                student.setLname(rs.getString(7));
                student.setFullname(rs.getString(6) + " " + rs.getString(7));
                student.setDaycareId(rs.getInt(5));
                studentList.add(student);
            } while (rs.next());
            return studentList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public int editStudent(String fname, String lname, String DOB, String address, int stuId) {
        int isupdate = -1;
        try {
            CallableStatement mystmt = conn.prepareCall("call edit_student(?, ?, ?, ?, ?)");
            mystmt.setString(1, fname);
            mystmt.setString(2, lname);
            mystmt.setString(3, DOB);
            mystmt.setString(4, address);
            //check duplicate student record
            mystmt.setInt(5, stuId);
            isupdate = mystmt.executeUpdate();


            return isupdate;
        } catch (SQLException ex) {

           // Log.e("SQL ERROR", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return isupdate;
    }

    public StudentClass fetchStudentById(int studentId) {

        StudentClass student = new StudentClass();

        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetchStudentById(?)");
            fetchStatement.setInt(1, studentId);
            ResultSet rs = fetchStatement.executeQuery();

            rs.first();
            student.setStudentId(studentId);
            student.setFname(rs.getString(6));
            student.setLname(rs.getString(7));
            student.setFullname(rs.getString(6) + rs.getString(7));
            student.setDaycareId(rs.getInt(5));

            return student;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<String> fetchParentsByStudentId(int studentId) {
        ArrayList<String> parentsList = new ArrayList<>();
        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetchParentsByStudentId(?)");
            fetchStatement.setInt(1, studentId);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                StudentClass student = new StudentClass();
                student.setParent1Name(rs.getString(1) + " " + rs.getString(2));
                parentsList.add(student.getParent1Name());
            } while (rs.next());

            return parentsList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<StudentClass> fetchAllStudents_address() {//Phoebe - check if not use

        ArrayList<StudentClass> studentList = new ArrayList<>();
        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetch_student_address");
            ResultSet rs = fetchStatement.executeQuery();

            rs.first();
            do {
                StudentClass student = new StudentClass();
                student.setStudentId(rs.getInt(1));
                student.setAddressId(rs.getInt(2));
                student.setFname(rs.getString(3));
                student.setLname(rs.getString(4));
                student.setDob(rs.getDate(5).toString());
                student.setAptNo(rs.getString(6));
                student.setFullname(rs.getString(3) + " " + rs.getString(4));
                student.setStrNo(rs.getString(7));
                student.setStreet(rs.getString(8));
                student.setCity(rs.getString(9));
                student.setProv(rs.getString(10));
                student.setPostCode(rs.getString(11));
                student.setDaycareId(rs.getInt(12));
                studentList.add(student);
            } while (rs.next());

            return studentList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public int updateStudent(StudentClass stu) {//Phoebe - update
        int count = -1;
        try {
            CallableStatement stmt;
            stmt = conn.prepareCall("call edit_student(?, ?, ?, ?, ?,?,?,?,?,?,?,?, ?, ?, ?, ?,?,?,?,?,?,?,?,?)");
            stmt.setInt(1, stu.getStudentId());
            stmt.setString(2, stu.getFname());
            stmt.setString(3, stu.getLname());
            stmt.setString(4, stu.getDob());
            stmt.setString(5, stu.getMedicare());
            stmt.setString(6, stu.getMedicareExpiry());
            stmt.setString(7, stu.getSeriousAllergies());
            stmt.setInt(8, stu.getEmergencyTreatment());
            stmt.setString(9, stu.getOtherAllergies());
            stmt.setInt(10, stu.getRoutine_services());
            stmt.setInt(11, stu.getPrev_attendance());
            stmt.setString(12, stu.getPrev_attendance_long());
            stmt.setString(13, stu.getPrev_attendance_experience());
            stmt.setString(14, stu.getHelp_dress());
            stmt.setString(15, stu.getHelp_eat());
            stmt.setString(16, stu.getHelp_toilet());
            stmt.setString(17, stu.getHelp_wash());
            stmt.setString(18, stu.getHelp_other());
            stmt.setString(19, stu.getHints());
            stmt.setString(20, stu.getLike_to_do());
            stmt.setString(21, stu.getExtra_info());
            stmt.setInt(22,stu.getAddressId());
            stmt.setInt(23,stu.getMedicalHistoryId());
            stmt.setInt(24,stu.getPractitionerId());
            count =stmt.executeUpdate();
            return count;

        } catch (SQLException ex) {
            Log.e("SQL Student ERROR", ex.getMessage());
        }
        return count;
    }


    public ArrayList<StudentClass> fetchStudentsByDaycareId(int id) {
        Log.e("StudentDL", "Inside getStudentByDaycareId in StudentClassDL");
        ArrayList<StudentClass> studentList = new ArrayList<>();
        String studentName;
        int studentId;
        try {
            PreparedStatement ps = conn.prepareCall("call fetchStudentsByDaycareId(" + id + ")");
            ResultSet rs = ps.executeQuery();
            rs.first();

            do {
                studentId = rs.getInt(1);
                studentName = rs.getString(2);

                StudentClass student = new StudentClass();
                student.setStudentId(studentId);
                student.setFullname(studentName);
                studentList.add(student);

            } while (rs.next());

            return studentList;

        } catch (SQLException ex) {
            //Log.e("SQL Student ERROR", ex.getMessage());
        }
        return null;
    }//end of fetchStudentsByDaycareId

    public StudentClass fetchStudentEmergencyInfoById(StudentClass student) {

        int studentId = student.getStudentId();
        ArrayList<ContactClass> listOfContacts = new ArrayList<>();
        String txtMedicare, txtSeriousAllergies, txtTreatment, txtAllergies, txtMedication, txtPractioner;

        try {
            PreparedStatement ps = conn.prepareCall("call fetchStudentInfoById(" + studentId + ")");
            ResultSet rs = ps.executeQuery();

            //take the first to get student information that is not going to be repeated, like practioner and student medical information
            rs.first();
            student.setMedicare(rs.getString(1));
            student.setSeriousAllergies(rs.getString(2));
            student.setEmergencyTreatment(rs.getInt(3));
            student.setOtherAllergies(rs.getString(4));
            student.setPractioner(rs.getString(5));
            ArrayList<ContactClass> contacts = new ArrayList<>();
            do {
                ContactClass contact = new ContactClass();
                contact.setRelationship(rs.getString(6));
                contact.setFirstName(rs.getString(7));
                contact.setHomePhone(rs.getString(8));
                contact.setCellPhone(rs.getString(9));
                contacts.add(contact);

            } while (rs.next());
            //create a do while loop to create a list of Contacts and then add that list of contacts to return the student object

            student.setContacts(contacts);
        } catch (SQLException ex) {
            //Log.e("SQL ERROR", ex.getMessage());
        }

        return student;
    }

    public int[] addStudent(StudentClass stu) {//Phoebe - check if not use
        int[] result = new int[2];

        try {
            CallableStatement mystmt = conn.prepareCall("call add_student(?, ?, ?, ?, ?, ?)");
            mystmt.setString(1, stu.getFname());
            mystmt.setString(2, stu.getLname());
            mystmt.setString(3, stu.getDob());
            mystmt.setInt(4, stu.getAddressId());

            //check duplicate student record
            mystmt.registerOutParameter(5, Types.INTEGER);
            mystmt.registerOutParameter(6, Types.INTEGER);
            mystmt.executeUpdate();
            result[0] = mystmt.getInt(5);
            result[1] = mystmt.getInt(6);

            return result;
        } catch (SQLException ex) {
            //Log.e("SQL ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());
        }
        return result;
    }
    public int[] addStudentInfo(StudentClass stu) {//Phoebe - updated
        int[] result = new int[2];

        try {
            CallableStatement mystmt = conn.prepareCall("call add_studentInfo(?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            mystmt.setString(1, stu.getFname());
            mystmt.setString(2, stu.getLname());
            mystmt.setString(3, stu.getDob());
            mystmt.setInt(4, stu.getAddressId());
            mystmt.setInt(5, stu.getPractitionerId());
            mystmt.setInt(6, stu.getMedicalHistoryId());
            mystmt.setInt(7, stu.getDaycareId());
            mystmt.setString(8, stu.getMedicare());
            mystmt.setString(9, stu.getMedicareExpiry());
            mystmt.setString(10, stu.getSeriousAllergies());
            mystmt.setInt(11, stu.getEmergencyTreatment());
            mystmt.setString(12, stu.getOtherAllergies());
            mystmt.setInt(13, stu.getRoutine_services());
            mystmt.setInt(14, stu.getPrev_attendance());
            mystmt.setString(15, stu.getPrev_attendance_long());
            mystmt.setString(16, stu.getPrev_attendance_experience());
            mystmt.setString(17, stu.getHelp_dress());
            mystmt.setString(18, stu.getHelp_eat());
            mystmt.setString(19, stu.getHelp_toilet());
            mystmt.setString(20, stu.getHelp_wash());
            mystmt.setString(21, stu.getHelp_other());
            mystmt.setString(22, stu.getHints());
            mystmt.setString(23, stu.getLike_to_do());
            mystmt.setString(24, stu.getExtra_info());

            //check duplicate student record
            mystmt.registerOutParameter(25, Types.INTEGER);
            mystmt.registerOutParameter(26, Types.INTEGER);
            mystmt.executeUpdate();
            result[0] = mystmt.getInt(25);
            result[1] = mystmt.getInt(26);

            return result;
        } catch (SQLException ex) {
           // Log.e("SQL ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());
        }
        return result;
    }
    public ArrayList<StudentClass> fetchStudentInfoByDaycareId(int id) {//Phoebe - updated: added "info" in the method's name
        ArrayList<StudentClass> students = new ArrayList<>();
        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call fetchStudentInfoByDayCareId(?)");
            fetchStatement.setInt(1, id);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                StudentClass student = new StudentClass();
                student.setStudentId(rs.getInt(1));
                student.setPractitionerId(rs.getInt(2));
                student.setMedicalHistoryId(rs.getInt(3));
                student.setAddressId(rs.getInt(4));
                student.setDaycareId(rs.getInt(5));
                student.setFname(rs.getString(6));
                student.setLname(rs.getString(7));
                student.setDob(rs.getString(8));
                // 9: phone
                student.setMedicare(rs.getString(10));
                student.setMedicareExpiry(rs.getString(11));
                student.setSeriousAllergies(rs.getString(12));
                student.setEmergencyTreatment(rs.getInt(13));
                student.setOtherAllergies(rs.getString(14));
                //15: allergy management plan, 16: emergency plan
                student.setRoutine_services(rs.getInt(17));
                //18: routine service plan
                student.setPrev_attendance(rs.getInt(19));
                student.setPrev_attendance_long(rs.getString(20));
                student.setPrev_attendance_experience(rs.getString(21));
                student.setHelp_dress(rs.getString(22));
                student.setHelp_eat(rs.getString(23));
                student.setHelp_toilet(rs.getString(24));
                student.setHelp_wash(rs.getString(25));
                student.setHelp_other(rs.getString(26));
                student.setHints(rs.getString(27));
                student.setLike_to_do(rs.getString(28));
                student.setExtra_info(rs.getString(29));


                students.add(student);
            } while (rs.next());

            return students;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }

    public ArrayList<StudentClass> searchStudent(String keyword) {//Phoebe
        ArrayList<StudentClass> students = new ArrayList<>();
        try {
            CallableStatement fetchStatement;
            fetchStatement = conn.prepareCall("call searchStudent(?)");
            fetchStatement.setString(1, keyword);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            do {
                StudentClass student = new StudentClass();
                student.setStudentId(rs.getInt(1));
                student.setPractitionerId(rs.getInt(2));
                student.setMedicalHistoryId(rs.getInt(3));
                student.setAddressId(rs.getInt(4));
                student.setDaycareId(rs.getInt(5));
                student.setFname(rs.getString(6));
                student.setLname(rs.getString(7));
                student.setDob(rs.getString(8));
                // 9: phone
                student.setMedicare(rs.getString(10));
                student.setMedicareExpiry(rs.getString(11));
                student.setSeriousAllergies(rs.getString(12));
                student.setEmergencyTreatment(rs.getInt(13));
                student.setOtherAllergies(rs.getString(14));
                //15: allergy management plan, 16: emergency plan
                student.setRoutine_services(rs.getInt(17));
                //18: routine service plan
                student.setPrev_attendance(rs.getInt(19));
                student.setPrev_attendance_long(rs.getString(20));
                student.setPrev_attendance_experience(rs.getString(21));
                student.setHelp_dress(rs.getString(22));
                student.setHelp_eat(rs.getString(23));
                student.setHelp_toilet(rs.getString(24));
                student.setHelp_wash(rs.getString(25));
                student.setHelp_other(rs.getString(26));
                student.setHints(rs.getString(27));
                student.setLike_to_do(rs.getString(28));
                student.setExtra_info(rs.getString(29));

                students.add(student);
            } while (rs.next());

            return students;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public StudentClass fetchStudentByName (String firstname, String lastname){
        StudentClass student = new StudentClass();
        CallableStatement fetchStatement;

        try {
            fetchStatement = conn.prepareCall("call fetchStudentByName (?,?)");
            fetchStatement.setString(1, firstname);
            fetchStatement.setString(2, lastname);
            ResultSet rs = fetchStatement.executeQuery();
            rs.first();
            student.setStudentId(rs.getInt(1));
            student.setFname(rs.getString(6));
            student.setLname(rs.getString(7));
            student.setFullname(rs.getString(6) + rs.getString(7));
            student.setDaycareId(rs.getInt(5));

            return student;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //only gets here if an error occurs
        return null;
    }



}
