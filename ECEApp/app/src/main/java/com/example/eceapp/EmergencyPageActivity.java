package com.example.eceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eceapp.businessLayer.DaycareClassBL;
import com.example.eceapp.businessLayer.StudentClassBL;

import java.util.ArrayList;
//Created By Ethan Feb25
public class EmergencyPageActivity extends AppCompatActivity {

    TextView txtParent1, txtParent1Phone1, txtParent1Phone2, txtParent2, txtParent2Phone1, txtParent2Phone2, txtMedicare, txtSeriousAllergies, txtTreatment, txtAllergies, txtMedication, txtPractioner, txtOtherName1;
    TextView txtOtherRelationship1, txtOtherPhone1, txtOtherName2, txtOtherRelationship2, txtOtherPhone2, txtOtherName3, txtOtherRelationship3, txtOtherPhone3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_page);

        txtParent1 = findViewById(R.id.txtParent1);
        txtParent1Phone1 = findViewById(R.id.txtParent1Phone1);
        txtParent1Phone2 = findViewById(R.id.txtParent1Phone2);
        txtParent2 = findViewById(R.id.txtParent2);
        txtParent2Phone1 = findViewById(R.id.txtParent2Phone1);
        txtParent2Phone2 = findViewById(R.id.txtParent2Phone2);
        txtMedicare = findViewById(R.id.txtMedicare);
        txtSeriousAllergies = findViewById(R.id.txtSeriousAllergies);
        txtAllergies = findViewById(R.id.txtAllergies);
        txtPractioner = findViewById(R.id.txtPractioner);
        txtOtherName1 = findViewById(R.id.txtOtherName1);
        txtOtherRelationship1 = findViewById(R.id.txtOtherRelationship1);
        txtOtherPhone1 = findViewById(R.id.txtOtherPhone1);
        txtOtherName2 = findViewById(R.id.txtOtherName2);
        txtOtherRelationship2 = findViewById(R.id.txtOtherRelationship2);
        txtOtherPhone2 = findViewById(R.id.txtOtherPhone2);
        txtOtherName3 = findViewById(R.id.txtOtherName3);
        txtOtherRelationship3 = findViewById(R.id.txtOtherRelationship3);
        txtOtherPhone3 = findViewById(R.id.txtOtherPhone3);
        final Spinner spinnerDaycare = findViewById(R.id.spinnerDaycare);
        final Spinner spinnerStudent = findViewById(R.id.spinnerStudent);
        fillDaycareSpinner(spinnerDaycare);

        spinnerDaycare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DaycareClass daycare = (DaycareClass) spinnerDaycare.getSelectedItem();
                int daycareId = daycare.getDaycareId();
                fillStudentSpinner(spinnerStudent, daycareId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StudentClass student = (StudentClass) spinnerStudent.getSelectedItem();
                txtParent1.setText("");
                txtParent1Phone1.setText("");;
                txtParent1Phone2.setText("");;
                txtParent2.setText("");;
                txtParent2Phone1.setText("");;
                txtParent2Phone2.setText("");;
                txtMedicare.setText("");
                txtSeriousAllergies.setText("");
                txtTreatment.setText("");
                txtAllergies.setText("");
                txtMedication.setText("");
                txtPractioner.setText("");
                txtOtherName1.setText("");
                txtOtherRelationship1.setText("");
                txtOtherPhone1.setText("");
                txtOtherName2.setText("");
                txtOtherRelationship2.setText("");
                txtOtherPhone2.setText("");
                txtOtherName3.setText("");
                txtOtherRelationship3.setText("");
                txtOtherPhone3.setText("");
                fillEmergencyPage(student);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private ArrayList<DaycareClass> GetDaycareList(){
        ArrayList<DaycareClass> listOfDaycares = new ArrayList<DaycareClass>();
        DaycareClassBL daycares = new DaycareClassBL();
        listOfDaycares = daycares.ListOfDaycares();

        return listOfDaycares;
    }//end of GetDaycareList();

    private ArrayList<StudentClass> GetStudentListByDaycareId(int id){
        ArrayList<StudentClass> listOfStudents = new ArrayList<StudentClass>();
        StudentClassBL students = new StudentClassBL();

        listOfStudents = students.getStudentsByDaycareId(id);
        return listOfStudents;
    }

    private void fillDaycareSpinner(Spinner spin){
        ArrayAdapter<DaycareClass> adapter = new ArrayAdapter<DaycareClass>(this, android.R.layout.simple_spinner_item, GetDaycareList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }//end of fillDaycareSpinner

    private void fillStudentSpinner(Spinner spin, int daycareId){
        ArrayList<StudentClass> listOfStudents = GetStudentListByDaycareId(daycareId);
        if(listOfStudents == null){
            ArrayList<StudentClass> empty = new ArrayList<>();
            StudentClass noStudents = new StudentClass();
            noStudents.setFullname("No Students Found");
            empty.add(noStudents);
            ArrayAdapter<StudentClass> adapter = new ArrayAdapter<StudentClass>(this, android.R.layout.simple_spinner_item, empty);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
        }
        else {
            ArrayAdapter<StudentClass> adapter = new ArrayAdapter<StudentClass>(this, android.R.layout.simple_spinner_item, GetStudentListByDaycareId(daycareId));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
        }
    }//end of fillStudentSpinner

    private void fillEmergencyPage(StudentClass student){
        student = new StudentClassBL().getStudentInfo(student);

        ArrayList<ContactClass> contacts = student.getContacts();

        fillContactInfo(contacts);
        //Change data type to String - Phoebe (04/02)
        txtMedicare.setText(student.getMedicare());


        txtSeriousAllergies.setText(student.getSeriousAllergies());

        //need something for emergency treatment?
        //need something for medication
        txtAllergies.setText(student.getOtherAllergies());
        txtPractioner.setText(student.getPractioner());

    }

    private void fillContactInfo(ArrayList<ContactClass> contacts){
        boolean parent1 = false;
        boolean parent2 = false;
        boolean contact1 = false;
        boolean contact2 = false;
        boolean contact3 = false;

        if(contacts != null){
        for(ContactClass c : contacts){
            if(c.getFirstName().equals(null)) break;
            if(c.getRelationship().equals("Parent/Guardian")){
                if(parent1 == false){
                    txtParent1.setText(c.getFirstName());
                    txtParent1Phone1.setText(c.getHomePhone());
                    txtParent1Phone2.setText(c.getCellPhone());
                    parent1 = true;
                    continue;
                }
                if(parent2 == false){
                    txtParent2.setText(c.getFirstName());
                    txtParent2Phone1.setText(c.getHomePhone());
                    txtParent2Phone2.setText(c.getCellPhone());
                    parent2 = true;
                    continue;
                }
            }


            if(contact1 == false){
                txtOtherName1.setText(c.getFirstName());
                txtOtherRelationship1.setText(c.getRelationship());
                txtOtherPhone1.setText(c.getCellPhone()); //might need to include a second number
                contact1 = true;
                continue;
            }

            if(contact2 == false){
                txtOtherName2.setText(c.getFirstName());
                txtOtherRelationship2.setText(c.getRelationship());
                txtOtherPhone2.setText(c.getCellPhone());
                contact2 = true;
                continue;
            }

            if(contact3 == false){
                txtOtherName3.setText(c.getFirstName());
                txtOtherRelationship3.setText(c.getRelationship());
                txtOtherPhone3.setText(c.getCellPhone());
                contact3 = true;
                break;
            }

        }}
    }
}
