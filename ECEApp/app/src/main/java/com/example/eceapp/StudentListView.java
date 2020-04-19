package com.example.eceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eceapp.businessLayer.AddressBL;
import com.example.eceapp.businessLayer.ContactBL;
import com.example.eceapp.businessLayer.MedicalHistoryBL;
import com.example.eceapp.businessLayer.MedicationBL;
import com.example.eceapp.businessLayer.NonAuthorizedBL;
import com.example.eceapp.businessLayer.PractitionerBL;
import com.example.eceapp.businessLayer.StudentClassBL;

import java.util.ArrayList;
//phoebe 02/12/2020


public class StudentListView extends AppCompatActivity {
    StudentClassBL studenBL = new StudentClassBL();
    StudentClass stu = new StudentClass();
    ArrayList<StudentClass> studentList;
    ArrayList<StudentClass> studentClassArrayListFullInfo = new ArrayList<>();
    int daycareId;
    boolean isAdmin;
    String keyword;
    TextView noStudentNotification;
    EditText etKeyword;
    Button btnSearch, btnCancel, btnAddStudent;
    ListView listView;
    ContactClass contactClass = new ContactClass();
    ArrayList<ContactClass> contactClassArrayList= new ArrayList<>();
    ContactBL contactBL = new ContactBL();
    NonAuthorized nonAuthorized = new NonAuthorized();
    NonAuthorizedBL nonAuthorizedBL = new NonAuthorizedBL();
    ArrayList<NonAuthorized> nonAuthorizedArrayList = new ArrayList<>();
    MedicalHistory medicalHistory = new MedicalHistory();
    MedicalHistoryBL medicalHistoryBL = new MedicalHistoryBL();
    Medication medication = new Medication();
    MedicationBL medicationBL = new MedicationBL();
    AddressClass addressClass = new AddressClass();
    AddressBL addressBL = new AddressBL();
    Practitioner practitioner = new Practitioner();
    PractitionerBL practitionerBL = new PractitionerBL();

    //ArrayList<StudentClass> studentList = students.getStudents();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_view);

        listView = findViewById(R.id.studentListView);
        etKeyword = findViewById(R.id.txtKeyWord);
        btnSearch= findViewById(R.id.btnSearchStudent);
        btnCancel=findViewById(R.id.btnStudentLVCancel);
        btnAddStudent=findViewById(R.id.btnAddNewStudent);
        noStudentNotification=findViewById(R.id.noStudent);

        //Check user role is admin or teacher. If it is teacher, the list only includes students in teacher's daycare
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        daycareId = pref.getInt("daycare", -1);
        //isAdmin = pref.getBoolean("admin", false);
        /*
        if(isAdmin){
            studentList = students.getStudents_address();
        }
        else

         */

        studentList=studenBL.fetchStudentsByDaycareId(daycareId);//

        //create adapter
        showStudentList();

        //search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = etKeyword.getText().toString().trim();
                if(keyword!=""){
                    studentList = studenBL.searchStudent(keyword);
                }

                //create adapter
                showStudentList();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoHomePage();
            }
        });
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoAddStudent();
            }
        });

    }
    private void showStudentList(){
        if(studentList!=null){
            studentClassArrayListFullInfo=new ArrayList<>();
            for(StudentClass student: studentList){
                studentClassArrayListFullInfo.add(getAllClassAttributes(student));
            }
            listView.setVisibility(View.VISIBLE);
            noStudentNotification.setVisibility(View.INVISIBLE);
            MyAdapter adapter = new MyAdapter(StudentListView.this, studentClassArrayListFullInfo);
            listView.setAdapter(adapter);

            //set item click on list view
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(StudentListView.this, studentList1.get(i).getFullname(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentListView.this, CreateStudentActivity.class);
                    intent.putExtra("student", studentClassArrayListFullInfo.get(i));
                    intent.putExtra("from","StudentListView");
                    startActivity(intent);
                }
            });
        }
        else{
            noStudentNotification.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    class MyAdapter extends ArrayAdapter<StudentClass> {
        Context context;
        ArrayList<StudentClass> rStudents;

        MyAdapter(Context c, ArrayList<StudentClass> aStudents) {
            super(c, R.layout.student_row, R.id.studentName, aStudents);
            this.context = c;
            this.rStudents = aStudents;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate((R.layout.student_row), parent, false);
            TextView myName = row.findViewById(R.id.studentName);
            TextView myDayCare = row.findViewById(R.id.studentDayCare);

            //set resources on views
            myName.setText(rStudents.get(position).getFullname());
            int stuId = rStudents.get(position).getStudentId();
            try {
                myDayCare.setText(String.valueOf(stuId));
            } catch (Exception ex) {
                Log.e("Daycare error", "Daycare error" + ex.getMessage());

            }

            return row;
        }
    }
    private void GotoHomePage(){
        Intent startIntent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(startIntent);
        finish();
    }
    private void GotoAddStudent(){
        Intent startIntent = new Intent(getApplicationContext(), CreateStudentActivity.class);
        startActivity(startIntent);
    }

    private StudentClass getAllClassAttributes(StudentClass s){
        //s is not null
        StudentClass stu=s;
        //Student Address
        stu.setAddressClass(addressBL.getAddressById(s.getAddressId()));

        //Practitioner
        Practitioner practitioner = new Practitioner();
        practitioner=practitionerBL.getPractitionerbyId(s.getPractitionerId());
        practitioner.setAddressClass(addressBL.getAddressById(practitioner.getAddressId()));
        stu.setPractitioner(practitioner);

        //Medical History
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory=medicalHistoryBL.getMedicalHistoryById(s.getMedicalHistoryId());
        //Medication
        Medication medication = new Medication();
        medication=medicationBL.getMedicationById(medicalHistory.getMedicationId());
        if(medication!=null){
            medicalHistory.setMedication(medication);
        }
        stu.setMedicalHistory(medicalHistory);

        //Contacts
        ArrayList<ContactClass> contactClassArrayList = new ArrayList<>();
        contactClassArrayList=contactBL.getContactsByStudentId(s.getStudentId());
        //System.out.println("Contacts");
        //System.out.println(contactClassArrayList);
        if(contactClassArrayList!=null){
            for (ContactClass c: contactClassArrayList){
                c.setAddress(addressBL.getAddressById(c.getAddressId()));
            }
            stu.setContacts(contactClassArrayList);
            //System.out.println(contactClassArrayList);
        }
        //NonAuthorized
        ArrayList<NonAuthorized> nonAuthorizedArrayList= new ArrayList<>();
        nonAuthorizedArrayList = nonAuthorizedBL.getNonAuthorizedByStudentId(s.getStudentId());
        if(nonAuthorizedArrayList!=null) {stu.setNonAuthorizeds(nonAuthorizedArrayList);}
        return stu;
    }


}
